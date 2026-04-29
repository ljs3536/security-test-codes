"""
URL configuration for test_project4 project.

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

    path('api/v1/data/pickle/vuln', views.deserialize_vuln),
    path('api/v1/data/pickle/safe', views.deserialize_safe),
    path('api/v1/data/xml/vuln', views.parse_xml_vuln),
    path('api/v1/data/xml/safe', views.parse_xml_safe),

    # JWT 서명 검증 우회
    path('api/v1/auth/jwt/vuln', views.jwt_verify_vuln),
    path('api/v1/auth/jwt/safe', views.jwt_verify_safe),
]
