package file;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * This interface deals with file IO operation, that is read from and write to a file.
 */
public interface FileIO {

  /**
   * This method reads from a file based on a provided filters.
   *
   * @param filters filters to read data
   * @param is      input stream
   * @return list of key value pairs with filters applied on data
   * @throws IOException error while reading file
   */
  List<Map<String, String>> read(Map<String, String> filters, InputStreamReader is)
      throws IOException;

  /**
   * This method writes to file.
   *
   * @param value data to be written to the file
   * @param os    Output Stream
   * @throws IOException error while writing to a file
   */
  void write(String value, FileWriter os) throws IOException;

  /**
   * Updates the line inside a store.
   *
   * @param old       older string to be replaced in file
   * @param store     output stream
   * @param newString new string to be replaced
   * @throws IOException error while writing to a file
   */
  void update(String old, String store, String newString) throws IOException;

}
