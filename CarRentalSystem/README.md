Car Rental System - Team Instructions

Follow these steps to set up the project on your laptop:

1. Project Documentation

Detailed project notes and UI mockups are located here: 

Link: https://docs.google.com/document/d/1EZHJKBCFYK80VYigqQsH_VqUav6CxytPqbfj6tH1TYk/edit?usp=sharing

2. Database Setup (XAMPP)

Start Services: Open XAMPP and start Apache and MySQL.

Access Admin: Go to http://localhost/phpmyadmin in your browser.

Create DB: Create a new database named car_rental_db.

Import Tables: Click on car_rental_db -> click the Import tab -> choose the database_setup.sql file from our project folder -> click Go.

3. NetBeans Library Fix

Check Libraries: Open the project in NetBeans and check the "Libraries" folder.

Fix Broken Link: If the MySQL JAR has a red error:

Right-click the JAR and select Remove.

Right-click the Libraries folder and select Add JAR/Folder.

Navigate to the lib/ folder inside our project and select mysql-connector-j-9.6.0.jar.

4. Final Connection Test

Run the file carrentalsystem.core.MySQLIntegrationTest.

If you see "Database Connected Successfully!", your setup is complete.