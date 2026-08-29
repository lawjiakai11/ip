import java.time.LocalDateTime;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    public Event(String description, String from, String to) {
        this(description, DateTimeUtil.parse(from), DateTimeUtil.parse(to));
    }

    /**
     * Returns the event start date/time.
     *
     * @return event start date/time
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the event end date/time.
     *
     * @return event end date/time
     */
    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + DateTimeUtil.formatForDisplay(from)
                + " to: " + DateTimeUtil.formatForDisplay(to) + ")";
    }

    /**
     * Returns this event in the save-file format.
     *
     * @return one line of event data
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + DateTimeUtil.formatForStorage(from)
                + " | " + DateTimeUtil.formatForStorage(to);
    }
}
