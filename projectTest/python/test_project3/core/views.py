import re
from django.http import HttpResponse, JsonResponse
from django.template import Template, Context
from django.views.decorators.http import require_http_methods
import logging


logger = logging.getLogger(__name__)

# ==========================================
# [Target 1] Server-Side Template Injection (CWE-1336 / CWE-74)
# ==========================================

@require_http_methods(["GET"])
def ssti_vuln(request):
    """[취약한 코드] CWE-1336: 사용자 입력을 템플릿 문자열에 직접 삽입"""
    user_name = request.GET.get('name', 'Guest')
    
    template_string = f"<h1>Hello, {user_name}!</h1>"
    template = Template(template_string)
    
    return HttpResponse(template.render(Context({})))

@require_http_methods(["GET"])
def ssti_safe(request):
    """[안전한 코드] 프레임워크의 정상적인 컨텍스트 바인딩 활용"""
    user_name = request.GET.get('name', 'Guest')
    
    template = Template("<h1>Hello, {{ name }}!</h1>")
    
    return HttpResponse(template.render(Context({'name': user_name})))


# ==========================================
# [Target 2] Regular Expression Denial of Service (CWE-400)
# ==========================================

@require_http_methods(["GET"])
def redos_vuln(request):
    """[취약한 코드] CWE-400: 파괴적 백트래킹을 유발하는 악성 정규식"""
    user_input = request.GET.get('text', '')
    
    try:
        evil_regex = re.compile(r"^(a+)+$")
        
        if evil_regex.match(user_input):
            return JsonResponse({"result": "Matched!"})
        return JsonResponse({"result": "Not Matched"})
    except Exception as e:
        logger.error(f"Error: {e}")
        return JsonResponse({"error": "Error"}, status=500)

@require_http_methods(["GET"])
def redos_safe(request):
    """[안전한 코드] 백트래킹이 발생하지 않는 안전한 정규식"""
    user_input = request.GET.get('text', '')
    
    try:
        safe_regex = re.compile(r"^a+$")
        
        if safe_regex.match(user_input):
            return JsonResponse({"result": "Matched!"})
        return JsonResponse({"result": "Not Matched"})
    except Exception as e:
        return JsonResponse({"error": "Error"}, status=500)
    

from django.template import Engine
from django.http import HttpResponse

@require_http_methods(["GET"])
def ssti_engine_vuln(request):
    """[취약한 코드] 다른 방식의 SSTI (Engine.from_string 사용)"""
    user_input = request.GET.get('name', 'Guest')
    
    engine = Engine.get_default()
    template = engine.from_string(f"Hello {user_input}")
    
    return HttpResponse(template.render(Context({})))

import re
from django.http import JsonResponse

@require_http_methods(["GET"])
def redos_real_world_vuln(request):
    """[취약한 코드] 실무에서 흔히 발생하는 ReDoS 패턴"""
    user_name = request.GET.get('name', '')
    
    try:
        bad_regex = re.compile(r"^([a-zA-Z]+\s?)+$")
        
        if bad_regex.match(user_name):
            return JsonResponse({"status": "Valid name"})
        return JsonResponse({"status": "Invalid name"})
    except Exception as e:
        logger.error(f"Error: {e}")
        return JsonResponse({"error": "Error"})