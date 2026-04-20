"""
URL configuration for vuln_project project.

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
from django.urls import path, include
from core import views

urlpatterns = [
    path('admin/', admin.site.urls),
    path('core/', include('core.urls')),
    # path('user/', views.get_user_info, name='get_user_info'),
    # path('ping/', views.system_ping, name='system_ping'),
    # path('search/', views.search_view, name='search_view'),
    # path('log/', views.read_log, name='read_log'),

    # path('calc/', views.calculate_expression, name='calculate'),
    # path('redirect/', views.login_redirect, name='login_redirect'),
    # path('connect/', views.connect_third_party, name='connect'),

    # path('debug/', views.debug_info_view, name='debug_info'),
    # path('admin-dash/', views.admin_dashboard, name='admin_dash'),
    # path('pay/', views.payment_process, name='payment'),
    # path('cookie/', views.login_cookie_set, name='cookie_set'),
    # path('proxy/', views.proxy_fetcher, name='proxy'),

    # path('nuke/', views.nuke_database_view, name='nuke'),
    # path('reboot/', views.system_shutdown_view, name='reboot'),

    # path('jwt-gen/', views.generate_jwt, name='generate_jwt'),
    # path('jwt-verify/', views.verify_jwt_vuln, name='verify_jwt_vuln'),

    path('sqliv1/', views.sqli_view_1, name='sqliv1'),
    path('sqliv2/', views.sqli_view_2, name='sqliv2'),
    path('cmdildap/', views.cmdi_ldap_view, name='cmdildap'),
    path('ttxxe/', views.toctou_xxe_view, name='ttxxe'),

]
