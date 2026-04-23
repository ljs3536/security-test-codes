import logging
from flask import Flask, request, render_template_string

app = Flask(__name__)
app.config['SECRET_KEY'] = 'SUPER_SECRET_PRODUCTION_KEY'
logging.basicConfig(level=logging.INFO)

@app.route('/api/v1/greet', methods=['GET'])
def greet_user():
    name = request.args.get('name', 'Guest')
    
    # [안전한 조치] 템플릿의 '구조'를 고정. 사용자 입력값을 템플릿 내부에 직접 더하지 않음.
    safe_template = "<h1>Hello, {{ user_name }}!</h1>"
    
    # render_template_string의 '컨텍스트 변수(user_name)'로 값을 전달
    # 해커가 {{config}}를 입력해도 그저 "{{config}}"라는 글자로만 출력됨 (안전함)
    # 스캐너가 이 안전한 데이터 바인딩 로직을 인지하고 미탐지(Pass)해야 합니다.
    return render_template_string(safe_template, user_name=name)

if __name__ == '__main__':
    app.run(port=8080)