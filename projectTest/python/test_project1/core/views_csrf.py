
from django.views.decorators.csrf import csrf_exempt
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods

@csrf_exempt
@require_http_methods(["POST"])
def update_profile_vuln(request):
    """CSRF 검증 없이 동작하는 프로필 업데이트 (위험!)"""
    return JsonResponse({"status": "취약한 프로필 업데이트 성공!"})

@require_http_methods(["POST"])
def update_profile_safe(request):
    """CSRF 토큰이 있어야만 동작하는 안전한 프로필 업데이트"""
    return JsonResponse({"status": "안전한 프로필 업데이트 성공!"})

from django.views.decorators.http import require_http_methods
import json
