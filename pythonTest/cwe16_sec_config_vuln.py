import logging
from flask import Flask, jsonify

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

# [취약한 환경 설정] CWE-16 (또는 서브 CWE들)
# 스캐너가 이 명시적인 'False' 설정들을 찾아내서 경고해야 합니다.

# 1. CSRF (크로스 사이트 요청 위조) 전역 방어 비활성화
app.config['WTF_CSRF_ENABLED'] = False

# 2. XSS 공격으로 쿠키 탈취가 가능해짐 (HttpOnly 해제)
app.config['SESSION_COOKIE_HTTPONLY'] = False

# 3. HTTP 평문 통신에서도 세션 쿠키가 전송됨 (Secure 해제)
app.config['SESSION_COOKIE_SECURE'] = False

@app.route('/api/v1/health', methods=['GET'])
def health_check():
    return jsonify({"status": "running"}), 200

if __name__ == '__main__':
    app.run(port=8080)