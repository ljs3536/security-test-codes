from flask import Flask, request, redirect
from urllib.parse import urlparse

app = Flask(__name__)
ALLOWED_DOMAIN = "www.our-secure-site.com"

@app.route('/login')
def login():
    next_url = request.args.get('next')
    
    if next_url:
        parsed_url = urlparse(next_url)
        
        if parsed_url.netloc == '' or parsed_url.netloc == ALLOWED_DOMAIN:
            return redirect(next_url)
        else:
            return "외부 도메인으로의 강제 이동은 차단되었습니다.", 403
            
    return "로그인 성공"

if __name__ == "__main__":
    app.run()