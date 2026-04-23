import logging
from flask import Flask, request, jsonify, session

app = Flask(__name__)
app.secret_key = 'SUPER_SECRET_KEY'
logging.basicConfig(level=logging.INFO)

# 가상의 DB (사용자 정보)
db_users = {1: "Alice", 2: "Bob", 3: "Charlie"}

@app.route('/api/v1/users/<int:user_id>', methods=['DELETE'])
def delete_user(user_id):
    """사용자 계정을 삭제하는 API (CWE-285 취약점)"""
    
    # 1. 인증(Authentication) 검사: 로그인은 했는가?
    current_user_id = session.get('user_id')
    if not current_user_id:
        return jsonify({"error": "로그인이 필요합니다."}), 401
        
    # [취약점 발생 지점] CWE-285 / IDOR
    # 2. 인가(Authorization) 검사가 누락됨!
    # 로그인한 Bob(2번)이 URL을 /api/v1/users/1 로 조작해서 호출하면, 
    # Alice(1번)의 계정이 삭제되어버림.
    
    if user_id in db_users:
        deleted_user = db_users.pop(user_id)
        logging.info(f"사용자 삭제됨: {deleted_user}")
        return jsonify({"message": "계정이 삭제되었습니다."}), 200
    else:
        return jsonify({"error": "사용자를 찾을 수 없습니다."}), 404

if __name__ == '__main__':
    app.run(port=8080)