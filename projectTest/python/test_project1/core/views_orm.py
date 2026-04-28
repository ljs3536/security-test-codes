import logging
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods
from .models import VIPUser

logger = logging.getLogger(__name__)

@require_http_methods(["POST"])
def mass_update_vuln(request):
    """클라이언트의 입력을 필터링 없이 그대로 ORM에 밀어넣음"""
    try:
        data = json.loads(request.body)
        user_id = data.pop('id', None)
        
        if user_id:
            VIPUser.objects.filter(id=user_id).update(**data)
            return JsonResponse({"status": "취약한 업데이트 완료"})
    except Exception as e:
        pass
    return JsonResponse({"error": "Bad Request"}, status=400)


@require_http_methods(["POST"])
def mass_update_safe(request):
    """White-list 기반의 안전한 ORM 업데이트"""
    try:
        data = json.loads(request.body)
        user_id = data.get('id')
        
        allowed_update_data = {
            "username": data.get("username")
        }
        
        if user_id and allowed_update_data["username"]:
            VIPUser.objects.filter(id=user_id).update(**allowed_update_data)
            return JsonResponse({"status": "안전한 업데이트 완료"})
    except Exception as e:
        pass
    return JsonResponse({"error": "Bad Request"}, status=400)

from django import forms

class VIPUserForm(forms.ModelForm):
    class Meta:
        model = VIPUser
        fields = ['username'] # [안전] 여기서 명시된 필드만 업데이트 허용

def update_with_form(request, user_id):
    user = VIPUser.objects.get(id=user_id)
    data = json.loads(request.body)
    
    # Form이 내부적으로 fields=['username'] 이외의 입력값은 전부 버림
    form = VIPUserForm(data, instance=user)
    if form.is_valid():
        form.save()

from rest_framework import serializers

class VIPUserSerializer(serializers.ModelSerializer):
    class Meta:
        model = VIPUser
        fields = ['username'] # [안전] 화이트리스트 자동 적용

def update_with_serializer(request, user_id):
    user = VIPUser.objects.get(id=user_id)
    
    # partial=True 로 부분 업데이트 허용, 악성 필드는 Serializer가 무시함
    serializer = VIPUserSerializer(user, data=request.data, partial=True)
    if serializer.is_valid():
        serializer.save()
