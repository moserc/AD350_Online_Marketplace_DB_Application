package AcmeApplication;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class acmeAppDriver {
    public static void main(String[] args) {
        displayIntro();
        loadConnectionDriver();
        Scanner console = new Scanner(System.in); // close console
        displaySetupMenu();
        String ip = "127.0.0.1";
        String dbName = "ACME";
        String url = "jdbc:mysql://" + ip + "/" + dbName;
        if(!processSetupMenu(console)) {
            System.out.print("\n>> Enter new IP: ");
            ip = getValidString(console);
            System.out.print("\n>> Enter new DB name: ");
            dbName = getValidString(console);
        }
        String name = "root";
        displayLoginMenu();
        if(!processSetupMenu(console)) {
            System.out.print("\n>> Enter new UserName: ");
            name = getValidString(console);
        }
        String password = "";
        displayPasswordMenu();
        password = getValidString(console);
        Connection reservationConn;
        try {
            reservationConn = DriverManager.getConnection(url, name, password);
            displaySuccess();
            displayMainMenu();
            processMainMenu(console, reservationConn);
            reservationConn.close();
        }
        catch (SQLException e) {
            StringBuilder temp = new StringBuilder();
            temp.append("\n" + getTopBorder());
            temp.append("|               An Error Has Occurred               |\n");
            temp.append(getTopBorder());
            temp.append("|             Please Reload Application             |\n");
            temp.append(getTopBorder());
            System.out.println(temp);
            System.out.println("\nCannot connect to database:" + e.getMessage());
        }
    }

    public static void loadConnectionDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Cannot connect to database:" + e.getMessage());
        }
    }

    public static void displayIntro() {
        StringBuilder temp = new StringBuilder();
        temp.append("\n" + getTopBorder());
        temp.append("|        _        ______  ____    ____  ________    |\n");
        temp.append("|       / \\     .' ___  ||_   \\  /   _||_   __  |   |\n");
        temp.append("|      / _ \\   / .'   \\_|  |   \\/   |    | |_ \\_|   |\n");
        temp.append("|     / ___ \\  | |         | |\\  /| |    |  _| _    |\n");
        temp.append("|   _/ /   \\ \\_\\ `.___.'\\ _| |_\\/_| |_  _| |__/ |   |\n");
        temp.append("|  |____| |____|`.____ .'|_____||_____||________|   |\n");
        temp.append(getBottomBorder());
        temp.append(getMiddle());
        temp.append("|   ACME Online Marketplace Database Admin Portal   |\n");
        temp.append(getBottomBorder() + "\n");
        System.out.println(temp);
    }

    public static void displaySetupMenu() {
        StringBuilder temp = new StringBuilder();
        temp.append(getTopBorder());
        temp.append("| Setup Menu:                                       |\n");
        temp.append(getTopBorder());
        temp.append("|  Default IP = 127.0.0.1   |   Default DB = ACME   |\n");
        temp.append(getTopBorder());
        temp.append("|   1.  Use Default Settings                        |\n");
        temp.append("|   2.  Custom Settings                             |\n");
        temp.append(getTopBorder() + "\n");
        System.out.print(temp);
        System.out.print(getPrompt());
    }

    public static void displayLoginMenu() {
        StringBuilder temp = new StringBuilder();
        temp.append("\n" + getTopBorder());
        temp.append("| Admin Login:                                      |\n");
        temp.append(getTopBorder());
        temp.append("|  Default UserName = root                          |\n");
        temp.append(getTopBorder());
        temp.append("|   1.  Use Default UserName                        |\n");
        temp.append("|   2.  Custom UserName                             |\n");
        temp.append(getTopBorder() + "\n");
        System.out.print(temp);
        System.out.print(getPrompt());
    }

    public static void displayMainMenu() {
        StringBuilder temp = new StringBuilder();
        temp.append(getTopBorder());
        temp.append("| Main Menu:                                        |\n");
        temp.append(getTopBorder());
        temp.append(getMiddle());
        temp.append("|   1.  List products currently in inventory        |\n");
        temp.append(getMiddle());
        temp.append("|   2.  Create new products                         |\n");
        temp.append(getMiddle());
        temp.append("|   3.  Modify amount of product in inventory       |\n");
        temp.append(getMiddle());
        temp.append("|   4.  Delete a product in inventory               |\n");
        temp.append(getMiddle());
        temp.append("|   5.  List most popular products for time range   |\n");
        temp.append(getMiddle());
        temp.append("|   6.  List least popular products for time range  |\n");
        temp.append(getMiddle());
        temp.append("|   7.  List users to send promotional emails       |\n");
        temp.append(getMiddle());
        temp.append("|   8.  End session                                 |\n");
        temp.append(getMiddle());
        temp.append(getTopBorder() + "\n");
        System.out.print(temp);
        System.out.print(getPrompt());
    }

    public static void displayPasswordMenu() {
        StringBuilder temp = new StringBuilder();
        temp.append("\n" + getTopBorder());
        temp.append("| Admin Login:                                      |\n");
        temp.append(getTopBorder());
        temp.append("|  Please enter your password below                 |\n");
        temp.append(getTopBorder() + "\n");
        System.out.print(temp);
        System.out.print(">> Enter password: ");
    }

    public static void displayExitMessage() {
        StringBuilder temp = new StringBuilder();
        temp.append("\n" + getTopBorder());
        temp.append("| Session Closed:                                   |\n");
        temp.append(getTopBorder());
        temp.append("|           Thank you for choosing ACME!            |\n");
        temp.append(getTopBorder());
        System.out.println(temp);
    }

    public static void displaySuccess() {
        StringBuilder temp = new StringBuilder();
        temp.append("\n" + getTopBorder());
        temp.append("|                    Successful                     |\n");
        temp.append(getTopBorder());
        System.out.println(temp);
    }

    public static void displayEnter() {
        StringBuilder temp = new StringBuilder();
        temp.append(getTopBorder());
        temp.append("|        Press ENTER to return to Main Menu         |\n");
        temp.append(getTopBorder());
        System.out.print(temp);
    }

    public static void displayNotAProduct() {
        StringBuilder temp = new StringBuilder();
        temp.append(getTopBorder());
        temp.append("|               Product ID not in DB                |\n");
        temp.append(getTopBorder());
        System.out.print(temp);
    }

    public static String getPrompt() {
        return ">> Enter your selection: ";
    }

    private static String generateString(int n, String s) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < n; i++) {
            temp.append(s);
        }
        return temp.toString();
    }

    private static String getTopBorder() {
        return generateString(53, "-") + "\n";
    }

    private static String getBottomBorder() {
        return "|" + generateString(51, "_") + "|" + "\n";
    }

    private static String getMiddle() {
        return "|" + generateString(51, " ") + "|" + "\n";
    }

    public static int getValidSelection(Scanner console, int lowLimit, int upLimit) {
        int selection = getValidInt(console, lowLimit, upLimit);
        while(selection < lowLimit || selection > upLimit) {
            System.out.println(errorMessage(
                    "Selection must be a number in the range", lowLimit, upLimit));
            System.out.print(getPrompt());
            selection = getValidInt(console, lowLimit, upLimit);
        }
        return selection;
    }

    public static int getValidInt(Scanner console, int lowLimit, int upLimit) {
        while (!console.hasNextInt()) {
            System.out.println(errorMessage(
                    "Selection must be a number in the range", lowLimit, upLimit));
            System.out.print(getPrompt());
            console.nextLine();
        }
        return console.nextInt();
    }

    public static double getValidDouble(Scanner console) {
        while (!console.hasNextDouble()) {
            System.out.println(errorMessage(
                    "Selection must be a number"));
            System.out.print(getPrompt());
            console.nextLine();
        }
        return console.nextDouble();
    }

    public static String getValidDate(Scanner console) {
        String date = getValidString(console);
        boolean valid = isValidDate(date);
        while(!valid) {
            System.out.print("\n>> Enter date (in format year-month-day i.e. 2000-01-01): ");
            date = getValidString(console);
            valid = isValidDate(date);
        }
        return date;
    }

    private static boolean isValidDate(String date) {
        boolean valid;
        String[] temp = date.split("-");
        if(temp.length != 3) {
            valid = false;
        } else {
            try {
                double a = Double.parseDouble(temp[0]);
                double b = Double.parseDouble(temp[1]);
                double c = Double.parseDouble(temp[2]);
                if(temp[0].length() != 4 && temp[1].length() != 2 && temp[2].length() != 2) {
                    valid = false;
                } else {
                    valid = true;
                }
            }
            catch (NumberFormatException e) {
                valid = false;
            }
        }
        return valid;
    }

    private static String errorMessage(String message, int lowLimit, int upLimit) {
        String error = "| " + message + " ";
        String limit = lowLimit + " - " + upLimit;
        StringBuilder temp = new StringBuilder();
        temp.append("\n" + getTopBorder());
        temp.append(getInvalidMenu());
        temp.append(getTopBorder());
        temp.append( error + limit + addSpaces(getTopBorder().length(),
                error.length() + limit.length() + 1) + "\n");
        temp.append(getTopBorder());
        return temp.toString();
    }

    private static String errorMessage(String message) {
        String error = "| " + message + " ";
        StringBuilder temp = new StringBuilder();
        temp.append("\n" + getTopBorder());
        temp.append(getInvalidMenu());
        temp.append(getTopBorder());
        temp.append( error + addSpaces(getTopBorder().length(),
                error.length() + 1) + "\n");
        temp.append(getTopBorder());
        return temp.toString();
    }

    public static String getValidString(Scanner console) {
        return console.nextLine().split(" ")[0];
    }

    public static String getInvalidMenu() {
        return "| Invalid Menu Selection:                           |\n";
    }

    private static String addSpaces(int borderLength, int lineLength) {
        String spaces = "";
        int numSpaces = borderLength - lineLength - 1;
        for(int i = 0; i < numSpaces; i++) {
            spaces += " ";
        }
        return spaces + "|";
    }

    public static boolean processSetupMenu(Scanner console) {
        int lowLimit = 1;
        int upLimit = 2;
        int selection = getValidSelection(console, lowLimit, upLimit);
        console.nextLine();
        if(selection == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void processMainMenu(Scanner console, Connection conn) throws SQLException {
        int lowLimit = 1;
        int upLimit = 8;
        int selection = getValidSelection(console, lowLimit, upLimit);
        console.nextLine();
        boolean more = true;
        while(more) {
            switch (selection) {
                case 1:
                    processCase1(conn);
                    break;
                case 2:
                    processCase2(console, conn);
                    break;
                case 3:
                    processCase3(console, conn);
                    break;
                case 4:
                    processCase4(console, conn);
                    break;
                case 5:
                    processCase5(console, conn);
                    break;
                case 6:
                    processCase6(console, conn);
                    break;
                case 7:
                    processCase7(console, conn);
                    break;
                case 8:
                    conn.close();
                    displayExitMessage();
                    more = false;
                    break;
                default:
                    System.out.println(errorMessage(
                            "Selection must be a number in the range", lowLimit, upLimit));
                    selection = getValidSelection(console, lowLimit, upLimit);
                    break;
            }
            if (selection != 8) {
                displayMainMenu();
                selection = getValidSelection(console, lowLimit, upLimit);
                console.nextLine();
            }
        }
    }

    private static void returnToMenu(Scanner console) {
        displayEnter();
        console.nextLine();
        displayMainMenu();
    }

    public static String case1() {
        StringBuilder temp = new StringBuilder();
        temp.append("SELECT product.productID AS id, productName AS name, quantity ");
        temp.append("FROM product, inventory ");
        temp.append("WHERE inventory.productID = product.productID ");
        temp.append("AND inventory.quantity > 0;");
        return temp.toString();
    }

    public static String case2(String productName, double currentMSRP, double costToProduce,
                               String date, String description) {
        StringBuilder temp = new StringBuilder();
        temp.append("INSERT INTO product (productName, currentMSRP, costToProduce, " +
                "releaseDate, description) ");
        temp.append(String.format("VALUES('%s', %.2f, %.2f, '%s', '%s');",
                productName, currentMSRP, costToProduce, date, description));
        return temp.toString();
    }

    public static String case3(int quantity, int id) {
        StringBuilder temp = new StringBuilder();
        temp.append("UPDATE inventory ");
        temp.append("SET quantity = " + quantity + " ");
        temp.append("WHERE productID = " + id + ";");
        return temp.toString();
    }

    public static String case4(int id) {
        StringBuilder temp = new StringBuilder();
        temp.append("DELETE FROM inventory ");
        temp.append("WHERE productID = " + id + ";");
        return temp.toString();
    }

    public static String case5(String startDate, String endDate) {
        StringBuilder temp = new StringBuilder();
        temp.append("SELECT transactionlineitem.productID AS id, product.productName AS name, ");
        temp.append("SUM(transactionlineitem.quantity) AS quantity ");
        temp.append("FROM transactionlineitem INNER JOIN transaction ON ");
        temp.append("transaction.transactionID = transactionlineitem.transactionID ");
        temp.append("INNER JOIN product ON product.productID = transactionlineitem.productID ");
        temp.append(String.format("WHERE transaction.transactionDate BETWEEN '%s' AND '%s' "
            ,startDate, endDate));
        temp.append("GROUP BY transactionlineitem.productID ORDER BY quantity DESC LIMIT 3;");
        return temp.toString();
    }

    public static String case6(String startDate, String endDate) {
        StringBuilder temp = new StringBuilder();
        temp.append("SELECT transactionlineitem.productID AS id, product.productName AS name, ");
        temp.append("SUM(transactionlineitem.quantity) AS quantity ");
        temp.append("FROM transactionlineitem INNER JOIN transaction ON ");
        temp.append("transaction.transactionID = transactionlineitem.transactionID ");
        temp.append("INNER JOIN product ON product.productID = transactionlineitem.productID ");
        temp.append(String.format("WHERE transaction.transactionDate BETWEEN '%s' AND '%s' "
                ,startDate, endDate));
        temp.append("GROUP BY transactionlineitem.productID ORDER BY quantity LIMIT 3;");
        return temp.toString();
    }

    public static String case7_1() {
        StringBuilder temp = new StringBuilder();
        temp.append("SELECT transaction.userID AS id, CONCAT(firstName, \" \", LastName) AS name, email ");
        temp.append("FROM user, transaction ");
        temp.append("WHERE user.userID = transaction.userID ");
        temp.append("GROUP BY transaction.userID;");
        return temp.toString();
    }

    public static String case7_2(String startDate, String endDate) {
        StringBuilder temp = new StringBuilder();
        temp.append("SELECT transaction.userID AS id ");
        temp.append("FROM transaction, user ");
        temp.append("WHERE transaction.userID NOT IN (SELECT transaction.userID ");
        temp.append("FROM user, transaction ");
        temp.append("WHERE user.userID = transaction.userID ");
        temp.append(String.format("AND transactionDate Between '%s' AND '%s') ", startDate, endDate ));
        temp.append("GROUP BY transaction.userID;");
        return temp.toString();
    }

    public static String case7_3(int userID) {
        StringBuilder temp = new StringBuilder();
        temp.append("SELECT user.userID, transactionlineitem.productID, product.productName, transactionlineitem.quantity ");
        temp.append("FROM transactionlineitem INNER JOIN transaction ON ");
        temp.append("transaction.transactionID = transactionlineitem.transactionID INNER JOIN product ON ");
        temp.append("product.productID = transactionlineitem.productID INNER JOIN user ON transaction.userID = user.userID ");
        temp.append(String.format("WHERE user.userID = %d ORDER BY quantity DESC LIMIT 1;", userID));
        return temp.toString();
    }

    public static void processCase1(Connection conn) throws SQLException {
        Statement connection = conn.createStatement();
        String case1 = case1();
        ResultSet results = connection.executeQuery(case1);
        StringBuilder temp = new StringBuilder();
        temp.append("\n"+ getTopBorder());
        temp.append("|  List products currently in inventory:            |\n");
        temp.append(getTopBorder());
        temp.append(String.format("|  %-8s%-20s%-3s%s|\n",
                "id", "name", "quantity", generateString(13, " ")));
        temp.append(getTopBorder());
        while (results.next()) {
            String line = String.format("|  %-8s%-20s%-3s",
                    results.getString("id"),
                    results.getString("name"),
                    results.getString("quantity"));
            temp.append(line + generateString(52 - line.length(), " ") + "|\n");
            temp.append(getTopBorder());
        }
        System.out.println(temp);
        results.close();
    }

    public static void processCase2(Scanner console, Connection conn) throws SQLException {
        StringBuilder header = new StringBuilder();
        header.append("\n"+ getTopBorder());
        header.append("|  Create New Product:                              |\n");
        header.append(getTopBorder());
        System.out.println(header);
        Statement connection = conn.createStatement();
        System.out.print(">> Enter products name (no spaces): ");
        String productName = getValidString(console);
        System.out.print("\n>> Enter products MSRP (a number): ");
        double currentMSRP = getValidDouble(console);
        System.out.print("\n>> Enter products cost to produce (a number): ");
        double costToProduce = getValidDouble(console);
        String date = getValidDate(console);
        System.out.print("\n>> Enter product description (short): ");
        String description = console.nextLine();
        String case2 = case2(productName,currentMSRP,costToProduce,date,description);
        connection.execute(case2);
        displaySuccess();
        System.out.print(">> Enter how many" + productName + "to add to inventory (1 - 1000): ");
        int quantity = getValidSelection(console, 1, 1000);
        int idToAdd = addToInventory(console, conn, productName);
        String insertInventory = String.format("INSERT INTO inventory VALUES(%d, %d, '%s');",
                idToAdd, quantity, date);
        connection.execute(insertInventory);
        displaySuccess();
    }

    public static int addToInventory(Scanner console, Connection conn, String productName) throws SQLException {
        Statement connection = conn.createStatement();
        String getId = "SELECT productID FROM product WHERE productName = '" + productName + "';";
        ResultSet results = connection.executeQuery(getId);
        results.next();
        int temp = Integer.parseInt(results.getString("productID"));
        results.close();
        return temp;
    }

    public static void processCase3(Scanner console, Connection conn) throws SQLException {
        StringBuilder header = new StringBuilder();
        header.append("\n" + getTopBorder());
        header.append("|  Modify Inventory Quantity:                       |\n");
        header.append(getTopBorder());
        System.out.println(header);
        Statement connection = conn.createStatement();
        String case1 = case1();
        ResultSet results = connection.executeQuery(case1);
        ArrayList<Integer> list = new ArrayList<>();
        while (results.next()) {
            list.add(Integer.valueOf(results.getString("id")));
        }
        results.close();
        System.out.print(">> Enter products id (a number): ");
        int id = getValidInt(console, 0, 1000);
        while(!list.contains(id)) {
            displayNotAProduct();
            System.out.print("\n>> Enter products id (a number): ");
            id = getValidInt(console, 0, 1000);
        }
        System.out.print("\n>> Enter new quantity (a number): ");
        int quantity = getValidSelection(console, 1, 1000);
        String case3 = case3(quantity, id);
        connection.execute(case3);
        displaySuccess();
    }

    public static void processCase4(Scanner console, Connection conn) throws SQLException {
        StringBuilder header = new StringBuilder();
        header.append("\n"+ getTopBorder());
        header.append("|  Delete a Product from Inventory:                 |\n");
        header.append(getTopBorder());
        System.out.println(header);
        Statement connection = conn.createStatement();
        String case1 = case1();
        ResultSet results = connection.executeQuery(case1);
        ArrayList<Integer> list = new ArrayList<>();
        while (results.next()) {
            list.add(Integer.valueOf(results.getString("id")));
        }
        results.close();
        System.out.print(">> Enter products id (a number): ");
        int id = getValidInt(console, 0, 1000);
        while(!list.contains(id)) {
            displayNotAProduct();
            System.out.print("\n>> Enter products id (a number): ");
            id = getValidInt(console, 0, 1000);
        }
        String case4 = case4(id);
        connection.execute(case4);
        displaySuccess();
    }

    public static void processCase5(Scanner console, Connection conn) throws SQLException {
        StringBuilder header = new StringBuilder();
        header.append("\n"+ getTopBorder());
        header.append("|  List most popular products for time range:       |\n");
        header.append(getTopBorder());
        System.out.println(header);
        Statement connection = conn.createStatement();
        System.out.print(">> Enter first date (in format year-month-day i.e. 2000-01-01): ");
        String startDate = getValidDate(console);
        System.out.print("\n>> Enter second date (in format year-month-day i.e. 2000-01-01): ");
        String endDate = getValidDate(console);
        boolean switchDate = false;
        if(Integer.parseInt(startDate.split("-")[0]) >
                Integer.parseInt(endDate.split("-")[0])) {
            switchDate = true;
        } else if(Integer.parseInt(startDate.split("-")[1]) >
                Integer.parseInt(endDate.split("-")[1])) {
            switchDate = true;
        } else if(Integer.parseInt(startDate.split("-")[2]) >
                Integer.parseInt(endDate.split("-")[2])) {
            switchDate = true;
        }
        if(switchDate) {
            String temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        String case5 = case5(startDate, endDate);
        ResultSet results = connection.executeQuery(case5);
        StringBuilder temp = new StringBuilder();
        temp.append("\n"+ getTopBorder());
        temp.append("|  Top selling products (3 max) from time range     |\n");
        temp.append(getTopBorder());
        temp.append(String.format("|  %-8s%-20s%-3s%s|\n",
                "id", "name", "quantity", generateString(13, " ")));
        temp.append(getTopBorder());
        int count = 0;
        while (results.next()) {
            String line = String.format("|  %-8s%-20s%-3s",
                    results.getString("id"),
                    results.getString("name"),
                    results.getString("quantity"));
            temp.append(line + generateString(52 - line.length(), " ") + "|\n");
            temp.append(getTopBorder());
            count++;
        }
        if(count == 0) {
            temp.append("|            No Products in given range             |\n");
            temp.append(getTopBorder());
        }
        System.out.println(temp);
        results.close();
    }

    public static void processCase6(Scanner console, Connection conn) throws SQLException {
        StringBuilder header = new StringBuilder();
        header.append("\n"+ getTopBorder());
        header.append("|  List Least popular products for time range:      |\n");
        header.append(getTopBorder());
        System.out.println(header);
        Statement connection = conn.createStatement();
        System.out.print(">> Enter first date (in format year-month-day i.e. 2000-01-01): ");
        String startDate = getValidDate(console);
        System.out.print("\n>> Enter second date (in format year-month-day i.e. 2000-01-01): ");
        String endDate = getValidDate(console);
        boolean switchDate = false;
        if(Integer.parseInt(startDate.split("-")[0]) >
                Integer.parseInt(endDate.split("-")[0])) {
            switchDate = true;
        } else if(Integer.parseInt(startDate.split("-")[1]) >
                Integer.parseInt(endDate.split("-")[1])) {
            switchDate = true;
        } else if(Integer.parseInt(startDate.split("-")[2]) >
                Integer.parseInt(endDate.split("-")[2])) {
            switchDate = true;
        }
        if(switchDate) {
            String temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        String case6 = case6(startDate, endDate);
        ResultSet results = connection.executeQuery(case6);
        StringBuilder temp = new StringBuilder();
        temp.append("\n"+ getTopBorder());
        temp.append("|  Worst selling products (3 max) from time range   |\n");
        temp.append(getTopBorder());
        temp.append(String.format("|  %-8s%-20s%-3s%s|\n",
                "id", "name", "quantity", generateString(13, " ")));
        temp.append(getTopBorder());
        int count = 0;
        while (results.next()) {
            String line = String.format("|  %-8s%-20s%-3s",
                    results.getString("id"),
                    results.getString("name"),
                    results.getString("quantity"));
            temp.append(line + generateString(52 - line.length(), " ") + "|\n");
            temp.append(getTopBorder());
            count++;
        }
        if(count == 0) {
            temp.append("|            No Products in given range             |\n");
            temp.append(getTopBorder());
        }
        System.out.println(temp);
        results.close();
    }

    public static void processCase7(Scanner console, Connection conn) throws SQLException {
        StringBuilder header = new StringBuilder();
        header.append("\n"+ getTopBorder());
        header.append("|  List users to send promotional emails:           |\n");
        header.append(getTopBorder());
        System.out.println(header);
        Statement connection = conn.createStatement();
        LinkedHashMap<Integer, String[]> map = new LinkedHashMap<>();
        String case7_1 = case7_1();
        ResultSet results = connection.executeQuery(case7_1);
        while (results.next()) {
            map.put(Integer.valueOf(results.getString("id")), new String[]{
                    results.getString("name"), results.getString("email"), ""});
        }
        results.close();
        for (int i : map.keySet()) {
            String case7_3 = case7_3(i);
            ResultSet results2 = connection.executeQuery(case7_3);
            results2.next();
            map.get(i)[2] = results2.getString("productName");
            results2.close();
        }
        LocalDate monthsAgo = LocalDate.now().minusMonths(3);
        LocalDate today = LocalDate.now();
        String startDate = monthsAgo.toString();
        String endDate = today.toString();
        String case7_2 = case7_2(startDate, endDate);
        ResultSet results3 = connection.executeQuery(case7_2);
        StringBuilder temp = new StringBuilder();
        temp.append(getTopBorder());
        temp.append("| Users who haven't made purchases in a few months  |\n");
        temp.append(getTopBorder());
        temp.append(String.format("|%-4s%-15s%-17s%-12s%s|\n",
                "id", "name", "email", "most_bought", generateString(3, " ")));
        temp.append(getTopBorder());
        int count = 0;
        while (results3.next()) {
            count++;
            int id = Integer.parseInt(results3.getString("id"));
            String line = String.format("|%-4d%-15s%-17s%-12s", id,
                    map.get(id)[0], map.get(id)[1], map.get(id)[2]);
            temp.append(line + generateString(52 - line.length(), " ") + "|\n");
            temp.append(getTopBorder());
        }
        if(count == 0) {
            temp.append("|       All Users Have Made Recent Purchases        |\n");
            temp.append(getTopBorder());
        }
        System.out.println(temp);
        results.close();
    }
}