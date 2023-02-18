package generics;

/**
 * This class is a Pair class that contains method to create pairs of variables.
 *
 * @param <E> Generic type of first pair member
 * @param <T> Generic type of second pair member
 */
public class Pair<E, T> {

  private E e;
  private T t;

  /**
   * Constructor for pair class that initializes the data members to the given values.
   *
   * @param e first value of pair
   * @param t second value of pair
   */
  public Pair(E e, T t) {
    this.e = e;
    this.t = t;
  }

  /**
   * This is a getter method to get the first pair value.
   *
   * @return first pair value
   */
  public E gete() {
    return this.e;
  }

  /**
   * This is a getter method to get the second pair value.
   *
   * @return second pair value
   */
  public T gett() {
    return this.t;
  }
}
