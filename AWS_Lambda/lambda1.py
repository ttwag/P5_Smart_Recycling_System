import json
import boto3


def lambda_handler(event, context):
    '''
    This function is triggered whenever a new .jpg object is created in the s3
    bucket. The function will read the .jpg file, detect labels
    through Rekognition, and check with the user-defined labels in /public/labels.json.
    If the labels match, Lambda pushes notification to a mobile phone through AWS Pinpoint
    '''
    
    # Initialize the Rekognition, s3, and Pinpoint client
    rekognition = boto3.client("rekognition")
    s3 = boto3.client("s3")
    pinpoint = boto3.client("pinpoint", region_name="us-west-2")
    iot = boto3.client('iot-data', region_name='us-west-1')
    # Pinpoint setup
    project_id = "your_Pinpoint_Project_ID"
    # Change the token
    device_token = "mobile_device_push_notification_token"
    


    
    
    # Change the S3 object name we want to retrieve depending on the s3 put event
    s3_image_object = event["Records"][0]["s3"]["object"]["key"]
    bucket_name = 'your_bucket_name'
    
    # Analyze the image and get the .json response
    s3Object={"Bucket":bucket_name, "Name":s3_image_object}
    rekog_response = rekognition.detect_labels(
        Image={"S3Object":s3Object},
        MaxLabels=5
    )
    
    # Retrieve the .json user-defined labels, user_Labels
    s3_labels = "path_to_user_label"
    raw_user_Labels = s3.get_object(Bucket=bucket_name, Key=s3_labels)
    text = raw_user_Labels['Body'].read().decode('utf-8')
    user_Labels = json.loads(text)
    
    # Print the .json message of user labels and the Rekognition labels
    print("user labels: ", user_Labels)
    print("Rekognition Labels: ", rekog_response)
    
    
    # Checks if the rekoginition labels are in user-defined labels and pushes notification if it's true
    for rekog_label in rekog_response["Labels"]:
        if (rekog_label["Name"] in user_Labels["label"]) and (rekog_label["Confidence"] > 96):
            print("Labels Match")
            pinpoint.send_messages(
                ApplicationId=project_id,
                MessageRequest={
                    'Addresses': {
                        device_token: {
                            'ChannelType': 'GCM'
                        }
                    },
                    'MessageConfiguration': {
                        'GCMMessage': {
                            'Action': 'OPEN_APP',
                            'Title': 'Special Object Found',
                            'Body': f'Special object with label "{rekog_label["Name"]}" found!'
                        }
                    }
                }
            )
            # Notify the Pi via MQTT to stop the process
            iot.publish(
                topic='topic_1',
                qos=1,
                payload=json.dumps({"message":"Stop"})
            )
            break
    
    return {
        'statusCode':200
    }