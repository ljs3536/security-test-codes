import pickle
import json
import lxml.etree as etree
import defusedxml.lxml as defused_etree
from django.http import JsonResponse, HttpResponse
from django.views.decorators.http import require_http_methods
import logging

logger = logging.getLogger(__name__)
# ==========================================
# [Target 1] Insecure Deserialization (CWE-502)
# ==========================================

@require_http_methods(["POST"])
def deserialize_vuln(request):
    """[취약한 코드] CWE-502: 검증 없이 pickle.loads 사용"""
    # 해커가 보낸 악성 바이너리 데이터
    raw_data = request.body
    
    try:
        # Taint Sink: pickle.loads()는 데이터를 객체로 만들며 악성 코드를 실행함!
        obj = pickle.loads(raw_data)
        return JsonResponse({"status": "success", "data": str(obj)})
    except pickle.UnpicklingError as e: 
        logger.error(f"역직렬화 에러 발생: {e}") 
        return JsonResponse({"error": "Decode failed"}, status=400)
        
    except Exception as e:
        logger.error(f"예상치 못한 서버 에러: {e}")
        return JsonResponse({"error": "Server Error"}, status=500)

@require_http_methods(["POST"])
def deserialize_safe(request):
    """[안전한 코드] JSON 직렬화 사용"""
    raw_data = request.body
    
    try:
        # [안전한 조치] 단순 데이터 포맷인 JSON을 사용하면 코드 실행 위험이 없음
        obj = json.loads(raw_data)
        return JsonResponse({"status": "success", "data": obj})
    except Exception as e:
        logger.error(f"예상치 못한 서버 에러: {e}")
        return JsonResponse({"error": "Decode failed"}, status=400)


# ==========================================
# [Target 2] XML External Entity (CWE-611: XXE)
# ==========================================

@require_http_methods(["POST"])
def parse_xml_vuln(request):
    """[취약한 코드] CWE-611: 보안 설정이 없는 lxml 파서 사용"""
    xml_data = request.body
    
    try:
        # lxml의 기본 파서는 외부 엔티티(Entity) 참조를 허용하여 XXE에 취약함
        parser = etree.XMLParser(resolve_entities=True)
        root = etree.fromstring(xml_data, parser=parser)
        return HttpResponse(f"Parsed Root: {root.tag}")
    except Exception as e:
        logger.error(f"예상치 못한 서버 에러: {e}")
        return HttpResponse("XML Error", status=400)

@require_http_methods(["POST"])
def parse_xml_safe(request):
    """[안전한 코드] defusedxml 라이브러리 사용"""
    xml_data = request.body
    
    try:
        # [안전한 조치] 외부 엔티티를 원천 차단하는 defusedxml 사용
        root = defused_etree.fromstring(xml_data)
        return HttpResponse(f"Parsed Root: {root.tag}")
    except Exception as e:
        logger.error(f"예상치 못한 서버 에러: {e}")
        return HttpResponse("XML Error", status=400)
    

import jwt # pip install pyjwt
from django.conf import settings
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods


# ==========================================
# [Target 3] Insufficient Signature Validation (CWE-345)
# ==========================================

@require_http_methods(["GET"])
def jwt_verify_vuln(request):
    """[취약한 코드] CWE-345: JWT 서명 검증 누락"""
    token = request.headers.get('Authorization', '').replace('Bearer ', '')
    
    if not token:
        return JsonResponse({"error": "No token"}, status=401)
        
    try:
        # [치명적 실수] verify_signature=False 로 인해 해커가 조작한 토큰도 무조건 통과됨!
        # 스캐너가 이 취약한 옵션 설정을 잡아내야 합니다.
        payload = jwt.decode(token, options={"verify_signature": False})
        return JsonResponse({"status": "Success", "user": payload.get('user')})
    except jwt.DecodeError as e:
        logger.error(f"JWT 디코딩 에러: {e}")
        return JsonResponse({"error": "Invalid token"}, status=401)
    except Exception as e:
        logger.error(f"JWT 처리 에러: {e}")
        return JsonResponse({"error": "Server error"}, status=500)

@require_http_methods(["GET"])
def jwt_verify_safe(request):
    """[안전한 코드] 정상적인 JWT 서명 및 알고리즘 검증"""
    token = request.headers.get('Authorization', '').replace('Bearer ', '')
    
    if not token:
        return JsonResponse({"error": "No token"}, status=401)
        
    try:
        # [안전한 조치] 시크릿 키와 명시적인 알고리즘(HS256)을 통해 위변조를 완벽히 차단함
        payload = jwt.decode(token, settings.SECRET_KEY, algorithms=["HS256"])
        return JsonResponse({"status": "Success", "user": payload.get('user')})
    except jwt.InvalidTokenError as e:
        logger.error(f"JWT 검증 실패: {e}")
        return JsonResponse({"error": "Invalid token"}, status=401)
    except Exception as e:
        logger.error(f"JWT 처리 에러: {e}")
        return JsonResponse({"error": "Server error"}, status=500)