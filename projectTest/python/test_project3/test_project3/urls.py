"""
URL configuration for test_project3 project.

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
    
    # [Stage 8] SSTI & ReDoS 테스트 라우터
    path('api/v1/template/ssti/vuln', views.ssti_vuln, name='ssti_vuln'),
    path('api/v1/template/ssti/safe', views.ssti_safe, name='ssti_safe'),
    
    path('api/v1/regex/redos/vuln', views.redos_vuln, name='redos_vuln'),
    path('api/v1/regex/redos/safe', views.redos_safe, name='redos_safe'),

    # [Stage 8 심화] SSTI & 실무형 ReDoS
    path('api/v1/template/ssti/engine', views.ssti_engine_vuln),
    path('api/v1/regex/redos/realworld', views.redos_real_world_vuln),
]