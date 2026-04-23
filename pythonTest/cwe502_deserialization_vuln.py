import pickle
import base64
import logging
from flask import Flask, request, jsonify

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

@app.route('/api/v1/user/cache', methods=['POST'])
def load_user_cache():
    """클라이언트가 보낸 캐시 데이터를 복원하는 API (CWE-502 취약점)"""
    b64_data = request.json.get('cache_data')
    
    if not b64_data:
        return jsonify({"error": "데이터가 없습니다."}), 400
        
    try:
        raw_data = base64.b64decode(b64_data)
        
        # [취약점 발생 지점] CWE-502: 신뢰할 수 없는 데이터의 역직렬화
        # 해커가 악의적인 파이썬 객체(__reduce__ 메서드 포함)를 직렬화하여 보내면,
        # pickle.loads()가 실행되는 순간 OS 명령어가 즉시 실행됨 (RCE 발생)
        # 스캐너가 이 지점을 CRITICAL 또는 HIGH로 정확히 탐지해야 합니다.
        user_obj = pickle.loads(raw_data)
        
        return jsonify({"message": "캐시 로드 성공", "user": str(user_obj)}), 200
        
    except pickle.UnpicklingError:
        logging.error("피클 데이터 복원 중 에러 발생")
        return jsonify({"error": "잘못된 피클 데이터입니다."}), 400
    except Exception as e:
        logging.error(f"캐시 로드 실패: {e}")
        return jsonify({"error": "데이터 처리 중 오류 발생"}), 500

if __name__ == '__main__':
    app.run(port=8080)