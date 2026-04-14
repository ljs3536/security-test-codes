from django.conf import settings
from django.http import HttpResponse
from django.db import connection
from django.shortcuts import redirect
import os
import requests
from django.contrib.auth.models import User
import jwt

# # 1. SQL Injection (CWE-89)
# # Django ORM 대신 Raw Query를 사용하며 사용자 입력을 직접 포맷팅함
# def get_user_info(request):
#     username = request.GET.get('user', '')
#     with connection.cursor() as cursor:
#         # 분석기 체크 포인트: request.GET이 cursor.execute로 바로 들어가는가?
#         query = f"SELECT * FROM auth_user WHERE username = '{username}'"
#         cursor.execute(query)
#         row = cursor.fetchone()
#     return HttpResponse(f"User info: {row}")

# # 2. Command Injection (CWE-78)
# # 사용자 입력값을 검증 없이 OS 시스템 명령어에 포함함
# def system_ping(request):
#     target = request.GET.get('target', 'localhost')
#     # 분석기 체크 포인트: target 변수가 os.system의 인자로 전달되는 흐름을 탐지하는가?
#     command = f"ping -c 1 {target}"
#     os.system(command)
#     return HttpResponse(f"Pinged {target}")

# # 3. Reflected Cross-Site Scripting (XSS) (CWE-79)
# # 템플릿 엔진의 자동 이스케이프를 거치지 않고 사용자 입력을 그대로 렌더링함
# def search_view(request):
#     keyword = request.GET.get('keyword', '')
#     # 분석기 체크 포인트: 검증되지 않은 keyword가 HttpResponse에 직접 렌더링되는가?
#     html_response = f"<h1>검색 결과: {keyword}</h1>"
#     return HttpResponse(html_response)

# # 4. Path Traversal / LFI (CWE-22)
# # 경로 조작 문자를 필터링하지 않고 파일 시스템에 접근함
# def read_log(request):
#     filename = request.GET.get('file', 'default.log')
#     # 분석기 체크 포인트: 사용자 입력이 open() 함수의 경로로 사용되는가?
#     file_path = f"/var/log/myapp/{filename}"
#     try:
#         with open(file_path, 'r') as f:
#             content = f.read()
#     except FileNotFoundError:
#         content = "File not found"
#     return HttpResponse(content)

# # [CWE-94] Code Injection (위험한 eval 사용)
# def calculate_expression(request):
#     # 공격 페이로드 예시: ?expr=__import__('os').system('whoami')
#     expr = request.GET.get('expr', '1 + 1')
#     try:
#         # 스캐너 체크: 사용자 입력이 파이썬 실행 함수(eval)로 전달되는가?
#         result = eval(expr) 
#         return HttpResponse(f"Result: {result}")
#     except:
#         return HttpResponse("Error")

# # [CWE-601] Open Redirect
# def login_redirect(request):
#     # 공격 페이로드 예시: ?next=http://malicious-site.com
#     next_url = request.GET.get('next', '/')
#     # 스캐너 체크: 검증되지 않은 외부 URL로 리다이렉트하는가?
#     return redirect(next_url)

# # [CWE-798] Hardcoded Secrets
# def setup_services(request):
#     # 스캐너 체크: 문자열 패턴(AKIA...)이나 변수명을 통해 시크릿을 탐지하는가?
#     AWS_KEY = "AKIAIOSFODNN7EXAMPLE" 
#     GH_TOKEN = "ghp_xYz1234567890abcdefGHIJKLMNOPQRSTuvw"
    
#     return HttpResponse("Services configured.")


# # 1. CWE-489: Active Debug Code (프레임워크 설정 연동)
# # 단순 백도어가 아니라, Django의 settings.DEBUG 값에 의존하여 위험한 정보를 노출합니다.
# def debug_info_view(request):
#     # 스캐너 체크 포인트: settings.py의 DEBUG=True 설정과 이 로직을 연결 지어 위험하다고 판단하는가?
#     if settings.DEBUG:
#         env_dump = str(os.environ)
#         return HttpResponse(f"System Environment: {env_dump}")
#     return HttpResponse("Access Denied: Not in debug mode")

# # 2. CWE-287: Improper Authentication (Django 세션 우회)
# # 안전한 request.user.is_authenticated를 쓰지 않고, 조작 가능한 쿠키를 믿어버립니다.
# def admin_dashboard(request):
#     # 스캐너 체크 포인트: 인증/인가 권한 분기 처리에 request.COOKIES 라는 오염 가능한 데이터가 쓰이는 걸 잡는가?
#     if request.COOKIES.get('admin_access') == 'granted':
#         return HttpResponse("Welcome to the Secret Admin Dashboard!")
#     return HttpResponse("Access Denied: Regular User")

# # 3. CWE-798: Hardcoded Secrets (정규식/엔트로피 테스트)
# # 이번엔 Stripe(결제) API 비밀키 패턴을 넣어보겠습니다.
# def payment_process(request):
#     # 스캐너 체크 포인트: sk_live_ 로 시작하는 Stripe 토큰 패턴 정규식을 가지고 있는가?
#     STRIPE_SECRET_KEY = "sk_live_51Mabcdefghijklmnopqrstuvwxyz1234567890"
#     return HttpResponse(f"Processing payment with key: {STRIPE_SECRET_KEY[:8]}...")

# # 4. CWE-614: Insecure Cookie Attributes (메서드 옵션 누락)
# # Django의 HttpResponse 객체에 내장된 set_cookie를 사용할 때의 흔한 실수입니다.
# def login_cookie_set(request):
#     response = HttpResponse("Authentication cookie set!")
#     # 스캐너 체크 포인트: set_cookie 함수 호출 시 파라미터로 secure=True, httponly=True 가 없는 것을 지적하는가?
#     response.set_cookie('user_session_token', 'random_token_value_123')
#     return response

# # 5. CWE-918: SSRF (서버 측 요청 위조)
# # 외부 라이브러리(requests)와 Django의 request.GET이 만나는 지점입니다.
# def proxy_fetcher(request):
#     target_url = request.GET.get('url', 'http://example.com')
#     try:
#         # 스캐너 체크 포인트: 검증 없는 target_url이 HTTP 요청 함수의 인자로 들어가는가?
#         res = requests.get(target_url, timeout=3)
#         return HttpResponse(f"Fetched Data length: {len(res.text)}")
#     except Exception:
#         # (CWE-778: 에러 로깅 누락 보너스 트랙)
#         return HttpResponse("Error fetching data")


# 1. CWE-287 (인증 우회) + 파괴적인 DB 명령 (User.objects.delete)
def nuke_database_view(request):
    # 외부 입력(쿠키)으로 권한을 체크합니다. (매우 취약)
    is_super_admin = request.COOKIES.get('role')
    
    if is_super_admin == 'god_mode':
        # [스캐너 체크 포인트 1] 
        # 위험한 Sink(DB 삭제)가 존재하므로, 이제는 앞단의 쿠키 기반 인증을 CWE-287로 경고하는가?
        try:
            User.objects.all().delete()
            return HttpResponse("All users have been deleted.")
        except Exception as e:
            return HttpResponse(f"Error: {e}")
            
    return HttpResponse("Access Denied: You cannot delete the DB.")

# 2. CWE-287 (인증 우회) + 파괴적인 OS 명령 (os.system)
def system_shutdown_view(request):
    # 역시 쿠키로만 인증을 처리합니다.
    auth_token = request.COOKIES.get('auth_token')
    
    if auth_token == 'secret_admin_token_123':
        # [스캐너 체크 포인트 2]
        # os.system('reboot')라는 명백한 위험 함수가 실행되는데, 
        # 이 조건문이 쿠키에 의존하고 있다는 사실을 분석해 내는가?
        os.system('reboot') 
        return HttpResponse("Server is rebooting...")
        
    return HttpResponse("Access Denied.")


# 임시 시크릿 키
JWT_SECRET = "my_super_secret_key"

# 1. 토큰 발급 (정상)
def generate_jwt(request):
    # 'admin' 권한을 가진 토큰을 발행
    payload = {"user": "test_user", "role": "admin"}
    token = jwt.encode(payload, JWT_SECRET, algorithm="HS256")
    return HttpResponse(f"Generated Token: {token}")

# 2. [핵심 테스트] JWT 검증 누락 (CWE-287 / CWE-347)
def verify_jwt_vuln(request):
    token = request.GET.get('token')
    
    if not token:
        return HttpResponse("Token required")

    try:
        # 🚨 스캐너 체크 포인트: 엔진이 'options={"verify_signature": False}' 라는 
        # 명시적인 보안 해제 옵션을 텍스트/구문 트리(AST) 수준에서 잡아내는가?
        decoded_payload = jwt.decode(token, options={"verify_signature": False})
        
        # 공격자가 서명 없이 payload만 조작해서 넘겨도 이 조건문을 통과해 버림
        if decoded_payload.get("role") == "admin":
            return HttpResponse("Welcome Admin! (Signature was NOT verified)")
            
        return HttpResponse("Welcome User.")
    
    except jwt.DecodeError:
        return HttpResponse("Invalid token format")
    