import secrets  # 안전한 서드파티/내장 라이브러리
import logging
from flask import Flask, jsonify, make_response

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

@app.route('/api/v1/auth/reset-token', methods=['GET'])
def generate_reset_token():
    """안전하게 토큰을 발급하고 CORS를 제한하는 API"""
    
    # [안전한 조치 1] 암호학적으로 안전한 난수 생성기(CSPRNG) 사용
    # OS에서 제공하는 무작위성(urandom)을 사용하므로 예측이 불가능함
    secure_token = secrets.token_hex(16)
    
    response = make_response(jsonify({"reset_token": secure_token}))
    
    # [안전한 조치 2] 명확하게 신뢰할 수 있는 도메인만 허용 (CORS 제한)
    response.headers['Access-Control-Allow-Origin'] = 'https://trusted-frontend.com'
    
    logging.info("안전한 토큰 발급 완료")
    return response

if __name__ == '__main__':
    app.run(port=8080)