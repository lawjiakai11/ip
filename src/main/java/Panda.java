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
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                } else if (isCommand(command, "mark")) {
                    int taskIndex = getTaskIndex(command, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(taskIndex));
                } else if (isCommand(command, "unmark")) {
                    int taskIndex = getTaskIndex(command, "unmark", tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(taskIndex));
                } else if (isCommand(command, "delete")) {
                    int taskIndex = getTaskIndex(command, "delete", tasks.size());
                    Task deletedTask = tasks.remove(taskIndex);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + deletedTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else {
                    Task task = createTask(command);
                    tasks.add(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                }
            } catch (PandaException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("____________________________________________________________");
        }
    }

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

    private static boolean isCommand(String command, String name) {
        return command.equals(name) || command.startsWith(name + " ");
    }

    private static String getArguments(String command, String name) {
        return command.length() == name.length()
                ? "" : command.substring(name.length()).trim();
    }

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
