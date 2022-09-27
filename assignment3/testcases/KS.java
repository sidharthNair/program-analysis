public class KS {

    static int calls;
    static int maxCalls;

    static int val[] = new int[] { 20, 5, 10, 40, 15, 25 };
    static int wt[] = new int[] { 1, 2, 3, 8, 7, 4 };

    static int max(int a, int b) {
        maxCalls++;
        return (a > b) ? a : b;
    }

    static int knapSack(int W, int n) {
        calls++;
        if (n == 0 || W == 0)
            return 0;

        if (wt[n - 1] > W)
            return knapSack(W, n - 1);

        else
            return max(val[n - 1] + knapSack(W - wt[n - 1], n - 1), knapSack(W, n - 1));
    }

    public static void main(String args[]) {
        int W = 10;
        int n = val.length;
        System.out.println(knapSack(W, n));
        System.out.println("Knapsack Function Calls: " + calls);
        System.out.println("Max Function Calls: " + maxCalls);
    }
}
