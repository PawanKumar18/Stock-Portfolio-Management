package stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Date;
import java.util.Random;
import org.junit.Test;

/**
 * Junit tests for StockImpl class.
 */
public class StockImplTest {

  private Stock st;
  private final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  protected String generateRandomString() {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      int t = (int) (Math.random() * 26);
      s.append(chars.charAt(t));
    }
    return s.toString();
  }

  @Test
  public void testGetTicker() {
    for (int i = 0; i < 200; i++) {
      String t = this.generateRandomString();
      st = new StockImpl(t);
      assertEquals(t, st.getTicker());
    }
  }

  @Test
  public void testEquals() {
    for (int i = 0; i < 200; i++) {
      String t = this.generateRandomString();
      st = new StockImpl(t);
      Stock st2 = new StockImpl(t);
      assertEquals(st, st2);
    }
  }

  @Test
  public void testNotEquals() {
    Random r = new Random(200);
    for (int i = 0; i < 200; i++) {
      st = new StockImpl(this.generateRandomString());
      Stock st2 = new StockImpl(this.generateRandomString());
      if (st.equals(st2)) {
        continue;
      }
      assertNotEquals(st, st2);
    }
  }

  @Test
  public void testHashCodeEquals() {
    for (int i = 0; i < 200; i++) {
      String t = this.generateRandomString();
      st = new StockImpl(t);
      Stock st2 = new StockImpl(t);
      assertEquals(st2.hashCode(), st.hashCode());
    }
  }

  @Test
  public void testHashCodeNotEquals() {
    for (int i = 0; i < 200; i++) {
      st = new StockImpl(this.generateRandomString());
      Stock st2 = new StockImpl(this.generateRandomString());
      if (st.equals(st2)) {
        continue;
      }
      assertNotEquals(st2.hashCode(), st.hashCode());
    }
  }

  @Test
  public void testToString() {
    for (int i = 0; i < 200; i++) {
      String t = this.generateRandomString();
      st = new StockImpl(t);
      assertEquals(t, st.toString());
    }
  }

  @Test
  public void testGetSetValue() {
    String t = this.generateRandomString();
    for (int i = 0; i < 200; i++) {
      st = new StockImpl(t);
      Double d = Math.random() * 10000;
      st.setValue(d);
      assertEquals(d, st.getValue());
    }
  }

  @Test
  public void testGetSetDate() {
    String t = this.generateRandomString();
    for (int i = 0; i < 200; i++) {
      st = new StockImpl(t);
      Date d = new Date(System.currentTimeMillis());
      st.setDate(d);
      assertEquals(d, st.getDate());
    }
  }

}