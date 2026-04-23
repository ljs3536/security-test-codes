import logging
from flask import Flask, jsonify

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

app.config['WTF_CSRF_ENABLED'] = False

app.config['SESSION_COOKIE_HTTPONLY'] = False

app.config['SESSION_COOKIE_SECURE'] = False

@app.route('/api/v1/health', methods=['GET'])
def health_check():
    return jsonify({"status": "running"}), 200

if __name__ == '__main__':
    app.run(port=8080)