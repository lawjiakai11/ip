package panda.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import panda.exception.ErrorType;
import panda.exception.PandaException;

class ParserTest {

    @Test
    void createTask_duplicateDeadlineParameter_throwsTypedException() {
        PandaException exception = assertThrows(PandaException.class,
                () -> Parser.createTask("deadline report /by 2026-10-20 /by 2026-10-21"));

        assertEquals(ErrorType.DUPLICATE_PARAMETER, exception.getErrorType());
    }

    @Test
    void getTaskIndex_multipleNumbers_throwsTypedException() {
        PandaException exception = assertThrows(PandaException.class,
                () -> Parser.getTaskIndex("mark 1 2", "mark", 2));

        assertEquals(ErrorType.NON_NUMERIC_TASK_NUMBER, exception.getErrorType());
    }

    @Test
    void validateCommandFormat_tabSeparatedCommand_throwsTypedException() {
        PandaException exception = assertThrows(PandaException.class,
                () -> Parser.validateCommandFormat("todo\tread book"));

        assertEquals(ErrorType.INVALID_COMMAND_FORMAT, exception.getErrorType());
    }
}
