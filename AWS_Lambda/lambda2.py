import boto3
import json

def lambda_handler(event, context):
    """
    lambda_handler is triggered when new txt file is uploaded onto S3 bucket.
    It sends publishes Start or Stop message to the MQTT topic, topic_1 depending on the content of the txt file.
    """

    s3 = boto3.client('s3')
    iot = boto3.client('iot-data', region_name='us-west-1')

    # Specify the bucket name and the key (path) of the file you want to read
    bucket_name = 'your_bucket_name'
    path = 'path_to_txt'
    response = s3.get_object(Bucket=bucket_name, Key=path)
    file_content = response['Body'].read().decode('utf-8')
    print(file_content)
    
    if (file_content == "stop"):
        iot.publish(
            topic='topic_1',
            qos=1,
            payload=json.dumps({"message":"Stop"})
        )
    else:
        iot.publish(
            topic='topic_1',
            qos=1,
            payload=json.dumps({"message":"Start"})
        )

    
    return {
        'statusCode': 200,
    }
