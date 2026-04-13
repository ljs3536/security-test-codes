import requests
import urllib.parse
from flask import Flask, request, jsonify

app = Flask(__name__)

# 허용할 도메인 목록(화이트리스트) 정의
ALLOWED_DOMAINS = ["api.github.com", "cdn.our-partner.com"]

@app.route('/api/v1/fetch-data', methods=['GET'])
def fetch_external_data():
    target_url = request.args.get('url')
    
    if not target_url:
        return jsonify({"error": "URL이 필요합니다."}), 400

    try:
        # URL 파싱을 통해 Hostname 추출
        parsed_url = urllib.parse.urlparse(target_url)
        hostname = parsed_url.hostname
        
        if hostname in ALLOWED_DOMAINS:
            response = requests.get(target_url, timeout=5)
            return response.text, 200
        else:
            return jsonify({"error": "허용되지 않은 도메인으로의 요청입니다."}), 403
            
    except requests.exceptions.RequestException as e:
        return jsonify({"error": "요청 처리 중 오류 발생"}), 500

if __name__ == '__main__':
    app.run(port=8080)