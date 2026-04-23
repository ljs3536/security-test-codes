# 1. 취약한 구버전 베이스 이미지 사용
# python:3.7은 이미 EOL(지원 종료)되었으며 수많은 OS 레벨 CVE를 포함합니다.
# 스캐너 체크: EOL 이미지 사용 또는 Alpine 3.8의 자체 취약점을 경고하는가?
FROM python:3.7.0-alpine3.8

WORKDIR /app

# 2. OS 레벨의 취약한 리눅스 패키지 설치
# 매우 구버전의 openssl과 curl을 강제로 설치합니다.
# 스캐너 체크: 파이썬 패키지(pip)가 아닌 apk(OS 패키지 매니저)로 설치된 취약점을 잡는가?
RUN apk update && apk add --no-cache \
    openssl=1.0.2n-r0 \
    curl=7.61.0-r0

# 3. pyproject.toml 복사 및 의존성 설치
COPY pyproject.toml /app/

# 스캐너가 RUN 명령어 내부의 pip 설치 내역이나 컨테이너 레이어를 분석할 수 있는지 확인
RUN pip install poetry==1.1.15 \
    && poetry config virtualenvs.create false \
    && poetry install --no-dev

COPY . /app/

CMD ["python", "manage.py", "runserver", "0.0.0.0:8000"]