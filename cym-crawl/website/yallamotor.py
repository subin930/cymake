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

def crawl_car_yallamotor():
    # chrome settings
    print('yallamotor crawling started')
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
    options.add_argument("--enable-features=NetworkService,NetworkServiceInProcess")
    options.add_experimental_option('excludeSwitches', ['enable-logging'])
    driver = webdriver.Chrome(service=service, options=options) 
    driver.get("https://yallamotor.com/news") 
    driver.implicitly_wait(2) # 2초 implicity wait

    today_date = datetime.datetime.today().strftime('%d %B %Y')

    # getting page html into soup
    html = driver.page_source 
    soup = BeautifulSoup(html, 'html.parser')
    news_url = []

    # appending news urls
    for i in range(1,11) : # originally 11
        uploaded_date = soup.select_one('#all-news > a:nth-child(' + str(i) + ') > div > div.col.is-8.font14.p0.p20l > div.display-flex.align-items-center.color-gray.m10t > span:nth-child(1)').text
        if (uploaded_date != today_date): continue
        news_url.append(soup.select_one('#all-news > a:nth-child(' + str(i) + ')').attrs['href'])

    driver.close()

    # save each news's image and article
    for url in news_url:
        print('Now Crawling : yallamotor, https://www.yallamotor.com{}'.format(url))
        date = datetime.datetime.today().strftime("%Y%m%d")
        letters_set = string.ascii_uppercase + string.digits
        random_filetag = date + ''.join(random.sample(letters_set, 10))

        news_driver = webdriver.Chrome(service=service, options=options)
        news_driver.get('https://yallamotor.com' + url)
        news_driver.implicitly_wait(5)
        html = news_driver.page_source
        soup = BeautifulSoup(html, 'html.parser') 
        news_driver.close()

        image_url = soup.select_one('#mainContent > div.box.p0.m20t > div > div.col.is-8.p16.leftside > div.main-article-progress > div.mainarticle.card-content > div').select_one('img').attrs['src']
        urllib.request.urlretrieve(image_url, 'downloadimages/' + random_filetag + '.jpg')

        textfile = open(('savetexts/' + random_filetag + '.txt'), 'w')
        textfile.write('https://yallamotor.com' + str(url) + "\n")
        title_text = soup.select_one('#mainContent > div.box.p0.m20t > div > div.col.is-8.p16.leftside > div.main-article-progress > h1').text
        textfile.write(str(title_text) + "\n")

        article_text = soup.select_one('#mainContent > div.box.p0.m20t > div > div.col.is-8.p16.leftside > div.main-article-progress > div.mainarticle.card-content > div').text
        textfile.write(str(article_text))
        textfile.close()
