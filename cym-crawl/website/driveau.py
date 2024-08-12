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

def crawl_car_driveau():
    # chrome settings
    print('driveau crawling started')
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
    driver.get("https://www.drive.com.au/news/") 
    driver.implicitly_wait(2) # 2초 implicity wait

    today_date = datetime.datetime.today().strftime("%d %b %Y")
    if (today_date[0] == '0'): today_date = today_date[1:]

    # getting page html into soup
    html = driver.page_source 
    soup = BeautifulSoup(html, 'html.parser')
    news_url = []

    # appending news urls
    for i in range(1,12) : # originally 11
        if (i == 7): continue # idk why but this works (maybe the 7th div is ad)
        uploaded_date = driver.find_element(By.XPATH, '/html/body/div[1]/div/div[3]/div[2]/div/div[1]/div[3]/div[1]/div[3]/div/div/div[' + str(i) + ']/div/div[1]/span/span/span[2]').text
        if (uploaded_date == today_date):
            news_url.append((driver.find_element(By.XPATH, '/html/body/div[1]/div/div[3]/div[2]/div/div[1]/div[3]/div[1]/div[3]/div/div/div[' + str(i) + ']/div/div[2]/div[1]/h3/a')).get_attribute('href'))
            #                                               /html/body/div[1]/div/div[3]/div[2]/div/div[1]/div[3]/div[1]/div[3]/div/div/div[1]
        driver.execute_script('window.scrollTo(0, ' + str(400*i) + ')')
        time.sleep(0.5)


    # save each news's image and article
    for url in news_url:
        print('Now Crawling : driveau, {}'.format(url))
        date = datetime.datetime.today().strftime("%Y%m%d")
        letters_set = string.ascii_uppercase + string.digits
        random_filetag = date + ''.join(random.sample(letters_set, 10))

        driver.get(url)
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser') 

        for i in range(20):
            driver.execute_script('window.scrollTo(0, ' + str(500*i) + ')')
            time.sleep(0.2)
        image_urls = (driver.find_element(By.XPATH, '/html/body/div[1]/div/div[3]/div[2]/div/div[2]/div[1]').find_elements(By.TAG_NAME, 'img'))
        image_url = image_urls[3].get_attribute('src')
        urllib.request.urlretrieve(image_url, 'downloadimages/' + random_filetag + '.jpg')

        textfile = open(('savetexts/' + random_filetag + 'raw.txt'), 'w')

        textfile.write(str(url) + "\n")

        #                                             
        article_text = driver.find_element(By.XPATH, '/html/body/div[1]/div/div[3]/div[2]/div/div[2]/div[1]').text
        textfile.write(str(article_text))

        textfile.close()
        textfile = open(('savetexts/' + random_filetag + 'raw.txt'), 'r')
        textfile_final = open (('savetexts/' + random_filetag + '.txt'), 'w')

        file_lines = textfile.readlines()
        for idx, line in enumerate(file_lines):
            if (idx == 1): continue
            if (idx >= 4 and idx <= 11): continue
            if line.strip('\n ') in ("MORE:", "More:", "Journalist"):
                break
            textfile_final.write(str(line))

        textfile.close()
        textfile_final.close()
        os.remove('savetexts/' + random_filetag + 'raw.txt')

    driver.close()