import logging
from flask import Flask, request

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

# [벡터 1] URL Query Parameter 방식을 통한 로그 인젝션
# 예: /api/v1/log/query?username=admin%0A[CRITICAL]+System+Hacked
@app.route('/api/v1/log/query', methods=['GET'])
def log_query_param():
    username = request.args.get('username', 'guest')
    
    # CWE-117: request.args.get() 의 오염된 데이터가 그대로 logging.warning 으로 유입
    logging.warning(f"로그인 시도 (Query): {username}")
    return "Query Logged", 200

# [벡터 2] URL Path Variable 방식을 통한 로그 인젝션
# 예: /api/v1/log/path/admin%0A[CRITICAL]+System+Hacked
@app.route('/api/v1/log/path/<username>', methods=['GET'])
def log_path_param(username):
    
    # CWE-117: Flask 라우터의 Path 변수가 그대로 logging.error 로 유입
    logging.error(f"비정상 접근 (Path): {username}")
    return "Path Logged", 200

# [벡터 3] JSON Body 방식을 통한 로그 인젝션 (이전 테스트 방식)
# 예: {"username": "admin\n[CRITICAL] System Hacked"}
@app.route('/api/v1/log/json', methods=['POST'])
def log_json_body():
    data = request.json or {}
    username = data.get('username', 'guest')
    
    # CWE-117: request.json 의 오염된 데이터가 그대로 logging.info 로 유입
    logging.info(f"사용자 동작 (JSON): {username}")
    return "JSON Logged", 200

if __name__ == '__main__':
    app.run(port=8080)