from django.db import models
from django.db import connection

class VIPUserManager(models.Manager):
    def raw_search_vuln(self, search_term):
        """[취약한 코드] CWE-89: f-string을 이용한 Raw SQL 포매팅"""
        query = f"SELECT id, username, credit FROM core_vipuser WHERE username LIKE '%{search_term}%'"
        
        with connection.cursor() as cursor:
            cursor.execute(query)
            results = cursor.fetchall()
        return results

    def raw_search_safe(self, search_term):
        """[안전한 코드] 파라미터화된 쿼리 사용 (오탐 방지 확인용)"""
        query = "SELECT id, username, credit FROM core_vipuser WHERE username LIKE %s"
        
        with connection.cursor() as cursor:
            # 안전한 바인딩 변수(%s) 사용
            cursor.execute(query, [f"%{search_term}%"])
            results = cursor.fetchall()
        return results

class VIPUser(models.Model):
    username = models.CharField(max_length=100)
    is_admin = models.BooleanField(default=False)
    credit = models.IntegerField(default=0)

    # 커스텀 매니저 연결
    objects = VIPUserManager()