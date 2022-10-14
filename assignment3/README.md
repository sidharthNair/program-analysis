# Java Pathfinder

Implement code coverage tools by utilizing Java Pathfinder (JPF), as
well as a memoization framework.


## Code Coverage

Implement a code coverage tool using the Listener mechanism available
in JPF.  Each coverage report should be a plain txt (call it
report.txt) and it should include file names and lines that are
covered; the lines in the report should be sorted (first based on the
class name and then based on the line numbers).  Do *not* print
duplicate lines.


## Memoization framework

Memoization is a technique that captures the results of methods and
returns those results if the method is invoked with same arguments (in
the same program state).  Note that this makes sense only for
side-effect free methods.

Your task is to implement a memoization framework (again the best is
to use Listeners) that captures the result of any static method that
returns a primitive type.  To check if the result can be reused, you
will have to traverse heap starting from both all the (reference)
arguments given to the method.  We will assume that static fields and
arrays are not used in any example.

Let's consider several examples:

(1)

```java
public static int m(int a, int b) {
  return a + b;
}
```

In this case, the method satisfy all constraints and we should use
memoization.  It means the following: if we invoke this method twice
with same values, e.g., m(10, 20), the computation will happen only
first time (and we would store the rest); the second time we invoke
this method with same arguments, we would only return the value (30).

Your key contribution should be to design a data structure (as a field
in a Listener) that keeps mapping from the given argument values to
the result of computation.

(2)

```java
public static Object m(int a, int b) { ... }

```

This method should be ignored because it does not satisfy constraints.
Namely, the return value has to be of primitive type (you can assume
int or double).

(3)

```java
class C { int f; int g; C(int f, int g) { this.f = f; this.g = g; }

public static int m(C obj) { return obj.f + obj.g; }
```

In this case all constraints are satisfied.  Here, we need to traverse
the object graph (i.e., all the objects reachable from the given
argument) and find the values of all fields.  In this case, our object
graph has only a single object "obj" (because none of its fields is a
reference).  We would take the values of the fields and remember the
result, e.g., for m(new C(10, 20)), we would remember "10, 20"->30.
If there is another call to the same function with same values, we
would immediately return the result without doing the actual
computation.

(4)

```java
class A { int f; A(int f) { this.f = f; }}
class C { int g; A a; C(int g, A a) { this.g = g; this.a = a; }}

public static int m(C obj) { return obj.g + obj.a.f; }
```

and assume the following invocations:
```java
  m(new C(5, new A(10)));
  m(new C(5, new A(10)));
```

In this case, our object graph has two objects: an object of class C
that points to an instance of class A.  We have to make sure to
traverse both objects that are reachable from the given argument
("obj").  In our example, we would traverse these objects and collect
the following values "5; 10", then we will remember the return value
"15".  Next time we call this method with the same object graph, we
only return the value.


## Submission Instructions

Simply use the same repository that you have already shared with us.

(a) You should have a script "s" that runs your code.  This script
should be in the root of the assignment directory (e.g., assignment3).
If you prefer to write script in another language (e.g., Python), feel
free to do so, but in the end you should invoke your script from s.
We will always be looking for "s" script that is a valid bash script.

The script "s" should accept as input an example program and run it
using your system (using both listeners).

(b) Any output of "s" should be placed in the directory "results" in
the same directory with "s".


## Important

Be creative.  When something is unclear make a reasonable assumption
(e.g., you can use checksum to collect the values in an object graph).

Also, do not try to cover everything at once; start with small
examples and then expand from there.

LocalWords:  JPF memoization txt
