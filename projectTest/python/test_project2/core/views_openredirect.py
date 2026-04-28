import ast
from django.shortcuts import redirect
from django.utils.http import url_has_allowed_host_and_scheme
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods

# ==========================================
# [Target 1] Open Redirect (CWE-601)
# ==========================================

@require_http_methods(["GET"])
def open_redirect_vuln(request):
    """[취약한 코드] CWE-601: 검증 없이 사용자 입력 URL로 리다이렉트"""
    next_url = request.GET.get('next', '/')
    
    # 해커가 ?next=https://evil.com 으로 피싱을 유도할 수 있음!
    return redirect(next_url)

@require_http_methods(["GET"])
def open_redirect_safe(request):
    """[안전한 코드] Django 내장 검증 함수 사용"""
    next_url = request.GET.get('next', '/')
    
    # [안전한 조치] Django가 제공하는 도메인 검증 함수 통과 시에만 리다이렉트
    if url_has_allowed_host_and_scheme(url=next_url, allowed_hosts={request.get_host()}):
        return redirect(next_url)
    
    return redirect('/')
