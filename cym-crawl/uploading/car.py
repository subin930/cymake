import boto3
import os
import datetime
import json

def s3_connection():
    try:
        # s3 클라이언트 생성
        s3 = boto3.client(
            service_name="s3",
            region_name="us-east-1",
            aws_access_key_id="AKIAUOOO62J76H2VQK5R",
            aws_secret_access_key="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",
        )

    except Exception as e:
        print(e)
    else:
        print("s3 bucket connected! uploading car news crawl data")
        return s3
    
def bedrock_connection():
    try:
        bedrock_runtime = boto3.client(service_name = "bedrock-runtime",
                                       region_name="us-east-1",
                                       aws_access_key_id="AKIAUOOO62J7ZF4LQSMU",
                                       aws_secret_access_key="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",)
    except Exception as e:
        print(e)
    else:
        print("bedrock connected!")
        return bedrock_runtime

def upload_car():
    bedrock_runtime = bedrock_connection()
    s3 = s3_connection()

    date_prefix = datetime.datetime.today().strftime("%Y%m%d")
    prefix_key_article = 'crawl/car/article/' + date_prefix + '/'
    prefix_key_image = 'crawl/car/image/' + date_prefix + '/'
    prefix_key_summary = 'crawl/car/summary/' + date_prefix + '/'
    local_path_article = 'savetexts/'
    local_path_image = 'downloadimages/'
    local_path_summary = 'summaries/'

    try:
        s3.put_object(Bucket="skku-cymake", Key=(prefix_key_article))
    except:
        print("Today's folder for article already exists!\n")
    for filename in os.listdir('savetexts'):
        if (filename == '.gitkeep'): continue
        try:
            print("Making Summary:", filename)
            modelId = 'anthropic.claude-3-haiku-20240307-v1:0'

            # temperature = 높을수록 답변의 랜덤성 증가
            temperature = 0
            # top_p = 높을수록 답변에 사용하는 단어의 종류가 증가 (설명은 이런데 잘은 모르겠음)
            top_p = 1
            # max tokens = 높을수록 답변 길게 나옴. 작으면 짤릴수도 있음, 4096이 최대
            max_tokens_to_generate = 2000

            with open(local_path_article + filename, 'r') as file:
                message = file.read()

            body = json.dumps({
                        "messages": [{'role': "user", "content": "You should answer only in Korean. Your task is to extract at least one and up to three keywords from the next news article and summarize the text concisely in three lines. Write the generated keywords and summary as in the following template in Korean.\n(at least one and up to three keywords)\n1. (Summary Line 1)\n2. (Summary Line 2)\n3. (Summary Line 3)\n\n" + message}],
                        "system": "",
                        "max_tokens": max_tokens_to_generate,
                        "temperature": temperature,
                        "top_p": top_p,
                        "anthropic_version": "bedrock-2023-05-31"
            })

            response = bedrock_runtime.invoke_model(body=body, modelId=modelId, accept="application/json", contentType="application/json")

            response_body = json.loads(response.get('body').read())
            result = response_body.get('content','')[0]['text']

            with open(local_path_summary + filename, 'w') as summary_text:
                summary_text.write(result)

            print("Uploading:", filename)
            s3.upload_file(local_path_article + filename, "skku-cymake", prefix_key_article + filename)
            os.remove(local_path_article + filename)
        except Exception as e:
            print(e)

    try:
        s3.put_object(Bucket="skku-cymake", Key=(prefix_key_image))
    except:
        print("Today's folder for image already exists!\n")
    for filename in os.listdir('downloadimages'):
        if (filename == '.gitkeep'): continue
        try:
            print("Uploading:", filename)
            s3.upload_file(local_path_image + filename, "skku-cymake", prefix_key_image + filename)
            os.remove(local_path_image + filename)
        except Exception as e:
            print(e)

    try:
        s3.put_object(Bucket="skku-cymake", Key=(prefix_key_summary))
    except:
        print("Today's folder for summary already exists!\n")
    for filename in os.listdir('summaries'):
        if (filename == '.gitkeep'): continue
        try:
            print("Uploading:", filename)
            s3.upload_file(local_path_summary + filename, "skku-cymake", prefix_key_summary + filename)
            os.remove(local_path_summary + filename)
        except Exception as e:
            print(e)

"""
upload code manual
try:
    s3.upload_file("testImage.jpg","skku-cymake-crawl","testImageUpload.jpg")
except Exception as e:
    print(e)
"""