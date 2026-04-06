from flask import Flask
app = Flask(__name__)

DB_PASSWORD = "super_secret_password_123!"
AWS_SECRET_KEY = "AKIAIOSFODNN7EXAMPLE"

@app.route('/')
def index():
    return '''
        <h1>Hardcoded Secrets Test</h1>
        <hr>
        <p>Checking if the scanner finds the hidden keys in the source code.</p>
    '''

@app.route('/connect')
def connect():
    return f"Connecting with key: {AWS_SECRET_KEY}"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5060, debug=False)