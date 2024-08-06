
import boto3
from botocore.exceptions import ClientError

company_code = input()
session_id = input()
message = input()

bedrock_id = {'0': 'TVZPVRTBPT', '123': 'FWPWSCMGKJ'}

bedrock_runtime = boto3.client(service_name = "bedrock-agent-runtime",
                      region_name="us-east-1",
                      aws_access_key_id="AKIAUOOO62J7ZF4LQSMU",
                      aws_secret_access_key="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",)

modelId = 'anthropic.claude-3-haiku-20240307-v1:0'

system_prompt = "This line explains how you should answer the chat inputs. First of all, you should answer only in Korean. The very next line of input is the 'Company Code'. 'Company Code' refers to the folder name in the 'files/' directory on your knowledge base. In other words, if the company code is 123, it refers to the files/123/'. When you refer to the knowledge base, you should only refer to the 'crawl/' and its lower directories and the directory that fits your company code like 'files/123' if your company code is 123, and files in other directories should never be referred to in the answer generation, even if you need the information. The contents of the chat entered by the user will come out from the next line of the company code.\n"

input_data = {
    'input' : {
        'text': system_prompt + '\n' + message
    },
    'retrieveAndGenerateConfiguration' : {
        'knowledgeBaseConfiguration': {
            'knowledgeBaseId': bedrock_id[company_code],
            'modelArn': 'arn:aws:bedrock:us-east-1::foundation-model/anthropic.claude-3-haiku-20240307-v1:0'
        },
        'type': 'KNOWLEDGE_BASE'
    }
}

if (session_id != '0'):
    input_data['sessionId'] = session_id

response = bedrock_runtime.retrieve_and_generate(**input_data)

print(response)