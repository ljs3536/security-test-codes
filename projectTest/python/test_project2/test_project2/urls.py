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
from core import views

urlpatterns = [
    path('admin/', admin.site.urls),

    
    # [Target 1] CBV 테스트 라우터 (as_view() 사용)
    path('api/v1/profile/cbv/vuln', views.ProfileUpdateVulnView.as_view(), name='cbv_vuln'),
    path('api/v1/profile/cbv/safe', views.ProfileUpdateSafeView.as_view(), name='cbv_safe'),
    
    # [Target 2] 파일 업로드 테스트 라우터
    path('api/v1/upload/vuln', views.file_upload_vuln, name='upload_vuln'),
    path('api/v1/upload/safe', views.file_upload_safe, name='upload_safe'),
]
