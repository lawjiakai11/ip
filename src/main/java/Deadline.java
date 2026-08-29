public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }

    /**
     * Returns this deadline in the save-file format.
     *
     * @return one line of deadline data
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + by;
    }
}
