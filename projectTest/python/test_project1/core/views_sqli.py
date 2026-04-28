import logging
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods
from .models import VIPUser

logger = logging.getLogger(__name__)

@require_http_methods(["GET"])
def search_vip_vuln(request):
    """[Taint Source] 사용자 입력이 시작되는 곳"""
    q = request.GET.get('q', '')
    logger.info(f"VIP 검색 요청: {q}")

    raw_results = VIPUser.objects.raw_search_vuln(q)

    data = [{"username": row[1], "credit": row[2]} for row in raw_results]
    return JsonResponse({"status": "success", "data": data})

@require_http_methods(["GET"])
def search_vip_safe(request):
    """안전한 조회 로직"""
    q = request.GET.get('q', '')
    
    raw_results = VIPUser.objects.raw_search_safe(q)
    
    data = [{"username": row[1], "credit": row[2]} for row in raw_results]
    return JsonResponse({"status": "success", "data": data})
