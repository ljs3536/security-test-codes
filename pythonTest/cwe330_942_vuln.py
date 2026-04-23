import random
import string
import logging
from flask import Flask, jsonify, make_response

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

@app.route('/api/v1/auth/reset-token', methods=['GET'])
def generate_reset_token():
    """비밀번호 초기화 토큰을 발급하는 API (취약점 2개 포함)"""
    
    # [취약점 1] CWE-330: 예측 가능한 난수 사용 (Insufficiently Random Values)
    # random 모듈은 통계적 시뮬레이션용(Mersenne Twister)이므로 해커가 다음 토큰을 예측할 수 있음!
    letters = string.ascii_letters + string.digits
    insecure_token = ''.join(random.choice(letters) for i in range(32))
    
    response = make_response(jsonify({"reset_token": insecure_token}))
    
    # [취약점 2] CWE-942: 과도하게 허용된 CORS 정책
    # 누구나(Access-Control-Allow-Origin: *) 이 API를 호출해서 토큰을 가로챌 수 있음!
    response.headers['Access-Control-Allow-Origin'] = '*'
    
    logging.info("취약한 토큰 발급 완료")
    return response

if __name__ == '__main__':
    app.run(port=8080)