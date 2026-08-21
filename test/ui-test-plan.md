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

- Aim: Verify that deadline and event commands create the correct `Task` subclasses and preserve their date/time strings.

### Input

```text
deadline submit report /by Friday 5pm
event team meeting /from Monday 2pm /to 4pm
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
  [D][ ] submit report (by: Friday 5pm)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] team meeting (from: Monday 2pm to: 4pm)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][ ] submit report (by: Friday 5pm)
2.[E][ ] team meeting (from: Monday 2pm to: 4pm)
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
deadline return book /by Sunday
deadline return book
list
event project meeting /from Monday 2pm /to 4pm
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
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS!!! A deadline must include a /by date.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Monday 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS!!! An event must include both a start and end time.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Monday 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][X] return book (by: Sunday)
3.[E][ ] project meeting (from: Monday 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Monday 2pm to: 4pm)
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
deadline return book /by Sunday
event project meeting /from Monday 2pm /to 4pm
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
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Monday 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Monday 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] project meeting (from: Monday 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] project meeting (from: Monday 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [E][X] project meeting (from: Monday 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][X] project meeting (from: Monday 2pm to: 4pm)
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
deadline return book /by Sunday
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
  [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][ ] return book (by: Sunday)
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
