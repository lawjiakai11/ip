package panda.model;

import java.time.LocalDateTime;

import panda.util.DateTimeUtil;

public class Deadline extends Task {
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    public Deadline(String description, String by) {
        this(description, DateTimeUtil.parse(by));
    }

    /**
     * Returns the deadline date/time.
     *
     * @return deadline date/time
     */
    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + DateTimeUtil.formatForDisplay(by) + ")";
    }

    /**
     * Returns this deadline in the save-file format.
     *
     * @return one line of deadline data
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + DateTimeUtil.formatForStorage(by);
    }
}
