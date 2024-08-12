import website.autocarindia
import website.beautynury
import website.cmncokr
import website.cosinkorea
import website.cosmorning
import website.drivearabia
import website.driveau
import website.gasgoo
import website.yallamotor

import uploading.car
import uploading.beauty

import os
import boto3
import time

os.chdir('cymake-crawl/')

website.autocarindia.crawl_car_autocarindia()
website.drivearabia.crawl_car_drivearabia()
website.driveau.crawl_car_driveau() # image 간헐적 깨짐
website.gasgoo.crawl_car_gasgoo()
website.yallamotor.crawl_car_yallamotor()
uploading.car.upload_car()

website.beautynury.crawl_beauty_beautynury()
website.cmncokr.crawl_beauty_cmncokr()
website.cosinkorea.crawl_beuaty_cosinkorea()
website.cosmorning.crawl_beuaty_cosmorning()
uploading.beauty.upload_beauty()

bedrock_agent = boto3.client(service_name = "bedrock-agent",
                      region_name="us-east-1",
                      aws_access_key_id="AKIAUOOO62J7ZF4LQSMU",
                      aws_secret_access_key="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",)

# knowledge-base-skku-cymake
bedrock_agent.start_ingestion_job(
    dataSourceId = "HVBHCTW1E4",
    knowledgeBaseId = "TVZPVRTBPT"
)

# knowledge-base-skku-cymake-SKKU (Crawl)
bedrock_agent.start_ingestion_job(
    dataSourceId = "KKQP5L1DRD",
    knowledgeBaseId = "BWDALJ0XAV"
)

# knowledge-base-skku-cymake-CYGLOBAL (Crawl)
bedrock_agent.start_ingestion_job(
    dataSourceId = "JOCZKIP99R",
    knowledgeBaseId = "Z6ZUWC2H8P"
)

exit()