import pickle
import base64
from flask import Flask, request

app = Flask(__name__)

@app.route('/')
def index():
    return '''
        <h1>Insecure Deserialization Lab</h1>
        <hr>
        <p>Pickle Deserialization Test (CWE-502)</p>
        <form action="/load" method="POST">
            Paste Base64 Serialized Data: <br>
            <textarea name="data" rows="5" cols="50"></textarea><br>
            <input type="submit" value="Deserialize and Load">
        </form>
    '''
@app.route('/load', methods=['POST'])
def load_data():
    user_input = request.form.get('data', '')
    
    try:
        raw_data = base64.b64decode(user_input)
        obj = pickle.loads(raw_data)  
        
        return f"Object loaded successfully: {type(obj)}"
    except Exception as e:
        return f"Error: {str(e)}"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050, debug=False)