import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * The cool entry point for the Panda chatbot.
 */
public class Panda {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        ArrayList<Task> tasks = Storage.loadTasks();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            ui.showDivider();

            CommandType commandType = getCommandType(command);
            if (commandType == CommandType.BYE) {
                ui.showBye();
                break;
            }

            try {
                switch (commandType) {
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case MARK:
                    int markIndex = getTaskIndex(command, "mark", tasks.size());
                    tasks.get(markIndex).markAsDone();
                    Storage.saveTasks(tasks);
                    ui.showTaskMarked(tasks.get(markIndex));
                    break;
                case UNMARK:
                    int unmarkIndex = getTaskIndex(command, "unmark", tasks.size());
                    tasks.get(unmarkIndex).markAsNotDone();
                    Storage.saveTasks(tasks);
                    ui.showTaskUnmarked(tasks.get(unmarkIndex));
                    break;
                case DELETE:
                    int deleteIndex = getTaskIndex(command, "delete", tasks.size());
                    Task deletedTask = tasks.remove(deleteIndex);
                    Storage.saveTasks(tasks);
                    ui.showTaskDeleted(deletedTask, tasks.size());
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = createTask(command);
                    tasks.add(task);
                    Storage.saveTasks(tasks);
                    ui.showTaskAdded(task, tasks.size());
                    break;
                case UNKNOWN:
                default:
                    throw new PandaException(ErrorType.UNKNOWN_COMMAND);
                }
            } catch (PandaException e) {
                ui.showError(e);
            }

            ui.showDivider();
        }
    }

    private static Task createTask(String command) throws PandaException {
        switch (getCommandType(command)) {
        case TODO:
            String description = getArguments(command, "todo");
            if (description.isEmpty()) {
                throw new PandaException(ErrorType.EMPTY_TODO_DESCRIPTION);
            }
            return new Todo(description);
        case DEADLINE:
            String details = getArguments(command, "deadline");
            if (details.isEmpty()) {
                throw new PandaException(ErrorType.EMPTY_DEADLINE_DESCRIPTION);
            }

            int byIndex = findMarker(details, "/by");
            if (byIndex < 0) {
                throw new PandaException(ErrorType.MISSING_DEADLINE_BY);
            }

            String deadlineDescription = details.substring(0, byIndex).trim();
            String by = details.substring(byIndex + 3).trim();
            if (deadlineDescription.isEmpty()) {
                throw new PandaException(ErrorType.EMPTY_DEADLINE_DESCRIPTION);
            }
            if (by.isEmpty()) {
                throw new PandaException(ErrorType.EMPTY_DEADLINE_BY);
            }
            return new Deadline(deadlineDescription, parseDateTime(by));
        case EVENT:
            String eventDetails = getArguments(command, "event");
            if (eventDetails.isEmpty()) {
                throw new PandaException(ErrorType.EMPTY_EVENT_DESCRIPTION);
            }

            int fromIndex = findMarker(eventDetails, "/from");
            int toIndex = findMarker(eventDetails, "/to", Math.max(0, fromIndex + 5));
            if (fromIndex < 0 || toIndex < 0) {
                throw new PandaException(ErrorType.MISSING_EVENT_TIMES);
            }

            String eventDescription = eventDetails.substring(0, fromIndex).trim();
            String from = eventDetails.substring(fromIndex + 5, toIndex).trim();
            String to = eventDetails.substring(toIndex + 3).trim();
            if (eventDescription.isEmpty()) {
                throw new PandaException(ErrorType.EMPTY_EVENT_DESCRIPTION);
            }
            if (from.isEmpty() || to.isEmpty()) {
                throw new PandaException(ErrorType.EMPTY_EVENT_TIME);
            }
            LocalDateTime eventStart = parseDateTime(from);
            LocalDateTime eventEnd = parseDateTime(to);
            if (eventEnd.isBefore(eventStart)) {
                throw new PandaException(ErrorType.EVENT_END_BEFORE_START);
            }
            return new Event(eventDescription, eventStart, eventEnd);
        default:
            throw new PandaException(ErrorType.UNKNOWN_COMMAND);
        }
    }

    private static LocalDateTime parseDateTime(String value) throws PandaException {
        try {
            return DateTimeUtil.parse(value);
        } catch (DateTimeParseException e) {
            throw new PandaException(ErrorType.INVALID_DATE_TIME);
        }
    }

    private static int findMarker(String text, String marker) {
        return findMarker(text, marker, 0);
    }

    private static int findMarker(String text, String marker, int startIndex) {
        int index = text.indexOf(marker, startIndex);
        while (index >= 0) {
            int markerEnd = index + marker.length();
            boolean hasBoundaryBefore = index == 0 || Character.isWhitespace(text.charAt(index - 1));
            boolean hasBoundaryAfter = markerEnd == text.length()
                    || Character.isWhitespace(text.charAt(markerEnd));
            if (hasBoundaryBefore && hasBoundaryAfter) {
                return index;
            }
            index = text.indexOf(marker, markerEnd);
        }
        return -1;
    }

    private static CommandType getCommandType(String command) {
        if (command.equals("todo") || command.startsWith("todo ")) {
            return CommandType.TODO;
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            return CommandType.DEADLINE;
        }
        if (command.equals("event") || command.startsWith("event ")) {
            return CommandType.EVENT;
        }
        if (command.equals("list")) {
            return CommandType.LIST;
        }
        if (command.equals("mark") || command.startsWith("mark ")) {
            return CommandType.MARK;
        }
        if (command.equals("unmark") || command.startsWith("unmark ")) {
            return CommandType.UNMARK;
        }
        if (command.equals("delete") || command.startsWith("delete ")) {
            return CommandType.DELETE;
        }
        if (command.equals("bye")) {
            return CommandType.BYE;
        }
        return CommandType.UNKNOWN;
    }

    private static String getArguments(String command, String name) {
        return command.length() == name.length()
                ? "" : command.substring(name.length()).trim();
    }

    private static int getTaskIndex(String command, String action, int taskCount)
            throws PandaException {
        String taskNumber = getArguments(command, action);
        if (taskNumber.isEmpty()) {
            throw new PandaException(ErrorType.MISSING_TASK_NUMBER, action);
        }

        int taskIndex;
        try {
            taskIndex = Integer.parseInt(taskNumber) - 1;
        } catch (NumberFormatException e) {
            throw new PandaException(ErrorType.NON_NUMERIC_TASK_NUMBER);
        }

        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new PandaException(ErrorType.TASK_NOT_FOUND);
        }
        return taskIndex;
    }
}
