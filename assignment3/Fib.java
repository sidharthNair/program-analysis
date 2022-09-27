public class Fib {

    static int calls;

    static int fib(int n)
    {
        calls++;
        if (n <= 1)
            return n;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String args[])
    {
        int n = 9;
        System.out.println(fib(n));
        System.out.println("Number of function calls: " + calls);
    }
}