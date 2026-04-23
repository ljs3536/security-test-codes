import logging
from flask import Flask, request, render_template_string

app = Flask(__name__)
# 가상의 서버 비밀키 (해커의 타겟)
app.config['SECRET_KEY'] = 'SUPER_SECRET_PRODUCTION_KEY'
logging.basicConfig(level=logging.INFO)

@app.route('/api/v1/greet', methods=['GET'])
def greet_user():
    """사용자 이름을 받아 HTML로 인사하는 API (SSTI 취약점)"""
    name = request.args.get('name', 'Guest')
    
    # [취약점 발생 지점] CWE-1336: Server-Side Template Injection
    # 해커가 ?name={{config.SECRET_KEY}} 라고 입력하면?
    # f-string에 의해 template 문자열 자체가 "<h1>Hello, {{config.SECRET_KEY}}!</h1>"가 됨.
    # 렌더링 엔진이 이걸 파싱하면서 서버의 비밀키가 화면에 그대로 노출됨!
    template = f"<h1>Hello, {name}!</h1>"
    
    # 스캐너가 render_template_string에 오염된 문자열이 들어가는 이 지점을 탐지해야 합니다.
    return render_template_string(template)

if __name__ == '__main__':
    app.run(port=8080)