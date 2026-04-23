import logging
from flask import Flask, request, jsonify

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

# 가상의 상품 DB (서버의 진실)
DB_ITEMS = {"item_1": 50000, "item_2": 30000}

@app.route('/api/v1/checkout', methods=['POST'])
def checkout():
    """상품 결제 및 로그를 남기는 API (취약점 2개 포함)"""
    data = request.json
    item_id = data.get('item_id')
    
    client_provided_price = data.get('price') 
    
    note = data.get('note', '')
    
    logging.info(f"결제 진행 - Item: {item_id}, Price: {client_provided_price}, Note: {note}")
    
    return jsonify({"status": "결제 성공", "amount_charged": client_provided_price}), 200

if __name__ == '__main__':
    app.run(port=8080)