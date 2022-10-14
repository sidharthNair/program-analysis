public class Input {
    static class Fib {

        int fib(int n)
        {
            if (n <= 1)
                return n;
            return fib(n - 1) + fib(n - 2);
        }

    }
    public static void main(String[] args) throws Exception {
        m(3, 4);
        Fib f = new Fib();
        System.out.println(f.fib(9));
    }

    public static void m(int a, int b) throws Exception {
    }
}
