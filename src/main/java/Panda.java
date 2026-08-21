import java.util.Scanner;

/**
 * The cool entry point for the Panda chatbot.
 */
public class Panda {
    /**
     * Starts Panda and processes commands until the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        System.out.println("____________________________________________________________");
        System.out.println("PANDA");
        System.out.println("Hello! I'm Panda.");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        Task[] tasks = new Task[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println("____________________________________________________________");

            if (command.equals("bye")) {
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
                if (command.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }
                } else if (isCommand(command, "mark")) {
                    int taskIndex = getTaskIndex(command, "mark", taskCount);
                    tasks[taskIndex].markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[taskIndex]);
                } else if (isCommand(command, "unmark")) {
                    int taskIndex = getTaskIndex(command, "unmark", taskCount);
                    tasks[taskIndex].markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[taskIndex]);
                } else {
                    Task task = createTask(command);
                    if (taskCount >= tasks.length) {
                        throw new PandaException("OOPS!!! Your task list is full.");
                    }

                    tasks[taskCount] = task;
                    taskCount++;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                }
            } catch (PandaException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("____________________________________________________________");
        }
    }

    /**
     * Parses a task command and creates its corresponding subtype.
     *
     * @param command the command entered by the user
     * @return the newly created task
     * @throws PandaException if the command is invalid or unknown
     */
    private static Task createTask(String command) throws PandaException {
        if (isCommand(command, "todo")) {
            String description = getArguments(command, "todo");
            if (description.isEmpty()) {
                throw new PandaException("OOPS!!! The description of a todo cannot be empty.");
            }
            return new Todo(description);
        }

        if (isCommand(command, "deadline")) {
            String details = getArguments(command, "deadline");
            if (details.isEmpty()) {
                throw new PandaException("OOPS!!! The description of a deadline cannot be empty.");
            }

            int byIndex = details.indexOf("/by");
            if (byIndex < 0) {
                throw new PandaException("OOPS!!! A deadline must include a /by date.");
            }

            String description = details.substring(0, byIndex).trim();
            String by = details.substring(byIndex + 3).trim();
            if (description.isEmpty()) {
                throw new PandaException("OOPS!!! The description of a deadline cannot be empty.");
            }
            if (by.isEmpty()) {
                throw new PandaException("OOPS!!! The /by date of a deadline cannot be empty.");
            }
            return new Deadline(description, by);
        }

        if (isCommand(command, "event")) {
            String details = getArguments(command, "event");
            if (details.isEmpty()) {
                throw new PandaException("OOPS!!! The description of an event cannot be empty.");
            }

            int fromIndex = details.indexOf("/from");
            int toIndex = details.indexOf("/to", fromIndex + 5);
            if (fromIndex < 0 || toIndex < 0) {
                throw new PandaException("OOPS!!! An event must include /from and /to times.");
            }

            String description = details.substring(0, fromIndex).trim();
            String from = details.substring(fromIndex + 5, toIndex).trim();
            String to = details.substring(toIndex + 3).trim();
            if (description.isEmpty()) {
                throw new PandaException("OOPS!!! The description of an event cannot be empty.");
            }
            if (from.isEmpty() || to.isEmpty()) {
                throw new PandaException("OOPS!!! An event must include both a start and end time.");
            }
            return new Event(description, from, to);
        }

        throw new PandaException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    /**
     * Checks whether a command is exactly a command name or starts with that name and an argument.
     *
     * @param command the complete user command
     * @param name the command name to check
     * @return whether the command matches the name
     */
    private static boolean isCommand(String command, String name) {
        return command.equals(name) || command.startsWith(name + " ");
    }

    /**
     * Extracts and trims the arguments after a command name.
     *
     * @param command the complete user command
     * @param name the command name
     * @return the command arguments, or an empty string when none were supplied
     */
    private static String getArguments(String command, String name) {
        return command.length() == name.length()
                ? "" : command.substring(name.length()).trim();
    }

    /**
     * Parses and validates a one-based task number for a status command.
     *
     * @param command the complete status command
     * @param action the status action, such as {@code mark} or {@code unmark}
     * @param taskCount the number of tasks currently stored
     * @return the zero-based task index
     * @throws PandaException if the task number is missing, invalid, or out of range
     */
    private static int getTaskIndex(String command, String action, int taskCount)
            throws PandaException {
        String taskNumber = getArguments(command, action);
        if (taskNumber.isEmpty()) {
            throw new PandaException("OOPS!!! Please specify a task number to " + action + ".");
        }

        int taskIndex;
        try {
            taskIndex = Integer.parseInt(taskNumber) - 1;
        } catch (NumberFormatException e) {
            throw new PandaException("OOPS!!! The task number must be a number.");
        }

        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new PandaException("OOPS!!! That task number does not exist.");
        }
        return taskIndex;
    }
}
