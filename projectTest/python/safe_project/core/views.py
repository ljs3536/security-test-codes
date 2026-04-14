from django.http import HttpResponse
from django.contrib.auth.models import User
from django.utils.html import escape
import subprocess
import os
from django.conf import settings
from django.contrib.auth.decorators import login_required, user_passes_test
import requests
from urllib.parse import urlparse

# # 1. SQL Injection 방어 (Safe)
# def get_user_info(request):
#     username = request.GET.get('user', '')
#     # 방어 기법: Django의 내장 ORM 사용 (자동으로 파라미터 바인딩 처리됨)
#     # 스캐너 체크 포인트: QuerySet API를 사용할 때 SQLi의 위험이 없다고 판단하는가?
#     user_exists = User.objects.filter(username=username).exists()
#     return HttpResponse(f"User exists: {user_exists}")

# # 2. Command Injection 방어 (Safe)
# def system_ping(request):
#     target = request.GET.get('target', 'localhost')
#     # 방어 기법 1: os.system 대신 subprocess.run 사용 + 쉘 실행 불가(shell=False 기본값)
#     # 방어 기법 2: 입력값 검증 (알파벳, 숫자, 점만 허용하는 간단한 필터링 예시)
#     if not all(c.isalnum() or c == '.' for c in target):
#         return HttpResponse("Invalid target format.")
    
#     # 스캐너 체크 포인트: 배열 형태로 명령어를 넘기고 필터링 로직이 있을 때 통과시키는가?
#     result = subprocess.run(['ping', '-c', '1', target], capture_output=True, text=True)
#     return HttpResponse(f"Ping result: {'Success' if result.returncode == 0 else 'Fail'}")

# # 3. XSS 방어 (Safe)
# def search_view(request):
#     keyword = request.GET.get('keyword', '')
#     # 방어 기법: django.utils.html.escape()를 사용하여 HTML 특수문자 치환 (< -> &lt;)
#     # 스캐너 체크 포인트: escape() 함수를 거친 데이터(Sanitized Data)를 안전하다고 인식하는가?
#     safe_keyword = escape(keyword)
#     html_response = f"<h1>검색 결과: {safe_keyword}</h1>"
#     return HttpResponse(html_response)

# # 4. Path Traversal 방어 (Safe)
# def read_log(request):
#     filename = request.GET.get('file', 'default.log')
#     # 방어 기법: os.path.basename()을 사용하여 상위 경로(../../) 이동 문자를 무력화하고 파일명만 추출
#     # 스캐너 체크 포인트: basename()을 거친 후의 파일 접근을 정상적인 흐름으로 보는가?
#     safe_filename = os.path.basename(filename)
#     file_path = f"/var/log/myapp/{safe_filename}"
    
#     try:
#         with open(file_path, 'r') as f:
#             content = f.read()
#     except Exception as e: # 광범위한 예외도 안전하게 로깅 (CWE-778 방어)
#         # 실제로는 logging.error(e) 등을 사용해야 함
#         content = "Error reading log"
        
#     return HttpResponse(escape(content))

# 1. CWE-489 (Active Debug Code) 방어
# 디버그 모드와 무관하게, 시스템 주요 정보는 '최고 관리자(Superuser)'에게만 노출하도록 통제합니다.
@user_passes_test(lambda u: u.is_superuser)
def debug_info_view(request):
    # 스캐너 체크: 권한 검증 데코레이터(@user_passes_test)를 인지하고 안전하다고 판단하는가?
    env_dump = str(os.environ.get('PATH', 'Hidden')) 
    return HttpResponse(f"Admin Only System Info: {env_dump}")

# 2. CWE-287 (Improper Authentication) 방어
# 위조 가능한 쿠키(request.COOKIES) 대신, Django의 검증된 세션 기반 인증 데코레이터를 사용합니다.
@login_required
def admin_dashboard(request):
    # 스캐너 체크: @login_required가 붙은 함수는 인증 우회 취약점이 없다고 똑똑하게 넘어가는가?
    return HttpResponse(f"Welcome to the Dashboard, {request.user.username}!")

# 3. CWE-798 (Hardcoded Secrets) 방어
# 시크릿 키를 코드에 박아두지 않고, OS 환경 변수에서 동적으로 불러옵니다.
def payment_process(request):
    # 스캐너 체크: os.environ.get()으로 가져오는 값을 보고 안전한 시크릿 관리로 인정하는가?
    STRIPE_SECRET_KEY = os.environ.get('STRIPE_SECRET_KEY')
    if not STRIPE_SECRET_KEY:
        return HttpResponse("Payment module misconfigured.")
    return HttpResponse("Processing payment safely...")

# 4. CWE-614 (Insecure Cookie) 방어
# 쿠키를 구울 때 반드시 필요한 보안 3대장 속성을 모두 명시적으로 추가합니다.
def login_cookie_set(request):
    response = HttpResponse("Secure authentication cookie set!")
    # 스캐너 체크: 파라미터로 secure, httponly, samesite가 모두 들어간 것을 보고 경고를 해제하는가?
    response.set_cookie(
        'user_session_token', 
        'random_token_value_123',
        secure=True,       # HTTPS에서만 전송
        httponly=True,     # 자바스크립트(XSS)로 접근 불가
        samesite='Lax'     # CSRF 방어용
    )
    return response

# 5. CWE-918 (SSRF) 방어
# 사용자가 입력한 URL을 무조건 믿지 않고, 허용된 도메인(Whitelist)인지 먼저 검증합니다.
ALLOWED_DOMAINS = ['api.github.com', 'example.com']

def proxy_fetcher(request):
    target_url = request.GET.get('url', 'https://example.com')
    
    try:
        parsed_url = urlparse(target_url)
        # 방어 기법: 추출한 호스트네임이 화이트리스트에 있는지 검증
        if parsed_url.hostname not in ALLOWED_DOMAINS:
            return HttpResponse("Error: Domain not allowed.")
            
        # 스캐너 체크: 호스트네임 검증 로직이 존재함을 인지하고 SSRF 오탐을 내지 않는가?
        res = requests.get(target_url, timeout=3)
        return HttpResponse(f"Fetched Data length: {len(res.text)}")
    except Exception as e:
        import logging
        logging.getLogger(__name__).error(f"Fetch failed: {e}") # CWE-778(로깅 누락) 방어
        return HttpResponse("Error fetching data")