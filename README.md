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

1. Administrator:

   - **CREATE TABLE**:

     Creates all tables for the sales system in the MySQL DBMS

   - **DELETE TABLE**:

     Deletes all existing tables of the sales system from MySQL DBMS

   - **LOAD DATA**:

     Reads all data files from a user-specified folder and inserts the records into the appropriate table in the database (The program assumes that the user-specified folder must contain all five input files named category.txt, manufacturer.txt, part.txt, salesperson.txt and transaction.txt. Each data file stores the data corresponds to its filename)

   - **SHOW TABLE CONTENT**:

     Prints out the content of a user-specified table

2. Salesperson:

   - **SEARCH FOR PARTS**:

     Search for computer parts available in the store based on part name or manufacturer name, only one search criterion can be selected for each query. Then choose to sort the parts by price in _ascending or descending_ order, and the result of matched parts will be printed in terms of their ID, Name, Manufacturer Name, Category Name, Available Quantity, Warranty Period and Price

   - **PERFORM TRANSACTION**:

     Sell a part (i.e. perform a transaction) by entering part ID of the part being sold and salesperson ID. Note that if Part Available Quantity _less than or equal to 0_, part is unavailable for sold. An informative message on remaining available quantity of the part sold will be printed

3. Manager:

   - **LIST SALESPERSONS IN ASC/DESC ORDER OF EXPERIENCE YEARS**:

     List all salespersons in either ascending or descending order of their years of experience. After the output order is specified, the program will perform the query and return the ID, name, phone number and years of experience of each salesperson

   - **COUNT TRANSACTION NUMBERS OF SALESPERSON WITHIN EXPERIENCE YEAR RANGES**:

     Count the number of transaction records of each salesperson within a given range on years of experience (e.g. from 2 year to 5 years) inclusively. After a specific range on years of experience is entered, the program will perform the query and return the ID, name, years of experience and number of transaction records of each salesperson within the range on years of experience specified by the user inclusively for the lower bound and exclusively for the upper bound. These transaction records should be sorted in _descending_ order of Salesperson ID and outputted as a table

   - **SORT AND LIST MANUFACTURERS IN DESC ORDER OF TOTAL SALES VALUE**:

     Sort the manufacturers according to their total sale values. After the program performs the query, it returns the results in terms of Manufacturer ID, Manufacturer Name and Total sales value in _descending_ order of Total sales value as a table

   - **SHOW N MOST POPULAR PARTS**:

     Show the N(_an integer larger than 0_) parts that are most popular. After the number of parts (N) is entered, the program will perform the query and return the N parts that are most popular in terms of Part ID, Part Name and Total Number of Transaction in descending order of Total Number of Transaction as a table; a part _without any transaction record_ should not be shown

## Authors: Group27

- Feiyang
- Tsz Hin
- Long Wai
