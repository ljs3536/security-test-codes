# 파일명: test_01_eval_cwe94_vuln.py

def process_user_data(user_input):
    """
    사용자 입력을 동적으로 실행하는 취약한 함수입니다.
    스캐너가 이 부분을 탐지하고 CWE-94로 분류해야 합니다.
    """
    print("데이터 처리를 시작합니다...")
    
    try:
        # 취약점 발생 지점: 외부 입력을 검증 없이 eval()로 실행
        result = eval(user_input)
        return f"처리 결과: {result}"
    except Exception as e:
        return f"에러 발생: {e}"

if __name__ == "__main__":
    # 스캐너가 문맥을 더 잘 파악할 수 있도록 작성된 실행 코드 (실제 악의적인 페이로드 예시)
    malicious_payload = "__import__('os').system('ls -al')"
    print(process_user_data(malicious_payload))