# README
PA 1 Assignment
Team 2: Raven O'Rourke & Lora Kalthoff

## Server:
+ Initialize server from default file "initialize.txt", or from another file containing a Company object.
++ The company in "initialize.txt" can be logged in from Admin App using alice@admin.com & password 'pw1234567', and from Catalog app using charlie@customer.com & password '1234567pw'.
+ If a company was saved previously, it can be initialized from "savedcompany.txt".
+ If initialize.txt does not exist or needs to be updated, run InitializeCompany.java to create the file.
+ If no file is selected, an empty company is created with admin login "admin@admin.com" and password "00000000".
+ Selecting "Start Server" will begin listening for client connections on port 10001.
+ Server creates seperate threads for each client. The server can be terminated by admin users.
+ When the server is terminated, it saves the current company to "savedcompany.txt".

## Admin:
+ Connect to the server with specified ip and port. (Server must be running)
+ Login with a valid email address and password.
+ To disconnect from the server, close the window.
+ To terminate the server and save the company, press the button on the home page.
#### Features:
+ Add new users to the database
+ Add and remove Products from the database
+ Add and remove Categories and add/remove them from Products
+ View finalized Order list and specify to view orders between to dates.

## Catalog:
+ Connect to the server with specified ip and port. (Server must be running)
+ Login with a valid email address and password.
+ To disconnect from the server, close the window.
#### Features:
+ Browse the catalog of Products by search or by catalog
+ Add and remove Products to the cart
+ Cancel Order
+ Finalize Order when completed.
