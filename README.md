# ProjectDAD

Total applications involved for this project: 
1. User: Make bookings involving cars.
2. Admin(owner): Approve the booking made from the user.

Brief Explanation for each applications:
1. User: User can book a car according to their preferences like sit type, car and car brand and also can choose the booking hours from 1 to 23 hours.
2. Admin(owner): User can approve or decline the booking made by the user(customer) and add a new car if the user wants to.

Architecture/Layer Diagram:
1. We are using RESTful.

List of URL end points middleware RESTful:
1. "http://localhost/ProjectDAD/restful.php"

Functions/Features in the middleware:
1. User Login (userlogin):
   Functionality: Authenticates a user based on the provided username and password.
   Query: SELECT * FROM users WHERE usrname = :usrname AND pass = :pass
   Response: Returns user details if authentication is successful, otherwise returns an error.
   
2. Car Table (cartable):
   Functionality: Retrieves information about cars based on their status.
   Query: SELECT * FROM ownercars WHERE Status = :Status
   Response: Returns a list of cars that match the given status.

3. Booking (booking):
   Functionality: Updates the status of a car to 'Pending' and creates a booking record.
   Queries: UPDATE ownercars SET Status='Pending' WHERE carid=:carid
    INSERT INTO booking(carId, userId, bookStart, bookEnd) VALUES (:carid, :userid, :currentDateTime, :until)
   Response: Returns success or failure status.

4. Pending Bookings (pending):
   Functionality: Retrieves information about pending bookings and associated user and car details.
   Query: SELECT * FROM booking JOIN users ON booking.userId=users.id JOIN ownercars ON ownercars.carid = booking.carId WHERE Status = :Status
   Response: Returns a list of pending bookings.

5. Approval (approval):
   Functionality: Approves or rejects a car booking.
   Query: If todo is "Accept": UPDATE ownercars SET Status='Booked' WHERE carid=:carid
    If todo is not "Accept": UPDATE ownercars SET Status='Available' WHERE carid=:carid
   Response: Returns success or failure status.

6. Add Car (addcar):
   Functionality: Adds a new car to the ownercars table.
   Query: INSERT INTO ownercars (carbrand, sitType, platNo, pricePerHour, Status) VALUES (:carbrand, :sitType, :platNo, :pricePerHour, 'Available')
   Response: Returns success or failure status.
   
The database and tables involve in the projects:
1. Database: carrental.sql
2. Tables Involve: i. user
                   ii. ownercars
                   iii. booking

Video Link:
https://youtu.be/x4A20Sis-EU
