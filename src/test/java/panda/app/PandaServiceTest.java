package panda.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import panda.model.Deadline;
import panda.model.Event;
import panda.model.TaskList;
import panda.model.Todo;

class PandaServiceTest {

    @Test
    void dateSortingEnabled_listOrdersDeadlinesBeforeOtherTasks() {
        Todo todo = new Todo("read book");
        Event event = new Event("team meeting",
                LocalDateTime.of(2026, 10, 20, 9, 0),
                LocalDateTime.of(2026, 10, 20, 10, 0));
        Deadline laterDeadline = new Deadline("submit report", LocalDateTime.of(2026, 10, 22, 12, 0));
        Deadline earlierDeadline = new Deadline("return book", LocalDateTime.of(2026, 10, 21, 12, 0));
        PandaService service = new PandaService(new TaskList(List.of(todo, event, laterDeadline, earlierDeadline)));

        String response = service.setDateSortingEnabled(true);

        assertEquals("Here are the tasks in date order:\n"
                        + "1.[D][ ] return book (by: Oct 21 2026 12:00 PM)\n"
                        + "2.[D][ ] submit report (by: Oct 22 2026 12:00 PM)\n"
                        + "3.[T][ ] read book\n"
                        + "4.[E][ ] team meeting (from: Oct 20 2026 9:00 AM to: Oct 20 2026 10:00 AM)",
                response);
    }

    @Test
    void dateSortingEnabled_markAndDeleteUseDisplayedTaskNumbers() {
        Todo todo = new Todo("read book");
        Deadline laterDeadline = new Deadline("submit report", LocalDateTime.of(2026, 10, 22, 12, 0));
        Deadline earlierDeadline = new Deadline("return book", LocalDateTime.of(2026, 10, 21, 12, 0));
        PandaService service = new PandaService(new TaskList(List.of(todo, laterDeadline, earlierDeadline)));
        service.setDateSortingEnabled(true);

        assertEquals("Nice! I've marked this task as done:\n  [D][X] return book (by: Oct 21 2026 12:00 PM)",
                service.getResponse("mark 1"));
        assertEquals("Noted. I've removed this task:\n  [D][X] return book (by: Oct 21 2026 12:00 PM)\n"
                        + "Now you have 2 tasks in the list.",
                service.getResponse("delete 1"));

        assertEquals("Here are the tasks in date order:\n"
                        + "1.[D][ ] submit report (by: Oct 22 2026 12:00 PM)\n"
                        + "2.[T][ ] read book", service.getResponse("list"));
    }

    @Test
    void dateSortingDisabled_listRestoresInsertionOrder() {
        Todo todo = new Todo("read book");
        Deadline laterDeadline = new Deadline("submit report", LocalDateTime.of(2026, 10, 22, 12, 0));
        Deadline earlierDeadline = new Deadline("return book", LocalDateTime.of(2026, 10, 21, 12, 0));
        PandaService service = new PandaService(new TaskList(List.of(todo, laterDeadline, earlierDeadline)));
        service.setDateSortingEnabled(true);

        String response = service.setDateSortingEnabled(false);

        assertEquals("Here are the tasks in your list:\n"
                        + "1.[T][ ] read book\n"
                        + "2.[D][ ] submit report (by: Oct 22 2026 12:00 PM)\n"
                + "3.[D][ ] return book (by: Oct 21 2026 12:00 PM)", response);
    }

    @Test
    void dateSortingEnabled_directionToggledOrdersDeadlinesDescending() {
        Deadline earlierDeadline = new Deadline("return book", LocalDateTime.of(2026, 10, 21, 12, 0));
        Deadline laterDeadline = new Deadline("submit report", LocalDateTime.of(2026, 10, 22, 12, 0));
        PandaService service = new PandaService(new TaskList(List.of(earlierDeadline, laterDeadline)));
        service.setDateSortingEnabled(true);

        String response = service.toggleDateSortDirection();

        assertEquals("Here are the tasks in date order:\n"
                        + "1.[D][ ] submit report (by: Oct 22 2026 12:00 PM)\n"
                        + "2.[D][ ] return book (by: Oct 21 2026 12:00 PM)", response);
    }

    @Test
    void dateSortingEnabled_findOrdersMatchingDeadlinesBeforeOtherTasks() {
        Todo todo = new Todo("review notes");
        Deadline laterDeadline = new Deadline("review report", LocalDateTime.of(2026, 10, 22, 12, 0));
        Event event = new Event("review meeting",
                LocalDateTime.of(2026, 10, 20, 9, 0),
                LocalDateTime.of(2026, 10, 20, 10, 0));
        Deadline earlierDeadline = new Deadline("review book", LocalDateTime.of(2026, 10, 21, 12, 0));
        PandaService service = new PandaService(new TaskList(List.of(todo, laterDeadline, event, earlierDeadline)));
        service.setDateSortingEnabled(true);

        String response = service.getResponse("find review");

        assertEquals("Here are the matching tasks in your list:\n"
                        + "1.[D][ ] review book (by: Oct 21 2026 12:00 PM)\n"
                        + "2.[D][ ] review report (by: Oct 22 2026 12:00 PM)\n"
                        + "3.[T][ ] review notes\n"
                        + "4.[E][ ] review meeting (from: Oct 20 2026 9:00 AM to: Oct 20 2026 10:00 AM)",
                response);
    }

    @Test
    void dateSortingEnabled_deadlineAddedShowsUpdatedDateOrder() {
        Deadline laterDeadline = new Deadline("submit report", LocalDateTime.of(2026, 10, 22, 12, 0));
        PandaService service = new PandaService(new TaskList(List.of(laterDeadline)));
        service.setDateSortingEnabled(true);

        String response = service.getResponse("deadline return book /by 2026-10-21 1200");

        assertEquals("Got it. I've added this task:\n"
                        + "  [D][ ] return book (by: Oct 21 2026 12:00 PM)\n"
                        + "Now you have 2 tasks in the list.\n"
                        + "Here are the tasks in date order:\n"
                        + "1.[D][ ] return book (by: Oct 21 2026 12:00 PM)\n"
                        + "2.[D][ ] submit report (by: Oct 22 2026 12:00 PM)", response);
    }

    @Test
    void emptyTaskList_dateSortingEnabled_returnsEmptySortResponse() {
        PandaService service = new PandaService(new TaskList());

        assertEquals("There are no tasks to sort.", service.setDateSortingEnabled(true));
    }
}
