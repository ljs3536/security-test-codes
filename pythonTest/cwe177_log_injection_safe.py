import logging
from flask import Flask, request

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

def sanitize_for_log(text):
    """로그 인젝션(CWE-117)을 방어하기 위한 개행 문자 제거 함수"""
    if not text:
        return ""
    # 개행 문자를 언더스코어로 치환하여 로그 구조를 보호함
    return str(text).replace('\n', '_').replace('\r', '_')

@app.route('/api/v1/log/query', methods=['GET'])
def log_query_param():
    # [안전한 조치] Query 파라미터 정화
    username = sanitize_for_log(request.args.get('username', 'guest'))
    logging.warning(f"로그인 시도 (Query): {username}")
    return "Query Logged", 200

@app.route('/api/v1/log/path/<username>', methods=['GET'])
def log_path_param(username):
    # [안전한 조치] Path 파라미터 정화
    safe_username = sanitize_for_log(username)
    logging.error(f"비정상 접근 (Path): {safe_username}")
    return "Path Logged", 200

@app.route('/api/v1/log/json', methods=['POST'])
def log_json_body():
    data = request.json or {}
    # [안전한 조치] JSON Body 데이터 정화
    username = sanitize_for_log(data.get('username', 'guest'))
    logging.info(f"사용자 동작 (JSON): {username}")
    return "JSON Logged", 200

if __name__ == '__main__':
    app.run(port=8080)