package datastore;

import static org.junit.Assert.assertEquals;

import file.FileIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;


/**
 * Junit tests for FileDataStore class. It mocks the FileIO class and performs the tests on its read
 * / write methods using it.
 */
public class FileDataStoreTest {

  private DataStore d;
  private final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


  class MockFileIO implements FileIO {

    private final StringBuilder sb;

    public MockFileIO(StringBuilder sb) {
      this.sb = sb;
    }

    @Override
    public List<Map<String, String>> read(Map<String, String> filters, InputStreamReader is)
        throws IOException {
      for (Map.Entry<String, String> fil : filters.entrySet()) {
        this.sb.append(fil.getKey()).append(":").append(fil.getValue()).append("\n");
      }

      try (Reader reader = new BufferedReader(is)) {
        int c = 0;
        while ((c = reader.read()) != -1) {
          sb.append((char) c);
        }
      }
      Map<String, String> o = new HashMap<>();
      o.put("test", "world");
      List<Map<String, String>> li = new ArrayList<>();
      li.add(o);
      return li;
    }

    @Override
    public void write(String value, FileWriter os) throws IOException {
      this.sb.append(value);
    }

    @Override
    public void update(String old, String store, String newString) throws IOException {
      this.sb.append(old).append(store).append(newString);
    }
  }

  protected String generateRandomString() {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      int t = (int) (Math.random() * 26);
      s.append(chars.charAt(t));
    }
    return s.toString();
  }

  @Test
  public void testFilterData() throws IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder logger = new StringBuilder();
      FileIO fi = new MockFileIO(logger);
      this.d = new FileDataStore(fi);
      Map<String, String> oo = new LinkedHashMap<>();
      StringBuilder curLog = new StringBuilder();

      for (int j = 0; j < Math.floor(Math.random() * 100); j++) {
        String tt = this.generateRandomString();
        String te = this.generateRandomString();
        oo.put(tt, te);
        curLog.append(tt).append(":").append(te).append("\n");
      }
      FileWriter f = new FileWriter("test.csv");
      String inp = this.generateRandomString();
      f.write(inp);
      f.close();
      this.d.filter(oo, "test.csv", x -> x);
      curLog.append(inp);

      assertEquals(logger.toString(), curLog.toString());
      Files.deleteIfExists(new File("test.csv").toPath());
    }
  }

  @Test
  public void testWriteData() throws IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder logger = new StringBuilder();
      FileIO fi = new MockFileIO(logger);
      this.d = new FileDataStore(fi);
      Map<String, String> oo = new LinkedHashMap<>();
      StringBuilder curLog = new StringBuilder();

      String p = this.generateRandomString();
      curLog.append("\n" + p);
      List<String> s = new ArrayList<>();
      s.add(p);
      this.d.addData(s, "test.csv", x -> x);

      assertEquals(logger.toString(), curLog.toString());
      Files.deleteIfExists(new File("test.csv").toPath());
    }

  }
}