import subprocess
from ldap.filter import filter_format
from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/api/v1/network/ping', methods=['GET'])
def network_ping():
    target = request.args.get('target')
    
    # [안전한 조치 1] shell=False(기본값)를 사용하고 인자를 리스트로 전달
    # 이 방식은 명령어와 인자를 분리하여 쉘 주입 공격을 원천 차단함
    subprocess.run(["ping", "-c", "1", target], check=True)
    
    return jsonify({"message": f"{target}으로 핑을 보냈습니다."}), 200

@app.route('/api/v1/user/search', methods=['GET'])
def user_search():
    username = request.args.get('username')
    
    # [안전한 조치 2] ldap.filter_format을 사용하여 입력을 안전하게 이스케이프
    # 특수 문자가 필터 구조를 깨뜨리지 못하게 함
    search_filter = filter_format("(uid=%s)", [username])
    
    # 스캐너가 이 안전한 라이브러리 사용을 인지하고 미탐지(Pass)해야 함
    return jsonify({"filter_used": search_filter}), 200

if __name__ == '__main__':
    app.run(port=8080)