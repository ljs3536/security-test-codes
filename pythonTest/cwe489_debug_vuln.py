import logging
from flask import Flask, jsonify, request

app = Flask(__name__)
logging.basicConfig(level=logging.DEBUG)

@app.route('/api/v1/system/status', methods=['GET'])
def system_status():
    """시스템 상태를 반환하는 간단한 API"""
    status_info = {
        "status": "operational",
        "version": "1.0.4",
        "maintenance_mode": False
    }
    return jsonify(status_info), 200

@app.route('/api/v1/user/info', methods=['POST'])
def get_user_info():
    """사용자 정보를 처리하는 API (에러 발생 시 디버그 페이지 노출 위험)"""
    user_id = request.json.get('user_id')
    if not user_id:
        # 디버그 모드일 경우 에러 발생 시 시스템 내부 정보가 화면에 그대로 출력됨
        raise ValueError("user_id가 제공되지 않았습니다.")
    
    return jsonify({"user_id": user_id, "name": "Test User"}), 200

if __name__ == '__main__':
    logging.info("서버를 시작합니다...")
    # [취약점 발생 지점] debug 플래그가 True로 하드코딩되어 있음
    # 예상 결과: CWE-489 (또는 유사한 설정 취약점)로 탐지되어야 함
    app.run(host='0.0.0.0', port=8080, debug=True)