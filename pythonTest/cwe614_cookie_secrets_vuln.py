import logging
from flask import Flask, request, jsonify, make_response

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

SESSION_SIGNING_KEY = "SUPER_SECRET_TOKEN_V1_9982"

@app.route('/api/v1/login', methods=['POST'])
def login():
    """로그인 후 세션 쿠키를 발급하는 API"""
    data = request.json
    
    # 임시 인증 로직
    if data.get('username') == 'admin':
        logging.info("관리자 로그인 성공, 쿠키를 발급합니다.")
        
        response = make_response(jsonify({"message": "로그인 성공"}))
        
        # [취약점 2] CWE-614: 쿠키 발급 시 Secure, HttpOnly 속성 누락
        response.set_cookie(
            'session_id', 
            f'admin_session_data_{SESSION_SIGNING_KEY}'
        )
        return response
        
    return jsonify({"error": "인증 실패"}), 401

if __name__ == '__main__':
    app.run(port=8080)