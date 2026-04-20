import logging
from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import text

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///:memory:'
db = SQLAlchemy(app)
logging.basicConfig(level=logging.INFO)

@app.route('/api/v2/users/search', methods=['GET'])
def search_users_orm():
    keyword = request.args.get('keyword', '')
    
    if not keyword:
        return jsonify({"error": "검색어를 입력하세요."}), 400

    # [안전한 조치] SQLAlchemy의 명명된 파라미터(:keyword) 사용
    safe_query = text("SELECT id, username FROM users WHERE username LIKE :keyword")
    search_pattern = f"%{keyword}%"
    
    try:
        # [회귀 검증] 스캐너가 딕셔너리 형태의 바인딩을 이해하고 미탐지(Pass)해야 함
        result = db.session.execute(safe_query, {"keyword": search_pattern})
        users = [{"id": row[0], "username": row[1]} for row in result]
        
        return jsonify(users), 200
    except Exception as e:
        logging.error(f"DB 에러: {e}")
        return jsonify({"error": "데이터베이스 오류"}), 500

if __name__ == '__main__':
    app.run(port=8080)