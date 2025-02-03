import os
import requests
from bs4 import BeautifulSoup
from concurrent.futures import ThreadPoolExecutor
from urllib.parse import urljoin

# Base URL of the website
BASE_URL = "https://papers.nips.cc/"

# Directory to save downloaded files
SAVE_DIRECTORY = "D:\\pdfs\\"

# Number of threads for concurrent downloads
THREAD_POOL_SIZE = 10


def create_save_directory(directory):
    """Create the save directory if it doesn't exist."""
    if not os.path.exists(directory):
        os.makedirs(directory)
        print(f"Created save directory: {directory}")


def fetch_page(url):
    """Fetch a webpage and return its content."""
    try:
        response = requests.get(url)
        response.raise_for_status()
        return response.text
    except requests.RequestException as e:
        print(f"Error fetching {url}: {e}")
        return None


def download_file(file_url, save_directory):
    """Download and save a file from a given URL."""
    try:
        response = requests.get(file_url, stream=True)
        response.raise_for_status()

        # Extract file name from URL
        file_name = file_url.split("/")[-1]
        file_path = os.path.join(save_directory, file_name)

        # Save the file
        with open(file_path, "wb") as file:
            for chunk in response.iter_content(chunk_size=4096):
                file.write(chunk)

        print(f"Saved: {file_path}")
    except requests.RequestException as e:
        print(f"Failed to download {file_url}: {e}")


def process_paper_page(paper_page_url, year_directory):
    """Process an individual paper page to find and download the PDF."""
    print(f"Processing paper page: {paper_page_url}")
    content = fetch_page(paper_page_url)
    if content:
        soup = BeautifulSoup(content, "html.parser")
        # Look for both types of download buttons
        download_button = soup.select_one(
            "a.btn.btn-primary.btn-spacer[href$='.pdf'], a.btn.btn-light.btn-spacer[href$='.pdf']"
        )
        if download_button:
            pdf_url = urljoin(BASE_URL, download_button["href"])
            print(f"Downloading paper: {pdf_url}")
            download_file(pdf_url, year_directory)
        else:
            print(f"Download button not found for paper: {paper_page_url}")


def process_year_page(year_url):
    """Process a year page to find paper links and create a folder for the year."""
    print(f"Processing year: {year_url}")
    content = fetch_page(year_url)
    if content:
        soup = BeautifulSoup(content, "html.parser")

        # Extract the year from the URL
        year_name = year_url.strip("/").split("/")[-1]
        year_directory = os.path.join(SAVE_DIRECTORY, year_name)

        # Create folder for the year
        create_save_directory(year_directory)

        # Find paper links
        paper_links = soup.select("a[href^='/paper_files/paper/']")
        print(f"Found {len(paper_links)} papers for year: {year_name}")

        with ThreadPoolExecutor(max_workers=THREAD_POOL_SIZE) as executor:
            for paper_link in paper_links:
                paper_page_url = urljoin(BASE_URL, paper_link["href"])
                executor.submit(process_paper_page, paper_page_url, year_directory)


def main():
    create_save_directory(SAVE_DIRECTORY)

    # Fetch the main page
    print(f"Fetching main page: {BASE_URL}")
    content = fetch_page(BASE_URL)

    if content:
        soup = BeautifulSoup(content, "html.parser")

        # Extract year links
        year_links = soup.select("a[href^='/paper_files/paper/']")
        print(f"Found {len(year_links)} year links.")

        for year_link in year_links:
            year_url = urljoin(BASE_URL, year_link["href"])
            process_year_page(year_url)


if __name__ == "__main__":
    main()
