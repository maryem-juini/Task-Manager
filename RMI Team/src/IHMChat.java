import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

public class IHMChat extends JFrame {
    JTextArea screen;
    JTextField input;
    JButton submit;
    ChatREMOTE r;
    String pseudo;

    public IHMChat(ChatREMOTE r, String pseudo, Date d) {
        this.r = r;
        this.pseudo = pseudo;
        this.setTitle("Chat With My Team");
        this.setLayout(new BorderLayout());

        screen = new JTextArea();
        screen.setEditable(false);
        JScrollPane scroll = new JScrollPane(screen);
        scroll.setPreferredSize(new Dimension(400, 400));
        this.add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        input = new JTextField();
        input.setPreferredSize(new Dimension(300, 30));

        submit = new JButton("Send");
        submit.setBackground(new Color(30, 144, 255)); // Set background color to blue
        submit.setForeground(Color.WHITE); // Set text color to white
        submit.setBorderPainted(false); // Remove button border
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputPanel.add(input, BorderLayout.CENTER);
        inputPanel.add(submit, BorderLayout.EAST);
        this.add(inputPanel, BorderLayout.SOUTH);

        this.setSize(new Dimension(500, 600));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        Thread threadAff = new Thread() {
            public void run() {
                while (true) {
                    try {
                        String aff = "";
                        ArrayList<Message> recu = r.getMessage();
                        for (Message m : recu) {
                            aff += m.getPseudo() + " :" + m.getMessage() + "\n" + "date: " + m.getTime() + "\n";
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }

                        screen.setText(aff);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        };
        threadAff.start();
    }

    private void sendMessage() {
        String message = input.getText().trim();
        if (!message.isEmpty()) {
            try {
                r.AddMsg(new Message(message, new Date(), pseudo));
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {

    }
}
