package portfolio;

import static org.junit.Assert.assertEquals;

import constant.Constant;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import org.junit.Test;
import stock.Stock;
import stock.StockImpl;

/**
 * Junit tests for PortfolioViewImpl class.
 */
public class PortfolioViewImplTest extends AbstractTest {

  private PortfolioView p;

  @Test
  public void testReturnOutput() {
    Random r = new Random(200);
    for (int i = 0; i < 200; i++) {
      String temp = this.getRandomWords(r.nextInt(20));
      OutputStream out = new ByteArrayOutputStream();
      p = new PortfolioViewImpl(new PrintStream(out));
      p.returnOutput(temp);
      assertEquals(temp + "\n", out.toString());
    }
  }

  @Test
  public void testpromptMenu() {
    OutputStream out = new ByteArrayOutputStream();
    p = new PortfolioViewImpl(new PrintStream(out));
    p.promptMenu();
    assertEquals("Operations that can be performed:"
        + "\n1. Create Portfolio"
        + "\n2. View Portfolio Composition"
        + "\n3. Get Portfolio Value"
        + "\n4. Back"
        + "\n", out.toString());
  }

  @Test
  public void testShowErrorMessage() {
    OutputStream out = new ByteArrayOutputStream();
    p = new PortfolioViewImpl(new PrintStream(out));
    p.showErrorMessage("");
    assertEquals(Constant.ERROR_TAG + "\n", out.toString());
    Random r = new Random(200);
    for (int i = 0; i < 200; i++) {
      String temp = this.getRandomWords(r.nextInt(20));
      out = new ByteArrayOutputStream();
      p = new PortfolioViewImpl(new PrintStream(out));
      p.showErrorMessage(temp);
      assertEquals(Constant.ERROR_TAG + temp + "\n", out.toString());
    }
  }

  @Test
  public void testExitMessage() {
    OutputStream out = new ByteArrayOutputStream();
    p = new PortfolioViewImpl(new PrintStream(out));
    p.exitMessage("");
    assertEquals(Constant.EXIT_MESSAGE + "\n", out.toString());
    for (int i = 0; i < 200; i++) {
      String temp = this.generateRandomString();
      out = new ByteArrayOutputStream();
      p = new PortfolioViewImpl(new PrintStream(out));
      p.exitMessage(temp);
      assertEquals(Constant.EXIT_MESSAGE + temp + "\n", out.toString());
    }
  }

  @Test
  public void testAddStockMessage() {
    OutputStream out = new ByteArrayOutputStream();
    p = new PortfolioViewImpl(new PrintStream(out));
    p.addStockMessage();
    assertEquals("1. Add Stock ?"
        + "\n2. Create Portfolio with current stocks ?"
        + "\n", out.toString());
  }

  @Test
  public void testDisplayComposition() {
    Random r = new Random(200);
    for (int i = 0; i < 200; i++) {
      Map<Stock, Double> po = new LinkedHashMap<>();
      StringBuilder outMessage = new StringBuilder("Composition of Portfolio:"
          + "\nStocks\t|\tStock Counts\n");
      for (int j = 0; j < r.nextInt(50); j++) {
        String tempStock = this.generateRandomString();
        if (po.containsKey(new StockImpl(tempStock))) {
          continue;
        }
        int tempCount = r.nextInt(10000);
        po.put(new StockImpl(tempStock), (double) tempCount);
        outMessage.append(tempStock).append("\t|\t").append((double) tempCount).append("0\n");
      }
      OutputStream out = new ByteArrayOutputStream();
      p = new PortfolioViewImpl(new PrintStream(out));
      p.displayComposition(po);
      assertEquals(outMessage.toString(), out.toString());
    }
  }

  @Test
  public void testdisplayStockValue() {
    OutputStream out;
    Random r = new Random(200);
    for (int i = 0; i < 200; i++) {
      String temp = this.generateRandomString();
      Double d = r.nextDouble();
      out = new ByteArrayOutputStream();
      p = new PortfolioViewImpl(new PrintStream(out));
      p.displayStockValue(temp, d);
      assertEquals(String.format("Portfolio %s value: $%.10f\n", temp, d), out.toString());
    }
  }

  @Test
  public void testPrompt() {
    OutputStream out;
    Random r = new Random(200);
    for (int i = 0; i < 200; i++) {
      String temp = this.getRandomWords(r.nextInt(10));
      out = new ByteArrayOutputStream();
      p = new PortfolioViewImpl(new PrintStream(out));
      p.prompt(temp);
      assertEquals(temp + ": ", out.toString());
    }
  }

  @Test
  public void testdisplayWelcomeMessage() {
    OutputStream out;
    for (int i = 0; i < 200; i++) {
      String user = this.generateRandomString();
      out = new ByteArrayOutputStream();
      p = new PortfolioViewImpl(new PrintStream(out));
      p.displayWelcomeMessage(user);
      assertEquals(String.format("Hello, %s\n", user), out.toString());
    }
  }

  @Test
  public void testuserInitialMessage() {
    OutputStream out = new ByteArrayOutputStream();
    p = new PortfolioViewImpl(new PrintStream(out));
    p.enterUserMessage();
    assertEquals("1. Enter Username ? \n2. Exit\n", out.toString());
  }

  @Test
  public void testdisplayInvestedAmount() {
    OutputStream out = new ByteArrayOutputStream();
    p = new PortfolioViewImpl(new PrintStream(out));
    p.displayInvestedAmount("test", "12/12/2022", 123.456d);
    assertEquals("Total invested amount in portfolio test till date 12/12/2022 is $123.456000\n",
        out.toString());
  }

  @Test
  public void testdisplayBarGraph() {
    OutputStream out = new ByteArrayOutputStream();
    p = new PortfolioViewImpl(new PrintStream(out));
    Map<String, String> oo = new LinkedHashMap<>();
    String pfName = this.generateRandomString();
    StringBuilder exp = new StringBuilder("Performance of " + pfName + " from ");
    String st = this.generateRandomString();
    String en = this.generateRandomString();
    exp.append(st).append(" to ").append(en).append("\n");
    for (int i = 0; i < 10; i++) {
      String t = this.generateRandomString();
      String te = this.generateRandomString();
      oo.put(t, te);
      exp.append(t).append(" : ").append(te).append("\n");
    }

    int base = (int) Math.floor(Math.random() * 100);
    p.displayBarGraph(pfName, oo, st, en, base);
    exp.append("Scale: * = $").append(base).append("\n");
    assertEquals(exp.toString(), out.toString());
  }

  @Test
  public void testpromptFlexibleMenu() {
    OutputStream out = new ByteArrayOutputStream();
    p = new PortfolioViewImpl(new PrintStream(out));
    p.promptFlexibleMenu();
    assertEquals("Operations that can be performed:\n"
        + "1. Create Portfolio\n"
        + "2. View Portfolio Composition\n"
        + "3. Get Portfolio Value\n"
        + "4. Buy Portfolio Stocks\n"
        + "5. Sell Portfolio Stocks\n"
        + "6. Draw Bar Plot\n"
        + "7. Get Portfolio Invested Amount\n"
        + "8. Back\n", out.toString());
  }

}