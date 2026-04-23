import logging
from flask import Flask, jsonify

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

# [안전한 환경 설정] 
# 스캐너가 정상 설정으로 인지하고 통과시켜야 합니다.

# 1. CSRF 방어 활성화 (Flask-WTF 등 사용 시 기본값)
app.config['WTF_CSRF_ENABLED'] = True

# 2. 자바스크립트에서 쿠키 접근 차단 (XSS 방어)
app.config['SESSION_COOKIE_HTTPONLY'] = True

# 3. HTTPS 통신에서만 쿠키 전송 (스니핑 방어)
app.config['SESSION_COOKIE_SECURE'] = True

@app.route('/api/v1/health', methods=['GET'])
def health_check():
    return jsonify({"status": "running"}), 200

if __name__ == '__main__':
    app.run(port=8080)