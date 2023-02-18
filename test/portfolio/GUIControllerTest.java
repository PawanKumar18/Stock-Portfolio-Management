package portfolio;

import static org.junit.Assert.assertEquals;

import constant.Constant;
import generics.Pair;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;
import stock.Stock;
import user.User;

/**
 * Junit Tests for GUI Controller. It mocks Model and View to test GUI Controller.
 */
public class GUIControllerTest extends AbstractTest {

  GUIFeatures guiController;
  ByteArrayOutputStream bytes;
  PortfolioFacadeModel model;
  StringBuilder modelLog;
  StringBuilder viewLog;
  GUIView view;
  StringBuilder expectedLog;

  String userName = "John";

  @Before
  public void setUp() throws Exception {

    bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    viewLog = new StringBuilder();
    view = new MockViewGUI(viewLog);
    modelLog = new StringBuilder();
    model = new MockModel(modelLog);
    guiController = new GUIController(model, view);
  }

  @Test
  public void testGetUser() {

    expectedLog = new StringBuilder();
    String user = this.generateRandomString();
    expectedLog.append("User Name: " + user);
    guiController.getUser(user);

    assertEquals(expectedLog.toString(), viewLog.toString());

  }

  @Test
  public void portfolioComposition() {

    expectedLog = new StringBuilder();
    String portfolioName = this.generateRandomString();
    String user = this.generateRandomString();
    String date = "11/29/2022";
    guiController.getUser(user);
    expectedLog.append("Portfolio Name: " + portfolioName);
    expectedLog.append("\nUser Name: " + user);
    guiController.portfolioComposition(portfolioName, date);
    expectedLog.append("\nDate: " + date);
    assertEquals(expectedLog.toString(), modelLog.toString());
  }

  @Test
  public void portfolioValue() {
    expectedLog = new StringBuilder();
    String portfolioName = this.generateRandomString();
    String user = this.generateRandomString();
    String date = "11/29/2022";
    guiController.getUser(user);
    expectedLog.append("Portfolio Name: " + portfolioName);
    expectedLog.append("\nUser Name: " + user);
    guiController.portfolioValue(portfolioName, date);
    expectedLog.append("\nDate: " + date);
    assertEquals(expectedLog.toString(), modelLog.toString());
  }


  @Test
  public void operateFlexibleBuyInputs() {
    expectedLog = new StringBuilder();

    String portfolioName = this.generateRandomString();
    String user = this.generateRandomString();
    expectedLog.append("Portfolio Name: " + portfolioName);
    expectedLog.append("\nUser Name: " + user);

    String stock = this.generateRandomString();
    expectedLog.append("\nStock: " + stock);
    String count = "16.0";
    expectedLog.append("\nStock count: " + count);

    String date = "10/11/2022";
    expectedLog.append("\nDate: " + date);
    String commissionFee = "10.15";
    expectedLog.append("\nCommission: " + commissionFee);

    guiController.getUser(user);

    guiController.operateFlexibleBuyInputs(portfolioName, stock, count, date, commissionFee);

    assertEquals(expectedLog.toString(), modelLog.toString());
  }

  @Test
  public void operateFlexibleSellInputs() {

    expectedLog = new StringBuilder();

    String portfolioName = this.generateRandomString();
    String user = this.generateRandomString();
    expectedLog.append("Portfolio Name: " + portfolioName);
    expectedLog.append("\nUser Name: " + user);

    String stock = this.generateRandomString();
    expectedLog.append("\nStock: " + stock);
    String count = "16.0";
    expectedLog.append("\nStock count: " + count);

    String date = "10/11/2022";
    expectedLog.append("\nDate: " + date);
    String commissionFee = "10.15";
    expectedLog.append("\nCommission: " + commissionFee);

    guiController.getUser(user);

    guiController.operateFlexibleSellInputs(portfolioName, stock, count, date, commissionFee);

    assertEquals(expectedLog.toString(), modelLog.toString());
  }

  @Test
  public void operateInvestedAmount() {

    expectedLog = new StringBuilder();
    String portfolioName = this.generateRandomString();
    String user = this.generateRandomString();
    String date = "11/29/2022";
    guiController.getUser(user);
    expectedLog.append("Portfolio Name: " + portfolioName);
    expectedLog.append("\nUser Name: " + user);
    guiController.operateInvestedAmount(portfolioName, date);
    expectedLog.append("\nDate: " + date);
    assertEquals(expectedLog.toString(), modelLog.toString());

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

  class MockViewGUI implements GUIView {

    StringBuilder log;

    public MockViewGUI(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void returnOutput(String out) {
      // perform nothing
    }

    @Override
    public void promptMenu() {
      // perform nothing
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
      // perform nothing
    }

    @Override
    public void addStockMessage() {
      // perform nothing
    }

    @Override
    public void displayComposition(Map<Stock, Double> stocks) {
      // perform nothing
    }

    @Override
    public void displayStockValue(String portfolio, Double value) {
      // perform nothing
    }

    @Override
    public void prompt(String s) {
      // perform nothing
    }

    @Override
    public void displayWelcomeMessage(String user) {
      this.log.append("User Name: " + user);
    }

    @Override
    public void enterUserMessage() {
      // perform nothing
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

    @Override
    public void addStocks() {
      // perform nothing
    }

    @Override
    public void addFeatures(GUIFeatures features) {
      // perform nothing
    }
  }
}

