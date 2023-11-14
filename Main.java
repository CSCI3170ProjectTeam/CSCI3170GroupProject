import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;

public class Main {

    public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db27?autoReconnect=true&useSSL=false";
    public static String dbUsername = "Group27";
    public static String dbPassword = "CSCI3170";

    public static Connection connectToMySQL() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
            System.out.println("Database Connected successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e) {
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
        // close the connection at the end
        if (con != null) {
            try {
                con.close();
                System.out.println("Exiting sales system...");
                System.out.println("Connection closed successfully!");
                System.exit(0); // end program
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void mainMenu(Connection con, Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println();
            System.out.println("-----Main menu-----");
            System.out.println("What kinds of operations would you like to perform?");
            System.out.println("1. Operations for administrator");
            System.out.println("2. Operations for salesperson");
            System.out.println("3. Operations for manager");
            System.out.println("4. Exit this program");
            System.out.print("Enter Your Choice: ");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    adminMenu(con, scanner);
                    break;
                case "2":
                    salespersonMenu(con, scanner);
                    break;
                case "3":
                    managerMenu(con, scanner);
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println("Invaild input");
                    break;
            }
        }
    }

    // 5.1 Operations for administration
    public static void adminMenu(Connection con, Scanner scanner) {
        boolean returnToMainMenu = false;
        while (!returnToMainMenu) {
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
            switch (input) {
                case "1":
                    createAllTables(con);
                    break;
                case "2":
                    deleteAllTables(con);
                    break;
                case "3":
                    loadDataFromFolder(con, scanner);
                    break;
                case "4":
                    showTable(con, scanner);
                    break;
                case "5":
                    returnToMainMenu = true;
                    break;
                default:
                    System.out.println("Invaild input");
                    break;
            }
        }
    }

    // 5.1.1 Create table schemas in the database
    public static void createAllTables(Connection con) {
        try {
            PreparedStatement[] statements = {
                    con.prepareStatement(
                            "CREATE TABLE category (cID INTEGER NOT NULL, cName VARCHAR(20) NOT NULL, PRIMARY KEY (cID))"),
                    con.prepareStatement(
                            "CREATE TABLE manufacturer (mID INTEGER NOT NULL, mName VARCHAR(20) NOT NULL, mAddress VARCHAR(50) NOT NULL, mPhoneNumber INTEGER NOT NULL, PRIMARY KEY (mID))"),
                    con.prepareStatement(
                            "CREATE TABLE part (pID INTEGER NOT NULL, pName VARCHAR(20) NOT NULL, pPrice INTEGER NOT NULL, mID INTEGER NOT NULL, cID INTEGER NOT NULL, pWarrantyPeriod INTEGER NOT NULL, pAvailableQuantity INTEGER NOT NULL, PRIMARY KEY (pID), FOREIGN KEY (mID) REFERENCES manufacturer(mID), FOREIGN KEY (cID) REFERENCES category(cID))"),
                    con.prepareStatement(
                            "CREATE TABLE salesperson (sID INTEGER NOT NULL, sName VARCHAR(20) NOT NULL, sAddress VARCHAR(50) NOT NULL, sPhoneNumber INTEGER NOT NULL, sExperience INTEGER NOT NULL, PRIMARY KEY (sID))"),
                    con.prepareStatement(
                            "CREATE TABLE transaction (tID INTEGER NOT NULL AUTO_INCREMENT, pID INTEGER NOT NULL, sID INTEGER NOT NULL, tDate DATE NOT NULL, PRIMARY KEY (tID), FOREIGN KEY (pID) REFERENCES part(pID), FOREIGN KEY (sID) REFERENCES salesperson(sID))")
            };

            System.out.print("Processing...");
            for (PreparedStatement stmt : statements) {
                stmt.execute();
                stmt.close(); // Close the prepared statement after execution
            }
            System.out.println("Done! Database is initialized!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // 5.1.2 Delete table schemas in the database
    public static void deleteAllTables(Connection con) {
        try {
            Statement stmt = con.createStatement();
            String[] tableNames = { "transaction", "part", "salesperson", "manufacturer", "category" }; // delete tables
                                                                                                        // that hv FK
                                                                                                        // first

            System.out.print("Processing...");
            for (String tableName : tableNames) {
                String sql = "DROP TABLE IF EXISTS " + tableName;
                stmt.executeUpdate(sql);
            }
            stmt.close(); // Close the statement after execution

            System.out.println("Done! Database is removed!");
        } catch (SQLException e) {
            System.out.println("Error deleting tables: " + e.getMessage());
        }
    }

    // 5.1.3 Load data from a dataset
    public static void loadDataFromFolder(Connection con, Scanner scanner) {
        try {
            // Scanner scanner = new Scanner(System.in);
            System.out.print("Type in the Source Data Folder Path: ");
            String folderPath = scanner.nextLine();
            loadDataFromFile(con, folderPath + "/category.txt", "category");
            loadDataFromFile(con, folderPath + "/manufacturer.txt", "manufacturer");
            loadDataFromFile(con, folderPath + "/part.txt", "part");
            loadDataFromFile(con, folderPath + "/salesperson.txt", "salesperson");
            loadDataFromFile(con, folderPath + "/transaction.txt", "transaction");
            System.out.println("Processing...Done! Data is inputted to the database!");
            // scanner.close();
        } catch (SQLException e) {
            System.out.println("Error loading data: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

    }

    private static void loadDataFromFile(Connection con, String filePath, String tableName) throws SQLException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            String line;
            String[] values;
            String toBeExecutedSQL;

            while ((line = reader.readLine()) != null) {
                values = line.split("\t"); // Split by tab

                switch (tableName) {
                    case "category":
                        toBeExecutedSQL = "INSERT INTO category (cID, cName) VALUES (?, ?)";
                        break;
                    case "manufacturer":
                        toBeExecutedSQL = "INSERT INTO manufacturer (mID, mName, mAddress, mPhoneNumber) VALUES (?, ?, ?, ?)";
                        break;
                    case "part":
                        toBeExecutedSQL = "INSERT INTO part (pID, pName, pPrice, mID, cID, pWarrantyPeriod, pAvailableQuantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        break;
                    case "salesperson":
                        toBeExecutedSQL = "INSERT INTO salesperson (sID, sName, sAddress, sPhoneNumber, sExperience) VALUES (?, ?, ?, ?, ?)";
                        break;
                    case "transaction":
                        toBeExecutedSQL = "INSERT INTO transaction (tID, pID, sID, tDate) VALUES (?, ?, ?, ?)";
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid table name: " + tableName);
                }

                PreparedStatement stmt = con.prepareStatement(toBeExecutedSQL);
                for (int i = 0; i < values.length; i++) {
                    if (i == 3 && tableName.equals("transaction")) {
                        // Convert the date format to YYYY-MM-DD to store it in database type date
                        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        java.sql.Date date = new java.sql.Date(inputFormat.parse(values[i]).getTime());
                        stmt.setDate(i + 1, date);
                    } else {
                        stmt.setString(i + 1, values[i]);
                    }
                }
                stmt.executeUpdate();
                stmt.close();
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new SQLException("Error loading data from file: " + filePath, e);
        }
    }

    // 5.1.4 Show the content of a specified table:
    public static void showTable(Connection con, Scanner scanner) {
        try {
            System.out.print("Which table would you like to show: ");
            String tableName = scanner.nextLine();
            showTableContent(con, tableName);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void showTableContent(Connection con, String tableName) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM " + tableName;
            ResultSet result = stmt.executeQuery(query);
            System.out.println("Content of table part:");
            // Get column names
            ResultSetMetaData rsmd = result.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.print("| " + rsmd.getColumnName(i) + " ");
            }
            System.out.println("|");

            // Print records
            while (result.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = result.getString(i);
                    if (rsmd.getColumnTypeName(i).equalsIgnoreCase("date")) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        value = dateFormat.format(result.getDate(i));
                    }
                    System.out.print("| " + value + " ");
                }
                System.out.println("|");
            }
            result.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving table content: " + e.getMessage());
        }
    }

    // 5.2 Operations for salesperson
    public static void salespersonMenu(Connection con, Scanner scanner) {
        boolean returnToMainMenu = false;
        while (!returnToMainMenu) {
            System.out.println();
            System.out.println("-----Operations for salesperson menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Search for parts");
            System.out.println("2. Sell a part");
            System.out.println("3. Return to the main menu");
            System.out.print("Enter Your Choice: ");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    searchPart(con, scanner);
                    break;
                case "2":
                    sellPart(con, scanner);
                    break;
                case "3":
                    returnToMainMenu = true;
                    break;
                default:
                    System.out.println("Invaild input");
                    break;
            }
        }
    }

    // 5.2.1 Search for computer parts available in the store based on part name or
    // manufacturer name
    public static void searchPart(Connection con, Scanner scanner) {
        try {
            System.out.println("Choose the search criterion: ");
            System.out.println("1. Part Name");
            System.out.println("2. Manufacturer Name");
            System.out.print("Choose the search criterion: ");
            String searchCriterion1 = scanner.nextLine().equals("1") ? "pName" : "mName";
            System.out.print("Type in the Search Keyword: ");
            String searchKeyWord = scanner.nextLine();
            System.out.println("Choose ordering: ");
            System.out.println("1. By price, ascending order");
            System.out.println("2. By price, descending order ");
            System.out.print("Choose the search criterion: ");
            String searchCriterion2 = scanner.nextLine().equals("1") ? "ASC" : "DESC";

            // retrive the result
            Statement stmt = con.createStatement();
            String query = String.format(
                    "SELECT part.pID, part.pName, manufacturer.mNAME, category.cNAME, part.pAvailableQuantity, part.pWarrantyPeriod, part.pPrice FROM part INNER JOIN manufacturer ON part.mID = manufacturer.mID INNER JOIN category ON part.cID = category.cID WHERE %s = '%s' ORDER BY part.pPrice %s",
                    searchCriterion1, searchKeyWord, searchCriterion2);
            ResultSet result = stmt.executeQuery(query);

            // print the result
            System.out.println("| ID | Name | Manufacturer | Category | Quantity | Warranty | Price |");
            while (result.next()) {
                for (int i = 1; i <= 7; i++) {
                    String value = result.getString(i);
                    System.out.print("| " + value + " ");
                }
                System.out.println("|");
            }
            result.close();
            stmt.close();
            System.out.println("End of Query");
        } catch (SQLException e) {
            System.out.println("Error searching parts: " + e.getMessage());
        }
    }

    // 5.2.2 Perform transaction for an available part
    public static void sellPart(Connection con, Scanner scanner) {
        try {
            System.out.print("Enter the Part ID: ");
            String partID = scanner.nextLine();
            System.out.print("Enter the Salesperson ID: ");
            String salespersonID = scanner.nextLine();

            // check if the part is not available; if so, it cannot be sold
            Statement stmt1 = con.createStatement();
            Statement stmt2 = con.createStatement();
            Statement stmt3 = con.createStatement();
            String query1 = String.format("SELECT part.pAvailableQuantity, part.pName FROM part WHERE part.pID = %s",
                    partID);
            ResultSet result = stmt1.executeQuery(query1);
            result.next();
            int remaningQuantity = Integer.valueOf(result.getString(1));
            if (remaningQuantity <= 0) {
                System.out.println("Error: Part Is Unavailable!");
                return;
            } else {
                // update part quantity
                String query2 = String.format("UPDATE part SET part.pAvailableQuantity = %d WHERE part.pID = %s",
                        remaningQuantity - 1, partID);
                stmt2.executeUpdate(query2);
                System.out
                        .println(String.format("Product: %s(id: %s) Remaining Quality: %d", result.getString(2), partID,
                                remaningQuantity - 1));
                // insert new transaction record
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String query3 = String.format("INSERT INTO transaction (pID, sID, tDate) VALUES (%s, %s, %s)", partID,
                        salespersonID, formatter.format(date));
                stmt3.executeUpdate(query3);

            }
            result.close();
            stmt1.close();
            stmt2.close();
            stmt3.close();
        } catch (SQLException e) {
            System.out.println("Error selling part: " + e.getMessage());
        }
    }

    //5.3 operations for manager
    public static void managerMenu(Connection con, Scanner scanner){ 
        boolean returnToMainMenu = false;
        while (!returnToMainMenu) {
            System.out.println();
            System.out.println("-----Operations for salesperson menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. List all salespersons");
            System.out.println("2. Count the number of transaction records of each salesperson within a given range on years of experience");
            System.out.println("3. Show the total sales value for each manufacturer");
            System.out.println("4. Show the N most popular parts");
            System.out.println("5. Return to the main menu");
            System.out.print("Enter Your Choice: ");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    listAllSalesperson(con, scanner);
                    break;
                case "2":
                    countSalespersonSales(con, scanner);
                    break;
                case "3":
                    countManufacturerSales(con, scanner);
                    break;
                case "4":
                    listMostPopularParts(con, scanner);
                    break;
                case "5":
                    returnToMainMenu=true;
                    break;
                default:
                System.out.println("Invaild input");
                break;
            }
        }
    }

    //5.3.1, complete
    public static void listAllSalesperson(Connection con, Scanner scanner){
        try{
            String ordering=new String("ASC");
            System.out.println("Choose ordering");
            System.out.println("1. By ascending order");
            System.out.println("2. By desending order");
            System.out.println("Choose the list ordering");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    ordering="ASC";
                    break;
                case "2":
                    ordering="DESC";
                    break;
                default:
                System.out.println("Invaild input");
                break;
            }
            
            Statement stmt=con.createStatement();
            String query=String.format("SELECT * FROM salesperson ORDER BY sExperience %s",ordering);
            ResultSet result=stmt.executeQuery(query);
            String[] field={"ID","Name","Mobile Phone","Years of Experience"};
            outputResult(result,field);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    //5.3.2, complete
    public static void countSalespersonSales(Connection con, Scanner scanner){
        try{
            String lowerBound=new String("0");
            String upperBound=new String("1");
            System.out.print("Type in lower bound for years of experience: ");
            String lowerBoundInput = scanner.nextLine();
            System.out.print("Type in uppper bound for years of experience: ");
            String upperBoundInput = scanner.nextLine();

            //check if input is an integer
            try{
                Integer.parseInt(lowerBoundInput);
                Integer.parseInt(upperBoundInput);
                lowerBound=lowerBoundInput;
                upperBound=upperBoundInput;
            } catch (Exception e){
                System.out.println("Invalid input!");
            }

            Statement stmt=con.createStatement();
            String query=String.format("SELECT sid,sname,sExperience,total FROM salesperson NATURAL JOIN (SELECT sid,count(*) as total FROM transaction GROUP BY sid) as VALUE WHERE sExperience>=%s AND sExperience <%s ORDER BY sid DESC",lowerBound,upperBound);
            ResultSet result=stmt.executeQuery(query);
            String[] field={"ID","Name","Years of Experience","Number of Transaction"};
            outputResult(result,field);
        } catch (Exception e){
            if (e instanceof NumberFormatException){
                System.out.println("Invalid Input!");
            }
            else System.out.println(e);
        }
    }

    //5.3.3, complete
    public static void countManufacturerSales(Connection con, Scanner scanner){
        try{
        Statement stmt=con.createStatement();
        //String query=new String("SELECT mID, mName, saleValue FROM manufacturer NATURAL JOIN (SELECT mID, SUM(saleCountPlusPID.saleCount*pPrice) as saleValue FROM part NATURAL JOIN (select pid,count(*) as saleCount FROM transaction GROUP BY pid) as saleCountPlusPID ) as saleValuePlusMID");
        String query="SELECT mID, mName, saleValue FROM manufacturer NATURAL JOIN (SELECT mID, SUM(saleCountPlusPID.saleCount*pPrice) as saleValue FROM part NATURAL JOIN (select pid,count(*) as saleCount FROM transaction GROUP BY pid) as saleCountPlusPID group by mID) as saleValuePlusMID ORDER BY saleValue DESC";
        ResultSet result=stmt.executeQuery(query);
        String[] field={"Manufacturer ID","Manufacturer Name","Total Sales Value"};
        outputResult(result,field);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    //5.3.4, potential bug
    public static void listMostPopularParts(Connection con, Scanner scanner){
        try{
        System.out.print("Type in number of parts: ");
        int N=1;
        String NInput=scanner.nextLine();
        //check if input is an integer and greater than 0
        int temp=Integer.parseInt(NInput);
        if (temp<0){
            throw new NumberFormatException();
        }
        else N=temp;
        
        Statement stmt=con.createStatement();
        String query=String.format("select pID, pName, saleCount FROM part NATURAL JOIN ( select pid, count(*) as saleCount FROM transaction GROUP BY pid ) as SALE WHERE saleCount != 0 ORDER BY saleCount DESC LIMIT %s",N);
        ResultSet result=stmt.executeQuery(query);
        String[] field={"Part ID","Part Name","No. of Transaction"};
        outputResult(result,field);
        }
        catch(Exception e){
            if (e instanceof NumberFormatException){
                System.out.println("Invalid Input!");
            }
            else System.out.println(e);
        }
    }

    //unfinish, enter null value in field to use original column name
    public static void outputResult(ResultSet result,String[] field){
        try{
            ResultSetMetaData rsmd = result.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (field==null)
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print("| " + rsmd.getColumnName(i) + " ");
                }
            else
                for (int i = 0; i < field.length; i++) {
                    System.out.print("| " + field[i] + " ");
                }
            System.out.println("|");

            // Print records
            while (result.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = result.getString(i);
                    if (rsmd.getColumnTypeName(i).equalsIgnoreCase("date")) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        value = dateFormat.format(result.getDate(i));
                    }
                    System.out.print("| " + value + " ");
                }
                System.out.println("|");
            }
    }
    catch (Exception e){
        System.out.println(e);
    }
    }
}