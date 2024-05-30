import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SignInAdmin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignInAdmin() {
        setTitle("Login Admin");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblUsername = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(255, 165, 0));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                authenticate(username, password);
            }
        });

        JButton btnSignUp = new JButton("Sign Up");
        btnSignUp.setBackground(new Color(255, 165, 0));
        btnSignUp.setForeground(Color.BLACK);
        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignUpAdmin signUpAdmin = new SignUpAdmin();
                signUpAdmin.setVisible(true);
                dispose();
            }
        });

        panel.add(lblUsername);
        panel.add(usernameField);
        panel.add(lblPassword);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(btnLogin);
        panel.add(new JLabel());
        panel.add(btnSignUp);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private void authenticate(String username, String password) {
        String serverAddress = "127.0.0.1";
        int port = 12347;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(username);
            out.println(password);

            String response = in.readLine();
            System.out.println("Authentication result: " + response);

            if ("true".equals(response)) {
                IHMAdmin adminInterface = new IHMAdmin(username + "( Admin )"  );
                adminInterface.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignInAdmin loginInterface = new SignInAdmin();
            loginInterface.setVisible(true);
        });
    }
}
