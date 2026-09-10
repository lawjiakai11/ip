# Panda User Guide

Panda manages todos, deadlines, and events through the command field in its chat window.

## Sorting deadlines by date

Use the **Sort deadlines by date** checkbox above the chat history to change the current task display.
When enabled, deadlines appear before todos and events, ordered by their `/by` date and time.
Todos and events remain after the deadlines in the order they were added.

The **Ascending** button shows the active direction. Ascending places the earliest deadline first;
selecting it changes the direction to **Descending**, which places the latest deadline first.
The direction control is available only while date sorting is enabled.

Sorting is temporary: unchecking the box restores the original insertion order. Panda does not change
the saved task-file order, and sorting resets to unchecked with ascending order when Panda restarts.
While sorting is enabled, task numbers used by commands such as `mark 1`, `unmark 1`, and `delete 1`
refer to the displayed date-sorted order.

If there are no tasks, enabling date sorting displays `There are no tasks to sort.`
