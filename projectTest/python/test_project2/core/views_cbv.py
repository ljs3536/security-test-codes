import os
from django.http import JsonResponse
from django.views import View
from django.contrib.auth.mixins import LoginRequiredMixin
from django.core.files.storage import FileSystemStorage
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator

# ==========================================
# [Target 1] CBV 인증 믹스인 (CWE-287 / CWE-285)
# ==========================================

# (테스트 집중을 위해 CSRF는 잠시 꺼둡니다)
@method_decorator(csrf_exempt, name='dispatch')
class ProfileUpdateVulnView(View):
    """[취약한 코드] 인증 없이 접근 가능한 클래스 기반 뷰(CBV)"""
    def post(self, request):
        return JsonResponse({"status": "누구나 프로필 수정 가능 (취약)"})

@method_decorator(csrf_exempt, name='dispatch')
class ProfileUpdateSafeView(LoginRequiredMixin, View):
    """[안전한 코드] Django 고유의 LoginRequiredMixin 사용"""
    # 스캐너가 다중 상속된 'LoginRequiredMixin'을 인증 장치로 인지할 수 있을까요?
    def post(self, request):
        return JsonResponse({"status": "인증된 사용자만 수정 가능 (안전)"})


# ==========================================
# [Target 2] 위험한 파일 업로드 (CWE-434)
# ==========================================

@csrf_exempt
def file_upload_vuln(request):
    """[취약한 코드] CWE-434: 확장자 검증 없는 파일 업로드"""
    if request.method == 'POST' and request.FILES.get('file'):
        upload_file = request.FILES['file']
        fs = FileSystemStorage()
        
        # 해커가 웹쉘(webshell.php)이나 악성 스크립트를 올려도 그대로 저장됨!
        # 관건: 스캐너가 프레임워크의 fs.save() 메서드를 위험한 Sink로 인지하는가?
        filename = fs.save(upload_file.name, upload_file)
        return JsonResponse({"uploaded": filename})
        
    return JsonResponse({"error": "No file"}, status=400)