# Carpooling-Mobile-App
A carpooling Android application.

1.	Completed tasks :
  Following functionalities are developed:
  •	Login Functionality: 
  New user can register to the application. User needs to enter his first name, Last name , email address , mobile number and password.
  After validation of the entered data, user will get redirected to decision page.
  Existing users can login by entering their username and password.
  Validation of the user will be done. If password is entered incorrectly, user will see the message saying incorrect password and he needs to enter it again.
  Application also offers Facebook and Google login.
  
  •	Decision Functionality:
  Every time user logs in, user can decide if he needs a ride or he needs to offer a ride.
  (Driver or passenger)
  
  •	Maps and direction functionality:
  User can see his location on Google map. User can enter his origin and destination.
  User will see the route from origin to destination on Google map.   
  
  •	Autocomplete Service:
  When user is entering origin and destination, application helps user to autocomplete by showing options of places.
  
  •	Connectivity with backend:
  Database used for the project is Backendless. The system table ‘User’ is used for handling users login data.
  Geolocation functionality is used to store driver’s and passenger’s origin and destination longitude and latitude.
  Fetching records in 5 Miles radius.
  Geolocation radius feature is used to fetch the records of users within 5 Miles of radius.
  
  •	Showing records in a list
  Depending on the entered origin and entered destination, the user will get only those list of available drivers or passengers whose origin lies within a radius of 5 miles from the entered origin and destination lies within 5 miles of the entered destination.
  A list of all real time passengers will be shown to the driver.
  The same functionality will also work for passengers.
  
  •	Sending requests to other users
  User can see list of drivers or passengers (depending on the user type) after he enters his origin and destination. The list contains name of the user, his origin and destination and status as available, pending or confirmed.
  Users can send request to other users for car-pooling on clicking on ‘Request’ button.
  
  •	User can confirm Requests:
  Users can get requests from other users for car-pooling. So a list will be shown where all requests for the user will be shown.
  User can confirm a request and a confirmation will be sent to both the users.
  
  •	User can view detailed information:
  The user can view detailed information about the available list of drivers/passengers.
  Phone calling and email sending functionality implemented in the detailed view.
  
  •	About Us page:
  User can click on about us page and see the information of developers and Facebook and twitter details.


2. Guidelines for user to use the application step by step:
  Step 1: 
  First time user needs to sign up to the application. User will see a profile creation page where he needs to enter all his personal data. 
  Email address will be his user name and he can set his password at this time.
  Users can log in using their Facebook as well as Google login.
  
  Step 2:
  After successful login, user will see a page where he can choose if he is a driver or passenger for that given time.
  
  Step 3:
  User will see the Google Map page. He will see his current location on Google map.
  He can enter his origin and destination.
  Application will help user to auto complete the origin and destination fields.
  Click on search button.
  
  Step 4:
  When user click on search button, user will see a list. If user is a driver then he will see a list of all passengers who has origin and destination within the radius of 5 Miles as that of user.
  User will see all the available passengers, their name, phone number and origin location and destination location.
  
  Step 5: 
  User can send request to available passengers by clicking Send button.

  Step 6:
  User can also see the requests which he has received from other users by clicking the ‘Request List’ tab on action menu.


3.	URL of video clip of the running application:
  Video is created which gives idea about application works and how database is created and maintained.    https://drive.google.com/open?id=0B89O-pZKRTYhOXVEZVlJQlQwdms

4.	Third party services used in project:
  1.	Google Login
  2.	Facebook Login
  3.	Google Map API
  4.	Map Direction API
  5.	Auto Completion API
  6.	Backendless Backend

5.	Technical skills used in project:
  Google Map API integration
  Backendless – Backend used for the project 
  Social login – Facebook and Google login
  Recycler View and Card View
  Call and Email functionality
  Toolbars and menus

6.	Database:
  Database used is Backendless database. 
  Used ‘Geolocation’ services to store and fetch longitude and latitude of the origin and destination.
  Used ‘Search in radius’ functionality to fetch suitable drivers or passengers

7.	Future Scope :
  User can set a time in advance for future rides when he needs to do car-pooling.
  Payment options through goggle wallet or paypal.
  Round trip car-pooling
  Car-pooling selection preference with respect to gender, age etc.


