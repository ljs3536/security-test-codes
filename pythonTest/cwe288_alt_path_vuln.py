from flask import Flask, jsonify, request, session
from functools import wraps
import logging

app = Flask(__name__)
app.secret_key = 'SUPER_SECRET_KEY'
logging.basicConfig(level=logging.INFO)

# 프레임워크 표준 인증 데코레이터
def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'user_id' not in session:
            return jsonify({"error": "Unauthorized"}), 401
        return f(*args, **kwargs)
    return decorated_function

# 중요 데이터를 반환하는 내부 비즈니스 로직
def get_sensitive_data(user_id):
    return {"user_id": user_id, "account_balance": 1000000, "status": "VVIP"}

# [정상 경로] 최신 API: 인증 데코레이터가 잘 적용됨
@app.route('/api/v2/users/<int:user_id>/data', methods=['GET'])
@login_required
def get_user_data_v2(user_id):
    return jsonify(get_sensitive_data(user_id)), 200

# [취약점 발생 지점] CWE-288: 대체 경로(Alternate Path) 방치
# 구버전 API를 삭제하지 않고 남겨두었으며, 여기에 @login_required가 빠져있음.
# 해커는 v2 대신 v1 경로로 접근하여 인증 없이 민감 데이터에 접근 가능!
@app.route('/api/v1/users/<int:user_id>/data', methods=['GET'])
def get_user_data_v1(user_id):
    logging.warning(f"누군가 구버전 API를 통해 {user_id}의 데이터를 조회함!")
    return jsonify(get_sensitive_data(user_id)), 200

if __name__ == '__main__':
    app.run(port=8080)