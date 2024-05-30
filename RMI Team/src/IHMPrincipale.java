import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IHMPrincipale extends JFrame {
    public IHMPrincipale() {
        setTitle("Login Choice");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnLoginUser = new JButton("Login as User");
        btnLoginUser.setBackground(new Color(30, 144, 255));
        btnLoginUser.setForeground(Color.WHITE);
        btnLoginUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open sign-in form for user
                SignInUser signInUser = new SignInUser();
                signInUser.setVisible(true);
                dispose();
            }
        });

        JButton btnLoginAdmin = new JButton("Login as Admin");
        btnLoginAdmin.setBackground(new Color(255, 165, 0));
        btnLoginAdmin.setForeground(Color.BLACK);
        btnLoginAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignInAdmin signInAdmin = new SignInAdmin();
                signInAdmin.setVisible(true);
                dispose();
            }
        });

        panel.add(btnLoginUser);
        panel.add(btnLoginAdmin);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IHMPrincipale loginChoice = new IHMPrincipale();
            loginChoice.setVisible(true);
        });
    }
}
