import java.sql.*;


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
        System.out.println("You have reached main body!");


        //close the connection at the end
        if (con != null) {
            try {
                con.close();
                System.out.println("Connection closed successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}