/**
 * Represents a task in Panda's task list.
 */
public class Task {
    protected String description;
    protected TaskType type;
    protected TaskStatus status;

    public Task(String description) {
        this(description, TaskType.TODO);
    }

    protected Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.status = TaskStatus.NOT_DONE;
    }

    public String getStatusIcon() {
        return status.getIcon();
    }

    @Override
    public String toString() {
        return "[" + type.getIcon() + "][" + getStatusIcon() + "] " + description;
    }

    public void markAsDone() {
        status = TaskStatus.DONE;
    }

    public void markAsNotDone() {
        status = TaskStatus.NOT_DONE;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns this task in the simple format used by the save file.
     *
     * @return one line of task data
     */
    public String toFileString() {
        return type.getIcon() + " | " + (status == TaskStatus.DONE ? "1" : "0")
                + " | " + description;
    }
}
