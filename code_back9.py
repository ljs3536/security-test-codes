from flask import Flask, request, render_template_string

app = Flask(__name__)

@app.route('/')
def index():
    return '''
        <h1>XSS Test Lab</h1>
        <hr>
        <p>Test if scanner detects Reflected XSS (CWE-79).</p>
        <form action="/search" method="GET">
            Search Keyword: <input type="text" name="query" value="<script>alert('XSS')</script>">
            <input type="submit" value="Search">
        </form>
    '''

@app.route('/search')
def search():
    query = request.args.get('query', '')
    
    html_template = f'''
        <h2>Search Results for: {query}</h2>
        <p>No results found for your search.</p>
        <a href="/">Back to home</a>
    '''
    
    return render_template_string(html_template)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050, debug=False)