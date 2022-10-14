# Dynamic Instrumentation using ASM

Develop an approach (based on ASM) to compute and report code coverage
for a given Java program.  An input to your tool will be a single Java
file with 1 or more classes; the input file will be called Input.java
(which will contain Input class and a main method).  It is expected
that you will instrument code dynamically (using Java agents).  In
this assignment, to keep things simple, we only care about method
coverage, i.e., each method is covered or not during the execution.

Additionally, you should collect for each method how many times it was
executed.  You can solve this in many ways; here are three: (a) you
can add a counter to each class for every method and increment (once
the execution is done print all counters), (b) collect information for
each method in a separate class that is invoked at the beginning of
each method (and the name of class+method is passed as an argument),
and (c) print (to stdout or a file) every method being executed and
then compute the count for each method in a script.

Output should be in the "output" file in the following format:
```java
  fully_qualified_class_name.method_signature COUNT
```

Example line (covered method m in class C 5 times):
```java
  C.m:(I)V 5
```

As always, you should have test cases for your implementation and
include a bash script that triggers the execution.

If something is unclear, make a reasonable assumption, document, and
proceed.

We will, as always, invoke your s script that is in the appropriate
assignment directory.
