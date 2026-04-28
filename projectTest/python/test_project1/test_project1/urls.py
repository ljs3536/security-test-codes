from django.contrib import admin
from django.urls import path
from core import views_sqli, views_csrf, views_orm, views_network


urlpatterns = [
    path('admin/', admin.site.urls),
    
    # [Stage 1] SQLi Cross-file 테스트 라우터
    path('api/v1/vip/search/vuln', views_sqli.search_vip_vuln, name='search_vip_vuln'),
    path('api/v1/vip/search/safe', views_sqli.search_vip_safe, name='search_vip_safe'),
    # [Stage 2] CSRF 데코레이터 오버라이딩 테스트 라우터 추가
    path('api/v1/profile/update/vuln', views_csrf.update_profile_vuln, name='update_profile_vuln'),
    path('api/v1/profile/update/safe', views_csrf.update_profile_safe, name='update_profile_safe'),

    # [Stage 3] Mass Assignment / ORM 취약점 테스트 라우터
    path('api/v1/profile/mass_update/vuln', views_orm.mass_update_vuln, name='mass_update_vuln'),
    path('api/v1/profile/mass_update/safe', views_orm.mass_update_safe, name='mass_update_safe'),

    # [Stage 4] SSRF & XSS(mark_safe) 테스트 라우터
    path('api/v1/admin/fetch/vuln', views_network.fetch_external_log_vuln, name='fetch_vuln'),
    path('api/v1/admin/fetch/safe', views_network.fetch_external_log_safe, name='fetch_safe'),
    
    path('api/v1/admin/report/vuln', views_network.report_preview_vuln, name='report_vuln'),
    path('api/v1/admin/report/safe', views_network.report_preview_safe, name='report_safe'),

]