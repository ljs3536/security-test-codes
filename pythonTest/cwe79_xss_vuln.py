import logging
from flask import Flask, request

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

@app.route('/api/v1/search', methods=['GET'])
def search_product():
    """사용자 검색어를 그대로 화면에 출력하는 API (XSS 취약점)"""
    query = request.args.get('q', '')
    
    # [취약점 발생 지점] CWE-79: Cross-Site Scripting
    # 해커가 ?q=<script>alert(document.cookie)</script> 를 입력하면
    # 브라우저가 이를 데이터가 아닌 '실행 가능한 스크립트'로 해석하여 실행함.
    # 스캐너가 이 f-string 반환 지점을 CWE-79로 정확히 탐지해야 합니다.
    html_response = f"<h1>'{query}'에 대한 검색 결과입니다.</h1>"
    
    return html_response, 200

if __name__ == '__main__':
    app.run(port=8080)