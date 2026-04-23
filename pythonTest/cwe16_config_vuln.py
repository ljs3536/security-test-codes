import logging
from flask import Flask, request, jsonify
from werkzeug.middleware.proxy_fix import ProxyFix

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

app.wsgi_app = ProxyFix(app.wsgi_app, x_for=10, x_proto=10)

@app.route('/api/v1/admin/status', methods=['GET'])
def admin_status():
    """관리자만 접근 가능한 상태 확인 API"""
    client_ip = request.remote_addr
    
    if client_ip == '127.0.0.1':
        return jsonify({"status": "All systems operational", "ip": client_ip}), 200
    else:
        logging.warning(f"외부 IP 접근 차단: {client_ip}")
        return jsonify({"error": "Access Denied"}), 403

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)