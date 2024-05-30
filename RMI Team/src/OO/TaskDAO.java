package OO;

import DATABASE.MyConnection;

import java.sql.*;

public class TaskDAO implements TaskDAOCRUD{
    Connection connection;
    Statement st = null;
    ResultSet rs = null;
    public TaskDAO(String url, String userName, String password){
        connection= MyConnection.getConnection(url,userName,password);

    }
    public  ResultSet  select (String req ){
        try {
            if(connection !=null ){
                st = connection.createStatement();
                ResultSet rs = st.executeQuery(req);
                return  rs ;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null ;
    }
    @Override
    public int insertTask(String name, String description, int workload) {
        if (connection != null) {
            try {
                String insertQuery = "INSERT INTO tasks (name, description, workload) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, description);
                preparedStatement.setInt(3, workload);
                int rowsAffected = preparedStatement.executeUpdate();
                preparedStatement.close();
                return rowsAffected;
            } catch (SQLException e) {
                throw new RuntimeException("Error inserting task: " + e.getMessage());
            }
        }
        return 0; // Return 0 if the connection is null
    }

    @Override
    public int deleteTask(int taskId) {
        try {
            String deleteQuery = "DELETE FROM tasks WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, taskId);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rowsAffected;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting task: " + e.getMessage());
        }
    }

    @Override
    public int updateTask(int taskId, String name, String description, int workload) {
        try {
            String updateQuery = "UPDATE tasks SET name=?, description=?, workload=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, workload);
            preparedStatement.setInt(4, taskId);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rowsAffected;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating task: " + e.getMessage());
        }
    }


    @Override
    public int displayAllTasks() {
        try {
            String selectQuery = "SELECT * FROM tasks";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int workload = resultSet.getInt("workload");
                System.out.println("Task: " + name + ", Description: " + description + ", Workload: " + workload);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving tasks: " + e.getMessage());
        }
        return 0;
    }

    public void updateTaskStatus(String name, TaskStatus status) {
    }
}
