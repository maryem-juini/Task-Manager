package OO;

public class Task {
    private String name;
    private String description;
    private int workload;
    private TaskStatus status;

    public Task(String name, String description, int workload) {
        this.name = name;
        this.description = description;
        this.workload = workload;
        this.status = TaskStatus.PENDING;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getWorkload() {
        return workload;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task: " + name + "\nDescription: " + description +
                "\nWorkload: " + workload + " minutes\nStatus: " + status;
    }
}

enum TaskStatus {
    PENDING, IN_PROGRESS, COMPLETED
}
