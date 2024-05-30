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

public class AuthUserSignUp {
    public static void main(String[] args) {
        int port = 12346;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("Client connected: " + socket.getInetAddress());

                    String username = in.readLine();
                    String password = in.readLine();

                    boolean isSignUpSuccessful = signUpUser(username, password);

                    out.println(isSignUpSuccessful);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean signUpUser(String username, String password) throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:9888/db_tasks";
        String dbUsername = "root";
        String dbPassword = "";

        String monDriver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(monDriver);
            System.out.println("Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver: " + e.getMessage());
        }

        // Establish connection to the database
        try (Connection con = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            System.out.println("Connected to the database");

            String insertQuery = "INSERT INTO users (username, password) VALUES (?,?)";
            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            return false;
        }
    }
}
