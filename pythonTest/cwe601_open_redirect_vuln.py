from flask import Flask, request, redirect

app = Flask(__name__)

@app.route('/login')
def login():

    next_url = request.args.get('next')
    if next_url:
        return redirect(next_url)
        
    return "로그인 성공"

if __name__ == "__main__":
    app.run()