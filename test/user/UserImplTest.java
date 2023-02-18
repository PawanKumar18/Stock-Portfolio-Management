package user;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Junit tests for UserImpl class.
 */
public class UserImplTest {

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
  public void testGetter() {
    User user;
    for (int i = 0; i < 200; i++) {
      String t = this.generateRandomString();
      user = new UserImpl(t);
      assertEquals(t, user.getName());
    }
  }

}