import os
import xml.etree.ElementTree as ET
from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/api/v1/file/update', methods=['POST'])
def update_file():
    """사용자가 지정한 파일을 업데이트하는 API (TOCTOU 취약점)"""
    filepath = request.json.get('filepath')
    
    # [취약점 1] CWE-367: TOCTOU (Time-of-Check to Time-of-Use)
    # 검사(Check) 지점
    if os.path.exists(filepath):
        # 🚨 이 주석이 있는 찰나의 시간에 해커가 파일을 심볼릭 링크(예: /etc/passwd)로 바꿔치기하면?
        
        # 사용(Use) 지점: 권한 없는 시스템 파일이 덮어씌워짐
        # 스캐너가 이 if문과 open 사이의 흐름을 CWE-367로 탐지해야 합니다.
        with open(filepath, 'w') as f:
            f.write("System updated by user.")
        return jsonify({"message": "파일 업데이트 완료"}), 200
        
    return jsonify({"error": "파일이 존재하지 않습니다."}), 404


@app.route('/api/v1/xml/parse', methods=['POST'])
def parse_xml():
    """외부 XML 데이터를 파싱하는 API (XXE 취약점)"""
    xml_data = request.data
    
    # [취약점 2] CWE-611: XXE (XML External Entity)
    # 파이썬 내장 기본 파서는 외부 엔티티(External Entity) 공격에 취약함
    # 해커가 <!ENTITY xxe SYSTEM "file:///etc/passwd"> 를 보내면 시스템 파일이 읽힘
    # 스캐너가 정확히 아래 라인을 CWE-611로 짚어내야 합니다.
    root = ET.fromstring(xml_data)
    
    return jsonify({"root_tag": root.tag}), 200

if __name__ == '__main__':
    app.run(port=8080)