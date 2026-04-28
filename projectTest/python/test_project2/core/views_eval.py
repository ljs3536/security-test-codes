import ast
from django.shortcuts import redirect
from django.utils.http import url_has_allowed_host_and_scheme
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods

# ==========================================
# [Target 2] Code Injection / Eval (CWE-94)
# ==========================================

@require_http_methods(["GET"])
def calculator_eval_vuln(request):
    """[취약한 코드] CWE-94: eval()을 통한 원격 코드 실행"""
    formula = request.GET.get('formula', '1+1')
    
    try:
        # 해커가 시스템 명령어를 주입하면 그대로 서버에서 실행되는 최악의 취약점!
        result = eval(formula) 
        return JsonResponse({"result": result})
    except Exception as e:
        return JsonResponse({"error": str(e)}, status=400)

@require_http_methods(["GET"])
def calculator_eval_safe(request):
    """[안전한 코드] ast.literal_eval() 사용"""
    formula = request.GET.get('formula', '{"a": 1}')
    
    try:
        # [안전한 조치] 파이썬 AST 모듈을 사용해 순수 데이터(리스트, 딕셔너리, 숫자 등)만 안전하게 평가
        result = ast.literal_eval(formula)
        return JsonResponse({"result": result})
    except Exception as e:
        return JsonResponse({"error": str(e)}, status=400)
