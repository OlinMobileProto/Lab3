##Mobile Proto Lab 3

###Learning Objectives

By the end of this lab, you should be able to:

- Developing universal and relevant Android development skills, including:
  - Improve abilities to create code that uses third party libraries
  - Demonstrate ability to use asynchronous sensors on cell phone
  - Improve ability to work with SQL relational database
  - Become closer to creating Android code that would be considered professional
- Organize your time effectively over the span of a project
- Work effectively with a partner
- Design an intuitive user interface

###Application Objectives

Students will create a scavenger hunt app that will guide a user on a video scavenger hunt. Using the GPS on their phone they will determine if the user has arrived at the next clue location. Once at the next location the user should use the camera to upload an image to Amazon S3 and then download a video showing the next clue. 

To accomplish these goals you will have to learn, use or improve:

- Switching between fragments
- Saving infromation to a web SQL database
- Make HTTP Requests using Volley
- Understand setup process for third party APIs
- Reading device sensor data
- Upload, download, and display media including photos and videos

###App functionalities
- Correctly view GPS data and trigger event when within specific location range
  - Protip: You can follow this link to mock your location so you don't have to run around everywhere. http://www.phonearena.com/news/Heres-how-to-easily-fake-your-GPS-location-on-Android_id62775 or if on an emulator http://stackoverflow.com/questions/2279647/how-to-emulate-gps-location-in-the-android-emulator
- Upload/download to/from a class SQL database
- Display video from S3 bucket
- Take picture, upload image to S3

###Weekly benchmark goals

You need to meet the following goals by the following dates:
- **8/8** - Your interface should be designed
- **8/15** - Two of the above functionalities should be implemented

###Implementation Tips

Since this lab is longer we will give you some guidance on implementation:

- We expect you to separate the functionalities logically into separate classes. 

####Using the database
- We have created a database on a web server that everyone can connect to. This database will contain a table that holds the UUID of the video, the latitude, longitude and clue id. This table is called SCAVENGER\_INFO and contains the columns LOCATION\_ID, LATITUDE, LONGITUDE, and VIDEO_ID. These should be pretty straight forward as to what they contain. **DO NOT ADD OR DELETE INFORMATION FROM THIS TABLE**
- You will also create your own table that should contain atleast columns for the UUID of the photo you upload to S3 and also the ID of the location the photo was uploaded at. Please name this table something identifiable, ie CHRIS_PHOTOS
- Speaking of UUID, heres a description http://www.tutorialspoint.com/java/util/uuid_randomuuid.htm you should give each of your photos a UUID and store them in S3 using that UUID.
- To log into the database: Its IP is 45.55.65.113, you can use the default port name. The database name is mobproto. Your username is student and your password is MobProto.
- This [tutorial](http://www.tutorialspoint.com/jdbc/jdbc-db-connections.htm) gives you a good way to get a Connection to a SQL database. The URL that is referenced in this tutorial would look like jdbc:mysql://45.55.65.113/mobproto
- Once you have a connection, you should be using a PreparedStatement to run your SQL queries. [This](http://www.mkyong.com/jdbc/jdbc-preparestatement-example-select-list-of-the-records/) shows a decent example of doing that. 
- 
####Using S3
- The s3 bucket name you will be using is olin-mobile-proto.
- The API key will be emailed to you.
- You must get the S3 URL of the videos in order to easily play the video in a VideoView.
- The database contains the S3 unique ID of the file. You need to use the Amazon SDK to get the URL from the unique identifier
- upload your images with a UUID.

####Video download
- You should use a VideoView to view the video. See documentation here: http://developer.android.com/reference/android/widget/VideoView.html

####Image upload
- We're using the Android S3 SDK. A super-relevant code example can be found here: https://github.com/aws/aws-sdk-android.  You can include the SDK in gradle with the line 'com.amazonaws:aws-android-sdk-core:2.2.7'

####Device hardware
- Information about sensor data can be found here: https://developer.android.com/guide/topics/sensors/index.html
- Information about the camera can be found here: http://developer.android.com/guide/topics/media/camera.html

*Note: we're intentionally giving you less information. You should be adept at googling issues and finding documentation for classes on your own.*

###Grading:

1. Functionality (65%):
  - Correctly view GPS data and trigger event when within specific location range (15%)
  - Upload/download to a class SQL database (15%)
  - Display video from S3 (20%)
  - Successfully take picture, upload image to S3 (15%)
2. Code Quality (20%):
  - Android guidelines (10%)
  - Java best practices/ general MVC guidelines/ comments (10%)
3. Effective and intuitive user interface (15%)

