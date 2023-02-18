package generics;

import java.io.IOException;

/**
 * This is an interface that implements a generic function.
 *
 * @param <T> generic type 1.
 * @param <R> generic type 2.
 */
public interface VFunction<T, R> {

  /**
   * This method applies the function passed in the argument.
   *
   * @param t function
   * @throws IllegalArgumentException if argument is incorrect.
   * @throws IOException              error accessing file.
   */
  void apply(T t) throws IllegalArgumentException, IOException;

}
