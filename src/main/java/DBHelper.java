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

    public void DeleteProductIDFromInventory() throws SQLException {
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
