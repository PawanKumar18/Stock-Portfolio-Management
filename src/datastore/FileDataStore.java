package datastore;


import constant.Constant;
import file.FileIO;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * FileDataStore Implements the DataStore interface. It implements methods to read and write data to
 * and from a CSV file.
 */
public class FileDataStore implements DataStore {

  private final FileIO csv;

  /**
   * Constructor for the FileDataStore class. It initializes the type of file to be dealt with.
   *
   * @param f file to be read / written to
   */
  public FileDataStore(FileIO f) {
    csv = f;
  }

  private List<String> assignValues(String line) {
    return new ArrayList<>(Arrays.asList(line.split(",")));
  }

  private void createIfNotExist(String store) throws IOException {
    File f = new File(store);
    if (!f.exists()) {
      try {
        FileWriter ff = new FileWriter(f);
        ff.write(Constant.DATA_HEADERS.get(store));
        ff.close();
      } catch (NullPointerException ex) {
        // perform nothing
      }
    }
  }

  @Override
  public <R> List<R> filter(Map<String, String> filters, String store,
      Function<List<Map<String, String>>, List<R>> f)
      throws NoSuchElementException, IOException, IllegalArgumentException {
    this.createIfNotExist(store);
    List<Map<String, String>> out = this.csv.read(filters, new FileReader(store));
    if (out.size() == 0) {
      throw new NoSuchElementException("Queried Element not Found!");
    }
    return f.apply(out);
  }

  @Override
  public <R> void addData(List<R> v, String store, Function<List<R>, List<String>> f)
      throws IOException {
    this.createIfNotExist(store);
    List<String> stringData = f.apply(v);
    FileWriter fw = new FileWriter(store, true);
    for (String d : stringData) {
      this.csv.write("\n" + d, fw);
    }
    fw.close();

  }

  @Override
  public <R> void updateData(List<R> v, String store, Function<List<R>, List<String>> f)
      throws IOException {
    this.createIfNotExist(store);
    List<String> stringData = f.apply(v);
    for (String d : stringData) {
      this.csv.update(d.split(";")[0], store, d.split(";")[1]);
    }
  }


}
