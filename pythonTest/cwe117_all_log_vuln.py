import logging
import sys
from flask import Flask, request

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

# 별도의 커스텀 로거 객체 생성
custom_logger = logging.getLogger("MyCustomLogger")

@app.route('/api/v1/log/sinks', methods=['GET'])
def test_all_log_sinks():
    """모든 종류의 파이썬 로깅/출력 함수를 테스트하는 API"""
    
    # 해커가 조작할 수 있는 Taint Source
    payload = request.args.get('payload', 'guest')
    
    # [Sink 1] 파이썬 표준 로깅 모듈 (루트 로거) - 에러 및 크리티컬 레벨
    logging.error(f"[Root Logger] 로그인 실패: {payload}")
    logging.critical(f"[Root Logger] 시스템 장애 발생: {payload}")
    
    # [Sink 2] 커스텀 로거 객체 (실무에서 가장 많이 쓰는 방식)
    custom_logger.warning(f"[Custom Logger] 비정상 접근: {payload}")
    
    # [Sink 3] 내장 print 함수 
    # (Docker나 systemd 환경에서는 print 출력도 모두 서버 로그로 수집됨)
    print(f"[Print Function] 디버그 정보: {payload}")
    
    # [Sink 4] 시스템 표준 출력/에러 스트림 직접 쓰기 (가장 로우레벨)
    sys.stdout.write(f"[Sys Stdout] 표준 출력: {payload}\n")
    sys.stderr.write(f"[Sys Stderr] 표준 에러: {payload}\n")
    
    return "All logging sinks tested", 200

if __name__ == '__main__':
    app.run(port=8080)