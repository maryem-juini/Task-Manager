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

public class IHMAdmin extends JFrame {
    JPanel pnorth;
    JLabel lb_name, lb_description, lb_workload;
    JTextField tf_name, tf_description, tf_workload;
    JButton btnAddTask, btnChat;
    JTable jt_tasks;
    TaskDAO dao;
    TaskTableModel model;
    Config c;

    public IHMAdmin(String username) {
        this.setTitle("Task Manager - Admin Interface");
        this.setSize(1200, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create panel for "List Tasks" section
        JPanel listTasksPanel = new JPanel(new BorderLayout());

        pnorth = new JPanel();
        pnorth.setLayout(new FlowLayout());
        pnorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add spacing

        lb_name = new JLabel("Name");
        tf_name = new JTextField(15);

        lb_description = new JLabel("Description");
        tf_description = new JTextField(15);

        lb_workload = new JLabel("Workload");
        tf_workload = new JTextField(15);

        btnAddTask = new JButton("Add Task");
        btnAddTask.setPreferredSize(new Dimension(120, 30));
        btnAddTask.setBackground(new Color(240, 165, 0));
        btnAddTask.setForeground(Color.BLACK);
        btnAddTask.setBorder(BorderFactory.createEmptyBorder());

        pnorth.add(Box.createRigidArea(new Dimension(20, 0)));
        pnorth.add(lb_name);
        pnorth.add(tf_name);
        pnorth.add(lb_description);
        pnorth.add(tf_description);
        pnorth.add(lb_workload);
        pnorth.add(tf_workload);
        pnorth.add(btnAddTask);

        listTasksPanel.add(pnorth, BorderLayout.NORTH);

        dao = new TaskDAO(c.url, c.userName, c.password);
        ResultSet rs = dao.select("Select * from Tasks");
        model = new TaskTableModel(rs, dao);
        jt_tasks = new JTable(model);
        jt_tasks.getTableHeader().setBackground(new Color(255, 215, 0));
        listTasksPanel.add(new JScrollPane(jt_tasks), BorderLayout.CENTER);

        // Create panel for "Chat" section
        JPanel chatPanel = new JPanel(new BorderLayout());
        btnChat = new JButton("Open Chat");
        btnChat.setPreferredSize(new Dimension(120, 40));
        btnChat.setBackground(new Color(255, 165, 0));
        btnChat.setForeground(Color.BLACK);
        btnChat.setBorder(BorderFactory.createEmptyBorder());
        chatPanel.add(btnChat, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listTasksPanel, chatPanel);
        splitPane.setResizeWeight(0.8);

        this.add(splitPane);

        btnAddTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tf_name.getText();
                String description = tf_description.getText();
                int workload = Integer.parseInt(tf_workload.getText());
                dao.insertTask(name, description, workload);
                model.refreshData();
            }
        });

        btnChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url="rmi://127.0.0.1:9001/chat";
                ChatREMOTE r=null;
                try {
                    r=(ChatREMOTE) Naming.lookup(url);
                } catch (NotBoundException | MalformedURLException | RemoteException x) {
                    throw new RuntimeException(x);
                }
                Date d=new Date();
                IHMChat aff=new IHMChat(r,username,d);
            }
        });

        jt_tasks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem itemDelete = new JMenuItem("Delete Task");
                    popup.add(itemDelete);
                    popup.show(jt_tasks, e.getX(), e.getY());
                    itemDelete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int selectedRow = jt_tasks.getSelectedRow();
                            if (selectedRow != -1) {
                                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this task?", "Confirmation", JOptionPane.YES_NO_OPTION);
                                if (option == JOptionPane.YES_OPTION) {
                                    model.deleteTask(selectedRow);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Please select a task to delete.");
                            }
                        }
                    });
                }
            }
        });
    }
}
