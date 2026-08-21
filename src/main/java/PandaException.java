/**
 * Represents an expected input or command error reported by Panda.
 */
public class PandaException extends Exception {
    /**
     * Creates an exception with a Panda-style error message.
     *
     * @param message the message to show the user
     */
    public PandaException(String message) {
        super(message);
    }
}
