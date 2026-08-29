# UI Test Plan

The runner executes each test case in a fresh `Panda` process. The `Input` block
contains one command per line. The `Expected output` block contains the
program's stdout only; terminal echo of typed commands is not included.

## Test Case 1: Add and list a todo

- Aim: Verify that a todo is stored and displayed with the todo type and incomplete status.

### Input

```text
todo buy groceries
list
bye
```

### Expected output

```text
____________________________________________________________
PANDA
Hello! I'm Panda.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] buy groceries
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] buy groceries
____________________________________________________________
____________________________________________________________
    ( ) ( ) ( )
      \ | /
       \|/
     .-----.
    /       \
   |   o o   |
    \_______/
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case 2: Error handling

- Aim: Verify that empty, unknown, and malformed task commands produce helpful Panda-style errors without ending the session.

### Input

```text
todo
blah
deadline report
deadline report /by
event meeting
event meeting /from Monday /to
mark
mark abc
mark 1
unmark
unmark abc
unmark 1
bye
```

### Expected output

```text
____________________________________________________________
PANDA
Hello! I'm Panda.
What can I do for you?
____________________________________________________________
____________________________________________________________
OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS!!! I'm sorry, but I don't know what that means :-(
____________________________________________________________
____________________________________________________________
OOPS!!! A deadline must include a /by date.
____________________________________________________________
____________________________________________________________
OOPS!!! The /by date of a deadline cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS!!! An event must include /from and /to times.
____________________________________________________________
____________________________________________________________
OOPS!!! An event must include both a start and end time.
____________________________________________________________
____________________________________________________________
OOPS!!! Please specify a task number to mark.
____________________________________________________________
____________________________________________________________
OOPS!!! The task number must be a number.
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
OOPS!!! Please specify a task number to unmark.
____________________________________________________________
____________________________________________________________
OOPS!!! The task number must be a number.
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
    ( ) ( ) ( )
      \ | /
       \|/
     .-----.
    /       \
   |   o o   |
    \_______/
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case 3: Add deadline and event tasks

- Aim: Verify that deadline and event commands parse typed date/time values and display them in Panda's readable format.

### Input

```text
deadline submit report /by 2/12/2019 1800
event team meeting /from 2019-10-16 1400 /to 2019-10-16 1600
list
bye
```

### Expected output

```text
____________________________________________________________
PANDA
Hello! I'm Panda.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] submit report (by: Dec 02 2019 6:00 PM)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] team meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][ ] submit report (by: Dec 02 2019 6:00 PM)
2.[E][ ] team meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
____________________________________________________________
____________________________________________________________
    ( ) ( ) ( )
      \ | /
       \|/
     .-----.
    /       \
   |   o o   |
    \_______/
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case 4: Interleaved errors preserve task state

- Aim: Verify that malformed additions and invalid task indexes do not change the valid tasks already stored or their completion statuses.

### Input

```text
todo read book
todo
list
deadline return book /by 2019-10-20
deadline return book
list
event project meeting /from 2019-10-16 1400 /to 2019-10-16 1600
event project meeting /from Monday 2pm /to
list
mark 2
mark 99
list
unmark 2
unmark 99
list
bye
```

### Expected output

```text
____________________________________________________________
PANDA
Hello! I'm Panda.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Oct 20 2019)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS!!! A deadline must include a /by date.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Oct 20 2019)
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS!!! An event must include both a start and end time.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Oct 20 2019)
3.[E][ ] project meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Oct 20 2019)
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][X] return book (by: Oct 20 2019)
3.[E][ ] project meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Oct 20 2019)
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Oct 20 2019)
3.[E][ ] project meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
____________________________________________________________
____________________________________________________________
    ( ) ( ) ( )
      \ | /
       \|/
     .-----.
    /       \
   |   o o   |
    \_______/
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case 5: Empty list and near-miss commands

- Aim: Verify empty-list output, whitespace-only descriptions, unknown command variants, and state preservation after invalid status indexes.

### Input

```text
list
todo   
todoish
todo buy milk
list
mark 0
unmark 2
list
bye
```

### Expected output

```text
____________________________________________________________
PANDA
Hello! I'm Panda.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS!!! I'm sorry, but I don't know what that means :-(
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] buy milk
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] buy milk
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] buy milk
____________________________________________________________
____________________________________________________________
    ( ) ( ) ( )
      \ | /
       \|/
     .-----.
    /       \
   |   o o   |
    \_______/
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case 6: Delete tasks and preserve shifted indexes

- Aim: Verify that deletion removes the selected task, renumbers later tasks, leaves state unchanged after an invalid delete, and allows subsequent commands to use the shifted index.

### Input

```text
todo read book
deadline return book /by 2019-10-20
event project meeting /from 2019-10-16 1400 /to 2019-10-16 1600
list
delete 2
list
delete 99
list
mark 2
list
bye
```

### Expected output

```text
____________________________________________________________
PANDA
Hello! I'm Panda.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Oct 20 2019)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Oct 20 2019)
3.[E][ ] project meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [D][ ] return book (by: Oct 20 2019)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] project meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] project meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [E][X] project meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][X] project meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
____________________________________________________________
____________________________________________________________
    ( ) ( ) ( )
      \ | /
       \|/
     .-----.
    /       \
   |   o o   |
    \_______/
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case 7: Mark and unmark a polymorphic task

- Aim: Verify that completion status changes work for a task stored through a `Task` reference.

### Input

```text
deadline return book /by 2019-10-20
mark 1
unmark 1
list
bye
```

### Expected output

```text
____________________________________________________________
PANDA
Hello! I'm Panda.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Oct 20 2019)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Oct 20 2019)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Oct 20 2019)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][ ] return book (by: Oct 20 2019)
____________________________________________________________
____________________________________________________________
    ( ) ( ) ( )
      \ | /
       \|/
     .-----.
    /       \
   |   o o   |
    \_______/
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case 8: Persist changes to the task list

- Aim: Exercise additions, completion changes, and deletion so the current task list is saved after every change.

### Input

```text
todo read book
deadline return book /by 2019-10-20
mark 2
unmark 2
delete 1
bye
```

### Expected output

```text
____________________________________________________________
PANDA
Hello! I'm Panda.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Oct 20 2019)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Oct 20 2019)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Oct 20 2019)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
    ( ) ( ) ( )
      \ | /
       \|/
     .-----.
    /       \
   |   o o   |
    \_______/
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case 9: Load saved tasks at startup

- Aim: Verify that Panda recreates todo, deadline, and event tasks—including their completion statuses—from the save file when a new session starts.

### Initial data

```text
T | 1 | read book
D | 0 | return book | 2019-10-20T00:00:00
E | 1 | project meeting | 2019-10-16T14:00:00 | 2019-10-16T16:00:00
```

### Input

```text
list
bye
```

### Expected output

```text
____________________________________________________________
PANDA
Hello! I'm Panda.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: Oct 20 2019)
3.[E][X] project meeting (from: Oct 16 2019 2:00 PM to: Oct 16 2019 4:00 PM)
____________________________________________________________
____________________________________________________________
    ( ) ( ) ( )
      \ | /
       \|/
     .-----.
    /       \
   |   o o   |
    \_______/
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case 10: Reject impossible and reversed date/times

- Aim: Verify that impossible calendar dates, invalid clock values, and event end times before their starts are rejected without changing the task list.

### Input

```text
deadline leap day /by 2019-02-29
deadline bad clock /by 2019-10-15 2560
event reversed /from 2019-10-16 1800 /to 2019-10-16 1700
list
bye
```

### Expected output

```text
____________________________________________________________
PANDA
Hello! I'm Panda.
What can I do for you?
____________________________________________________________
____________________________________________________________
OOPS!!! Please enter a valid date/time, such as 2019-10-15 or 2/12/2019 1800.
____________________________________________________________
____________________________________________________________
OOPS!!! Please enter a valid date/time, such as 2019-10-15 or 2/12/2019 1800.
____________________________________________________________
____________________________________________________________
OOPS!!! An event's end date/time cannot be before its start date/time.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
    ( ) ( ) ( )
      \ | /
       \|/
     .-----.
    /       \
   |   o o   |
    \_______/
Bye. Hope to see you again soon!
____________________________________________________________
```
