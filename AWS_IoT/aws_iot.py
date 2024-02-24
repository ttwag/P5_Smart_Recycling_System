from awsiot import mqtt_connection_builder
from awscrt import mqtt
from uuid import uuid4
import json
import time
import threading

start_event = threading.Event()


def read_file(file_path):
    with open(file_path, 'r') as file:
        file_content = file.read()
    return file_content


def on_message_received(topic, payload, dup, qos, retain, **kwargs):
    global start_event, stop_event
    message = json.loads(payload)['message']
    print("Received message from topic '{}': {}".format(topic, json.loads(payload)['message']))
    if message == 'Start':
        start_event.set()
    else:
        start_event.clear()


def send_images():
    while True:
        start_event.wait()
        print("I am sending images")
        time.sleep(1)


if __name__ == '__main__':
    send_images_thread = threading.Thread(target=send_images)
    send_images_thread.start()
    # Create a MQTT connection
    try:
        mqtt_connection = mqtt_connection_builder.mtls_from_path(
            endpoint=read_file("./certs/endpoint.txt"),
            port=8883,
            cert_filepath='./certs/certificate.pem.crt',
            pri_key_filepath='./certs/private.pem.key',
            ca_filepath='./certs/AmazonRootCA1.pem',
            client_id="test-" + str(uuid4()),
            clean_session=False,
            keep_alive_secs=60)
        connect_future = mqtt_connection.connect()
        connect_future.result()
    except:
        print("Cannot Establish a Connection with MQTT")
    print("Connected!")

    # Subscribing to topic
    subscribe_future, packet_id = mqtt_connection.subscribe(
        topic='topic_1',
        qos=mqtt.QoS.AT_LEAST_ONCE,
        callback=on_message_received)
