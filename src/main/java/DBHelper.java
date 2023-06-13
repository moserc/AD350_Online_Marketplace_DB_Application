import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Scanner;

//class to house methods used in the main program driver class
public class DBHelper {

    Scanner scanner;
    Connection connection;

    public DBHelper(Scanner scanner, Connection connection ){
        this.scanner = scanner;
        this.connection = connection;
    }

    public void ShowInventory() {

        System.out.println("Products in inventory: ");

        try{ //select statement to isolate products currently stocked in inventory
            Statement invStatement = connection.createStatement();
            String getInventory =  "SELECT product.productID, productName, quantity "
                    + "FROM inventory, product "
                    + "WHERE inventory.productID = product.productID "
                    + "AND quantity > 0 AND quantity IS NOT NULL "
                    + "ORDER BY quantity desc";
            ResultSet invSet = invStatement.executeQuery(getInventory);

            //display product name, ID, and quantity
            while (invSet.next()){
                System.out.println(invSet.getString("productName") +
                        ", " + invSet.getString("product.productID") +
                        ", " + invSet.getInt("quantity") + " in stock.");
            }
            invSet.close();
        }catch (SQLException e){
            System.out.println("Unable to display products in the inventory.");
        }
    }

    public void PopularItems() {

        //Capture date range. Returns no list if typed incorrectly.
        System.out.println("Enter begin date formatted as yyyy-mm-dd: ");
        String t1 = scanner.next();
        System.out.println("Enter end date formatted as yyyy-mm-dd: ");
        String t2 = scanner.next();

        //Sum units sold by product ID, order by descending and limit to three items
        try{
            System.out.println("Three top-selling items between dates " + t1 + " and " + t2 + ": ");

            String getPopular =  "SELECT productName, product.productID, SUM(quantity) "
                    + "FROM product "
                    + "INNER JOIN transactionLineItem LI ON LI.productID = product.productID "
                    + "INNER JOIN transaction T ON T.transactionID = LI.transactionID "
                    + "WHERE T.transactionDate BETWEEN DATE(?) AND DATE(?) "
                    + "GROUP BY product.productID "
                    + "ORDER BY SUM(quantity) desc "
                    + "LIMIT 3";
            PreparedStatement popularStatement = connection.prepareStatement(getPopular);
            popularStatement.setString(1,t1);
            popularStatement.setString(2,t2);

            ResultSet popularSet = popularStatement.executeQuery();

            //Display items by popularity, show item name, ID, and units sold within the given date range
            while (popularSet.next()){
                System.out.println("Product: " + popularSet.getString("productName") +
                        ", ID: " + popularSet.getString("product.productID") +
                        ", Units sold: " + popularSet.getString("SUM(quantity)"));
            }
            popularSet.close();
        }catch (SQLException e){
            System.out.println("Unable to display results. Check date format.");
        }
    }

    public void ModifyProductIDQuantityFromInventory()  {

        System.out.println("Enter product ID to update:");
        int productNum = scanner.nextInt();

        System.out.println("Enter new quantity:");
        int newQuantity = scanner.nextInt();

        try{
            //prepared statment to update quantity
            String quantityUpdate = "UPDATE inventory SET quantity = ? " + "WHERE productID = ?";
            PreparedStatement quantityPS = connection.prepareStatement(quantityUpdate);
            quantityPS.setInt(1, newQuantity);
            quantityPS.setInt(2, productNum);

            quantityPS.executeUpdate();
            quantityPS.close();

            //display result
            Statement inventoryStatement = connection.createStatement();
            String productNameQuery = "Select productName FROM product WHERE productID = " + productNum;
            ResultSet productNameSet = inventoryStatement.executeQuery(productNameQuery);
            while (productNameSet.next()) {
                System.out.println("Success! Updated: " + productNameSet.getString("productName") + " Quantity: " + newQuantity);
            }
            productNameSet.close();
        }
        catch (SQLException e){
            System.out.println("Unable to update product quantity");
        }
    }

    public void DeleteProductIDFromInventory() {
        System.out.println("Enter product ID to delete:");
        int productNum = scanner.nextInt();

        try {
            //get display results
            Statement inventoryStatement = connection.createStatement();
            String productNameQuery = "Select productName FROM product WHERE productID = " + productNum;
            ResultSet productNameSet = inventoryStatement.executeQuery(productNameQuery);

            String productName = "";
            while (productNameSet.next()) {
                productName += productNameSet.getString("productName");
            }
            productNameSet.close();

            //find and delete the product
            String productDelete = "DELETE FROM inventory " + "WHERE productID = ?";
            PreparedStatement deleteProductPS = connection.prepareStatement(productDelete);
            deleteProductPS.setInt(1, productNum);

            deleteProductPS.execute();
            deleteProductPS.close();

            //feedback
            System.out.println("Success! Deleted: " + productName);
        }
        catch(SQLException e) {
            System.out.println("Unable to delete product ");
        }
    }

    public void processCase7() throws SQLException {
        StringBuilder header = new StringBuilder();
        header.append("\n"+ getBorder());
        header.append("|  List users to send promotional emails:           |\n");
        header.append(getBorder());
        System.out.println(header);
        Statement connection = this.connection.createStatement();
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
            ResultSet results1 = connection.executeQuery(case7_3);
            results1.next();
            map.get(i)[2] = results1.getString("productName");
            results1.close();
        }
        String case7_4 = case7_4();
        ResultSet results2 = connection.executeQuery(case7_4);
        while (results2.next()) {
            map.put(Integer.valueOf(results2.getString("id")), new String[]{
                    results2.getString("name"), results2.getString("email"),
                    results2.getString("most_bought")});
        }
        results2.close();
        LocalDate monthsAgo = LocalDate.now().minusMonths(3);
        LocalDate today = LocalDate.now();
        String startDate = monthsAgo.toString();
        String endDate = today.toString();
        String case7_2 = case7_2(startDate, endDate);
        ResultSet results3 = connection.executeQuery(case7_2);
        StringBuilder temp = new StringBuilder();
        temp.append(getBorder());
        temp.append("| Users who haven't made purchases in a few months  |\n");
        temp.append(getBorder());
        temp.append(String.format("|%-4s%-15s%-17s%-12s%s|\n",
                "id", "name", "email", "most_bought", generateString(3, " ")));
        temp.append(getBorder());
        int count = 0;
        while (results3.next()) {
            count++;
            int id = Integer.parseInt(results3.getString("id"));
            String line = String.format("|%-4d%-15s%-17s%-12s", id,
                    map.get(id)[0], map.get(id)[1], map.get(id)[2]);
            temp.append(line + generateString(52 - line.length(), " ") + "|\n");
            temp.append(getBorder());
        }
        if(count == 0) {
            temp.append("|       All Users Have Made Recent Purchases        |\n");
            temp.append(getBorder());
        }
        System.out.println(temp);
        results.close();
    }

    private String generateString(int n, String s) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < n; i++) {
            temp.append(s);
        }
        return temp.toString();
    }

    private String getBorder() {
        return generateString(53, "-") + "\n";
    }

    private String case7_1() {
        StringBuilder temp = new StringBuilder();
        temp.append("SELECT transaction.userID AS id, CONCAT(firstName, \" \", LastName) AS name, email " +
                "FROM user, transaction WHERE user.userID = transaction.userID " +
                "GROUP BY transaction.userID;");
        return temp.toString();
    }

    private String case7_2(String startDate, String endDate) {
        StringBuilder temp = new StringBuilder();
        temp.append("SELECT user.userID AS id FROM transaction, user " +
                "WHERE user.userID NOT IN (SELECT transaction.userID " +
                "FROM user, transaction WHERE user.userID = transaction.userID " +
                String.format("AND transactionDate Between '%s' AND '%s') ", startDate, endDate ) +
                "GROUP BY user.userID;") ;
        return temp.toString();
    }

    private String case7_3(int userID) {
        StringBuilder temp = new StringBuilder();
        temp.append("SELECT user.userID, transactionlineitem.productID, product.productName, transactionlineitem.quantity " +
                "FROM transactionlineitem INNER JOIN transaction ON " +
                "transaction.transactionID = transactionlineitem.transactionID INNER JOIN product ON " +
                "product.productID = transactionlineitem.productID INNER JOIN user ON transaction.userID = user.userID " +
                String.format("WHERE user.userID = %d ORDER BY quantity DESC LIMIT 1;", userID));
        return temp.toString();
    }

    private String case7_4() {
        StringBuilder temp = new StringBuilder();
        temp.append("SELECT user.userID AS id, CONCAT(firstName, \" \", LastName) AS name, " +
                "email, \"No_Purchases\" AS most_bought FROM user, transaction " +
                "WHERE user.userID NOT IN (SELECT transaction.userID AS id " +
                "FROM user, transaction WHERE user.userID = transaction.userID " +
                "GROUP BY transaction.userID) Group By user.userID;");
        return temp.toString();
    }
}
