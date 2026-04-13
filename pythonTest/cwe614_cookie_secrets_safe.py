import os
import logging
from flask import Flask, request, jsonify, make_response

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

SESSION_SIGNING_KEY = os.environ.get("SESSION_SIGNING_KEY", "default-dev-key")

@app.route('/api/v1/login', methods=['POST'])
def login():
    """로그인 후 안전한 세션 쿠키를 발급하는 API"""
    data = request.json
    
    if data.get('username') == 'admin':
        logging.info("관리자 로그인 성공, 안전한 쿠키를 발급합니다.")
        
        response = make_response(jsonify({"message": "로그인 성공"}))
        
        response.set_cookie(
            'session_id', 
            'admin_encrypted_session_data_here',
            secure=True,     # HTTPS 통신에서만 쿠키 전송
            httponly=True,   # JavaScript에서 쿠키 접근 원천 차단
            samesite='Lax'   # CSRF 공격 방어용 속성
        )
        return response
        
    return jsonify({"error": "인증 실패"}), 401

if __name__ == '__main__':
    app.run(port=8080)