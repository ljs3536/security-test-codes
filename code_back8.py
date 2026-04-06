import xml.etree.ElementTree as ET
from flask import Flask, request

app = Flask(__name__)

@app.route('/')
def index():
    return '''
        <h1>Real XXE Vulnerability Lab</h1>
        <hr>
        <p>Test if scanner detects REAL XML External Entity (CWE-611) vulnerability.</p>
        <form action="/parse_xml" method="POST">
            Paste XML Data here: <br>
            <textarea name="xml_data" rows="10" cols="50">
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE test [ <!ENTITY xxe SYSTEM "file:///etc/passwd"> ]>
<root><data>&xxe;</data></root>
            </textarea><br>
            <input type="submit" value="Parse XML">
        </form>
    '''

@app.route('/parse_xml', methods=['POST'])
def parse_xml():
    xml_data = request.form.get('xml_data', '')
    
    try:
        root = ET.fromstring(xml_data)
        
        parsed_text = root.find('data').text
        return f"Parsed Data: {parsed_text}"
    except Exception as e:
        return f"Error: {str(e)}"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050, debug=False)