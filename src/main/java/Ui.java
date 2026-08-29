import java.util.List;

/**
 * Handles Panda's console input/output presentation.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";

    /** Displays Panda's welcome message. */
    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println("PANDA");
        System.out.println("Hello! I'm Panda.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /** Displays the divider used between chatbot turns. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /** Displays Panda's goodbye illustration and message. */
    public void showBye() {
        System.out.println("    ( ) ( ) ( )");
        System.out.println("      \\ | /");
        System.out.println("       \\|/");
        System.out.println("     .-----.");
        System.out.println("    /       \\");
        System.out.println("   |   o o   |");
        System.out.println("    \\_______/");
        System.out.println("Bye. Hope to see you again soon!");
        showDivider();
    }

    /** Displays all tasks with their one-based list positions. */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /** Displays confirmation for a newly added task. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /** Displays confirmation for marking a task done. */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /** Displays confirmation for marking a task not done. */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /** Displays confirmation for deleting a task. */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /** Displays a Panda-style command error. */
    public void showError(PandaException exception) {
        System.out.println(exception.getMessage());
    }
}
