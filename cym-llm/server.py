from flask import Flask, request, render_template
import boto3, json
from werkzeug.utils import secure_filename
from socket import *
import chat

app = Flask(__name__)

@app.route("/tospring", methods = ['POST'])
def to_spring():
    dto_json = request.get_json()
    #dto_json을 dumps 메서드 사용하여 response에 저장 -> json을 문자열로 변환

    company_code = dto_json.get("companyCode")
    session_id = dto_json.get("sessionId")
    question = dto_json.get("question")
    url = dto_json.get("url")

    #Spring에서 받은 데이터를 출력해서 확인
    print(company_code, session_id, question, url)
    
    result = chat.doChat(company_code, session_id, question)
    try:
        # Spring에서 받은 데이터를 출력해서 확인
        result = chat.doChat(company_code, session_id, question)
        return json.dumps(result, ensure_ascii=False), 200
    except KeyError as e:
        return json.dumps({"error": f"KeyError: {str(e)}"}, ensure_ascii=False), 400
    except Exception as e:
        return json.dumps({"error": str(e)}, ensure_ascii=False), 500

if __name__ == '__main__':
    app.run(debug=True, host = "localhost", port = "5000")