import json
import base64
import logging
from flask import Flask, request, jsonify

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

@app.route('/api/v1/user/cache', methods=['POST'])
def load_user_cache():
    """클라이언트가 보낸 데이터를 안전하게 복원하는 API"""
    b64_data = request.json.get('cache_data')
    
    if not b64_data:
        return jsonify({"error": "데이터가 없습니다."}), 400
        
    try:
        # Base64 디코딩 후 문자열로 변환
        raw_data = base64.b64decode(b64_data).decode('utf-8')
        
        # [안전한 조치] 실행 코드가 포함될 수 없는 순수 데이터 포맷(JSON) 사용
        # 스캐너가 JSON 파싱은 안전한 로직으로 인지하고 미탐지(Pass)해야 합니다.
        user_obj = json.loads(raw_data)
        
        return jsonify({"message": "캐시 로드 성공", "user": user_obj}), 200
        
    except json.JSONDecodeError:
        logging.error("JSON 파싱 에러 발생")
        return jsonify({"error": "잘못된 JSON 형식입니다."}), 400
    except Exception as e:
        logging.error(f"캐시 로드 실패: {e}")
        return jsonify({"error": "데이터 처리 중 오류 발생"}), 500

if __name__ == '__main__':
    app.run(port=8080)