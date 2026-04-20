import os
import subprocess
import ldap
from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/api/v1/network/ping', methods=['GET'])
def network_ping():
    """사용자가 입력한 IP로 핑 테스트 (CMDi 취약점)"""
    target = request.args.get('target')
    
    # [취약점 발생 지점 1] CWE-78: OS Command Injection
    # 해커가 ?target=8.8.8.8; cat /etc/passwd 와 같이 입력 가능
    # 예상 결과: CRITICAL 등급으로 탐지되어야 함 (기존 High에서 상향 확인)
    os.system(f"ping -c 1 {target}")
    
    return jsonify({"message": f"{target}으로 핑을 보냈습니다."}), 200

@app.route('/api/v1/user/search', methods=['GET'])
def user_search():
    """LDAP에서 사용자 정보를 검색 (LDAP Injection)"""
    username = request.args.get('username')
    
    # LDAP 서버 연결 설정 (가상)
    l = ldap.initialize("ldap://localhost:389")
    
    # [취약점 발생 지점 2] CWE-90: LDAP Injection
    # 검증 없이 필터에 사용자 입력을 직접 삽입
    search_filter = f"(uid={username})"
    
    # 예상 결과: CWE-90으로 정확히 탐지되어야 함
    result = l.search_s("dc=example,dc=com", ldap.SCOPE_SUBTREE, search_filter)
    return jsonify({"result": str(result)}), 200

if __name__ == '__main__':
    app.run(port=8080)