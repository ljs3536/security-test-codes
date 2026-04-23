from flask import Flask, jsonify, request, session
from functools import wraps
import logging

app = Flask(__name__)
app.secret_key = 'SUPER_SECRET_KEY'
logging.basicConfig(level=logging.INFO)

def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'user_id' not in session:
            return jsonify({"error": "Unauthorized"}), 401
        return f(*args, **kwargs)
    return decorated_function

def get_sensitive_data(user_id):
    return {"user_id": user_id, "account_balance": 1000000, "status": "VVIP"}

@app.route('/api/v2/users/<int:user_id>/data', methods=['GET'])
@login_required
def get_user_data_v2(user_id):
    return jsonify(get_sensitive_data(user_id)), 200

# [안전한 조치] 유지해야 하는 대체 경로(구버전 API)에도 
# 프레임워크 표준 인증 데코레이터를 확실하게 적용함.
@app.route('/api/v1/users/<int:user_id>/data', methods=['GET'])
@login_required
def get_user_data_v1(user_id):
    return jsonify(get_sensitive_data(user_id)), 200

if __name__ == '__main__':
    app.run(port=8080)