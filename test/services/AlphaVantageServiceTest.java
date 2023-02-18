package services;

import static org.junit.Assert.assertEquals;

import file.FileIO;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Junit Test for AlphaVantage service that fetches the sotcks data.
 */
public class AlphaVantageServiceTest {

  private ArrayList<String> stocks;
  private final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private Service serv;

  private FileIO sv;

  class MockFIleIO implements FileIO {

    private final StringBuilder sb;

    public MockFIleIO(StringBuilder sb) {
      this.sb = sb;
    }

    @Override
    public List<Map<String, String>> read(Map<String, String> filters, InputStreamReader is)
        throws IOException {
      for (Map.Entry<String, String> filter : filters.entrySet()) {
        sb.append(filter.getKey()).append("=").append(filter.getValue()).append("\n");
      }

      try (Reader reader = new BufferedReader(is)) {
        int c = 0;
        while ((c = reader.read()) != -1) {
          sb.append((char) c);
        }
      }
      return null;
    }

    @Override
    public void write(String value, FileWriter os) throws IOException {
      this.sb.append(value);
    }

    @Override
    public void update(String old, String store, String newString) throws IOException {
      //perform nothing
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

  @Before
  public void setup() {
    this.stocks = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      this.stocks.add(this.generateRandomString());
    }

  }

  @Test
  public void testStockExists() throws IOException {
    StringBuilder logger = new StringBuilder();
    StringBuilder curLog = new StringBuilder();
    this.sv = new MockFIleIO(logger);
    this.serv = new AlphaVantageService(this.sv);

    Map<String, String> oo = new HashMap<>();
    for (int i = 0; i < this.stocks.size(); i++) {
      oo.put("Name", this.stocks.get(i));
      String tt = this.generateRandomString();
      this.sv.read(oo, new InputStreamReader(new ByteArrayInputStream(tt.getBytes())));
      curLog.append("Name=").append(this.stocks.get(i)).append("\n");
      curLog.append(tt);
    }

    assertEquals(logger.toString(), curLog.toString());
  }

  @Test
  public void testGetStockValue() throws IOException {
    StringBuilder logger = new StringBuilder();
    StringBuilder curLog = new StringBuilder();
    this.sv = new MockFIleIO(logger);
    this.serv = new AlphaVantageService(this.sv);
    Map<String, String> oo = new HashMap<>();
    for (int i = 0; i < this.stocks.size(); i++) {
      oo.put("Name", this.stocks.get(i));
      String tt = this.generateRandomString();
      this.sv.read(oo, new InputStreamReader(new ByteArrayInputStream(tt.getBytes())));
      curLog.append("Name=").append(this.stocks.get(i)).append("\n");
      curLog.append(tt);
    }

    assertEquals(logger.toString(), curLog.toString());
  }


}