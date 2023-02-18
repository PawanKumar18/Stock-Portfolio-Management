package datastore;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * This interface represents a way to connect to the data. It has functions to create and retrieve
 * the data to and from the given data source.
 */
public interface DataStore {

  /**
   * This method reads data from the given input source and applies the provided filters to it.
   *
   * @param filters filters to be applied to the data
   * @param store   the file from which data needs to be read
   * @param f       map that converts output of one format to another
   * @param <R>     custom data type of output
   * @return filtered data from data source
   * @throws IOException              if file not read correctly
   * @throws NoSuchElementException   if stock not present
   * @throws IllegalArgumentException error thrown when an argument is incorrect
   */
  <R> List<R> filter(Map<String, String> filters, String store,
      Function<List<Map<String, String>>, List<R>> f)
      throws NoSuchElementException, IOException, IllegalArgumentException;

  /**
   * this method adds data to a given file.
   *
   * @param v     list to be added to a file
   * @param store file to which data needs to be written to
   * @param f     map that converts output of one format to another
   * @param <R>   custom data type of input
   * @throws IOException if file not written correctly
   */
  <R> void addData(List<R> v, String store, Function<List<R>, List<String>> f) throws IOException;

  /**
   * this method updates the data to a given file.
   *
   * @param v     list ot be updated to a file
   * @param store file to which the data needs to be written to
   * @param f     map that converts output from a custom format to string list
   * @param <R>   custom data type format for input
   * @throws IOException if there is interruption when writing / reading from file
   */
  <R> void updateData(List<R> v, String store, Function<List<R>, List<String>> f)
      throws IOException;
}
