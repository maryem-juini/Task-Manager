import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthAdminSignUp {
    public static void main(String[] args) {
        int port = 12348;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("Client connected: " + socket.getInetAddress());

                    String username = in.readLine();
                    String password = in.readLine();

                    boolean isValidUser = (signUpUser(username, password)>0);

                    out.println(isValidUser);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static int signUpUser(String username, String password) throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:9888/db_tasks";
        String dbUsername = "root";
        String dbPassword = "";

        String monDriver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(monDriver);
            System.out.println("Drive charge . . .");
        } catch (ClassNotFoundException e) {
            System.out.println("erreur chargement drive " + e.getMessage());
        }

        //2: Connection a la base
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, dbUsername, dbPassword);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.out.println("Erreur conncetion " + e.getMessage());
        }
        System.out.println("success1");
        String insertQuery = "INSERT INTO admins (username, password) VALUES (?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        System.out.println("success");
        int rowsAffected = preparedStatement.executeUpdate();
        preparedStatement.close();
        return rowsAffected;
    }
}
