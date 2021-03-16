package ca.jrvs.apps.fibonacci;

public class Fibonacci {
  /**
   * Big-O: O(2^n)
   * Justification: Every call results in 2 more stacks being created. We do this n times
   */
  public int fibonacciNaive(int n) {
    if (n == 1 || n == 2) {
      return 1;
    } else {
      return fibonacciNaive(n-1) + fibonacciNaive(n-2);
    }
  }

  /**
   * Big-O: O(n)
   * Justification: Bottom up dynamic programming approach. We store the previous number
   * in a variable, then use that variable in the next calculation in linear time.
   */
  public int fibonacciIterative(int n) {
    if (n == 1 || n == 2) {
      return 1;
    }
    int first = 1;
    int second = 1;
    int current = 1;
    for (int i = 0; i < n-2; i++) {
      current = first + second;
      first = second;
      second = current;
    }
    return current;
  }

  public static void main(String[] args) {
    int x = new Fibonacci().fibonacciIterative(3);
    System.out.println(x);

  }


}
