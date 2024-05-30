package OO;

public interface TaskDAOCRUD {
    int insertTask(String name, String description, int workload);
    int deleteTask(int taskId);
    int updateTask(int taskId, String name, String description, int workload);
    int displayAllTasks();
}
