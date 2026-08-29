package panda.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import panda.model.Deadline;
import panda.model.Event;
import panda.model.Task;
import panda.model.Todo;

/**
 * Saves Panda's task list to a fixed file on disk.
 */
public class Storage {
    private static final Path SAVE_FILE = Path.of("data", "panda.txt");

    /**
     * Writes the current task list to disk, creating the data directory when needed.
     *
     * @param tasks tasks to save
     */
    public static void saveTasks(List<Task> tasks) {
        List<String> taskLines = new ArrayList<>();
        for (Task task : tasks) {
            taskLines.add(task.toFileString());
        }

        try {
            Files.createDirectories(SAVE_FILE.getParent());
            Files.write(SAVE_FILE, taskLines);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save tasks to " + SAVE_FILE, e);
        }
    }

    /**
     * Loads tasks saved by {@link #saveTasks(List)}. A missing save file represents
     * a new user with an empty task list.
     *
     * @return the tasks stored in the save file
     */
    public static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(SAVE_FILE)) {
            return tasks;
        }

        try {
            for (String taskLine : Files.readAllLines(SAVE_FILE)) {
                if (!taskLine.isBlank()) {
                    try {
                        tasks.add(createTask(taskLine));
                    } catch (RuntimeException ignored) {
                        // Ignore malformed records and keep loading valid tasks.
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load tasks from " + SAVE_FILE, e);
        }
        return tasks;
    }

    /**
     * Recreates one task from a line in Panda's save-file format.
     *
     * @param taskLine one saved task line
     * @return the recreated task
     */
    private static Task createTask(String taskLine) {
        String[] taskDetails = taskLine.split("\\s*\\|\\s*", -1);
        if (taskDetails.length < 3 || taskDetails[1].isBlank() || taskDetails[2].isBlank()) {
            throw new IllegalArgumentException("Malformed task record");
        }
        Task task;
        switch (taskDetails[0]) {
        case "T":
            if (taskDetails.length != 3) {
                throw new IllegalArgumentException("Malformed todo record");
            }
            task = new Todo(taskDetails[2]);
            break;
        case "D":
            if (taskDetails.length != 4 || taskDetails[3].isBlank()) {
                throw new IllegalArgumentException("Malformed deadline record");
            }
            task = new Deadline(taskDetails[2], taskDetails[3]);
            break;
        case "E":
            if (taskDetails.length != 5 || taskDetails[3].isBlank() || taskDetails[4].isBlank()) {
                throw new IllegalArgumentException("Malformed event record");
            }
            task = new Event(taskDetails[2], taskDetails[3], taskDetails[4]);
            break;
        default:
            throw new IllegalArgumentException("Unknown task type in save file: " + taskDetails[0]);
        }

        if (taskDetails[1].equals("1")) {
            task.markAsDone();
        } else if (!taskDetails[1].equals("0")) {
            throw new IllegalArgumentException("Malformed task status");
        }
        return task;
    }
}
