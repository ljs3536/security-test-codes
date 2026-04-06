from flask import Flask, request, render_template_string, send_file
import os

app = Flask(__name__)

IMAGE_DIR = os.path.join(os.getcwd(), 'images')

if not os.path.exists(IMAGE_DIR):
    os.makedirs(IMAGE_DIR)
    with open(os.path.join(IMAGE_DIR, 'test_image.txt'), 'w') as f:
        f.write("This is a test image file content.")

@app.route('/')
def index():
    files = ['test_image.txt']
    
    html_template = '''
        <h1>Vulnerable Image Viewer</h1>
        <hr>
        <h3>Available Images:</h3>
        <ul>
            {% for file in files %}
                <li><a href="/view?filename={{ file }}">{{ file }}</a></li>
            {% endfor %}
        </ul>
        <p>Test Path Traversal (CWE-22) here.</p>
    '''
    return render_template_string(html_template, files=files)

@app.route('/view')
def view_image():
    filename = request.args.get('filename', '')
    
    if not filename:
        return "Filename is required.", 400

    try:
        file_path = os.path.join(IMAGE_DIR, filename)
        
        if not os.path.isfile(file_path):
             return f"File not found: {filename}", 404

        return send_file(file_path)

    except Exception as e:
        return f"Internal Server Error: {str(e)}", 500

if __name__ == '__main__':
    print(f"[*] Starting vulnerable app on port 5050...")
    print(f"[*] Try attacking: http://localhost:5050/view?filename=../vulnerable_app.py")
    app.run(host='0.0.0.0', port=5050, debug=False)