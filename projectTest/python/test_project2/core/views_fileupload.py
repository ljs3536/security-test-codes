import os
from django.http import JsonResponse
from django.views import View
from django.contrib.auth.mixins import LoginRequiredMixin
from django.core.files.storage import FileSystemStorage
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator

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

@csrf_exempt
def file_upload_safe(request):
    """[안전한 코드] 화이트리스트 기반 확장자 검증"""
    if request.method == 'POST' and request.FILES.get('file'):
        upload_file = request.FILES['file']
        ext = os.path.splitext(upload_file.name)[1].lower()
        
        # [안전한 조치] 허용된 이미지/문서 확장자만 업로드 가능하도록 통제
        ALLOWED_EXTENSIONS = ['.png', '.jpg', '.jpeg', '.pdf']
        if ext not in ALLOWED_EXTENSIONS:
            return JsonResponse({"error": "허용되지 않은 확장자입니다."}, status=403)
            
        fs = FileSystemStorage()
        filename = fs.save(upload_file.name, upload_file)
        return JsonResponse({"uploaded": filename})
        
    return JsonResponse({"error": "No file"}, status=400)