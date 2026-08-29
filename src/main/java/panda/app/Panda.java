package panda.app;

import java.util.Scanner;

import panda.exception.ErrorType;
import panda.exception.PandaException;
import panda.model.CommandType;
import panda.model.Task;
import panda.model.TaskList;
import panda.parser.Parser;
import panda.storage.Storage;
import panda.ui.Ui;

/**
 * The cool entry point for the Panda chatbot.
 */
public class Panda {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        TaskList tasks = new TaskList(Storage.loadTasks());
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            ui.showDivider();

            CommandType commandType = Parser.getCommandType(command);
            if (commandType == CommandType.BYE) {
                ui.showBye();
                break;
            }

            try {
                switch (commandType) {
                case LIST:
                    ui.showTaskList(tasks.asList());
                    break;
                case MARK:
                    int markIndex = Parser.getTaskIndex(command, "mark", tasks.size());
                    Task markedTask = tasks.markTask(markIndex);
                    Storage.saveTasks(tasks.asList());
                    ui.showTaskMarked(markedTask);
                    break;
                case UNMARK:
                    int unmarkIndex = Parser.getTaskIndex(command, "unmark", tasks.size());
                    Task unmarkedTask = tasks.unmarkTask(unmarkIndex);
                    Storage.saveTasks(tasks.asList());
                    ui.showTaskUnmarked(unmarkedTask);
                    break;
                case DELETE:
                    int deleteIndex = Parser.getTaskIndex(command, "delete", tasks.size());
                    Task deletedTask = tasks.remove(deleteIndex);
                    Storage.saveTasks(tasks.asList());
                    ui.showTaskDeleted(deletedTask, tasks.size());
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = Parser.createTask(command);
                    tasks.add(task);
                    Storage.saveTasks(tasks.asList());
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

}
