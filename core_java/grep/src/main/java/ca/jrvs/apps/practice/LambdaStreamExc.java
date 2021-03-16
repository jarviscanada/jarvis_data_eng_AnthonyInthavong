package ca.jrvs.apps.practice;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface LambdaStreamExc {

  /**
   * Create a String stream from array
   *
   * note: arbitrary number of value will be stored in an array
   *
   * @param strings
   * @return
   */
  Stream<String> createStrStream(String ... strings);


  /**
   * Convert all strings to uppercase
   * please use createStrStream
   *
   * @param strings
   * @return
   */
  Stream<String> toUpperCase(String ... strings);

  /**
   * filter strings that contains the pattern
   * e.g.
   * filter(stringStream, "a") will return another stream which no element contains a
   *
   *
   * @param stringStream
   * @param pattern
   * @return
   */
  Stream<String> filter(Stream<String> stringStream, String pattern);

  /**
   * Create a intStream from a arr[]
   * @param arr
   * @return
   */
  IntStream createIntStream(int[] arr);

  /**
   * Convert a stream to list
   *
   * @param stream
   * @param <E>
   * @return
   */
  <E> List<E> toList(Stream<E> stream);

  /**
   * Convert a intStream to list
   * @param intStream
   * @return
   */
  List<Integer> toList(IntStream intStream);

  /**
   * Create a IntStream range from start to end inclusive
   * @param start
   * @param end
   * @return
   */
  IntStream createIntStream(int start, int end);

  /**
   * Convert a intStream to a doubleStream
   * and compute square root of each element
   * @param intStream
   * @return
   */
  DoubleStream squareRootIntStream(IntStream intStream);


  /**
   * filter all even number and return odd numbers from a intStream
   * @param intStream
   * @return
   */
  IntStream getOdd(IntStream intStream);

  /**
   * Return a lambda function that print a message with a prefix and suffix
   * This lambda can be useful to format logs
   *
   * You will learn:
   *   - functional interface http://bit.ly/2pTXRwM & http://bit.ly/33onFig
   *   - lambda syntax
   *
   * e.g.
   * LambdaStreamExc lse = new LambdaStreamImp();
   * Consumer<String> printer = lse.getLambdaPrinter("start>", "<end");
   * printer.accept("Message body");
   *
   * sout:
   * start>Message body<end
   *
   * @param prefix prefix str
   * @param suffix suffix str
   * @return
   */
  Consumer<String> getLambdaPrinter(String prefix, String suffix);

  /**
   * Print each message with a given printer
   * Please use `getLambdaPrinter` method
   *
   * e.g.
   * String[] messages = {"a","b", "c"};
   * lse.printMessages(messages, lse.getLambdaPrinter("msg:", "!") );
   *
   * sout:
   * msg:a!
   * msg:b!
   * msg:c!
   *
   * @param messages
   * @param printer
   */
  void printMessages(String[] messages, Consumer<String> printer);

  /**
   * Print all odd number from a intStream.
   * Please use `createIntStream` and `getLambdaPrinter` methods
   *
   * e.g.
   * lse.printOdd(lse.createIntStream(0, 5), lse.getLambdaPrinter("odd number:", "!"));
   *
   * sout:
   * odd number:1!
   * odd number:3!
   * odd number:5!
   *
   * @param intStream
   * @param printer
   */
  void printOdd(IntStream intStream, Consumer<String> printer);

  /**
   * Square each number from the input.
   * Please write two solutions and compare difference
   *   - using flatMap
   *
   * @param ints
   * @return
   */
  Stream<Integer> flatNestedInt(Stream<List<Integer>> ints);
}