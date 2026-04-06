from flask import Flask, request, render_template_string, make_response, redirect, send_file
import sqlite3
import os
import base64
import pickle
import hashlib
import random

app = Flask(__name__)

ADMIN_PASSWORD = "SuperSecretPassword123!"
API_KEY = "sk-1234abcd-5678-efgh"

def generate_weak_session():
    val = str(random.randint(1, 1000))
    return hashlib.md5(val.encode()).hexdigest()

@app.route('/login_cookie')
def set_cookie():
    user_id = request.args.get('user_id')
    is_admin = request.args.get('admin') == 'true'
    
    resp = make_response(f"Login successful for {user_id}")
    resp.set_cookie('sessionID', generate_weak_session())
    resp.set_cookie('role', 'admin' if is_admin else 'user')
    return resp

@app.route('/ping')
def ping():
    ip = request.args.get('ip')
    command = f"ping -c 1 {ip}"
    response = os.popen(command).read()
    return f"<pre>{response}</pre>"

@app.route('/redirect')
def open_redirect():
    target = request.args.get('url')
    return redirect(target)

@app.route('/download')
def download_file():
    filename = request.args.get('filename')
    file_path = os.path.join("uploads", filename)
    try:
        return send_file(file_path)
    except Exception as e:
        return str(e), 500 

@app.route('/load_config', methods=['POST'])
def load_config():
    config_data = request.form.get('config')
    try:
        decoded_data = base64.b64decode(config_data)
        data = pickle.loads(decoded_data) 
        return f"Config loaded: {str(data)}"
    except Exception as e:
        return "Invalid config", 400

@app.route('/user')
def get_user():
    user_id = request.args.get('id')
    conn = sqlite3.connect('test.db')
    cursor = conn.cursor()
    query = f"SELECT name FROM users WHERE id = '{user_id}'"
    cursor.execute(query)
    user = cursor.fetchone()
    conn.close()
    return f"User: {user[0]}" if user else "Not Found"

@app.route('/greet')
def greet():
    name = request.args.get('name', 'Guest')
    return render_template_string(f"<h3>Hello, {name}!</h3>")

if __name__ == '__main__':
    init_db() 
    app.run(host='0.0.0.0', port=5050, debug=True)