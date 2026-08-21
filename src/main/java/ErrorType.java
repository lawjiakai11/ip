/**
 * User-input errors that Panda can report.
 */
public enum ErrorType {
    EMPTY_TODO_DESCRIPTION("OOPS!!! The description of a todo cannot be empty."),
    EMPTY_DEADLINE_DESCRIPTION("OOPS!!! The description of a deadline cannot be empty."),
    MISSING_DEADLINE_BY("OOPS!!! A deadline must include a /by date."),
    EMPTY_DEADLINE_BY("OOPS!!! The /by date of a deadline cannot be empty."),
    EMPTY_EVENT_DESCRIPTION("OOPS!!! The description of an event cannot be empty."),
    MISSING_EVENT_TIMES("OOPS!!! An event must include /from and /to times."),
    EMPTY_EVENT_TIME("OOPS!!! An event must include both a start and end time."),
    UNKNOWN_COMMAND("OOPS!!! I'm sorry, but I don't know what that means :-("),
    MISSING_TASK_NUMBER("OOPS!!! Please specify a task number to %s."),
    NON_NUMERIC_TASK_NUMBER("OOPS!!! The task number must be a number."),
    TASK_NOT_FOUND("OOPS!!! That task number does not exist.");

    private final String messageTemplate;

    ErrorType(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    /**
     * Formats the user-facing message for this error.
     *
     * @param arguments values used by any placeholders in the message
     * @return the formatted Panda-style error message
     */
    public String getMessage(Object... arguments) {
        return String.format(messageTemplate, arguments);
    }
}
