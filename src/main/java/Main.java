import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class Main {


    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        Connection conn = null;

        //Capture username & password
        String url = "jdbc:mysql://localhost:3306/ACME";

        System.out.print("mysql username: ");
        String username = input.nextLine();

        System.out.print("password: ");
        String password = input.nextLine();

        DBHelper dbHelper = null;

        try{
            //Connect
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful.");
            dbHelper = new DBHelper(input, conn); // connection needs to be established before setting up the helper
        }catch (SQLException e){
            System.out.println("Cannot connect to DB: " + e.getMessage());
            System.exit(0);
        }

        int option = -1;
        while (option != 8) {
            System.out.println("\nMenu options: ");
            System.out.println("1 --> Products in inventory");
            System.out.println("2 --> Most popular items in time range");
            System.out.println("3 --> Create new Product");
            System.out.println("4 --> Least Popular Items in time range");
            System.out.println("5 --> Modify a product's quantity");
            System.out.println("6 --> Delete a product from inventory");
            System.out.println("7 --> List users to send promotional emails");
            System.out.println("8 --> exit\n");

            System.out.print("Enter Option: ");
            option = input.nextInt();

            switch (option) {
                case 1:
                    dbHelper.ShowInventory();
                    break;
                case 2:
                    dbHelper.PopularItems();
                    break;
                case 3:
                    try {
                        PreparedStatement st = conn.prepareStatement("INSERT INTO Product (productName, currentMSRP, costToProduce, releaseDate, description) VALUES (?, ?, ?, ?, ?)");
                        System.out.print("Enter Name: ");
                        input.nextLine();
                        st.setString(1, input.nextLine());
                        st.setString(2, Double.toString(ensureInputDouble("currentMSRP")));
                        st.setString(3, Double.toString(ensureInputDouble("costToProduce")));
                        st.setString(4, ensureInputFormat("^\\d{4}-\\d{2}-\\d{2}$", "releaseDate"));
                        System.out.println("Enter Description: ");
                        st.setString(5, input.nextLine());
                        st.executeUpdate();
                        System.out.println("Product Successfully Created\n");
                        st.close();
                    }catch (SQLException e)
                    {
                        System.out.println("Product not Created\n");
                        System.err.println(e.getMessage());
                    }
                    break;
                case 4:
                    try
                    {
                        PreparedStatement st = conn.prepareStatement("SELECT sub.productName AS productName, sub.productID AS productID " +
                                "FROM (" +
                                "    SELECT p.productName, p.productID, COUNT(*) AS occurrences" +
                                "    FROM transactionLineItem tli" +
                                "    JOIN transaction t ON tli.transactionID = t.transactionID" +
                                "    JOIN product p ON tli.productID = p.productID" +
                                "    WHERE t.transactionDate BETWEEN ? AND ?" +
                                "    GROUP BY tli.productID" +
                                ") AS sub " +
                                "WHERE sub.occurrences = (" +
                                "    SELECT MIN(occurrences) FROM (" +
                                "        SELECT COUNT(*) AS occurrences" +
                                "        FROM transactionLineItem tli" +
                                "        JOIN transaction t ON tli.transactionID = t.transactionID" +
                                "        WHERE t.transactionDate BETWEEN ? AND ?" +
                                "        GROUP BY tli.productID" +
                                "    ) AS sub2" +
                                ")");

                        input.nextLine();
                        String startDate = ensureInputFormat("^\\d{4}-\\d{2}-\\d{2}$", "Start Date");
                        String endDate = ensureInputFormat("^\\d{4}-\\d{2}-\\d{2}$", "End Date");

                        st.setString(1, startDate);
                        st.setString(2, endDate);
                        st.setString(3, startDate);
                        st.setString(4, endDate);

                        ResultSet set = st.executeQuery();
                        System.out.println("Result:");
                        while(set.next())
                        {
                            System.out.printf("\tProduct\n\t\tID : %s Name : %s%n", set.getString("productID"), set.getString("productName"));
                        }
                        System.out.println();

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 5:
                    dbHelper.ModifyProductIDQuantityFromInventory();
                    break;
                case 6:
                    dbHelper.DeleteProductIDFromInventory();
                    break;
                case 7:
                    try {
                        dbHelper.processCase7();
                    } catch (SQLException e) {
                        System.out.println(e);
                    }
                    break;
                default:
                    if(option == 8) continue;
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

    private static double ensureInputDouble(String fieldName)
    {
        double toReturn = Double.NaN;
        boolean isValid = false;
        while(!isValid)
        {
            try {
                System.out.printf("Enter %s: " , fieldName);
                toReturn = Double.parseDouble(input.nextLine());
                isValid = true;
            }catch (NumberFormatException e)
            {
                System.out.printf("%s must be an Double, Try again%n", fieldName);
            }
        }

        return toReturn;
    }

    private static int ensureInputInteger(String fieldName)
    {
        int toReturn = Integer.MIN_VALUE;
        boolean isValid = false;
        while(!isValid)
        {
            try {
                System.out.printf("Enter %s: " , fieldName);
                toReturn = Integer.parseInt(input.nextLine());
                isValid = true;
            }catch (NumberFormatException e)
            {
                System.out.printf("%s must be an Double, Try again%n", fieldName);
            }
        }

        return toReturn;
    }

    private static String ensureInputFormat(String regex, String fieldName)
    {
        String toReturn;
        System.out.printf("Enter %s: ", fieldName);
        while(!(toReturn = input.nextLine()).matches(regex))
        {
            System.out.printf("%s does not match desired format, try again%n", fieldName);
            System.out.printf("Enter %s: ", fieldName);
        }
        return toReturn;
    }

}
