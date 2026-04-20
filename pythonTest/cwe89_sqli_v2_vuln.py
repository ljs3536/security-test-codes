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
    """사용자 이름을 검색하는 API"""
    keyword = request.args.get('keyword', '')
    
    if not keyword:
        return jsonify({"error": "검색어를 입력하세요."}), 400

    conn = get_db_connection()
    cursor = conn.cursor()

    # [데이터 오염 발생 지점] 외부 입력을 문자열 포매팅으로 쿼리에 삽입
    # 이전 버전(v1)에서는 이 라인과 아래 execute 라인에서 중복 알람이 떴을 수 있음
    raw_query = f"SELECT id, username, email FROM users WHERE username LIKE '%{keyword}%'"
    
    try:
        # [취약점 실행 지점 - Sink]
        # v2 패치 후: 이 파일 전체에서 SQL 인젝션(CWE-89)은 딱 '1건'만 크리티컬로 탐지되어야 함!
        cursor.execute(raw_query)
        users = cursor.fetchall()
        
        logging.info(f"검색 완료: {len(users)}건 발견")
        return jsonify([dict(ix) for ix in users]), 200
        
    except sqlite3.Error as e:
        # 품질 룰(CWE-778) 예외 처리 로깅
        logging.error(f"데이터베이스 에러: {e}")
        return jsonify({"error": "데이터베이스 에러 발생"}), 500
    finally:
        conn.close()

if __name__ == '__main__':
    app.run(port=8080)