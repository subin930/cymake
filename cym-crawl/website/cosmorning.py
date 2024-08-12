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

def crawl_beuaty_cosmorning():
    # chrome settings
    print('cosmorning crawling started')
    options = webdriver.ChromeOptions()
    service = Service(executable_path='/home/ubuntu/chromedriver-linux64/chromedriver')
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
    driver.get("https://www.cosmorning.com/news/section_list_all.html?sec_no=36&page=1") 
    driver.implicitly_wait(2) # 2초 implicity wait

    today_date = datetime.datetime.today().strftime("%Y-%m-%d")

    # getting page html into soup
    html = driver.page_source 
    soup = BeautifulSoup(html, 'html.parser')
    news_url = []

    # appending news urls
    for i in range(1,13) : # originally 11
        uploaded_date = soup.select_one('#container > div > div:nth-child(1) > div > div.ara_001 > ul > li:nth-child(' + str(i) + ') > a > ul > li.date').text[:10]
        if (uploaded_date != today_date): continue
        news_url.append(soup.select_one('#container > div > div:nth-child(1) > div > div.ara_001 > ul > li:nth-child(' + str(i) + ') > a').attrs['href'])


    # save each news's image and article
    for url in news_url:
        print('Now Crawling : cosmorning, https://www.cosmorning.com{}'.format(url))
        date = datetime.datetime.today().strftime("%Y%m%d")
        letters_set = string.ascii_uppercase + string.digits
        random_filetag = date + ''.join(random.sample(letters_set, 10))

        driver.get('https://www.cosmorning.com' + url)
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser') 

        try:
            image_url = soup.select_one('#container > div > div:nth-child(1) > div > div:nth-child(2) > div > div.cnt_view.news_body_area').select_one('img').attrs['src']
            urllib.request.urlretrieve('https:' + image_url, 'downloadimages/' + random_filetag + '.jpg')
        except:
            print('No image for news url : {}'.format('https://www.cosmorning.com' + url))

        textfile = open(('savetexts/' + random_filetag + 'raw.txt'), 'w')
        textfile.write('https://www.cosmorning.com' + str(url) + "\n")
        title_text = soup.select_one('#container > div > div:nth-child(1) > div > div:nth-child(2) > div > div.fix_art_top > div > div > h2').text
        textfile.write(str(title_text) + "\n")

        subtitle_text = soup.select_one('#container > div > div:nth-child(1) > div > div:nth-child(2) > div > div.fix_art_top > div > div > h3').text
        textfile.write(str(subtitle_text) + "\n")

        article_text = soup.select_one('#container > div > div:nth-child(1) > div > div:nth-child(2) > div > div.cnt_view.news_body_area').text
        textfile.write(str(article_text))

        textfile.close()
        textfile = open(('savetexts/' + random_filetag + 'raw.txt'), 'r')
        textfile_final = open (('savetexts/' + random_filetag + '.txt'), 'w')

        file_lines = textfile.readlines()
        for line in file_lines[:-1]:
            textfile_final.write(str(line))

        textfile.close()
        textfile_final.close()
        os.remove('savetexts/' + random_filetag + 'raw.txt')

    driver.close()