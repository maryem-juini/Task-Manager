package DATABASE;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    public static Connection getConnection(String url, String userName, String password){
        // 1: Chargement driver
        String monDriver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(monDriver);
            System.out.println ("Drive charge . . .");
        } catch (ClassNotFoundException e) {
            System.out.println("erreur chargement drive "+e.getMessage());
        }

        //2: Connection a la base
        Connection con = null;
        try {
            con = DriverManager.getConnection(url,userName,password);
            System.out.println ("Connected");
        } catch (SQLException e) {
            System.out.println ("Erreur conncetion "+e.getMessage());
        }
        return con;
    }
}
