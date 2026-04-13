import requests
from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/api/v1/fetch-image', methods=['GET'])
def fetch_external_image():
    """사용자가 제공한 URL에서 이미지를 다운로드하는 API"""
    
    # 해커가 ?url=http://169.254.169.254/latest/meta-data/ 와 같이 입력 가능
    target_url = request.args.get('url')
    
    if not target_url:
        return jsonify({"error": "URL이 필요합니다."}), 400

    try:
        # 검증 없이 사용자 입력을 외부 요청에 그대로 사용
        response = requests.get(target_url, timeout=5)
        return response.text, 200
        
    except requests.exceptions.RequestException as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(port=8080)