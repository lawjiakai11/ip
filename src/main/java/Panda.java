import java.util.ArrayList;
import java.util.Scanner;

/**
 * The cool entry point for the Panda chatbot.
 */
public class Panda {
    public static void main(String[] args) {
        System.out.println("____________________________________________________________");
        System.out.println("PANDA");
        System.out.println("Hello! I'm Panda.");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        ArrayList<Task> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println("____________________________________________________________");

            CommandType commandType = getCommandType(command);
            if (commandType == CommandType.BYE) {
                System.out.println("    ( ) ( ) ( )");
                System.out.println("      \\ | /");
                System.out.println("       \\|/");
                System.out.println("     .-----.");
                System.out.println("    /       \\");
                System.out.println("   |   o o   |");
                System.out.println("    \\_______/");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }

            try {
                switch (commandType) {
                case LIST:
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                    break;
                case MARK:
                    int markIndex = getTaskIndex(command, "mark", tasks.size());
                    tasks.get(markIndex).markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(markIndex));
                    break;
                case UNMARK:
                    int unmarkIndex = getTaskIndex(command, "unmark", tasks.size());
                    tasks.get(unmarkIndex).markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(unmarkIndex));
                    break;
                case DELETE:
                    int deleteIndex = getTaskIndex(command, "delete", tasks.size());
                    Task deletedTask = tasks.remove(deleteIndex);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + deletedTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = createTask(command);
                    tasks.add(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    break;
                case UNKNOWN:
                default:
                    throw new PandaException(ErrorType.UNKNOWN_COMMAND);
                }
            } catch (PandaException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("____________________________________________________________");
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

            int byIndex = details.indexOf("/by");
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
            return new Deadline(deadlineDescription, by);
        case EVENT:
            String eventDetails = getArguments(command, "event");
            if (eventDetails.isEmpty()) {
                throw new PandaException(ErrorType.EMPTY_EVENT_DESCRIPTION);
            }

            int fromIndex = eventDetails.indexOf("/from");
            int toIndex = eventDetails.indexOf("/to", fromIndex + 5);
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
            return new Event(eventDescription, from, to);
        default:
            throw new PandaException(ErrorType.UNKNOWN_COMMAND);
        }
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
