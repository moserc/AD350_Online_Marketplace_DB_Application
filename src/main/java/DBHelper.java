import java.sql.*;
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
}
