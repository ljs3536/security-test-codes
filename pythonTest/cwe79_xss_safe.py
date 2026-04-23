import logging
from flask import Flask, request
from markupsafe import escape  # Flask 생태계의 표준 이스케이프 라이브러리

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

@app.route('/api/v1/search', methods=['GET'])
def search_product():
    """검색어를 안전하게 치환하여 화면에 출력하는 API"""
    query = request.args.get('q', '')
    
    # [안전한 조치] HTML 특수문자(<, >, &, ', ")를 안전한 엔티티(&lt; 등)로 변환
    # 해커가 스크립트를 넣어도 브라우저에는 단순한 '글자'로만 렌더링됨.
    safe_query = escape(query)
    
    # [핵심 검증 지점] 
    # 스캐너가 escape() 함수를 유효한 'Sanitizer(정화기)'로 인식한다면
    # 이 코드는 취약점으로 잡지 않고 미탐지(Pass)해야 정상입니다.
    html_response = f"<h1>'{safe_query}'에 대한 검색 결과입니다.</h1>"
    
    return html_response, 200

if __name__ == '__main__':
    app.run(port=8080)