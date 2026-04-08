import os
import logging
from flask import Flask, request, jsonify
import boto3
from botocore.exceptions import ClientError

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

# [안전한 조치] 환경 변수에서 자격 증명을 로드합니다.
# 하드코딩된 문자열이 아니므로 스캐너가 미탐지(Pass)해야 합니다.
AWS_ACCESS_KEY_ID = os.environ.get("AWS_ACCESS_KEY_ID")
AWS_SECRET_ACCESS_KEY = os.environ.get("AWS_SECRET_ACCESS_KEY")
AWS_REGION = os.environ.get("AWS_DEFAULT_REGION", "ap-northeast-2")
S3_BUCKET_NAME = os.environ.get("S3_BUCKET_NAME", "my-company-secure-bucket")

def get_s3_client():
    """환경 변수 기반으로 S3 클라이언트를 생성합니다."""
    if AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY:
        return boto3.client(
            's3',
            aws_access_key_id=AWS_ACCESS_KEY_ID,
            aws_secret_access_key=AWS_SECRET_ACCESS_KEY,
            region_name=AWS_REGION
        )
    else:
        # 키가 명시되지 않은 경우 IAM Role 등에 위임하는 기본 동작
        return boto3.client('s3', region_name=AWS_REGION)

@app.route('/api/v1/resource/download', methods=['GET'])
def download_resource():
    """S3 버킷에서 특정 리소스의 다운로드 URL을 생성하는 API"""
    file_name = request.args.get('file_name')
    if not file_name:
        return jsonify({"error": "file_name 파라미터가 필요합니다."}), 400

    s3_client = get_s3_client()
    
    try:
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