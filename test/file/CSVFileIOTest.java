package file;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Junit tests to read or write the given csv file. It creates a test csv file for doing so.
 */
public class CSVFileIOTest {

  private final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private FileIO f;
  private final List<Map<String, String>> log = new ArrayList<>();

  protected String generateRandomString() {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      int t = (int) (Math.random() * 26);
      s.append(chars.charAt(t));
    }
    return s.toString();
  }

  @Before
  public void setup() throws IOException, ParseException {
    this.f = new CSVFileIO();
    File pFile = new File("test_portfolio.csv");
    Files.deleteIfExists(pFile.toPath());

    FileWriter fw = new FileWriter(pFile);
    Map<String, String> hm = new HashMap<String, String>();
    String a = this.generateRandomString();
    String b = this.generateRandomString();
    String c = this.generateRandomString();
    String d = this.generateRandomString();
    hm.put(a, c);
    hm.put(b, d);
    String o = a + "," + b + "\n";
    o += c + "," + d;

    this.log.add(hm);
    fw.write(o);
    fw.close();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCorrectlyFormatted() throws IOException {
    this.f = new CSVFileIO();
    Map<String, String> hmm = new HashMap<>();
    hmm.put(this.generateRandomString(), this.generateRandomString());
    List<Map<String, String>> ss = this.f.read(hmm,
        new InputStreamReader(new FileInputStream("test_portfolio.csv")));
  }

  @Test
  public void testRead() throws IOException {
    this.f = new CSVFileIO();
    List<Map<String, String>> ss = this.f.read(new HashMap<String, String>(),
        new InputStreamReader(new FileInputStream("test_portfolio.csv")));
    assertEquals(this.log.toString(), ss.toString());
  }

  @Test
  public void testWrite() throws IOException {
    this.f = new CSVFileIO();
    String s = this.generateRandomString();
    FileWriter fw = new FileWriter("test_portfolio.csv");
    this.f.write(s, fw);
    fw.close();
    Path p = Path.of(new File("test_portfolio.csv").getPath());
    assertEquals(s, Files.readString(p, Charset.defaultCharset()));
  }

  @After
  public void after() throws IOException {
    File pFile = new File("test_portfolio.csv");
    Files.deleteIfExists(pFile.toPath());
  }


}