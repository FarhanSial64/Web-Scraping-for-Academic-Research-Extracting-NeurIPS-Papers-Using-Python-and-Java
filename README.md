# WebDataScrapper-in-Java-Python

```markdown
# Web Scrapers for NeurIPS Papers

This repository contains two web scraping projects designed to download NeurIPS research papers:

- **Java Scraper**: A Maven-based Java project to scrape and download NeurIPS papers from their website.
- **Python Scraper**: A Python script that uses BeautifulSoup and Requests to scrape and download NeurIPS papers.

## Overview

Both scrapers aim to:
- Fetch paper metadata (including paper URLs).
- Download research papers as PDFs.
- Organize the files by year, saving them in respective year-based directories.

## Features

### Java Scraper
- **Maven Project**: The scraper is built using Maven to manage dependencies and execute tasks.
- **Multithreaded Downloading**: Uses a fixed-size thread pool to download papers concurrently for faster processing.
- **Year-Based Folders**: Creates separate folders for each year and saves the papers in the corresponding folders.
- **HTML Parsing**: Uses JSoup for extracting links and handling HTML content.

### Python Scraper
- **Script-Based**: A Python script that scrapes the NeurIPS website using BeautifulSoup and Requests.
- **Threaded Downloads**: Utilizes Python's `ThreadPoolExecutor` to perform concurrent downloads.
- **Year-Based Folders**: Similar to the Java scraper, this Python script organizes the papers into year-based folders.
- **HTML Parsing**: BeautifulSoup is used to extract paper URLs and download buttons.

## Installation

### Java Scraper

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/repository-name.git
   ```
2. Navigate to the Java scraper directory:
   ```bash
   cd repository-name/java-scraper
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the scraper:
   ```bash
   mvn exec:java
   ```

### Python Scraper

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/repository-name.git
   ```
2. Navigate to the Python scraper directory:
   ```bash
   cd repository-name/python-scraper
   ```
3. Install the required Python packages:
   ```bash
   pip install -r requirements.txt
   ```
4. Run the scraper:
   ```bash
   python scraper.py
   ```

## Usage

- Both scrapers will download NeurIPS research papers in PDF format and save them in a `pdfs` directory on your local machine.
- The papers will be organized into year-based folders (e.g., `2023/`, `2022/`, etc.).
- You can modify the base save directory (`SAVE_DIRECTORY` in Python or `SAVE_DIRECTORY` in Java) if you wish to store the files elsewhere.

## Contributing

Feel free to contribute by:
- Opening issues for bugs or feature requests.
- Submitting pull requests with improvements or fixes.

## Responsible Web Scraping Practices

- Please avoid overwhelming the server with too many requests in a short time.
- Ensure that you respect the website's `robots.txt` rules and scraping policies.
- Use rate-limiting and avoid scraping too frequently to minimize the impact on the website's server performance.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
```

### Key Sections:
1. **Overview**: Describes the functionality of both scrapers.
2. **Features**: Outlines key features of each scraper.
3. **Installation**: Provides instructions for setting up both the Java and Python scrapers.
4. **Usage**: Describes how the scrapers will save the downloaded files.
5. **Contributing**: Encourages contributions from others.
6. **Responsible Web Scraping Practices**: Reminds users to scrape responsibly and be considerate of server load.
7. **License**: Adds a standard section for project licensing (you can update the license type as per your choice).

You can modify the content according to your specific needs or details of your project.
