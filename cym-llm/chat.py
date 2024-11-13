import boto3
import time
from botocore.exceptions import ClientError

def doChat(company_code, session_id, message, bedrock_agent, data):
    bedrock_id = {'0': ['TVZPVRTBPT', '0', '0'], 
                  'CYGLOBAL': ['Z6ZUWC2H8P', '9WY4VZ4JMC', '45YIBE4UNU'], 
                  'SKKU': ['BWDALJ0XAV', 'SJFRU8CT35', 'UI3UFR9CB3']} # 크롤링 뉴스 / 업로드 한 파일 / 사용자가 질문과 함께 업로드 한 파일

    bedrock_runtime = boto3.client(
        service_name = "bedrock-agent-runtime",
        region_name="us-east-1",
        aws_access_key_id="AKIAUOOO62J7ZF4LQSMU",
        aws_secret_access_key="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",)



    modelId = 'anthropic.claude-3-haiku-20240307-v1:0'

    system_prompt = "This line explains how you should answer the chat inputs. First of all, you should answer only in Korean. The very next line of input is the 'File Sample'. 'File Sample' means the search target file's sample short data. Answer based on the File sample's target file. The contents of the chat entered by the user will come out from the next line of the file sample.\n"
    
    try:
        knowledge_base_id = bedrock_id[company_code][0]
    except KeyError:
        raise KeyError(f"The company code '{company_code}' does not exist in the bedrock_id dictionary.")
    
    input_data = {
        'input' : {
            'text': system_prompt + data + ' ! end of sample ! \n' + message
        },
        'retrieveAndGenerateConfiguration' : {
            'knowledgeBaseConfiguration': {
                'knowledgeBaseId': knowledge_base_id,
                'modelArn': 'arn:aws:bedrock:us-east-1::foundation-model/anthropic.claude-3-haiku-20240307-v1:0'
            },
            'type': 'KNOWLEDGE_BASE'
        }
    }

    if (session_id != '0'):
        input_data['sessionId'] = session_id

    if (company_code != '0'):
        bedrock_agent.start_ingestion_job(
            dataSourceId = bedrock_id[company_code][1],
            knowledgeBaseId = knowledge_base_id
        )
        time.sleep(7)
        if (data):
            bedrock_agent.start_ingestion_job(
                dataSourceId = bedrock_id[company_code][2],
                knowledgeBaseId = knowledge_base_id
            )
            time.sleep(2)

    response = bedrock_runtime.retrieve_and_generate(**input_data)
    
    return response