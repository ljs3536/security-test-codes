import logging
from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import text

app = Flask(__name__)
# 메모리 DB 설정 (테스트용)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///:memory:'
db = SQLAlchemy(app)
logging.basicConfig(level=logging.INFO)

@app.route('/api/v2/users/search', methods=['GET'])
def search_users_orm():
    """SQLAlchemy ORM을 사용하는 검색 API"""
    keyword = request.args.get('keyword', '')
    
    if not keyword:
        return jsonify({"error": "검색어를 입력하세요."}), 400

    # [취약점 발생 지점] ORM을 쓰더라도 text() 안에 f-string을 쓰면 SQLi 발생!
    # 스캐너가 이 부분을 기존 sqlite3 때처럼 '단 1건'의 CWE-89로 탐지해야 합니다.
    dangerous_query = text(f"SELECT id, username FROM users WHERE username LIKE '%{keyword}%'")
    
    try:
        # [실행 지점] 여기서 중복 탐지가 일어나지 않아야 함
        result = db.session.execute(dangerous_query)
        users = [{"id": row[0], "username": row[1]} for row in result]
        
        return jsonify(users), 200
    except Exception as e:
        logging.error(f"DB 에러: {e}")
        return jsonify({"error": "데이터베이스 오류"}), 500

if __name__ == '__main__':
    app.run(port=8080)