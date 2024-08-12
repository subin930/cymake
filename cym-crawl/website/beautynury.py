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

def crawl_beauty_beautynury():
    # chrome settings
    print('beautynury crawling started')
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
    driver.get("https://www.beautynury.com/news/lists/cat/10") 
    driver.implicitly_wait(2) # 2초 implicity wait

    today_date = datetime.datetime.today().strftime("%Y-%m-%d")

    # getting page html into soup
    html = driver.page_source 
    soup = BeautifulSoup(html, 'html.parser')
    news_url = []

    # appending news urls
    for i in range(1,15) : # originally 11
        uploaded_date = soup.select_one('#main_con > div > div > div.main_left_con > div > div.news_list_con > div.list_con > ul > li:nth-child(' + str(i) + ') > a > div.info_con > div > span.ns').text[:10]
        if (uploaded_date != today_date): continue
        news_url.append(soup.select_one('#main_con > div > div > div.main_left_con > div > div.news_list_con > div.list_con > ul > li:nth-child(' + str(i) + ') > a').attrs['href'])


    # save each news's image and article
    for url in news_url:
        print('Now Crawling : beautynury, https://www.beautynury.com{}'.format(url))
        date = datetime.datetime.today().strftime("%Y%m%d")
        letters_set = string.ascii_uppercase + string.digits
        random_filetag = date + ''.join(random.sample(letters_set, 10))

        driver.get('https://www.beautynury.com/' + url)
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser') 

        try:
            image_url = soup.select_one('#main_con > div > div > div.main_left_con > div > div.news_view_con > div.contents_con > div.content > div.text.article_view').select_one('figure.image').select_one('img').attrs['src']
            urllib.request.urlretrieve('https://www.beautynury.com/' + image_url, 'downloadimages/' + random_filetag + '.jpg')
        except:
            print(url + " has no image")

        textfile = open(('savetexts/' + random_filetag + 'raw.txt'), 'w')
        textfile.write('https://www.beautynury.com/' + str(url) + "\n")
        title_text = soup.select_one('#main_con > div > div > div.main_left_con > div > div.news_view_con > div.contents_con > div.title_con > strong').text
        textfile.write(str(title_text) + "\n")

        subtitle_text = soup.select_one('#main_con > div > div > div.main_left_con > div > div.news_view_con > div.contents_con > div.title_con > span').text
        textfile.write(str(subtitle_text) + "\n")

        article_text = soup.select_one('#main_con > div > div > div.main_left_con > div > div.news_view_con > div.contents_con > div.content > div.text.article_view').text
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