import sqlite3
import logging
from flask import Flask, request, jsonify

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

def get_db_connection():
    conn = sqlite3.connect('database.db')
    conn.row_factory = sqlite3.Row
    return conn

@app.route('/api/v1/users/search', methods=['GET'])
def search_users():
    keyword = request.args.get('keyword', '')
    
    if not keyword:
        return jsonify({"error": "검색어를 입력하세요."}), 400

    conn = get_db_connection()
    cursor = conn.cursor()

    # [안전한 조치] 쿼리문에는 '?' 기호(바인딩 변수)만 배치하여 구조를 고정
    safe_query = "SELECT id, username, email FROM users WHERE username LIKE ?"
    
    # 검색 패턴 문자열을 튜플 형태로 분리
    search_pattern = f"%{keyword}%"
    
    try:
        # [회귀 검증 지점] 스캐너가 변수 바인딩을 인지하고 미탐지(Pass)해야 함.
        # 이전 버전처럼 탐지해버리면 패치가 잘못된 것임 (오탐)
        cursor.execute(safe_query, (search_pattern,))
        users = cursor.fetchall()
        
        logging.info(f"검색 완료: {len(users)}건 발견")
        return jsonify([dict(ix) for ix in users]), 200
        
    except sqlite3.Error as e:
        logging.error(f"데이터베이스 에러: {e}")
        return jsonify({"error": "데이터베이스 에러 발생"}), 500
    finally:
        conn.close()

if __name__ == '__main__':
    app.run(port=8080)