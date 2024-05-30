import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SignUpAdmin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignUpAdmin() {
        setTitle("Sign Up Admin");
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

        JButton btnSignUp = new JButton("Sign Up");
        btnSignUp.setBackground(new Color(255, 165, 0));
        btnSignUp.setForeground(Color.BLACK);
        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                signUp(username, password);
            }
        });

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(255, 165, 0));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignInAdmin adminSignIn = new SignInAdmin();
                adminSignIn.setVisible(true);
                dispose();
            }
        });

        panel.add(lblUsername);
        panel.add(usernameField);
        panel.add(lblPassword);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(btnSignUp);
        panel.add(new JLabel());
        panel.add(btnLogin);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private void signUp(String username, String password) {
        String serverAddress = "127.0.0.1";
        int port = 12348;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(username);
            out.println(password);

            String response = in.readLine();
            System.out.println("Admin Sign-up result: " + response);

            if ("true".equals(response)) {
                JOptionPane.showMessageDialog(this, "Admin sign-up successful! You can now log in.", "Sign-up Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();

                SwingUtilities.invokeLater(() -> {
                    SignInAdmin adminSignIn = new SignInAdmin();
                    adminSignIn.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(this, "Failed to sign up. Please try again with a different username.", "Sign-up Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignUpAdmin signUpAdmin = new SignUpAdmin();
            signUpAdmin.setVisible(true);
        });
    }
}
