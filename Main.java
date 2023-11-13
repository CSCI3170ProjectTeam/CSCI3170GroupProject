import java.sql.*;
import java.util.Scanner;

public class Main {

    public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db27?autoReconnect=true&useSSL=false";
    public static String dbUsername = "Group27";
    public static String dbPassword = "CSCI3170";

    public static Connection connectToMySQL(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
            System.out.println("Database Connected successfully!");
        } catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e){
            System.out.println(e);
        }
        return con;
    }

    public static void main(String[] args) {
        Connection con = connectToMySQL();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to sales system!");
        
        mainMenu(con, scanner);

        scanner.close();
        //close the connection at the end
        if (con != null) {
            try {
                con.close();
                System.out.println("Exiting sales system...");
                System.out.println("Connection closed successfully!");
                System.exit(0); //end program
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void mainMenu(Connection con, Scanner scanner){
        boolean exit = false;
        while(!exit){
            System.out.println();
            System.out.println("-----Main menu-----");
            System.out.println("What kinds of operations would you like to perform?");
            System.out.println("1. Operations for administrator");
            System.out.println("2. Operations for salesperson");
            System.out.println("3. Operations for manager");
            System.out.println("4. Exit this program");
            System.out.print("Enter Your Choice: ");
            String input = scanner.nextLine();
            switch(input){
                case "1":
                    adminMenu(con, scanner);
                    break;
                case "2":
                    // salespersonMenu(con);
                    break;
                case "3":
                    // manager(con);
                    break;
                case "4":
                    exit = true;
                    break;
                default :
                    System.out.println("Invaild input");
                    break;
            }
        }
    }

    //1. Operations for administration 5.1
    public static void adminMenu(Connection con, Scanner scanner){
        boolean returnToMainMenu = false;
        while(!returnToMainMenu){
            System.out.println();
            System.out.println("-----Operations for administrator menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Create all tables");
            System.out.println("2. Delete all tables");
            System.out.println("3. Load from datafile");
            System.out.println("4. Show content of a table");
            System.out.println("5. Return to the main menu");
            System.out.print("Enter Your Choice: ");
            String input = scanner.nextLine();
            switch(input){
                case "1":
                    createAllTables(con);
                    break;
                case "2":
                    deleteAllTables(con);
                    break;
                case "3":
                    // lib(con);
                    break;
                case "4":

                    break;
                case "5":
                    returnToMainMenu = true;
                    break;
                default :
                    System.out.println("Invaild input");
                    break;
            }
        }
    }
    //5.1.1 Create table schemas in the database
    public static void createAllTables(Connection con) {
        try{
            PreparedStatement[] statements = {
                con.prepareStatement("CREATE TABLE category (cID INTEGER NOT NULL, cName VARCHAR(20) NOT NULL, PRIMARY KEY (cID))"),
                con.prepareStatement("CREATE TABLE manufacturer (mID INTEGER NOT NULL, mName VARCHAR(20) NOT NULL, mAddress VARCHAR(50) NOT NULL, mPhoneNumber INTEGER NOT NULL, PRIMARY KEY (mID))"),
                con.prepareStatement("CREATE TABLE part (pID INTEGER NOT NULL, pName VARCHAR(20) NOT NULL, pPrice INTEGER NOT NULL, mID INTEGER NOT NULL, cID INTEGER NOT NULL, pWarrantyPeriod INTEGER NOT NULL, pAvailableQuantity INTEGER NOT NULL, PRIMARY KEY (pID), FOREIGN KEY (mID) REFERENCES manufacturer(mID), FOREIGN KEY (cID) REFERENCES category(cID))"),
                con.prepareStatement("CREATE TABLE salesperson (sID INTEGER NOT NULL, sName VARCHAR(20) NOT NULL, sAddress VARCHAR(50) NOT NULL, sPhoneNumber INTEGER NOT NULL, sExperience INTEGER NOT NULL, PRIMARY KEY (sID))"),
                con.prepareStatement("CREATE TABLE transaction (tID INTEGER NOT NULL, pID INTEGER NOT NULL, sID INTEGER NOT NULL, tDate DATE NOT NULL, PRIMARY KEY (tID), FOREIGN KEY (pID) REFERENCES part(pID), FOREIGN KEY (sID) REFERENCES salesperson(sID))")
            };
        
            for (PreparedStatement stmt : statements) {
                stmt.execute();
                stmt.close(); // Close the prepared statement after execution
            }
            System.out.print("Processing...");
            System.out.println("Done! Database is initialized!");
        }catch (Exception  e) {
            System.out.println(e);
        }
    }
    //5.1.2 Delete table schemas in the database
    public static void deleteAllTables(Connection con) {
        try {
            Statement stmt = con.createStatement();
            String[] tableNames = {"transaction", "part", "salesperson", "manufacturer", "category"}; //delete tables that hv FK first
    
            for (String tableName : tableNames) {
                String sql = "DROP TABLE IF EXISTS " + tableName;
                stmt.executeUpdate(sql);
            }
            stmt.close(); // Close the statement after execution
            System.out.print("Processing...");
            System.out.println("Done! Database is removed!");
        } catch (SQLException e) {
            System.out.println("Error deleting tables: " + e.getMessage());
        }
    }



}