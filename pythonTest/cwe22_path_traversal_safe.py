# 파일명: cwe22_path_traversal_safe.py
import os
from flask import Flask, request
from werkzeug.utils import secure_filename

app = Flask(__name__)
UPLOAD_DIR = "/var/www/uploads"

@app.route('/upload', methods=['POST'])
def upload_file():
    file = request.files['user_file']
    raw_filename = request.form['filename']
    
    safe_name = secure_filename(raw_filename)
    
    save_path = os.path.join(UPLOAD_DIR, safe_name)
    
    if not os.path.abspath(save_path).startswith(os.path.abspath(UPLOAD_DIR)):
        return "허용되지 않는 파일 경로입니다.", 403
        
    file.save(save_path)
    return "파일이 안전하게 저장되었습니다."

if __name__ == "__main__":
    app.run()