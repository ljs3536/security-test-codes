import logging
from flask import Flask, request, jsonify, session

app = Flask(__name__)
app.secret_key = 'SUPER_SECRET_KEY'
logging.basicConfig(level=logging.INFO)

db_users = {1: "Alice", 2: "Bob", 3: "Charlie"}

@app.route('/api/v1/users/<int:user_id>', methods=['DELETE'])
def delete_user(user_id):
    """안전하게 권한을 검증하고 사용자 계정을 삭제하는 API"""
    
    current_user_id = session.get('user_id')
    user_role = session.get('role', 'user')
    
    if not current_user_id:
        return jsonify({"error": "로그인이 필요합니다."}), 401
        
    # [안전한 조치] 인가(Authorization) 검증
    # 요청한 user_id가 본인이 아니면서 동시에 관리자도 아니라면 차단!
    if current_user_id != user_id and user_role != 'admin':
        logging.warning(f"권한 없는 삭제 시도: User {current_user_id} -> Target {user_id}")
        return jsonify({"error": "이 작업을 수행할 권한이 없습니다."}), 403
        
    if user_id in db_users:
        deleted_user = db_users.pop(user_id)
        logging.info(f"사용자 삭제됨: {deleted_user}")
        return jsonify({"message": "계정이 삭제되었습니다."}), 200
    else:
        return jsonify({"error": "사용자를 찾을 수 없습니다."}), 404

if __name__ == '__main__':
    app.run(port=8080)