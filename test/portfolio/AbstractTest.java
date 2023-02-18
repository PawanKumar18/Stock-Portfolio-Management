package portfolio;

import java.util.Random;

/**
 * An abstract class that stores all the common methods required across the tests in the module.
 */
public abstract class AbstractTest {

  protected String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  protected String generateRandomString() {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      int t = (int) (Math.random() * 26);
      s.append(chars.charAt(t));
    }
    return s.toString();
  }

  protected String getRandomWords(int n) {
    StringBuilder rs = new StringBuilder();
    Random random = new Random(n);
    for (int i = 0; i < 1; i++) {
      char[] word = new char[random.nextInt(8) + 3];
      for (int j = 0; j < word.length; j++) {
        word[j] = (char) ('a' + random.nextInt(26));
      }
      rs.append(" ").append(word);
    }
    return rs.toString();
  }

}