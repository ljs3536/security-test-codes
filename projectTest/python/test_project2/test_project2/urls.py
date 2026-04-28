"""
URL configuration for test_project2 project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/6.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from core import views, views_cbv, views_eval, views_fileupload, views_openredirect

urlpatterns = [
    path('admin/', admin.site.urls),

    
    # [Target 1] CBV 테스트 라우터 (as_view() 사용)
    path('api/v1/profile/cbv/vuln', views_cbv.ProfileUpdateVulnView.as_view(), name='cbv_vuln'),
    path('api/v1/profile/cbv/safe', views_cbv.ProfileUpdateSafeView.as_view(), name='cbv_safe'),
    
    # [Target 2] 파일 업로드 테스트 라우터
    path('api/v1/upload/vuln', views_fileupload.file_upload_vuln, name='upload_vuln'),
    path('api/v1/upload/safe', views_fileupload.file_upload_safe, name='upload_safe'),

    # [Stage 6] Open Redirect & Eval 테스트 라우터
    path('api/v1/auth/redirect/vuln', views_eval.open_redirect_vuln, name='redirect_vuln'),
    path('api/v1/auth/redirect/safe', views_eval.open_redirect_safe, name='redirect_safe'),
    
    path('api/v1/calc/eval/vuln', views_openredirect.calculator_eval_vuln, name='eval_vuln'),
    path('api/v1/calc/eval/safe', views_openredirect.calculator_eval_safe, name='eval_safe'),

    # [Stage 7] Path Traversal & Crypto 테스트 라우터
    path('api/v1/system/log/vuln', views.read_log_vuln, name='log_vuln'),
    path('api/v1/system/log/safe', views.read_log_safe, name='log_safe'),
    
    path('api/v1/crypto/token/vuln', views.generate_token_vuln, name='token_vuln'),
    path('api/v1/crypto/token/safe', views.generate_token_safe, name='token_safe'),
]
