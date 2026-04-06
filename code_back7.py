from flask import Flask, request, redirect

app = Flask(__name__)

@app.route('/')
def index():
    return '''
        <h1>Open Redirect Lab</h1>
        <hr>
        <p>Test if scanner detects Open Redirect (CWE-601).</p>
        <ul>
            <li><a href="/login?next=/dashboard">Normal Login (Safe)</a></li>
            <li><a href="/login?next=http://evil-phishing.com">Attacker Link (Vulnerable)</a></li>
        </ul>
    '''


@app.route('/login')
def login():

    next_url = request.args.get('next', '/')
    
    return redirect(next_url)

@app.route('/dashboard')
def dashboard():
    return "Welcome to your secure dashboard!"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050, debug=False)