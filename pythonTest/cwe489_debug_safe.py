import os
import logging
from flask import Flask, jsonify, request

app = Flask(__name__)

# 환경변수 FLASK_ENV를 확인하여 운영(production) 환경인지 판별
# 기본값은 'production'으로 설정하여 보수적으로 접근
ENV = os.environ.get('FLASK_ENV', 'production')

if ENV == 'development':
    logging.basicConfig(level=logging.DEBUG)
    IS_DEBUG = True
else:
    logging.basicConfig(level=logging.INFO)
    IS_DEBUG = False

@app.route('/api/v1/system/status', methods=['GET'])
def system_status():
    status_info = {
        "status": "operational",
        "version": "1.0.4",
        "environment": ENV
    }
    return jsonify(status_info), 200

@app.route('/api/v1/user/info', methods=['POST'])
def get_user_info():
    user_id = request.json.get('user_id')
    if not user_id:
        # 안전한 에러 핸들링
        return jsonify({"error": "user_id가 필요합니다."}), 400
    
    return jsonify({"user_id": user_id, "name": "Test User"}), 200

if __name__ == '__main__':
    logging.info(f"서버를 시작합니다. (환경: {ENV}, 디버그 모드: {IS_DEBUG})")
    
    # [안전한 조치] 하드코딩된 True가 아닌 변수를 통해 디버그 모드 제어
    # 예상 결과: 미탐지 (안전한 설정으로 간주해야 함)
    app.run(host='0.0.0.0', port=8080, debug=IS_DEBUG)