import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

//class to house methods used in the main program driver class
public class DBHelper {

    Scanner scanner;
    Connection connection;

    public DBHelper(Scanner scanner, Connection connection ){
        this.scanner = scanner;
        this.connection = connection;
    }
    public void ModifyInventory() throws SQLException {

        System.out.println("Enter product ID:");
        int productNum = scanner.nextInt();

        System.out.println("Enter new quantity:");
        int newQuantity = scanner.nextInt();

        String quantityUpdate = "UPDATE inventory SET quantity = ? " + "WHERE productID = ?";
        PreparedStatement quantityPS = connection.prepareStatement(quantityUpdate);
        quantityPS.setInt(1, newQuantity);
        quantityPS.setInt(2, productNum);

        quantityPS.executeUpdate();
        quantityPS.close();
    }
}
