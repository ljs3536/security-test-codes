import requests
from flask import Flask, request

app = Flask(__name__)

@app.route('/')
def index():
    return '''
        <h1>SSRF Test Lab</h1>
        <hr>
        <p>Test if scanner detects Server-Side Request Forgery.</p>
        <form action="/proxy" method="GET">
            Image URL to fetch: <input type="text" name="url" value="http://example.com/image.png" size="50"><br>
            <input type="submit" value="Fetch">
        </form>
    '''

@app.route('/proxy')
def proxy_request():
    target_url = request.args.get('url', 'http://example.com')
    
    try:
        response = requests.get(target_url, timeout=3)
        return f"Successfully fetched {len(response.content)} bytes from target."
    except Exception as e:
        return f"Error: {str(e)}"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050, debug=False)