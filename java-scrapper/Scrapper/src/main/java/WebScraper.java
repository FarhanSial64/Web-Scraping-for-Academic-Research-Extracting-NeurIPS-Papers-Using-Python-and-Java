

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebScraper {

    private static final String BASE_URL = "https://papers.nips.cc/";
    private static final String SAVE_DIRECTORY = "E:\\pdfs\\";
    private static final ExecutorService executor = Executors.newFixedThreadPool(10); // Adjust pool size as needed

    public static void main(String[] args) {
        try {
            Path savePath = Paths.get(SAVE_DIRECTORY);
            if (!Files.exists(savePath)) {
                Files.createDirectories(savePath);
                System.out.println("Created save directory: " + savePath);
            }
            
            System.out.println("Fetching main page: " + BASE_URL);
            Document mainPage = Jsoup.connect(BASE_URL).get();

            Elements yearLinks = mainPage.select("a[href^=/paper_files/paper/]"); 
            System.out.println("Found " + yearLinks.size() + " year links.");

            for (Element yearLink : yearLinks) {
                String yearUrl = BASE_URL + yearLink.attr("href");
                System.out.println("Processing year: " + yearUrl);

                // Extract the year name from the URL (e.g., "2023" from "/paper_files/paper/2023")
                String yearName = yearUrl.substring(yearUrl.lastIndexOf('/') + 1);
                Path yearFolderPath = Paths.get(SAVE_DIRECTORY, yearName);

                // Create the year folder if it doesn't exist
                if (!Files.exists(yearFolderPath)) {
                    Files.createDirectories(yearFolderPath);
                    System.out.println("Created year folder: " + yearFolderPath);
                }

                Document yearPage = Jsoup.connect(yearUrl).get();

                Elements paperLinks = yearPage.select("a[href^=/paper_files/paper/]"); 
                System.out.println("Found " + paperLinks.size() + " papers for year: " + yearName);

                for (Element paperLink : paperLinks) {
                    String paperPageUrl = BASE_URL + paperLink.attr("href");
                    System.out.println("Processing paper page: " + paperPageUrl);

                    Document paperPage = Jsoup.connect(paperPageUrl).get();

                    Element downloadButton = paperPage.selectFirst("a.btn.btn-primary.btn-spacer[href$=.pdf], a.btn.btn-light.btn-spacer[href$=.pdf]");
                    if (downloadButton != null) {
                        String pdfUrl = BASE_URL + downloadButton.attr("href");
                        System.out.println("Downloading paper: " + pdfUrl);

                        // Submit download task to thread pool with the year folder path
                        executor.submit(() -> downloadFile(pdfUrl, yearFolderPath.toString()));
                    } else {
                        System.err.println("Download button not found for paper: " + paperPageUrl);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static void downloadFile(String fileUrl, String saveDirectory) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Extract file name from URL
            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            Path yearFolderPath = Paths.get(saveDirectory); // Ensure the year folder path is used
            Path filePath = yearFolderPath.resolve(fileName); // Save inside the respective year folder

            // Ensure the year folder exists
            if (!Files.exists(yearFolderPath)) {
                Files.createDirectories(yearFolderPath);
            }

            // Download the file
            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(filePath.toFile())) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("Saved: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to download: " + fileUrl);
            e.printStackTrace();
        }
    }

}