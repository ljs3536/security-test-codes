
[A01]CWE-22 / CWE-22 (path traversal) [확인]<br>
[A01]CWE-285 / CWE-287 (Improper Authentication) [다른 인증 인가와 같이 확인하는것 확인]<br>
[A01]CWE-288 / CWE-287 (Improper Authentication) [다른 인증 인가와 같이 확인하는것 확인]<br>
[A02]CWE-327 / CWE-327 (WeakCrypto) [다른항목과 동시 확인]<br>
[A02]CWE-330 / CWE-330 (WeakRandom) [확인]<br>
[A02]CWE-798 / CWE-798 (secrets) [확인]<br>
[A03]CWE-89 / CWE-89 (orm sqli, sqli) [확인]<br>
[A03]CWE-79 / CWE-79 (XSS) [확인]<br>
[A03]CWE-78 / CWE-78 (OS Command injection) [확인]<br>
[A03]CWE-90 / CWE-90 (LDAP Injection) [확인]<br>
[A03]CWE-611 / CWE-611 (XXE[XML External Entity]) [확인]<br>
[A04]CWE-434 / CWE-434 (FileUpload) [다른항목과 동시 확인]<br>
[A04]CWE-367 / CWE-367 (TOCTOU[Time-of-Check to Time-of-Use]) [확인]<br>
[A05]CWE-942 / CWE-942 (CORS) [확인]<br>
[A05]CWE-614 / CWE-614 (cookie secrets) [확인] <br>
[A05]CWE-16 / CWE-16 (configuration) [확인]<br>
[A06]CWE-937 / SBOM pip 검증 필요<br>
[A06]CWE-1104 / SBOM pip 검증 필요<br>
[A07]CWE-287 / CWE-287 (auth_bypass) [확인]<br>
[A07]CWE-602 / CWE-602 (Client-Side Enforcement of Server-Side Security) [확인 어려움] (CWE-285,288 과 비슷)<br>
[A08]CWE-502 / CWE-502 (pickle) [확인]<br>
[A09]CWE-778 / CWE-778 (Logging) [다른항목과 동시 확인]<br>
[A09]CWE-117 / CWE-117 (Log Injection) [확인]<br>
[A10]CWE-918 / CWE-918 (ssrf) [확인]<br>
<br><br>
[A05]CWE-489 (debug) [확인]<br>
[A03]CWE-94 (eval) [확인]<br>
[A01]CWE-601 (open redirect) [확인]<br>
<br><br>
[Stage 1] <br>
[A03]Cross-File SQL Injection (CWE-89) [확인] <br>
[A05]Django 설정 파일(settings.py) (CWE-489, 614) [확인]<br>
<br>
[Stage 2] <br>
[A01]CWE-352 (CSRF) 미탐 [보고 완료] [수정 확인]<br>
<br>
[Stage 3]<br>
[A01]ORM Mass Assignment (CWE-915 / IDOR) [보고 완료]	[수정 확인]<br>
[A09]빈 예외 처리 (CWE-778) [확인]<br>
<br>
[Stage 4]<br>
[A10]CWE-918: 서버 측 요청 위조 (SSRF) [확인]<br>
[A03]CWE-79: 프레임워크 렌더링 우회 (XSS / SSTI) [확인]<br>
<br>
[Stage 5]<br>
[A07]CBV 인증 믹스인 (CWE-287) [확인]<br>
[A04]파일 업로드 (CWE-434) [확인]<br>
<br>
[Stage 6]<br>
[A03]CWE-94 (Code Injection) [확인]<br>
[A01]CWE-601 (Open Redirect) [보고 완료] <br>
<br>
[Stage 7]<br>
[A01]Path Traversal / 경로 조작 (CWE-22) [확인]<br>
[A02]취약한 암호화 및 난수 (CWE-327 / CWE-330) [확인]<br>
<br>
[Stage 8]<br>
[A03] CWE-1336 / CWE-74 (SSTI - 서버 측 템플릿 인젝션) [보고 완료] [수정 확인]<br>
[A01] CWE-400 / 1333 (ReDoS - 정규식 서비스 거부) [보고 완료] [수정 확인]<br>
<br>
[Stage 9]<br>
[A08]안전하지 않은 역직렬화 (CWE-502: Insecure Deserialization) [확인]<br>
[A05]XML 외부 엔티티 (CWE-611: XXE) [확인]<br>
[A08] 불충분한 서명 검증 / JWT (CWE-345) [CWE-287로 매핑하여 정탐 / 확인]<br>

