import java.io.*;
import java.net.*;
import java.sql.*;

public class AuthAdminSignIn {
    public static void main(String[] args) {
        int port = 12347;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("Client connected: " + socket.getInetAddress());

                    // Read username and password from client
                    String username = in.readLine();
                    String password = in.readLine();

                    // Verify user credentials
                    boolean isValidUser = verifyUser(username, password);

                    // Send verification result to client
                    out.println(isValidUser);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean verifyUser(String username, String password) {
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

        String selectSql = "SELECT * FROM admins WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(selectSql)) {

            // Set parameters for username and password
            stmt.setString(1, username);
            stmt.setString(2, password);


            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Returns true if user exists, false otherwise

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}

