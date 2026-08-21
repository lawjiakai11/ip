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

            if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                try {
                    int taskIndex = Integer.parseInt(command.substring(5).trim()) - 1;
                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        tasks[taskIndex].markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks[taskIndex]);
                    } else {
                        System.out.println("That task number does not exist.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please specify a valid task number.");
                }
            } else if (command.startsWith("unmark ")) {
                try {
                    int taskIndex = Integer.parseInt(command.substring(7).trim()) - 1;
                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        tasks[taskIndex].markAsNotDone();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks[taskIndex]);
                    } else {
                        System.out.println("That task number does not exist.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please specify a valid task number.");
                }
            } else {
                Task task = createTask(command);
                if (task == null) {
                    System.out.println("I don't understand that command.");
                } else if (taskCount >= tasks.length) {
                    System.out.println("Your task list is full.");
                } else {
                    tasks[taskCount] = task;
                    taskCount++;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                }
            }

            System.out.println("____________________________________________________________");
        }
    }

    private static Task createTask(String command) {
        if (command.startsWith("todo ")) {
            String description = command.substring(5).trim();
            return description.isEmpty() ? null : new Todo(description);
        }

        if (command.startsWith("deadline ")) {
            String details = command.substring(9);
            int byIndex = details.indexOf(" /by ");
            if (byIndex < 0) {
                return null;
            }

            String description = details.substring(0, byIndex).trim();
            String by = details.substring(byIndex + 5).trim();
            return description.isEmpty() || by.isEmpty() ? null : new Deadline(description, by);
        }

        if (command.startsWith("event ")) {
            String details = command.substring(6);
            int fromIndex = details.indexOf(" /from ");
            int toIndex = details.indexOf(" /to ", fromIndex + 7);
            if (fromIndex < 0 || toIndex < 0) {
                return null;
            }

            String description = details.substring(0, fromIndex).trim();
            String from = details.substring(fromIndex + 7, toIndex).trim();
            String to = details.substring(toIndex + 5).trim();
            return description.isEmpty() || from.isEmpty() || to.isEmpty()
                    ? null : new Event(description, from, to);
        }

        return null;
    }
}
