import os
import random
import hashlib
import secrets
from django.http import HttpResponse, JsonResponse
from django.views.decorators.http import require_http_methods

# ==========================================
# [Target 1] Path Traversal (CWE-22)
# ==========================================

@require_http_methods(["GET"])
def read_log_vuln(request):
    """[취약한 코드] CWE-22: 검증 없는 파일 경로 접근"""
    filename = request.GET.get('file', 'error.log')
    
    # 해커가 ../../../etc/passwd 를 넣으면 로컬 시스템 파일이 노출됨!
    filepath = os.path.join('/var/log/myapp/', filename)
    
    try:
        # Taint Sink: open() 함수
        with open(filepath, 'r') as f:
            return HttpResponse(f.read(), content_type='text/plain')
    except Exception as e:
        return HttpResponse("File not found", status=404)

@require_http_methods(["GET"])
def read_log_safe(request):
    """[안전한 코드] os.path.basename을 이용한 경로 조작 방어"""
    filename = request.GET.get('file', 'error.log')
    
    # [안전한 조치] 경로 문자열(../)을 모두 날려버리고 순수 파일명만 추출
    safe_filename = os.path.basename(filename)
    filepath = os.path.join('/var/log/myapp/', safe_filename)
    
    try:
        with open(filepath, 'r') as f:
            return HttpResponse(f.read(), content_type='text/plain')
    except Exception as e:
        return HttpResponse("File not found", status=404)


# ==========================================
# [Target 2] Weak Crypto & Random (CWE-327 / CWE-330)
# ==========================================

@require_http_methods(["GET"])
def generate_token_vuln(request):
    """[취약한 코드] CWE-330 (취약한 난수) & CWE-327 (취약한 해시)"""
    
    # 1. 취약한 난수 생성기 사용 (예측 가능)
    otp_code = str(random.randint(100000, 999999))
    
    # 2. 크랙 가능한 구형 해시 알고리즘 사용
    hashed_token = hashlib.md5(otp_code.encode()).hexdigest()
    
    return JsonResponse({"token": hashed_token})

@require_http_methods(["GET"])
def generate_token_safe(request):
    """[안전한 코드] 암호학적으로 안전한 모듈 사용"""
    
    # 1. [안전한 조치] OS 레벨의 안전한 난수 생성기 사용
    otp_code = str(secrets.randbelow(900000) + 100000)
    
    # 2. [안전한 조치] SHA-256 이상의 안전한 해시 사용
    hashed_token = hashlib.sha256(otp_code.encode()).hexdigest()
    
    return JsonResponse({"token": hashed_token})