package ca.jrvs.apps.fibonacci;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FibonacciUnitTest {

  Fibonacci fib;

  @Before
  public void setUp() throws Exception {
    fib = new Fibonacci();
  }

  @Test
  public void fibonacciNaive() {
    System.out.println("Test case: test oddEvenMod method from the test class");
    int expected;
    expected = 5;
    Assert.assertEquals(expected,fib.fibonacciNaive(5));
  }

  @Test
  public void fibonacciIterative() {
    int expected;
    expected = 5;
    Assert.assertEquals(expected,fib.fibonacciNaive(5));
  }
}