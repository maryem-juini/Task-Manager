import java.io.*;
import java.net.*;
import java.sql.*;

public class AuthUserSignIn {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("Client connected: " + socket.getInetAddress());

                    String username = in.readLine();
                    String password = in.readLine();

                    boolean isValidUser = verifyUser(username, password);

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

        String selectSql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(selectSql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    }

