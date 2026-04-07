# 파일명: cwe22_path_traversal_vuln.py
import os
from flask import Flask, request, send_from_directory

app = Flask(__name__)
UPLOAD_DIR = "/var/www/uploads"

@app.route('/upload', methods=['POST'])
def upload_file():
    # 사용자가 업로드한 파일과 파일명을 가져옴
    file = request.files['user_file']
    filename = request.form['filename'] 
    
    save_path = os.path.join(UPLOAD_DIR, filename)
    
    file.save(save_path)
    return "파일이 성공적으로 저장되었습니다."

if __name__ == "__main__":
    app.run()