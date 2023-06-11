import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {

        Connection conn = null;

        //Capture username & password
        Scanner input = new Scanner(System.in);
        String url = "jdbc:mysql://localhost:3306/ACME";

        System.out.print("mysql username: ");
        String username = input.nextLine();

        System.out.print("password: ");
        String password = input.nextLine();

        DBHelper dbHelper = null;

        try{
            //Connect
            conn = DriverManager.getConnection(url, username, password); //connector jar file must be in the classpath
            System.out.println("Connection successful.");
            dbHelper = new DBHelper(input, conn); // connection needs to be established before setting up the helper
        }catch (SQLException e){
            System.out.println("Cannot connect to DB: " + e.getMessage());
            System.exit(0);
        }

        //do stuff
        int option = -1;
        while (option != 8) {
            System.out.println("Menu options: ");
            System.out.println("1 --> <placeholder>");
            System.out.println("2 --> <placeholder>");
            System.out.println("3 --> Create new Product");
            System.out.println("4 --> Least Popular Items in time range");
            System.out.println("5 --> Modify a product's quantity");
            System.out.println("6 --> Delete a product from inventory");
            System.out.println("7 --> <placeholder>");
            System.out.println("8 --> exit");

            System.out.print("Enter Option: ");
            option = input.nextInt();

            switch (option) {
                case 1:
                    System.out.println("placeholder");
                    break;
                case 2:
                    System.out.println("placeholder");
                    break;
                case 3:
                    System.out.println("Create New Product");
                    break;
                case 4:
                    System.out.println("Least Popular Items in time range");
                    break;
                case 5:
                    dbHelper.ModifyProductIDQuantityFromInventory();
                    break;
                case 6:
                    dbHelper.DeleteProductIDFromInventory();
                    break;
                case 7:
                    System.out.println("placeholder");
                    break;
                default:
                    System.out.println("Menu options are integers 1 - 8.");
                    break;
            }
        }

        input.close();
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connection closed. Goodbye.");
    }
}
