import logging
from flask import Flask, request, jsonify
import boto3
from botocore.exceptions import ClientError

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

# [취약점 발생 지점] 하드코딩된 AWS 자격 증명
# 스캐너가 이 부분을 기존 CWE-327이 아닌 CWE-798로 정확히 탐지해야 합니다.
AWS_ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE"
AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
AWS_REGION = "ap-northeast-2"
S3_BUCKET_NAME = "my-company-secure-bucket"

def get_s3_client():
    """하드코딩된 키를 사용하여 S3 클라이언트를 생성합니다."""
    return boto3.client(
        's3',
        aws_access_key_id=AWS_ACCESS_KEY_ID,
        aws_secret_access_key=AWS_SECRET_ACCESS_KEY,
        region_name=AWS_REGION
    )

@app.route('/api/v1/resource/download', methods=['GET'])
def download_resource():
    """S3 버킷에서 특정 리소스의 다운로드 URL을 생성하는 API"""
    file_name = request.args.get('file_name')
    if not file_name:
        return jsonify({"error": "file_name 파라미터가 필요합니다."}), 400

    s3_client = get_s3_client()
    
    try:
        # S3 객체 다운로드를 위한 Presigned URL 생성
        url = s3_client.generate_presigned_url(
            ClientMethod='get_object',
            Params={'Bucket': S3_BUCKET_NAME, 'Key': file_name},
            ExpiresIn=3600
        )
        logging.info(f"URL 생성 완료: {file_name}")
        return jsonify({"download_url": url, "status": "success"}), 200
        
    except ClientError as e:
        logging.error(f"S3 접근 중 에러 발생: {e}")
        return jsonify({"error": "파일을 가져오는 중 문제가 발생했습니다."}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)