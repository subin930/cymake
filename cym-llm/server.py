from fastapi import FastAPI, Request, HTTPException
from contextlib import asynccontextmanager
import boto3
import json
from socket import *
import chat
from botocore.exceptions import ClientError


def start():
    print("service is started.")
    global bedrock_agent

    bedrock_agent = boto3.client(
        service_name = "bedrock-agent",
        region_name="us-east-1",
        aws_access_key_id="AKIAUOOO62J7ZF4LQSMU",
        aws_secret_access_key="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",
    )

    print("AWS 클라이언트 초기화 완료")

def shutdown():
    print("service is stopped.")  

@asynccontextmanager
async def lifespan(app: FastAPI):
    # When service starts.
    start()
    
    yield
    
    # When service is stopped.
    shutdown()


app = FastAPI(lifespan=lifespan)


    
    
@app.post("/tospring")
async def to_spring(request: Request):
    try:
        dto_json = await request.json()
        company_code = dto_json.get("companyCode")
        session_id = dto_json.get("sessionId")
        question = dto_json.get("question")
        url = dto_json.get("url")
        data = dto_json.get("data")
        print()
        print(data)

        print(company_code, session_id, question, url)

        result = chat.doChat(company_code, session_id, question, bedrock_agent, data)
        return result

    except KeyError as e:
        print(f"KeyError: {e}")  #KeyError
        raise HTTPException(status_code=400, detail=f"KeyError: {str(e)}")

    except ClientError as e:
        print(f"ClientError: {e}")  # AWS ClientError 세부 정보
        raise HTTPException(status_code=500, detail=f"AWS ClientError: {str(e)}")

    except Exception as e:
        print(f"Exception: {e}")  # 터미널에 일반 예외 정보
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == '__main__':
    import uvicorn
    uvicorn.run(app, host="localhost", port=5000)