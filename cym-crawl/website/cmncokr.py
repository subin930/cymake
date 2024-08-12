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

def crawl_beauty_cmncokr():
    # chrome settings
    print("cmncokr crawling started")
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
    driver.get("http://www.cmn.kr/main/?ltype=LIST&goPage=1&s_cls_det_cd=&s_cls_cd=") 
    driver.implicitly_wait(2) # 2초 implicity wait

    today_date = datetime.datetime.today().strftime("%Y-%m-%d")

    # getting page html into soup
    html = driver.page_source 
    soup = BeautifulSoup(html, 'html.parser')
    news_url = []

    # appending news urls
    #news_url.append(soup.select_one('#container > div.head_line.sub > div.head_line_left > a').attrs['href'])
    #news_url.append(soup.select_one('#container > div.head_line.sub > div.head_line_right > a').attrs['href'])
    try:
        driver.find_element(By.CSS_SELECTOR, '#tab2 > a').click()
    except:
        print("No Cookie Acception Buttons on CMNCOKR site")
    driver.execute_script('window.scrollTo(0, 700)')
    time.sleep(0.3)
    for i in range(1,11) : # originally 11
        #                                
        news_url.append(soup.select_one('#container > div.in_main > div > div.in_left > div > div > ul > li:nth-child(' + str(i) + ') > a').attrs['href'])


    # save each news's image and article
    for url in news_url:
        print('Now Crawling : cmncokr, https://www.cmn.co.kr{}'.format(url))
        date = datetime.datetime.today().strftime("%Y%m%d")
        letters_set = string.ascii_uppercase + string.digits
        random_filetag = date + ''.join(random.sample(letters_set, 10))

        driver.get('https://www.cmn.co.kr' + url)
        time.sleep(3)
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser')

        uploaded_date = soup.select_one('#container > div.in_sub.no_head > div.in_left > div > div:nth-child(3) > p > span').text[8:18]
        if (uploaded_date != today_date):
            print("Not today's news! Skipping to the next news")
            continue

        image_url = soup.select_one('#container > div.in_sub.no_head > div.in_left > div > div.sub_con').find('img').attrs['src']
        urllib.request.urlretrieve('https://www.cmn.co.kr' + image_url, 'downloadimages/' + random_filetag + '.jpg')

        textfile = open(('savetexts/' + random_filetag + 'raw.txt'), 'w')
        textfile.write('https://www.cmn.co.kr' + str(url) + "\n")
        title_text = soup.select_one('#container > div.in_sub.no_head > div.in_left > div > p.title > strong').text
        textfile.write(str(title_text) + "\n")

        subtitle_text = soup.select_one('#container > div.in_sub.no_head > div.in_left > div > p.s_title').text
        textfile.write(str(subtitle_text) + "\n")

        article_text = soup.select_one('#container > div.in_sub.no_head > div.in_left > div > div.sub_con').text
        textfile.write(str(article_text))

        textfile.close()
        textfile = open(('savetexts/' + random_filetag + 'raw.txt'), 'r')
        textfile_final = open (('savetexts/' + random_filetag + '.txt'), 'w')

        file_lines = textfile.readlines()
        for line in file_lines[:-2]:
            textfile_final.write(str(line))

        textfile.close()
        textfile_final.close()
        os.remove('savetexts/' + random_filetag + 'raw.txt')

    driver.close()