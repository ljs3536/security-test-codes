import logging
from flask import Flask, request, jsonify

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

DB_ITEMS = {"item_1": 50000, "item_2": 30000}

@app.route('/api/v1/checkout', methods=['POST'])
def checkout():
    """서버에서 재검증하고 로그를 정화하는 안전한 결제 API"""
    data = request.json
    item_id = data.get('item_id')
    
    if item_id not in DB_ITEMS:
        return jsonify({"error": "존재하지 않는 상품입니다."}), 404

    actual_price = DB_ITEMS[item_id]
    
    note = data.get('note', '')
    
    safe_note = note.replace('\n', ' ').replace('\r', ' ')
    
    logging.info(f"결제 진행 - Item: {item_id}, Price: {actual_price}, Note: {safe_note}")
    
    return jsonify({"status": "결제 성공", "amount_charged": actual_price}), 200

if __name__ == '__main__':
    app.run(port=8080)