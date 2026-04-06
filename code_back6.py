import os
from flask import Flask, request

app = Flask(__name__)
UPLOAD_FOLDER = 'uploads'

@app.route('/')
def index():
    return '''
        <h1>File Upload Vulnerability Lab</h1>
        <hr>
        <p>Test if scanner detects unrestricted file upload (Web Shell / CWE-434).</p>
        <form action="/upload" method="POST" enctype="multipart/form-data">
            Select file to upload: <input type="file" name="file"><br>
            <input type="submit" value="Upload">
        </form>
    '''
@app.route('/upload', methods=['POST'])
def upload_file():
    if 'file' not in request.files:
        return "No file part"
    
    file = request.files['file']
    if file.filename == '':
        return "No selected file"
        
    if file:
        filepath = os.path.join(UPLOAD_FOLDER, file.filename)
        file.save(filepath)
        
        return f"File successfully saved to: {filepath}"

if __name__ == '__main__':
    if not os.path.exists(UPLOAD_FOLDER):
        os.makedirs(UPLOAD_FOLDER)
    app.run(host='0.0.0.0', port=5050, debug=False)