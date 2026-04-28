import requests
from django.utils.safestring import mark_safe
from django.http import HttpResponse
import logging
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods
from .models import VIPUser

logger = logging.getLogger(__name__)
# ==========================================
# [Stage 4-1] SSRF (서버 측 요청 위조) 테스트
# ==========================================

@require_http_methods(["GET"])
def fetch_external_log_vuln(request):
    """[취약한 코드] CWE-918: 검증 없이 외부 URL 호출"""
    target_url = request.GET.get('url', '')
    
    if not target_url:
        return JsonResponse({"error": "URL이 필요합니다."}, status=400)
        
    try:
        # 해커가 내부망 IP나 AWS 메타데이터 주소를 넣어도 그대로 실행됨!
        response = requests.get(target_url, timeout=5)
        return HttpResponse(response.content)
    except Exception as e:
        # (참고) 여기서도 CWE-778을 피하기 위해 로깅을 해봅니다.
        logger.error(f"URL Fetch Error: {e}")
        return JsonResponse({"error": "Fetch Failed"}, status=500)

@require_http_methods(["GET"])
def fetch_external_log_safe(request):
    """[안전한 코드] 화이트리스트 기반 URL 호출"""
    target_url = request.GET.get('url', '')
    
    # [안전한 조치] 사전에 허가된 도메인만 허용 (SSRF 방어)
    ALLOWED_DOMAINS = ['https://api.github.com', 'https://trusted-partner.com']
    
    if not any(target_url.startswith(domain) for domain in ALLOWED_DOMAINS):
        return JsonResponse({"error": "허용되지 않은 도메인입니다."}, status=403)
        
    try:
        response = requests.get(target_url, timeout=5)
        return HttpResponse(response.content)
    except Exception as e:
        logger.error(f"URL Fetch Error: {e}")
        return JsonResponse({"error": "Fetch Failed"}, status=500)


# ==========================================
# [Stage 4-2] 프레임워크 렌더링 우회 (XSS) 테스트
# ==========================================

@require_http_methods(["GET"])
def report_preview_vuln(request):
    """[취약한 코드] CWE-79: mark_safe를 악용한 XSS 발생"""
    user_title = request.GET.get('title', '보고서 제목')
    
    # Django의 방어막을 개발자가 직접 해제해버림. 
    # 해커가 title에 <script>alert('XSS')</script> 를 넣으면 그대로 실행됨!
    unsafe_html = mark_safe(f"<h1>{user_title}</h1><p>보고서 내용입니다.</p>")
    
    return HttpResponse(unsafe_html)

@require_http_methods(["GET"])
def report_preview_safe(request):
    """[안전한 코드] Django 기본 Auto-escape 활용"""
    user_title = request.GET.get('title', '보고서 제목')
    
    # mark_safe 없이 렌더링하면 Django가 <script>를 &lt;script&gt;로 안전하게 치환함
    safe_html = f"<h1>{user_title}</h1><p>보고서 내용입니다.</p>"
    
    return HttpResponse(safe_html)