package panda.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates Panda's task collection and the basic operations performed on it.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list with the provided initial tasks.
     *
     * @param initialTasks the tasks to start with
     */
    public TaskList(List<Task> initialTasks) {
        this.tasks = new ArrayList<>(initialTasks);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at a given position.
     *
     * @param index the zero-based task index
     * @return the task at that index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at a given position.
     *
     * @param index the zero-based task index
     * @return the removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given index as done.
     *
     * @param index the zero-based task index
     * @return the updated task
     */
    public Task markTask(int index) {
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at the given index as not done.
     *
     * @param index the zero-based task index
     * @return the updated task
     */
    public Task unmarkTask(int index) {
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the tasks as a mutable list for persistence operations.
     *
     * @return the underlying task list
     */
    public ArrayList<Task> asList() {
        return tasks;
    }
}
