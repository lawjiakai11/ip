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
                    System.out.println((i + 1) + ".[" + tasks[i].getStatusIcon() + "] "
                            + tasks[i].getDescription());
                }
            } else if (command.startsWith("mark ")) {
                try {
                    int taskIndex = Integer.parseInt(command.substring(5).trim()) - 1;
                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        tasks[taskIndex].markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  [X] " + tasks[taskIndex].getDescription());
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
                        System.out.println("  [ ] " + tasks[taskIndex].getDescription());
                    } else {
                        System.out.println("That task number does not exist.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please specify a valid task number.");
                }
            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println("added: " + command);
            }

            System.out.println("____________________________________________________________");
        }
    }
}
