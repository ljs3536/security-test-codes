import os
import sqlite3
import subprocess
from flask import Flask, request, render_template_string

app = Flask(__name__)

# 테스트용 DB 초기화
def init_db():
    conn = sqlite3.connect('test.db')
    cursor = conn.cursor()
    cursor.execute('CREATE TABLE IF NOT EXISTS users (id TEXT, name TEXT)')
    cursor.execute("INSERT OR IGNORE INTO users VALUES ('1', 'Admin')")
    conn.commit()
    conn.close()

@app.route('/')
def index():
    return '''
        <h1>Critical Vulnerability Lab</h1>
        <hr>
        <ul>
            <li><strong>CWE-94 (Code Injection):</strong> <a href="/evalTest?cmd=__import__('os').uname()">Test Eval</a></li>
            <li><strong>CWE-78 (Command Injection):</strong> <a href="/subprocessTest?cmd=whoami">Test Subprocess</a></li>
        </ul>
    '''

@app.route('/evalTest')
def critical_rce():
    user_input = request.args.get('cmd')
    result = eval(user_input) 
    return f"Code Execution Result: {result}"

@app.route('/subprocessTest')
def final_boss():
    cmd = request.args.get('cmd')
    output = subprocess.check_output(cmd, shell=True, stderr=subprocess.STDOUT)
    return f"<pre>OS Command Result:\n{output.decode('cp949', errors='ignore')}</pre>"

# [타겟 3] CWE-1336: Server-Side Template Injection (SSTI)
# 스캐너가 render_template_string 함수의 위험성을 인지하는지 테스트
@app.route('/templateTest')
def ssti_attack():
    name = request.args.get('name', 'Guest')
    
    # ❌ 치명적인 실수: 사용자 입력을 템플릿 문자열에 직접 결합(f-string)한 뒤 렌더링
    template = f"<h2>Hello, {name}!</h2>"
    
    return render_template_string(template)

if __name__ == '__main__':
    init_db()
    app.run(host='0.0.0.0', port=5050, debug=True)