from django.http import HttpResponse
from django.db import connection
import os

# 1. SQL Injection (CWE-89)
# Django ORM 대신 Raw Query를 사용하며 사용자 입력을 직접 포맷팅함
def get_user_info(request):
    username = request.GET.get('user', '')
    with connection.cursor() as cursor:
        # 분석기 체크 포인트: request.GET이 cursor.execute로 바로 들어가는가?
        query = f"SELECT * FROM auth_user WHERE username = '{username}'"
        cursor.execute(query)
        row = cursor.fetchone()
    return HttpResponse(f"User info: {row}")

# 2. Command Injection (CWE-78)
# 사용자 입력값을 검증 없이 OS 시스템 명령어에 포함함
def system_ping(request):
    target = request.GET.get('target', 'localhost')
    # 분석기 체크 포인트: target 변수가 os.system의 인자로 전달되는 흐름을 탐지하는가?
    command = f"ping -c 1 {target}"
    os.system(command)
    return HttpResponse(f"Pinged {target}")

# 3. Reflected Cross-Site Scripting (XSS) (CWE-79)
# 템플릿 엔진의 자동 이스케이프를 거치지 않고 사용자 입력을 그대로 렌더링함
def search_view(request):
    keyword = request.GET.get('keyword', '')
    # 분석기 체크 포인트: 검증되지 않은 keyword가 HttpResponse에 직접 렌더링되는가?
    html_response = f"<h1>검색 결과: {keyword}</h1>"
    return HttpResponse(html_response)

# 4. Path Traversal / LFI (CWE-22)
# 경로 조작 문자를 필터링하지 않고 파일 시스템에 접근함
def read_log(request):
    filename = request.GET.get('file', 'default.log')
    # 분석기 체크 포인트: 사용자 입력이 open() 함수의 경로로 사용되는가?
    file_path = f"/var/log/myapp/{filename}"
    try:
        with open(file_path, 'r') as f:
            content = f.read()
    except FileNotFoundError:
        content = "File not found"
    return HttpResponse(content)