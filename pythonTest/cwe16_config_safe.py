import logging
from flask import Flask, request, jsonify
from werkzeug.middleware.proxy_fix import ProxyFix

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

# [안전한 설정 1] 정확한 리버스 프록시 대수 명시
# 바로 앞단에 있는 1대의 리버스 프록시(예: Nginx)가 넘겨준 IP만 신뢰함.
# 해커가 X-Forwarded-For 헤더를 조작해도 최상단 프록시가 덮어씌우므로 안전.
app.wsgi_app = ProxyFix(app.wsgi_app, x_for=1, x_proto=1)

@app.route('/api/v1/admin/status', methods=['GET'])
def admin_status():
    client_ip = request.remote_addr
    
    if client_ip == '127.0.0.1':
        return jsonify({"status": "All systems operational", "ip": client_ip}), 200
    else:
        logging.warning(f"외부 IP 접근 차단: {client_ip}")
        return jsonify({"error": "Access Denied"}), 403

if __name__ == '__main__':
    # [안전한 설정 2] 외부망 노출 방지
    # 내장 서버는 반드시 로컬호스트(127.0.0.1)에만 바인딩하여 
    # 리버스 프록시를 통해서만 접근 가능하도록 설정.
    app.run(host='127.0.0.1', port=8080)