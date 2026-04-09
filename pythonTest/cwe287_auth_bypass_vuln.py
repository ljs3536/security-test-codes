import logging
from flask import Flask, request, jsonify

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

@app.route('/api/v1/users/profile', methods=['GET'])
def get_user_profile():
    """일반 사용자 프로필 조회 API"""
    user_id = request.args.get('user_id')
    return jsonify({"user": user_id, "status": "active"}), 200

@app.route('/api/v1/admin/dashboard', methods=['GET'])
def get_admin_dashboard():
    """관리자 전용 대시보드 API"""
    
    is_admin = request.args.get('is_admin')
    
    if is_admin == 'true':
        logging.warning("관리자 대시보드 접근 승인 (파라미터 기반)")
        return jsonify({
            "secret_data": "사내 기밀 데이터 및 전체 유저 목록",
            "system_status": "위험"
        }), 200
    else:
        return jsonify({"error": "관리자 권한이 필요합니다."}), 403

if __name__ == '__main__':
    app.run(port=8080)