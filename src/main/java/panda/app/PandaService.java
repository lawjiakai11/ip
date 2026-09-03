package panda.app;

import java.util.List;

import panda.exception.ErrorType;
import panda.exception.PandaException;
import panda.model.CommandType;
import panda.model.Task;
import panda.model.TaskList;
import panda.parser.Parser;
import panda.storage.Storage;

/**
 * Processes Panda commands independently of the user interface.
 */
public class PandaService {
    /** Response used by the console UI to end its session. */
    public static final String BYE_RESPONSE = "__PANDA_BYE__";

    private final TaskList tasks;

    /** Creates a service with tasks loaded from storage. */
    public PandaService() {
        tasks = new TaskList(Storage.loadTasks());
    }

    /**
     * Processes one command and returns text suitable for display in a chat bubble.
     *
     * @param command raw command text
     * @return Panda's response
     */
    public String getResponse(String command) {
        CommandType commandType = Parser.getCommandType(command);
        try {
            switch (commandType) {
            case LIST:
                return formatTasks("Here are the tasks in your list:", tasks.asList());
            case MARK:
                return "Nice! I've marked this task as done:\n  "
                        + updateTask(command, "mark", true);
            case UNMARK:
                return "OK, I've marked this task as not done yet:\n  "
                        + updateTask(command, "unmark", false);
            case DELETE:
                int deleteIndex = Parser.getTaskIndex(command, "delete", tasks.size());
                Task deletedTask = tasks.remove(deleteIndex);
                Storage.saveTasks(tasks.asList());
                return "Noted. I've removed this task:\n  " + deletedTask
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            case FIND:
                String keyword = Parser.getArguments(command, "find");
                if (keyword.isEmpty()) {
                    throw new PandaException(ErrorType.EMPTY_FIND_KEYWORD);
                }
                return formatTasks("Here are the matching tasks in your list:", tasks.find(keyword));
            case TODO:
            case DEADLINE:
            case EVENT:
                Task task = Parser.createTask(command);
                tasks.add(task);
                Storage.saveTasks(tasks.asList());
                return "Got it. I've added this task:\n  " + task
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            case BYE:
                return BYE_RESPONSE;
            case UNKNOWN:
            default:
                throw new PandaException(ErrorType.UNKNOWN_COMMAND);
            }
        } catch (PandaException e) {
            return e.getMessage();
        }
    }

    private Task updateTask(String command, String action, boolean markDone) throws PandaException {
        int index = Parser.getTaskIndex(command, action, tasks.size());
        Task task = markDone ? tasks.markTask(index) : tasks.unmarkTask(index);
        Storage.saveTasks(tasks.asList());
        return task;
    }

    private String formatTasks(String heading, List<Task> taskList) {
        StringBuilder response = new StringBuilder(heading);
        for (int i = 0; i < taskList.size(); i++) {
            response.append("\n").append(i + 1).append(".").append(taskList.get(i));
        }
        return response.toString();
    }
}
