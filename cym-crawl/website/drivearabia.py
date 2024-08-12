from selenium import webdriver # 크롬 돌릴 webdriver
from selenium.webdriver.common.by import By # 얘네는
from selenium.webdriver.common.keys import Keys # 당장은
from selenium.webdriver.chrome.service import Service # 안씀 (쓸모는 찾아봐야함)
from bs4 import BeautifulSoup # bs4
import time
import requests
import urllib.request
import datetime
import random
import string
import os

def crawl_car_drivearabia():
    # chrome settings
    print('drivearabia crawling started')
    service = Service(executable_path='/home/ubuntu/chromedriver-linux64/chromedriver')
    options = webdriver.ChromeOptions() 
    options.add_argument('start-maximized')	# 크롬 최대화 설정
    options.add_argument('--headless')
    options.add_argument("--window-size=1920,1080")
    options.add_argument('--remote-debugging-port=9222')
    options.add_argument('user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36')
    options.add_argument('--no-sandbox')
    options.add_argument('--disable-dev-shm-usage')
    options.add_argument('--disable-setuid-sandbox')
    options.add_experimental_option('excludeSwitches', ['enable-logging'])
    driver = webdriver.Chrome(service=service, options=options)
    driver.get("https://www.drivearabia.com/news/") 
    driver.implicitly_wait(2) # 2초 implicity wait
    driver.find_element(By.CSS_SELECTOR, '#gdpr-accept').click()

    today_date = datetime.datetime.today().strftime("%B %d, %Y")

    # getting page html into soup
    html = driver.page_source 
    soup = BeautifulSoup(html, 'html.parser')
    news_url = []

    # appending news urls
    for i in range(1,11) : # originally 11
        uploaded_time = soup.select_one('#main > section > div > div:nth-child(2) > div.col-8-half > article:nth-child(' + str(i) + ') > div.post-content > div.post-meta > p > span.post-date').text
        if (uploaded_time != today_date): continue
        news_url.append(soup.select_one('#main > section > div > div:nth-child(2) > div.col-8-half > article:nth-child(' + str(i) + ') > div.post-image > a').attrs['href'])


    # save each news's image and article
    for url in news_url:
        print('Now Crawling : drivearabia, {}'.format(url))
        date = datetime.datetime.today().strftime("%Y%m%d")
        letters_set = string.ascii_uppercase + string.digits
        random_filetag = date + ''.join(random.sample(letters_set, 10))

        driver.get(url)
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser')

        image_url = driver.find_element(By.XPATH, '/html/body/main/section/div/div/div[1]/article/div[1]/a/img').get_attribute('src')
        r = requests.get(image_url, stream=True)
        with open(('downloadimages/' + random_filetag + '.jpg'), "wb") as outfile:
            outfile.write(r.content)

        textfile = open(('savetexts/' + random_filetag + '.txt'), 'w')

        textfile.write(str(url) + "\n")

        article_text = driver.find_element(By.XPATH, '/html/body/main/section/div/div/div[1]/article').text
        textfile.write(str(article_text))
        textfile.close()
    """
        textfile.close()
        textfile = open(('savetexts/' + random_filetag + 'raw.txt'), 'r')
        textfile_final = open (('savetexts/' + random_filetag + '.txt'), 'w')

        file_lines = textfile.readlines()
        for line in file_lines:
            if line.strip('\n ') in ("Also See:", "Also see:"):
                break
            textfile_final.write(str(line))

        textfile.close()
        textfile_final.close()
        os.remove('savetexts/' + random_filetag + 'raw.txt')
    """
    driver.close()