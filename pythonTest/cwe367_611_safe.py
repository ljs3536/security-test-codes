import os
import defusedxml.ElementTree as ET  # 안전한 서드파티 라이브러리
from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/api/v1/file/update', methods=['POST'])
def update_file():
    filepath = request.json.get('filepath')
    
    # [안전한 조치 1] 원자적(Atomic) 파일 오퍼레이션 (TOCTOU 방어)
    # Check와 Use를 분리하지 않고, OS 레벨에서 예외 처리를 통해 한 번에 시도함
    try:
        # 파일이 존재하고 쓰기 가능한지 시도하며 열기 (LBYL 대신 EAFP 패턴)
        with open(filepath, 'r+') as f:
            f.write("System updated safely.")
        return jsonify({"message": "파일 업데이트 완료"}), 200
    except FileNotFoundError:
        return jsonify({"error": "파일이 존재하지 않습니다."}), 404
    except PermissionError:
        return jsonify({"error": "권한이 없습니다."}), 403


@app.route('/api/v1/xml/parse', methods=['POST'])
def parse_xml():
    xml_data = request.data
    
    try:
        # [안전한 조치 2] defusedxml을 이용한 안전한 파싱 (XXE 방어)
        # 스캐너가 이 라이브러리 사용을 인지하고 XXE 미탐지(Pass)해야 합니다.
        root = ET.fromstring(xml_data)
        return jsonify({"root_tag": root.tag}), 200
    except ET.ParseError:
        return jsonify({"error": "잘못된 XML 형식입니다."}), 400

if __name__ == '__main__':
    app.run(port=8080)