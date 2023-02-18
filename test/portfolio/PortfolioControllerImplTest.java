package portfolio;

import static org.junit.Assert.assertEquals;
import static portfolio.PortfolioStrategy.DOLLAR_AVERAGE;
import static portfolio.PortfolioStrategy.FIXED_VALUE;

import constant.Constant;
import generics.Pair;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import stock.Stock;
import user.User;

/**
 * Junit tests for PortfolioControllerImpl class that mocks View and Modal for perform unit-testing
 * on the controller.
 */
public class PortfolioControllerImplTest extends AbstractTest {

  InputStream in;
  ByteArrayOutputStream bytes;
  PortfolioFacadeModel model;
  StringBuilder modelLog;
  StringBuilder viewLog;
  MockView view;

  CommandController controller;

  @Before
  public void setUp() throws Exception {
    bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    viewLog = new StringBuilder();
    view = new MockView(viewLog);
    modelLog = new StringBuilder();
    model = new MockModel(modelLog);
  }

  @Test
  public void testInputToCreateInFlexiblePortfolio() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {
      Map<String, Long> stock = new HashMap<String, Long>();
      for (int k = 0; k < 1000; k++) {
        stock.put(this.generateRandomString(), (long) (Math.random() * 1000 + 1));
      }
      StringBuilder input = new StringBuilder(
          "1\r" + userName + "\r2\r" + "1" + "\r" + portfolioName[0]);
      StringBuilder expectedLog = new StringBuilder();
      System.out.println(input);
      expectedLog.append("Portfolio Name: ").append(portfolioName[i]);
      expectedLog.append("\nUser Name: ").append(userName);
      expectedLog.append("\nPortfolio Name: ").append(portfolioName[i]);
      expectedLog.append("\nUser Name: ").append(userName);
      int count = 0;
      for (Map.Entry<String, Long> entry : stock.entrySet()) {
        if (count != 0) {
          input.append("\r").append("1");
        }
        count++;
        expectedLog.append("\nStock: ").append(entry.getKey()).append("\t")
            .append((float) entry.getValue());
        input.append("\r").append(entry.getKey()).append("\r").append(entry.getValue());
      }
      input.append("\r").append("2").append("\r").append("4\r").append("3");
      in = new ByteArrayInputStream(input.toString().getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog.toString(), modelLog.toString());
    }
  }

  @Test
  public void testInputToCreateFlexiblePortfolio() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {
      Map<String, Long> stock = new HashMap<String, Long>();
      float commissionFee = 10f;
      for (int k = 0; k < 1000; k++) {
        stock.put(this.generateRandomString(), (long) (Math.random() * 1000 + 1));
      }
      StringBuilder input = new StringBuilder(
          "1\r" + userName + "\r1\r" + "1" + "\r" + portfolioName[0] + "\r3\r"
              + Constant.convertDateToString(new Date(System.currentTimeMillis())) + "\r");
      StringBuilder expectedLog = new StringBuilder();
      System.out.println(input);
      expectedLog.append("Portfolio Name: ").append(portfolioName[i]);
      expectedLog.append("\nUser Name: ").append(userName);
      expectedLog.append("\nPortfolio Name: ").append(portfolioName[i]);
      expectedLog.append("\nUser Name: ").append(userName);
      int count = 0;
      for (Map.Entry<String, Long> entry : stock.entrySet()) {
        if (count != 0) {
          input.append("\r").append("1");
        }
        count++;
        expectedLog.append("\nStock: ").append(entry.getKey()).append("\t")
            .append((float) entry.getValue()).append("\t").append(commissionFee);
        input.append("\r").append(entry.getKey()).append("\r").append(entry.getValue())
            .append("\r").append(commissionFee);
      }
      input.append("\r").append("2").append("\r").append("8\r").append("3");
      in = new ByteArrayInputStream(input.toString().getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog.toString(), modelLog.toString());
    }
  }

  @Test
  public void testGetFlexiblePortfolioComposition() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {

      String expectedLog = "Portfolio Name: " + portfolioName[i]
          + "\nUser Name: " + userName
          + "\nDate: " + "10/11/2022";
      String input = "1\r" + userName + "\r1\r" + "2" + "\r" + portfolioName[0] + "\r10/11/2022"
          + "\r" + "8\r" + "3";
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog, modelLog.toString());
    }
  }

  @Test
  public void testGetInFlexiblePortfolioComposition() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {

      String expectedLog = "Portfolio Name: " + portfolioName[i]
          + "\nUser Name: " + userName;

      in = new ByteArrayInputStream(
          ("1\r" + userName + "\r2\r" + "2" + "\r" + portfolioName[0] + "\r" + "4\r"
              + "3").getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog, modelLog.toString());
    }
  }

  @Test
  public void testGetFlexiblePortfolioValue() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {

      String expectedLog = "Portfolio Name: " + portfolioName[i]
          + "\nUser Name: " + userName
          + "\nDate: " + "10/11/2022";
      String input = "1\r" + userName + "\r1\r" + "3" + "\r" + portfolioName[0] + "\r10/11/2022"
          + "\r" + "8\r" + "3";
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog, modelLog.toString());
    }
  }

  @Test
  public void testGetInFlexiblePortfolioValue() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {

      String expectedLog = "Portfolio Name: " + portfolioName[i]
          + "\nUser Name: " + userName
          + "\nDate: " + "10/11/2022";
      String input = "1\r" + userName + "\r2\r" + "3" + "\r" + portfolioName[0] + "\r10/11/2022"
          + "\r" + "4\r" + "3";
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog, modelLog.toString());
    }
  }

  @Test
  public void testBuyStocks() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {

      String expectedLog = "Portfolio Name: " + portfolioName[i]
          + "\nUser Name: " + userName
          + "\nStock: aapl"
          + "\nStock count: 16.0"
          + "\nDate: " + "10/11/2022"
          + "\nCommission: " + "10.15";

      String input = "1\r" + userName + "\r1\r" + "4" + "\r" + portfolioName[0] + "\r3" + "\raapl"
          + "\r16"
          + "\r10/11/2022"
          + "\r10.15"
          + "\r" + "8\r" + "3";
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog, modelLog.toString());
    }
  }

  @Test
  public void testSellStocks() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {

      String expectedLog = "Portfolio Name: " + portfolioName[i]
          + "\nUser Name: " + userName
          + "\nStock: aapl"
          + "\nStock count: 16.0"
          + "\nDate: " + "10/11/2022"
          + "\nCommission: " + "10.15";

      String input = "1\r" + userName + "\r1\r" + "5" + "\r" + portfolioName[0] + "\raapl"
          + "\r16"
          + "\r10/11/2022"
          + "\r10.15"
          + "\r" + "8\r" + "3";
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog, modelLog.toString());
    }
  }

  @Test
  public void testMoneyInvested() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {

      String expectedLog = "Portfolio Name: " + portfolioName[i]
          + "\nUser Name: " + userName
          + "\nDate: " + "10/11/2022";

      String input = "1\r" + userName + "\r1\r" + "7" + "\r" + portfolioName[0] + "\r10/11/2022"
          + "\r" + "8\r" + "3";
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog, modelLog.toString());
    }
  }

  @Test
  public void testPerformanceGraph() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {

      String expectedLog = "Portfolio Name: " + portfolioName[i]
          + "\nUser Name: " + userName
          + "\nStart Date: " + "10/11/2022"
          + "\nEnd Date: " + "11/16/2022";

      String input = "1\r" + userName + "\r1\r" + "6" + "\r" + portfolioName[0] + "\r10/11/2022"
          + "\r11/16/2022"
          + "\r" + "8\r" + "3";
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog, modelLog.toString());
    }
  }

  @Test
  public void testInputToCreatePortfolioIncorrectOption() {
    for (int i = 0; i < 200; i++) {
      String input = "1\rJohn" + "\r" + (int) (Math.random() * 100 + 5) + "\r" + "3";
      in = new ByteArrayInputStream(input.getBytes());
      viewLog = new StringBuilder();
      controller = new PortfolioControllerImpl(new MockView(viewLog), model, in);
      controller.operate();
      assertEquals("Value entered is out of the specified numbers!", viewLog.toString());
    }
  }

  @Test
  public void testInputToCreatePortfolioIncorrectUserName() {
    String input = "1\r\rJohn\r3";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new PortfolioControllerImpl(view, model, in);
    controller.operate();
    assertEquals(" Username cannot be empty!", viewLog.toString());
  }

  @Test
  public void testInputToCreatePortfolioIncorrectPortfolioName() {
    String input = "1\rJohn\r" + "2" + "\r" + "2" + "\r" + "" + "\rqwe" + "\r4\r3";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new PortfolioControllerImpl(view, model, in);
    controller.operate();
    assertEquals(" Portfolio name cannot be empty!", viewLog.toString());
  }

  @Test
  public void testInputToCreatePortfolioIncorrectStockValueString() {
    for (int i = 0; i < 200; i++) {
      String input = "1\rJohn\r" + "2\r" + "1" + "\r" + "portfolio1" + "\r" + "TSLA" + "\r-"
          + this.generateRandomString() + "\r1\r2\r" + "4\r"
          + "3";
      viewLog = new StringBuilder();
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(new MockView(viewLog), model, in);
      controller.operate();
      assertEquals(" Stock count has to be positive and non-fractional!", viewLog.toString());
    }
  }

  @Test
  public void testInputToCreatePortfolioIncorrectStockValueNegative() {
    for (int i = 0; i < 200; i++) {
      Random r = new Random(200);
      String input =
          "1\rJohn\r" + "2\r" + "1" + "\r" + "portfolio1" + "\r" + "TSLA" + "\r-" + r.nextInt(1000)
              + "\r1\r2\r" + "4\r3";
      viewLog = new StringBuilder();
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(new MockView(viewLog), model, in);
      controller.operate();
      assertEquals(" Stock count has to be positive and non-fractional!", viewLog.toString());
    }
  }

  @Test
  public void testInputToCreatePortfolioIncorrectStockValueFractional() {
    for (int i = 0; i < 200; i++) {
      double val = (Math.random() * 100 + 2);
      String input =
          "1\rJohn\r" + "2\r" + "1" + "\r" + "portfolio1" + "\r" + "TSLA" + "\r" + val
              + "\r1\r2\r" + "4\r3";
      viewLog = new StringBuilder();
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(new MockView(viewLog), model, in);
      controller.operate();
      assertEquals(" Stock count has to be positive and non-fractional!", viewLog.toString());
    }
  }

  @Test
  public void testInputToCreatePortfolioIncorrectDate1() {
    for (int i = 0; i < 200; i++) {
      viewLog = new StringBuilder();
      String date =
          this.generateRandomString() + "/" + (Math.random() * 100)
              + "/" + (Math.random() * 10000);
      String input =
          "1\rJohn\r" + "2\r" + "3" + "\r" + "portfolio1\r" + date + "\r11/16/2022" + "\r4\r3";
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(new MockView(viewLog), model, in);
      controller.operate();
      assertEquals(" Entered Date is invalid!", viewLog.toString());
    }
  }

  @Test
  public void testInputToCreatePortfolioIncorrectDate2() {
    for (int i = 0; i < 200; i++) {
      viewLog = new StringBuilder();
      String input =
          "1\rJohn\r" + "2\r" + "3" + "\r" + "portfolio1\r" + "11/16/2024" + "\r11/16/2022"
              + "\r4\r3";
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(new MockView(viewLog), model, in);
      controller.operate();
      assertEquals(" Date cannot be in future!", viewLog.toString());
    }
  }

  @Test
  public void testCreatePortfolioReEnterSameStockInvalidInput() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      modelLog = new StringBuilder();
      viewLog = new StringBuilder();
      StringBuilder st = new StringBuilder("Portfolio Name: portfolio1\nUser Name: "
          + "John\nPortfolio Name: portfolio1\nUser Name: John\nStock: ");
      String stock = this.generateRandomString();
      int c1 = (int) (Math.random() * 100 + 1);
      int c2 = (int) (Math.random() * 100 + 1);
      st.append(stock).append("\t").append((float) c2);
      String input = "1\rJohn\r2\r1\rportfolio1\r" + stock + "\r" + c1 + "\r1\r" + stock + "\r" + c2
          + "\rxxx\rY\r2\r4\r3";
      in = new ByteArrayInputStream(input.getBytes());
      controller = new PortfolioControllerImpl(new MockView(viewLog), new MockModel(modelLog), in);
      controller.operate();
      assertEquals("Value must be Y/N", viewLog.toString());
      assertEquals(st.toString(), modelLog.toString());
    }
  }

  @Test
  public void testCreatePortfolioFixedValueStrategy() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {
      Map<String, Double> stock = new HashMap<String, Double>();
      double percent = 100;
      while (percent > 0) {
        double num = (Math.random() * 100 + 1);
        num = Double.parseDouble(String.format("%.2f", num));
        if (percent - num < 0) {
          num = percent;
        }
        stock.put(this.generateRandomString(), num);
        percent -= num;
      }
      double totalAmt = Math.random() * 1000 + 1;
      double commissionFee = 10.4;
      String date = "10/10/2010";
      StringBuilder input = new StringBuilder(
          "1\r" + userName + "\r1\r" + "1" + "\r" + portfolioName[0] + "\r1" + "\r" + totalAmt
              + "\r" + commissionFee
              + "\r" + date);
      StringBuilder expectedLog = new StringBuilder();
      System.out.println(input);
      expectedLog.append("Portfolio Name: ").append(portfolioName[i]);
      expectedLog.append("\nUser Name: ").append(userName);
      int count = 0;
      for (Map.Entry<String, Double> entry : stock.entrySet()) {
        count++;
        expectedLog.append("\nStock: ").append(entry.getKey()).append("\t")
            .append(entry.getValue());
        input.append("\r").append(entry.getKey()).append("\r").append(entry.getValue());
      }
      input.append("\r").append("8\r").append("3");
      expectedLog.append("\nCommission: " + commissionFee);
      expectedLog.append("\nStart Date: " + date);
      expectedLog.append("\nPeriod: " + "-1");
      expectedLog.append("\nMoney Invested: " + totalAmt);
      expectedLog.append("\nPortfolio Type: " + PortfolioType.FLEXIBLE);
      in = new ByteArrayInputStream(input.toString().getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog.toString(), modelLog.toString());
    }
  }

  @Test
  public void testCreatePortfolioDollarCostAveragingStrategy() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {
      Map<String, Double> stock = new HashMap<String, Double>();
      double percent = 100;
      while (percent > 0) {
        double num = Math.random() * 100 + 1;
        num = Double.parseDouble(String.format("%.2f", num));
        if (percent - num < 0) {
          num = percent;
        }
        stock.put(this.generateRandomString(), num);
        percent -= num;
      }
      double totalAmt = Math.random() * 1000 + 1;
      double commissionFee = 10.4;
      String startDate = "10/10/2010";
      String endDate = "10/10/2030";
      String period = "30";
      StringBuilder input = new StringBuilder(
          "1\r" + userName + "\r1\r" + "1" + "\r" + portfolioName[0] + "\r2"
              + "\r" + totalAmt + "\r" + commissionFee
              + "\r" + startDate + "\r" + endDate + "\r" + period);
      StringBuilder expectedLog = new StringBuilder();
      System.out.println(input);
      expectedLog.append("Portfolio Name: ").append(portfolioName[i]);
      expectedLog.append("\nUser Name: ").append(userName);
      int count = 0;
      for (Map.Entry<String, Double> entry : stock.entrySet()) {
        count++;
        expectedLog.append("\nStock: ").append(entry.getKey()).append("\t")
            .append(entry.getValue());
        input.append("\r").append(entry.getKey()).append("\r").append(entry.getValue());
      }
      input.append("\r").append("8\r").append("3");
      expectedLog.append("\nCommission: " + commissionFee);
      expectedLog.append("\nStart Date: " + startDate);
      expectedLog.append("\nEnd Date: " + endDate);
      expectedLog.append("\nPeriod: " + period);
      expectedLog.append("\nMoney Invested: " + totalAmt);
      expectedLog.append("\nPortfolio Type: " + PortfolioType.FLEXIBLE);
      in = new ByteArrayInputStream(input.toString().getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog.toString(), modelLog.toString());
    }
  }

  @Test
  public void testBuyStocksFixedValueStrategy() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {
      Map<String, Double> stock = new HashMap<String, Double>();
      double percent = 100;
      while (percent > 0) {
        double num = Math.random() * 100 + 1;
        num = Double.parseDouble(String.format("%.2f", num));
        if (percent - num < 0) {
          num = percent;
        }
        stock.put(this.generateRandomString(), num);
        percent -= num;
      }
      double totalAmt = Math.random() * 1000 + 1;
      double commissionFee = 10.4;
      String date = "10/10/2010";
      StringBuilder input = new StringBuilder(
          "1\r" + userName + "\r1\r" + "4" + "\r" + portfolioName[0] + "\r1" + "\r" + totalAmt
              + "\r" + commissionFee
              + "\r" + date);
      StringBuilder expectedLog = new StringBuilder();
      System.out.println(input);
      expectedLog.append("Portfolio Name: ").append(portfolioName[i]);
      expectedLog.append("\nUser Name: ").append(userName);
      int count = 0;
      for (Map.Entry<String, Double> entry : stock.entrySet()) {
        count++;
        expectedLog.append("\nStock: ").append(entry.getKey()).append("\t")
            .append(entry.getValue());
        input.append("\r").append(entry.getKey()).append("\r").append(entry.getValue());
      }
      input.append("\r").append("8\r").append("3");
      expectedLog.append("\nCommission: " + commissionFee);
      expectedLog.append("\nStart Date: " + date);
      expectedLog.append("\nPeriod: " + "-1");
      expectedLog.append("\nMoney Invested: " + totalAmt);
      expectedLog.append("\nPortfolio Type: " + FIXED_VALUE);
      in = new ByteArrayInputStream(input.toString().getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog.toString(), modelLog.toString());
    }
  }


  @Test
  public void testbuyStockDollarCostAveragingStrategy() {
    String userName = "John";
    String[] portfolioName = {"portfolio1"};
    int nPortfolio = portfolioName.length;
    for (int i = 0; i < nPortfolio; i++) {
      Map<String, Double> stock = new HashMap<String, Double>();
      double percent = 100;
      while (percent > 0) {
        double num = Math.random() * 100 + 1;
        num = Double.parseDouble(String.format("%.2f", num));
        if (percent - num < 0) {
          num = percent;
        }
        stock.put(this.generateRandomString(), num);
        percent -= num;
      }
      double totalAmt = Math.random() * 1000 + 1;
      double commissionFee = 10.4;
      String startDate = "10/10/2010";
      String endDate = "10/10/2030";
      String period = "30";
      StringBuilder input = new StringBuilder(
          "1\r" + userName + "\r1\r" + "4" + "\r" + portfolioName[0] + "\r2"
              + "\r" + totalAmt + "\r" + commissionFee
              + "\r" + startDate + "\r" + endDate + "\r" + period);
      StringBuilder expectedLog = new StringBuilder();
      System.out.println(input);
      expectedLog.append("Portfolio Name: ").append(portfolioName[i]);
      expectedLog.append("\nUser Name: ").append(userName);
      int count = 0;
      for (Map.Entry<String, Double> entry : stock.entrySet()) {
        count++;
        expectedLog.append("\nStock: ").append(entry.getKey()).append("\t")
            .append(entry.getValue());
        input.append("\r").append(entry.getKey()).append("\r").append(entry.getValue());
      }
      input.append("\r").append("8\r").append("3");
      expectedLog.append("\nCommission: " + commissionFee);
      expectedLog.append("\nStart Date: " + startDate);
      expectedLog.append("\nEnd Date: " + endDate);
      expectedLog.append("\nPeriod: " + period);
      expectedLog.append("\nMoney Invested: " + totalAmt);
      expectedLog.append("\nPortfolio Type: " + DOLLAR_AVERAGE);
      in = new ByteArrayInputStream(input.toString().getBytes());
      controller = new PortfolioControllerImpl(view, model, in);
      controller.operate();
      assertEquals(expectedLog.toString(), modelLog.toString());
    }
  }

  class MockModel implements PortfolioFacadeModel {

    private final StringBuilder log;

    public MockModel(StringBuilder log) {
      this.log = log;
    }

    @Override
    public PortfolioType getPortfolioType(String name, User user) throws IOException {
      this.log.append("Portfolio Name: " + name);
      this.log.append("\nUser Name: " + user.getName() + "\n");
      return null;
    }

    @Override
    public void createUnFlexiblePortfolio(String name, Map<Stock, Double> s, User user)
        throws IOException {
      this.log.append("Portfolio Name: " + name);
      this.log.append("\nUser Name: " + user.getName());
      for (Map.Entry<Stock, Double> stock : s.entrySet()) {
        this.log.append("\nStock: " + stock.getKey().getTicker() + "\t" + stock.getValue());
      }
    }

    @Override
    public void createFlexiblePortfolio(String name, Map<Stock, Pair<Double, Double>> s, User user,
        Date d)
        throws IOException {
      this.log.append("Portfolio Name: " + name);
      this.log.append("\nUser Name: " + user.getName());
      for (Map.Entry<Stock, Pair<Double, Double>> stock : s.entrySet()) {
        this.log.append("\nStock: " + stock.getKey().getTicker() + "\t" + stock.getValue().gete()
            + "\t" + stock.getValue().gett());
      }
    }

    @Override
    public Map<Stock, Double> getFlexiblePortfolioComposition(User user, String portfolioName,
        Date d) throws IOException, ParseException, NoSuchElementException {
      this.log.append("Portfolio Name: " + portfolioName);
      this.log.append("\nUser Name: " + user.getName());
      this.log.append("\nDate: " + Constant.convertDateToString(d));
      return new HashMap<>();
    }

    @Override
    public Map<Stock, Double> getUnFlexiblePortfolioComposition(User user, String portfolioName)
        throws IOException, ParseException, NoSuchElementException {
      this.log.append("Portfolio Name: " + portfolioName);
      this.log.append("\nUser Name: " + user.getName());
      return new HashMap<>();
    }

    @Override
    public Double getFlexiblePortfolioValue(User user, String portfolioName, Date date)
        throws IOException, ParseException {
      this.log.append("Portfolio Name: " + portfolioName);
      this.log.append("\nUser Name: " + user.getName());
      this.log.append("\nDate: " + Constant.convertDateToString(date));
      return 0.0;
    }

    @Override
    public Double getUnFlexiblePortfolioValue(User user, String portfolioName, Date date)
        throws IOException, ParseException {
      this.log.append("Portfolio Name: " + portfolioName);
      this.log.append("\nUser Name: " + user.getName());
      this.log.append("\nDate: " + Constant.convertDateToString(date));
      return 0.0;
    }

    @Override
    public boolean checkStockExists(Stock stock) throws IOException {
      // this.log.append(stock);
      return true;
    }

    @Override
    public boolean checkPortfolioExists(String name, String user, Date d, PortfolioType type)
        throws IOException, ParseException {
      // this.log.append(name).append(user).append(d).append(type.name());
      return false;
    }


    @Override
    public void buyStock(String name, User user, Stock stock, Double count, Date d,
        Double commissionFee) throws IOException, NoSuchElementException, ParseException {
      this.log.append("Portfolio Name: " + name);
      this.log.append("\nUser Name: " + user.getName());
      this.log.append("\nStock: " + stock.getTicker());
      this.log.append("\nStock count: " + count);
      this.log.append("\nDate: " + Constant.convertDateToString(d));
      this.log.append("\nCommission: " + commissionFee);

    }

    @Override
    public void sellStock(String name, User user, Stock stock, Double count, Date d,
        Double commissionFee) throws IOException, ParseException {
      this.log.append("Portfolio Name: " + name);
      this.log.append("\nUser Name: " + user.getName());
      this.log.append("\nStock: " + stock.getTicker());
      this.log.append("\nStock count: " + count);
      this.log.append("\nDate: " + Constant.convertDateToString(d));
      this.log.append("\nCommission: " + commissionFee);
    }

    @Override
    public double moneyInvested(String name, String user, Date d)
        throws IOException, ParseException {
      this.log.append("Portfolio Name: " + name);
      this.log.append("\nUser Name: " + user);
      this.log.append("\nDate: " + Constant.convertDateToString(d));
      return 0.0;
    }

    @Override
    public Map<Long, Double> getPortfolioPerformance(User user, String name, Date sDate, Date eDate)
        throws ParseException, IOException {
      this.log.append("Portfolio Name: " + name);
      this.log.append("\nUser Name: " + user.getName());
      this.log.append("\nStart Date: " + Constant.convertDateToString(sDate));
      this.log.append("\nEnd Date: " + Constant.convertDateToString(eDate));
      return new HashMap<>();
    }

    @Override
    public boolean createStrategyPortfolio(User user, String portfolioName,
        Map<Stock, Double> stocks, Double commissionFee, Date st, Date en, String strategyType,
        long period, double moneyInvested, PortfolioType type) throws IOException {
      this.log.append("Portfolio Name: " + portfolioName);
      this.log.append("\nUser Name: " + user.getName());
      for (Map.Entry<Stock, Double> stock : stocks.entrySet()) {
        this.log.append("\nStock: " + stock.getKey().getTicker() + "\t" + stock.getValue());
      }
      this.log.append("\nCommission: " + commissionFee);
      this.log.append("\nStart Date: " + Constant.convertDateToString(st));
      if (en != null) {
        this.log.append("\nEnd Date: " + Constant.convertDateToString(en));
      }
      this.log.append("\nPeriod: " + period);
      this.log.append("\nMoney Invested: " + moneyInvested);
      this.log.append("\nPortfolio Type: " + type);

      return true;
    }

    @Override
    public void buyStrategyStock(User user, String portfolioName, Map<Stock, Double> stocks,
        Double commissionFee, Date st, Date en, String strategyType,
        long period, double moneyInvested) throws IOException, ParseException {
      this.log.append("Portfolio Name: " + portfolioName);
      this.log.append("\nUser Name: " + user.getName());
      for (Map.Entry<Stock, Double> stock : stocks.entrySet()) {
        this.log.append("\nStock: " + stock.getKey().getTicker() + "\t" + stock.getValue());
      }
      this.log.append("\nCommission: " + commissionFee);
      this.log.append("\nStart Date: " + Constant.convertDateToString(st));
      if (en != null) {
        this.log.append("\nEnd Date: " + Constant.convertDateToString(en));
      }
      this.log.append("\nPeriod: " + period);
      this.log.append("\nMoney Invested: " + moneyInvested);
      this.log.append("\nPortfolio Type: " + strategyType);

    }
  }

  class MockView implements PortfolioView {

    StringBuilder log;

    public MockView(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void returnOutput(String out) {
      // return the output.
    }

    @Override
    public void promptMenu() {
      // show the menu
    }

    @Override
    public void promptFlexibleMenu() {
      // perform nothing
    }

    @Override
    public void showErrorMessage(String message) {
      this.log.append(message);
    }

    @Override
    public void exitMessage(String username) {
      // display on exit
    }

    @Override
    public void addStockMessage() {
      // prompt to add stock
    }

    @Override
    public void displayComposition(Map<Stock, Double> stocks) {
      // perform nothing
    }

    @Override
    public void displayStockValue(String portfolio, Double value) {
      // displays the stock value
    }

    @Override
    public void prompt(String s) {
      // prompts for some input
    }

    @Override
    public void displayWelcomeMessage(String user) {
      // prints the welcome message once username provided
    }

    @Override
    public void enterUserMessage() {
      // prints the message on entry
    }

    @Override
    public void displayInvestedAmount(String name, String date, Double amount) {
      // perform nothing
    }

    @Override
    public void displayBarGraph(String pfName, Map<String, String> data, String start, String end,
        int base) {
      // perform nothing
    }

    @Override
    public void displayStrategy() {
      // perform nothing
    }

  }

}

