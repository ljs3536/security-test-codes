import logging
from flask import Flask, request, jsonify, session
import os

app = Flask(__name__)
# 실제 환경에서는 강력한 난수로 설정해야 함
app.secret_key = os.environ.get('FLASK_SECRET_KEY', 'default-dev-key')
logging.basicConfig(level=logging.INFO)

@app.route('/api/v1/auth/login', methods=['POST'])
def login():
    """가상의 로그인 처리 API"""
    data = request.json

    if data.get('username') == 'admin' and data.get('password') == 'supersecret':
        session['user_role'] = 'admin' 
        return jsonify({"message": "로그인 성공"}), 200
    return jsonify({"error": "인증 실패"}), 401

@app.route('/api/v1/admin/dashboard', methods=['GET'])
def get_admin_dashboard():
    """관리자 전용 대시보드 API"""
    
    user_role = session.get('user_role')
    
    if user_role == 'admin':
        logging.info("관리자 대시보드 접근 승인 (세션 기반)")
        return jsonify({
            "secret_data": "사내 기밀 데이터 및 전체 유저 목록",
            "system_status": "안전"
        }), 200
    else:
        return jsonify({"error": "관리자 권한이 필요합니다. 먼저 로그인하세요."}), 403

if __name__ == '__main__':
    app.run(port=8080)