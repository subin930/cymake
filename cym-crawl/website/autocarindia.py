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

def crawl_car_autocarindia():
    # chrome settings
    print('autocarindia crawling started')
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

    today_date = datetime.datetime.today().strftime("%b %d")

    # car tab
    driver.get("https://www.autocarindia.com/car-news")
    driver.implicitly_wait(2) # 2초 implicity wait

    # getting page html into soup
    html = driver.page_source 
    soup = BeautifulSoup(html, 'html.parser')
    news_url = []

    # appending news urls
    for i in range(1,8) : # originally 11
        if (i%4==0): continue
                                    #  /html/body/section[2]/div/div/div/div[1]/div[1]/div[1]/div[1]/a
        driver.execute_script('window.scrollTo(0, ' + str(400*i) + ')')
        time.sleep(0.5)
        if (i == 1):
            uploaded_date = driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[1]/div[2]/div[1]/p').text[13:19]
        else:
            uploaded_date = (driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[' + str(i) + ']/div[2]/div[1]/p[1]').text)[13:19]
        if (uploaded_date == today_date):
            news_url.append(driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[' + str(i) + ']/div[1]/a').get_attribute('href'))


    # bike tab
    driver.get("https://www.autocarindia.com/bike-news")
    driver.implicitly_wait(2) # 2초 implicity wait

    # getting page html into soup
    html = driver.page_source 
    soup = BeautifulSoup(html, 'html.parser')

    # appending news urls
    for i in range(1,8) : # originally 11
        if (i%4==0): continue
                                    #  /html/body/section[2]/div/div/div/div[1]/div[1]/div[1]/div[1]/a
        driver.execute_script('window.scrollTo(0, ' + str(400*i) + ')')
        time.sleep(0.5)
        if (i == 1):
            uploaded_date = driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[1]/div[2]/div[1]/p').text[13:19]
        else:
            uploaded_date = (driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[' + str(i) + ']/div[2]/div[1]/p[1]').text)[13:19]
        if (uploaded_date == today_date):
            news_url.append(driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[' + str(i) + ']/div[1]/a').get_attribute('href'))

    # motorsports tab
    driver.get("https://www.autocarindia.com/motor-sports-news")
    driver.implicitly_wait(2) # 2초 implicity wait

    # getting page html into soup
    html = driver.page_source 
    soup = BeautifulSoup(html, 'html.parser')

    # appending news urls
    for i in range(1,8) : # originally 11
        if (i%4==0): continue
                                    #  /html/body/section[2]/div/div/div/div[1]/div[1]/div[1]/div[1]/a
        driver.execute_script('window.scrollTo(0, ' + str(400*i) + ')')
        time.sleep(0.5)
        if (i == 1):
            uploaded_date = driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[1]/div[2]/div[1]/p').text[13:19]
        else:
            uploaded_date = (driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[' + str(i) + ']/div[2]/div[1]/p[1]').text)[13:19]
        if (uploaded_date == today_date):
            news_url.append(driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[' + str(i) + ']/div[1]/a').get_attribute('href'))

    # industry tab
    driver.get("https://www.autocarindia.com/industry")
    driver.implicitly_wait(2) # 2초 implicity wait

    # getting page html into soup
    html = driver.page_source 
    soup = BeautifulSoup(html, 'html.parser')

    # appending news urls
    for i in range(1,8) : # originally 11
        if (i%4==0): continue
                                    #  /html/body/section[2]/div/div/div/div[1]/div[1]/div[1]/div[1]/a
        driver.execute_script('window.scrollTo(0, ' + str(400*i) + ')')
        time.sleep(0.5)
        if (i == 1):
            uploaded_date = driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[1]/div[2]/div[1]/p').text[13:19]
        else:
            uploaded_date = (driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[' + str(i) + ']/div[2]/div[1]/p[1]').text)[13:19]
        if (uploaded_date == today_date):
            news_url.append(driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[' + str(i) + ']/div[1]/a').get_attribute('href'))

    # marketplace tab
    driver.get("https://www.autocarindia.com/marketplace-news")
    driver.implicitly_wait(2) # 2초 implicity wait

    # getting page html into soup
    html = driver.page_source 
    soup = BeautifulSoup(html, 'html.parser')

    # appending news urls
    for i in range(1,8) : # originally 11
        if (i%4==0): continue
                                    #  /html/body/section[2]/div/div/div/div[1]/div[1]/div[1]/div[1]/a
        driver.execute_script('window.scrollTo(0, ' + str(400*i) + ')')
        time.sleep(0.5)
        if (i == 1):
            uploaded_date = driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[1]/div[2]/div[1]/p').text[13:19]
        else:
            uploaded_date = (driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[' + str(i) + ']/div[2]/div[1]/p[1]').text)[13:19]
        if (uploaded_date == today_date):
            news_url.append(driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/div[' + str(i) + ']/div[1]/a').get_attribute('href'))


    # save each news's image and article
    for url in news_url:
        print('Now Crawling : autocarindia, https://www.autocarindia.com{}'.format(url))
        date = datetime.datetime.today().strftime("%Y%m%d")
        letters_set = string.ascii_uppercase + string.digits
        random_filetag = date + ''.join(random.sample(letters_set, 10))

        driver.get(url)
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser') 

        #                                          /html/body/section[2]/div/div/div/div[1]/div[1]/article
        image_url = driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/article/div[2]').find_element(By.TAG_NAME, 'img').get_attribute('src')
        urllib.request.urlretrieve(image_url, 'downloadimages/' + random_filetag + '.jpg')

        textfile = open(('savetexts/' + random_filetag + 'raw.txt'), 'w')
        textfile.write(str(url) + "\n")
        title_text = driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/article/h1').text
        textfile.write(str(title_text) + "\n")

        article_text = driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div/div[1]/div[1]/article/div[6]').text
        textfile.write(str(article_text))

        textfile.close()
        textfile = open(('savetexts/' + random_filetag + 'raw.txt'), 'r')
        textfile_final = open(('savetexts/' + random_filetag + '.txt'), 'w')

        file_lines = textfile.readlines()
        for line in file_lines:
            textfile_final.write(str(line))

        textfile.close()
        textfile_final.close()
        os.remove('savetexts/' + random_filetag + 'raw.txt')

    driver.close()