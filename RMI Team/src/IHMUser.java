import DATABASE.Config;
import OO.TaskDAO;
import OO.TaskTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.util.Date;

public class IHMUser extends JFrame {
    JPanel pnorth;
    JButton btnChat;
    JTable jt_tasks;
    TaskDAO dao;
    TaskTableModel model;
    Config c;

    public IHMUser(String username) {
        this.setTitle("Task Manager - User Interface");
        this.setSize(1200, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel listTasksPanel = new JPanel(new BorderLayout());

        JPanel chatPanel = new JPanel(new BorderLayout());
        btnChat = new JButton("Open Chat");
        btnChat.setPreferredSize(new Dimension(120, 40));
        btnChat.setBackground(new Color(30, 144, 255));
        btnChat.setBorder(BorderFactory.createEmptyBorder());
        chatPanel.add(btnChat, BorderLayout.NORTH);

        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listTasksPanel, chatPanel);
        splitPane.setResizeWeight(0.8);
        this.add(splitPane);

        btnChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "rmi://127.0.0.1:9001/chat";
                ChatREMOTE r = null;
                try {
                    r = (ChatREMOTE) Naming.lookup(url);
                } catch (NotBoundException | MalformedURLException | RemoteException x) {
                    throw new RuntimeException(x);
                }
                Date d=new Date();
                IHMChat aff = new IHMChat(r,username, d);
            }
        });

        dao = new TaskDAO(c.url, c.userName, c.password);
        ResultSet rs = dao.select("Select * from Tasks");
        model = new TaskTableModel(rs, dao);
        jt_tasks = new JTable(model);
        jt_tasks.getTableHeader().setBackground(new Color(30, 144, 255));
        listTasksPanel.add(new JScrollPane(jt_tasks), BorderLayout.CENTER);

        jt_tasks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JOptionPane.showMessageDialog(null, "You do not have permission to delete tasks.");
                }
            }
        });

        TaskUpdateThread taskUpdateThread = new TaskUpdateThread();
        taskUpdateThread.start();
    }

    private class TaskUpdateThread extends Thread {
        public void run() {
            while (true) {
                try {
                    ResultSet rs = dao.select("Select * from Tasks");
                    model.refreshData();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}