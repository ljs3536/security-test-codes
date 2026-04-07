import ast
import logging

# 로거 설정
logging.basicConfig(level=logging.ERROR)

def process_user_data_safe(user_input):
    """
    안전한 함수(ast.literal_eval)를 사용하여 데이터를 처리합니다.
    스캐너가 이 코드를 취약점(CWE-94 등)으로 탐지하지 않아야 합니다.
    """
    print("데이터 처리를 시작합니다...")
    
    try:
        # 안전한 조치: eval() 대신 ast.literal_eval() 사용
        result = ast.literal_eval(user_input)
        return f"처리 결과: {result}"
    except Exception as e:
        # 안전한 조치: 예외 발생 시 로그를 남김 (CWE-778 방지)
        logging.error(f"데이터 파싱 에러 발생: {e}")
        return "데이터 처리 중 에러가 발생했습니다."

if __name__ == "__main__":
    # 정상적인 딕셔너리 형태의 문자열 입력
    safe_payload = "{'key': 'value', 'number': 42}"
    print(process_user_data_safe(safe_payload))