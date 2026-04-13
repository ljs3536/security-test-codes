from django.http import HttpResponse
from django.contrib.auth.models import User
from django.utils.html import escape
import subprocess
import os

# 1. SQL Injection 방어 (Safe)
def get_user_info(request):
    username = request.GET.get('user', '')
    # 방어 기법: Django의 내장 ORM 사용 (자동으로 파라미터 바인딩 처리됨)
    # 스캐너 체크 포인트: QuerySet API를 사용할 때 SQLi의 위험이 없다고 판단하는가?
    user_exists = User.objects.filter(username=username).exists()
    return HttpResponse(f"User exists: {user_exists}")

# 2. Command Injection 방어 (Safe)
def system_ping(request):
    target = request.GET.get('target', 'localhost')
    # 방어 기법 1: os.system 대신 subprocess.run 사용 + 쉘 실행 불가(shell=False 기본값)
    # 방어 기법 2: 입력값 검증 (알파벳, 숫자, 점만 허용하는 간단한 필터링 예시)
    if not all(c.isalnum() or c == '.' for c in target):
        return HttpResponse("Invalid target format.")
    
    # 스캐너 체크 포인트: 배열 형태로 명령어를 넘기고 필터링 로직이 있을 때 통과시키는가?
    result = subprocess.run(['ping', '-c', '1', target], capture_output=True, text=True)
    return HttpResponse(f"Ping result: {'Success' if result.returncode == 0 else 'Fail'}")

# 3. XSS 방어 (Safe)
def search_view(request):
    keyword = request.GET.get('keyword', '')
    # 방어 기법: django.utils.html.escape()를 사용하여 HTML 특수문자 치환 (< -> &lt;)
    # 스캐너 체크 포인트: escape() 함수를 거친 데이터(Sanitized Data)를 안전하다고 인식하는가?
    safe_keyword = escape(keyword)
    html_response = f"<h1>검색 결과: {safe_keyword}</h1>"
    return HttpResponse(html_response)

# 4. Path Traversal 방어 (Safe)
def read_log(request):
    filename = request.GET.get('file', 'default.log')
    # 방어 기법: os.path.basename()을 사용하여 상위 경로(../../) 이동 문자를 무력화하고 파일명만 추출
    # 스캐너 체크 포인트: basename()을 거친 후의 파일 접근을 정상적인 흐름으로 보는가?
    safe_filename = os.path.basename(filename)
    file_path = f"/var/log/myapp/{safe_filename}"
    
    try:
        with open(file_path, 'r') as f:
            content = f.read()
    except Exception as e: # 광범위한 예외도 안전하게 로깅 (CWE-778 방어)
        # 실제로는 logging.error(e) 등을 사용해야 함
        content = "Error reading log"
        
    return HttpResponse(escape(content))