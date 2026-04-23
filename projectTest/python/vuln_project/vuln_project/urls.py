from django.contrib import admin
from django.urls import path
from core import views

urlpatterns = [
    path('admin/', admin.site.urls),
    
    # [Stage 1] SQLi Cross-file 테스트 라우터
    path('api/v1/vip/search/vuln', views.search_vip_vuln, name='search_vip_vuln'),
    path('api/v1/vip/search/safe', views.search_vip_safe, name='search_vip_safe'),
]