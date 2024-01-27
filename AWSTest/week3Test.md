# Week 3 sTest Log

This test log documents the response from AWS Rekognition on the test1 to test10 in ```../image```

## Complex Test
The test images contain multiple objects to mimic real world scenario.

Accuracy: 10%

| Test | Manual Label | Rekognition's 5 Label | Label Match? |
| -----|---------|------|---|
|1| Digital Watch, Key | Box, Accessories, Bag, Handbag, First Aid | No
|2| Key | Business Card, Paper, Text, Document, Receipt | No
|3|Wallet| Accessories, Wallet | Yes
|4|Laptop| Text, Document, Paper, Bandage, First Aid |No
|5|Laptop| Text, Document, Paper | No
|6|Mobile Phone| Text, Bandage, First Aid, Tape | No
|7|Mobile Phone| Cheese, Food, Document, Receipt, Text | No
|8|Electronics| Document, Receipt, Text, Bag | No
|9|Credit Card| Text, Accessories, Bag, Handbag, Bandage | No
|10|Credit Card| Text, Bandage, First Aid, Bag, Document | No

## Simple Test
The test images contain only the item of interest. Note that the test gives correct credit to closely match label such as Digital watch and Wristwatch

Accuracy: 90%

| Test | Manual Label | Rekognition's Top 5 Label | Label Match? |
| -----|---------|------|---|
|11|Sock|Clothing, Hoisery, Sock, Animal, Reptile | Yes
|12|Digital Watch | Wristwatch, Arm, Body Part, Person, Electronics | Yes
|13|Laptop| Computer, Electronics, Laptop, PC, Computer Hardware | Yes
|14|Mobile Phone| Electronics, Mobile Phone, Phone, iPhone, iPod | Yes
|15|Laptop| Computer Electronics, Laptop, PC, Computer Hardware | Yes
|16|Wallet|Accessories, Wallet| Yes
|17|Key|Appliance, Ceiling Fan, Device, Electrical Device, Gun | No
|18|Water bottle| Bottle, Mortar Shell, Weapon, Water bottle, Cup | Yes
|19|Key|Key| Yes
|20|Digital Watch| Wristwatch, Arm, Body Part, Person, Accessories | Yes

## Issues Found
* Some labels are too general. (e.g., Accessories)
* How would the user choose the correct label out of similar ones? (e.g., Wristwatch, Digital watch)
* Rekognition cannot detect object from complex scene with multiple objects.