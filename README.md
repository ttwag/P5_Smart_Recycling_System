# Smart Recycling System

This project addresses the issue of waste management by designing a robotic trash sorting system. The system enhances recycling efforts by sorting plastic bottles from landfill waste and incorporating a detection mechanism for special objects. Our design also communicates with users through a mobile application, providing real-time updates on the sorting process.

Here's a video demo of the project: 



This GitHub repository only stores the code for the special object detection part.

The robotic arm part could be found in 


## High-Level Goal

**Special Object Detection**

![Figure1](./image/Figure1.png)

**Robotic Arm**

![Figure2](./image/Figure2.png)

## Implementation

**Special Object Detection**
1. Users upload a label representing the special object they want to find (EX: Cat) in a file labels.json to the AWS **S3 Bucket**.
2. Raspberry Pi uploads the top-view image of the conveyor belt to the S3 Bucket continuously.
3. The AWS **Lambda** function sends the Raspberry Pi image to the AWS **Rekognition**.
4. Lambda checks if the user label matches with the object labels detected by Rekognition.
5. If the labels match, Lambda asks AWS Pinpoint to push a message to the user's mobile app and AWS IoT Core to publish the stop message that stops the Raspberry Pi from taking more image. 
6. If the labels don't match, nothing happens and the Raspberry Pi will continue to send images to AWS S3.
7. Note that once the Raspberry Pi stops, user can publish a message to the IoT Topic to ask it to continue.

![Figure3](./image/Figure3.png)

**Robotic Arm**
* The robotic arm's setup and implementation is not included in this GitHub Repository, but how it generally works is illstrated below.

1. The laptop connects to the Teensy microcontroller via USB.
2. Before we can run any program, the Teensy microcontroller calibrates the robotic arm through the limit switches. The limit switches helps the arm to go back to it's stnadby position.
3. When user instructs the joint of the robot to move via the app on the laptop, Teensy uses the Stepper Driver to run the motor and move the robot's joint. The encoder helps Teensy to move the motor precisely.
4. The laptop controls the Arduino nano to use the relay and solenoid valve to open and close the gripper.

![Figure4](./image/Figure4.png)



## List of Material

## File Structure

## Acknowledgment


## Helpful Links


