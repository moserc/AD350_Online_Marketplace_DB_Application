import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Connection conn = null;

        try{
            //Capture username & password
            Scanner input = new Scanner(System.in);
            String url = "jdbc:mysql://localhost:3306/ACME";

            System.out.println("mysql username: ");
            String username = input.nextLine();

            System.out.println("password: ");
            String password = input.nextLine();

            //Connect
            conn = DriverManager.getConnection(url, username, password); //connector jar file must be in the classpath
            System.out.println("Connection successful.");

            //do stuff
            int option;
            while (true) {
                System.out.println("Menu options: ");
                System.out.println("1 --> <blank>");
                System.out.println("2 --> <blank>");
                System.out.println("3 --> <blank>");
                System.out.println("4 --> <blank>");
                System.out.println("5 --> <blank>");
                System.out.println("6 --> <blank>");
                System.out.println("7 --> <blank>");
                System.out.println("8 --> exit");

                option = input.nextInt();

                switch (option) {
                    case 1:
                        System.out.println("You chose Option 1");
                        break;
                    case 2:
                        System.out.println("You chose Option 2");
                        break;
                    case 3:
                        System.out.println("You chose Option 3");
                        break;
                    case 4:
                        System.out.println("You chose Option 4");
                        break;
                    case 5:
                        System.out.println("You chose Option 5");
                        break;
                    case 6:
                        System.out.println("You chose Option 6");
                        break;
                    case 7:
                        System.out.println("You chose Option 7");
                        break;
                    case 8:
                        //close connection
                        conn.close();
                        System.out.println("Connection closed. Goodbye.");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }



        }catch (Exception e){
            System.out.println("Cannot connect to DB: " + e);
        }
    }

}
