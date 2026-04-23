import logging
from flask import Flask, request, jsonify
from werkzeug.middleware.proxy_fix import ProxyFix

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

# [취약점 1] CWE-16 (또는 CWE-346): 잘못된 리버스 프록시 신뢰 설정
# 프록시가 1대(Nginx)뿐인데, 10대의 프록시 체인을 신뢰하도록 잘못 설정함.
# 해커가 "X-Forwarded-For: 127.0.0.1(관리자IP), <해커IP>" 로 헤더를 조작하면
# 서버는 해커를 127.0.0.1로 인식해버리는 IP 스푸핑 취약점 발생!
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
    # [취약점 2] CWE-16 (또는 CWE-200): 불필요한 네트워크 인터페이스 바인딩
    # 프로덕션에서는 WSGI(Gunicorn 등)를 써야 하며, 
    # 내장 개발 서버를 0.0.0.0으로 열어 외부망에 노출시키는 것은 매우 위험함.
    app.run(host='0.0.0.0', port=8080)