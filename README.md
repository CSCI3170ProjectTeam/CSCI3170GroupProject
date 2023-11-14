# CSCI3170 Group Project

## Description
This is an implementation of a sales system for a computer part store to store information about transactions, computer parts and salespersons. The system supports interactive inquiries from user input in command lines.

###

## Getting Started
### Compile and Run the Program
- To compile: javac Main.java
- To run: java -cp .:mysql-jdbc.jar Main

### Functions of the System
The Java program consists of the following functions:
1. Functions of an administrator:
   * ***Create table schemas in the database***: creates all tables for the sales system in the MySQL DBMS
   * ***Delete table schemas in the database***: deletes all existing tables of the sales system from MySQL DBMS
   * ***Load data from a dataset***: reads all data files from a user-specified folder and inserts the records into the appropriate table in the database (The program assumes that the user-specified folder must contain all five input files named category.txt, manufacturer.txt, part.txt, salesperson.txt and transaction.txt. Each data file stores the data corresponds to its filename)
   * ***Show the content of a specified table***: prints out the content of a user-specified table
     
2. Functions of a salesperson:
   * ***Search for Parts***: a salesperson can search for computer parts available in the store based on part name or manufacturer name, only one search criterion can be selected for each query. Then a salesperson can choose to sort the parts by price in ascending or descending order, and the result of matched parts will be printed in terms of their ID, Name, Manufacturer Name, Category Name, Available Quantity, Warranty Period and Price
   * ***Perform Transaction***: A salesperson can sell a part (i.e. perform a transaction) through the sales system by entering part ID of the part being sold and his/her(salesperson) ID. Note that if Part Available Quantity less than or equal to 0, part is unavailable for sold. An informative message on remaining available quantity of the part sold will be printed

3. Functions of a manager:
   * ***List all salespersons in ascending or descending order of years of experience***: a manager can list all salespersons in either ascending or descending order of their years of experience. After he/she specifies the output order, the program will perform the query and return the ID, name, phone number and years of experience of each salesperson
   * ***Count the number of transaction records of each salesperson within a given range on years of experience***: a manager can count the number of transaction records of each salesperson within a given range on years of experience (e.g. from 1 year to 3 years) inclusively. After he/she enters a specific range on years of experience, the program will perform the query and return the ID, name, years of experience and number of transaction records of each salesperson within the range on years of experience specified by the user inclusively. These transaction records should be sorted in **descending** order of Salesperson ID and outputted as a table
   * ***Sort and list the manufacturers in descending order of total sales value***: a manager cam sort the manufacturers according to their total sale values. After the program performs the query, it returns the results in terms of Manufacturer ID, Manufacturer Name and Total sales value in **descending** order of Total sales value as a table
   * ***Show the N most popular parts***: a manager can show the N(**an integer larger than 0**) parts that are most popular. After the manager enters the number of parts (N) that he/she wants to list, the program will perform the query and return the N parts that are most popular in terms of Part ID, Part Name and Total Number of Transaction in descending order of Total Number of Transaction as a table; a part without any transaction record should not be shown


## Authors: Group27
- Shang Feiyang	1155210365
- Ngai Tsz Hin	1155172069
- Fong Long Wai	1155177220
