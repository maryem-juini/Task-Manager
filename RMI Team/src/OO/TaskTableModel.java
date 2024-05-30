package OO;

import DATABASE.Config;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskTableModel extends AbstractTableModel {
    private final TaskDAO dao;
    private final ArrayList<Object[]> data = new ArrayList<>();
    private ResultSetMetaData rsmd;

    public TaskTableModel(ResultSet rs, TaskDAO dao) {
        this.dao = dao;
        if (rs == null) {
            throw new IllegalArgumentException("ResultSet cannot be null");
        }
        try {
            rsmd = rs.getMetaData();
            while (rs.next()) {
                Object[] row = new Object[rsmd.getColumnCount()];
                for (int i = 0; i < row.length; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                data.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void insertTask(String name,String description,int workload){
        int result = dao.insertTask(name, description, workload);
        if (result == 0) {
            data.add(new Object[]{name, description, workload});
            fireTableDataChanged();
            JOptionPane.showMessageDialog(null, "Task added successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Error inserting task.");
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        try {
            return rsmd.getColumnCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        try {
            return rsmd.getColumnName(column + 1);
        } catch (SQLException e) {
            return null;
        }
    }



    public void deleteTask(int rowIndex) {
        Object[] rowData = data.get(rowIndex);
        int index =(int) rowData[0];
        data.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
        int rowsAffected = dao.deleteTask(index);
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Task deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Error deleting task.");
        }
    }


    public void refreshData() {
        try {
            ResultSet updatedResultSet = dao.select("Select * from Tasks");
            data.clear();
            rsmd = updatedResultSet.getMetaData();
            while (updatedResultSet.next()) {
                Object[] row = new Object[rsmd.getColumnCount()];
                for (int i = 0; i < row.length; i++) {
                    row[i] = updatedResultSet.getObject(i + 1);
                }
                data.add(row);
            }
            fireTableDataChanged();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error refreshing data: " + e.getMessage());
        }
    }



}
