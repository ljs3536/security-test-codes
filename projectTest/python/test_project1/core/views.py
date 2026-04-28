import logging
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods
from .models import VIPUser

logger = logging.getLogger(__name__)

# @require_http_methods(["GET"])
# def search_vip_vuln(request):
#     """[Taint Source] 사용자 입력이 시작되는 곳"""
#     q = request.GET.get('q', '')
#     logger.info(f"VIP 검색 요청: {q}")

#     raw_results = VIPUser.objects.raw_search_vuln(q)

#     data = [{"username": row[1], "credit": row[2]} for row in raw_results]
#     return JsonResponse({"status": "success", "data": data})

# @require_http_methods(["GET"])
# def search_vip_safe(request):
#     """안전한 조회 로직"""
#     q = request.GET.get('q', '')
    
#     raw_results = VIPUser.objects.raw_search_safe(q)
    
#     data = [{"username": row[1], "credit": row[2]} for row in raw_results]
#     return JsonResponse({"status": "success", "data": data})

# from django.views.decorators.csrf import csrf_exempt
# from django.http import JsonResponse
# from django.views.decorators.http import require_http_methods

# @csrf_exempt
# @require_http_methods(["POST"])
# def update_profile_vuln(request):
#     """CSRF 검증 없이 동작하는 프로필 업데이트 (위험!)"""
#     return JsonResponse({"status": "취약한 프로필 업데이트 성공!"})

# @require_http_methods(["POST"])
# def update_profile_safe(request):
#     """CSRF 토큰이 있어야만 동작하는 안전한 프로필 업데이트"""
#     return JsonResponse({"status": "안전한 프로필 업데이트 성공!"})

# from django.views.decorators.http import require_http_methods
# import json

# @require_http_methods(["POST"])
# def mass_update_vuln(request):
#     """클라이언트의 입력을 필터링 없이 그대로 ORM에 밀어넣음"""
#     try:
#         data = json.loads(request.body)
#         user_id = data.pop('id', None)
        
#         if user_id:
#             VIPUser.objects.filter(id=user_id).update(**data)
#             return JsonResponse({"status": "취약한 업데이트 완료"})
#     except Exception as e:
#         pass
#     return JsonResponse({"error": "Bad Request"}, status=400)


# @require_http_methods(["POST"])
# def mass_update_safe(request):
#     """White-list 기반의 안전한 ORM 업데이트"""
#     try:
#         data = json.loads(request.body)
#         user_id = data.get('id')
        
#         allowed_update_data = {
#             "username": data.get("username")
#         }
        
#         if user_id and allowed_update_data["username"]:
#             VIPUser.objects.filter(id=user_id).update(**allowed_update_data)
#             return JsonResponse({"status": "안전한 업데이트 완료"})
#     except Exception as e:
#         pass
#     return JsonResponse({"error": "Bad Request"}, status=400)

# from django import forms

# class VIPUserForm(forms.ModelForm):
#     class Meta:
#         model = VIPUser
#         fields = ['username'] # [안전] 여기서 명시된 필드만 업데이트 허용

# def update_with_form(request, user_id):
#     user = VIPUser.objects.get(id=user_id)
#     data = json.loads(request.body)
    
#     # Form이 내부적으로 fields=['username'] 이외의 입력값은 전부 버림
#     form = VIPUserForm(data, instance=user)
#     if form.is_valid():
#         form.save()

# from rest_framework import serializers

# class VIPUserSerializer(serializers.ModelSerializer):
#     class Meta:
#         model = VIPUser
#         fields = ['username'] # [안전] 화이트리스트 자동 적용

# def update_with_serializer(request, user_id):
#     user = VIPUser.objects.get(id=user_id)
    
#     # partial=True 로 부분 업데이트 허용, 악성 필드는 Serializer가 무시함
#     serializer = VIPUserSerializer(user, data=request.data, partial=True)
#     if serializer.is_valid():
#         serializer.save()

import requests
from django.utils.safestring import mark_safe
from django.http import HttpResponse

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