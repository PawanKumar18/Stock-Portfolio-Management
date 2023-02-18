package portfolio;

import static org.junit.Assert.assertEquals;

import constant.Constant;
import datastore.DataStore;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import services.Service;
import stock.Stock;
import stock.StockImpl;
import user.UserImpl;

/**
 * Junit tests for AdvancedPortfolioModelImpl class. It mocks the datastore class, Service class and
 * performs the tests on model.
 */
public class AdvancedPortfolioModelImplTest extends AbstractTest {

  private AdvancedPortfolioModelImpl modal;
  private Service sService;

  private List<Map<String, String>> oo;

  @Before
  public void begin() {
    this.oo = new ArrayList<>();
    Map<String, String> hm = new HashMap<>();
    hm.put("Date", "12/12/2021");
    hm.put("Operation", "buy");
    hm.put("CommissionFee", "123.33");
    hm.put("Count", "100.0");
    hm.put("StockName", "AAPL");
    hm.put("Period", "-1");
    oo.add(hm);
    hm = new HashMap<>();
    hm.put("Date", "5/6/2022");
    hm.put("Operation", "buy");
    hm.put("CommissionFee", "123.33");
    hm.put("Count", "100.0");
    hm.put("StockName", "GOOG");
    hm.put("Period", "-1");
    oo.add(hm);
    hm = new HashMap<>();
    hm.put("Date", "5/7/2022");
    hm.put("Operation", "sell");
    hm.put("CommissionFee", "123.33");
    hm.put("Count", "50.0");
    hm.put("StockName", "GOOG");
    oo.add(hm);

  }


  @Test
  public void testbuyStock() throws ParseException, IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      modal = new AdvancedPortfolioModelImpl(new MockDataStore(log), null);
      String testName = this.generateRandomString();
      String testUser = this.generateRandomString();
      String stk = this.generateRandomString();
      Double cnt = Math.random() * 100;
      Double cmFee = Math.random() * 100;
      String out = Constant.PORTFOLIO_STORE + "Portfolio:" + testName + "OwnedUser:" + testUser;
      out += Constant.PORTFOLIO_STORE + "Portfolio:" + testName + "OwnedUser:" + testUser;
      out += Constant.PORTFOLIO_TRANSACTIONS_STORE + testName + "," + testUser + ",FLEXIBLE," + stk
          + ",";
      out += cnt + ",buy," + Constant.convertDateToString(
          new Date(System.currentTimeMillis())) + "," + cmFee;
      try {
        modal.buyStock(testName, new UserImpl(testUser), new StockImpl(stk), cnt,
            new Date(System.currentTimeMillis()), cmFee);
      } catch (NullPointerException ex) {
        // perform nothing
      }
      assertEquals(out, log.toString());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockMoreThanExist() throws ParseException, IOException {
    StringBuilder log = new StringBuilder();
    modal = new AdvancedPortfolioModelImpl(new MockDataStore(log),
        new MockService(new StringBuilder()));
    String testName = this.generateRandomString();
    String testUser = this.generateRandomString();
    String stk = "GOOG";
    Double cnt = 200d;
    Double cmFee = Math.random() * 100;

    modal.sellStock(testName, new UserImpl(testUser), new StockImpl(stk), cnt,
        new Date(System.currentTimeMillis()), cmFee);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStock() throws ParseException, IOException {
    StringBuilder log = new StringBuilder();
    modal = new AdvancedPortfolioModelImpl(new MockDataStore(log),
        new MockService(new StringBuilder()));
    String testName = this.generateRandomString();
    String testUser = this.generateRandomString();
    String stk = this.generateRandomString();
    Double cnt = 2d;
    Double cmFee = Math.random() * 100;
    String out = Constant.PORTFOLIO_STORE + "Portfolio:" + testName + "OwnedUser:" + testUser;
    out += out;
    out +=
        Constant.PORTFOLIO_TRANSACTIONS_STORE + "Portfolio:" + testName + "OwnedUser:" + testUser;

    try {
      modal.sellStock(testName, new UserImpl(testUser), new StockImpl(stk), cnt,
          new Date(System.currentTimeMillis()), cmFee);
    } catch (NullPointerException ex) {
      // perform nothing
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockNegativeCommissionFee() throws IOException, ParseException {
    StringBuilder log = new StringBuilder();
    modal = new AdvancedPortfolioModelImpl(new MockDataStore(log),
        new MockService(new StringBuilder()));
    String testName = this.generateRandomString();
    String testUser = this.generateRandomString();
    String stk = this.generateRandomString();
    Double cnt = Math.random() * 100;
    Double cmFee = -123.4d;
    modal.sellStock(testName, new UserImpl(testUser), new StockImpl(stk), cnt,
        new Date(System.currentTimeMillis()), cmFee);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyStockNegativeCommissionFee() throws IOException, ParseException {
    StringBuilder log = new StringBuilder();
    modal = new AdvancedPortfolioModelImpl(new MockDataStore(log),
        new MockService(new StringBuilder()));
    String testName = this.generateRandomString();
    String testUser = this.generateRandomString();
    String stk = this.generateRandomString();
    Double cnt = Math.random() * 100;
    Double cmFee = -123.4d;
    modal.buyStock(testName, new UserImpl(testUser), new StockImpl(stk), cnt,
        new Date(System.currentTimeMillis()), cmFee);
  }

  @Test
  public void testCreatePortfolio() throws IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      StringBuilder sb = new StringBuilder();
      sService = new MockService(sb);
      modal = new AdvancedPortfolioModelImpl(new MockDataStoreExceptionTwo(log), sService);
      String testName = this.generateRandomString();
      String testUser = this.generateRandomString();
      StringBuilder out = new StringBuilder();
      Map<Stock, Double> po = new LinkedHashMap<>();
      Random r = new Random(System.currentTimeMillis());
      for (int j = 0; j < r.nextInt(50); j++) {
        String tempStock = this.generateRandomString();
        int tempCount = r.nextInt(10000);
        po.put(new StockImpl(tempStock), (double) tempCount);
      }
      out.append(Constant.PORTFOLIO_STORE + "Portfolio:").append(testName).append("OwnedUser:")
          .append(testUser);
      modal.createPortfolio(testName, po, new UserImpl(testUser));
      assertEquals(out.toString(), log.toString());
    }
  }

  @Test
  public void testcheckPortfolioExists() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      modal = new AdvancedPortfolioModelImpl(new MockDataStore(log), null);
      String testName = this.generateRandomString();
      String testUser = this.generateRandomString();
      String out = Constant.PORTFOLIO_STORE + "Portfolio:" + testName + "OwnedUser:" + testUser;
      try {
        modal.checkPortfolioExists(testName, testUser, new Date(System.currentTimeMillis()));
      } catch (NullPointerException ex) {
        // perform nothing
      }
      assertEquals(out, log.toString());
    }
  }

  // Period??

  @Test
  public void testmoneyInvested() throws ParseException, IOException {
    StringBuilder log = new StringBuilder();
    sService = new MockService(new StringBuilder());
    modal = new AdvancedPortfolioModelImpl(new MockDataStore(log), sService);
    String testName = this.generateRandomString();
    String testUser = this.generateRandomString();
    String out = Constant.PORTFOLIO_STORE + "Portfolio:" + testName + "OwnedUser:" + testUser;
    out +=
        Constant.PORTFOLIO_TRANSACTIONS_STORE + "StrategyType:DOLLAR_AVERAGEPortfolio:" + testName
            + "OwnedUser:" + testUser;
    out +=
        Constant.PORTFOLIO_TRANSACTIONS_STORE + "Portfolio:" + testName + "OwnedUser:" + testUser;
    Double d = modal.moneyInvested(testName, testUser, new Date(System.currentTimeMillis()));
    Date dd = new Date(System.currentTimeMillis());
    Double oout = 0d;
    for (Map<String, String> datum : oo) {
      if (dd.compareTo(Constant.convertToDate(datum.get("Date"))) >= 0) {
        if (datum.get("Operation").equals("sell")) {
          oout += Double.parseDouble(datum.get("CommissionFee"));
        } else {
          oout += Double.parseDouble(datum.get("Count")) * 123.1d + Double.parseDouble(
              datum.get("CommissionFee"));
        }
      }
    }
    assertEquals(out, log.toString());
    assertEquals(oout, d);
  }

  @Test
  public void testPortfolioPerformance() throws ParseException, IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      modal = new AdvancedPortfolioModelImpl(new MockDataStore(log), null);
      String testName = this.generateRandomString();
      String testUser = this.generateRandomString();
      String out = Constant.PORTFOLIO_STORE + "Portfolio:" + testName + "OwnedUser:" + testUser;
      try {
        modal.getPortfolioPerformance(new UserImpl(testUser), testName,
            new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000),
            new Date(System.currentTimeMillis()));
      } catch (IllegalArgumentException ex) {
        // perform nothing
      }
      assertEquals(out, log.toString());
    }
  }

  @Test
  public void testgetPortfolioComposition() throws ParseException, IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      modal = new AdvancedPortfolioModelImpl(new MockDataStore(log), null);
      String testName = this.generateRandomString();
      String testUser = this.generateRandomString();
      String out = Constant.PORTFOLIO_STORE + "Portfolio:" + testName + "OwnedUser:" + testUser;
      out +=
          Constant.PORTFOLIO_TRANSACTIONS_STORE + "StrategyType:DOLLAR_AVERAGEPortfolio:" + testName
              + "OwnedUser:" + testUser;

      try {
        modal.getPortfolioComposition(new UserImpl(testUser), testName,
            new Date(System.currentTimeMillis()));
      } catch (NullPointerException ex) {
        // perform nothing
      }
      assertEquals(out, log.toString());
    }
  }

  @Test
  public void testCheckStockExists() throws IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder sb = new StringBuilder();
      sService = new MockService(sb);
      StringBuilder log = new StringBuilder();
      modal = new AdvancedPortfolioModelImpl(new MockDataStore(log), sService);
      String testStock = this.generateRandomString();
      String out = testStock;
      modal.checkStockExists(new StockImpl(testStock));
      assertEquals(out, sb.toString());
    }
  }

  @Test
  public void testGetPortfolioValueNoSuchElementFound() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      StringBuilder sb = new StringBuilder();
      sService = new MockService(sb);
      modal = new AdvancedPortfolioModelImpl(new MockDataStoreException(log), sService);
      String testPortfolio = this.generateRandomString();
      String testUser = this.generateRandomString();
      Date d = new Date(System.currentTimeMillis());
      String out = String.format("Portfolio %s does not exist for the user %s",
          testPortfolio, testUser);
      try {
        modal.getPortfolioValue(new UserImpl(testUser), testPortfolio, d);
      } catch (NoSuchElementException ex) {
        log.append(ex.toString().replace("java.util.NoSuchElementException: ",
            ""));
      }

      assertEquals(out, log.toString());
    }
  }

  @Test
  public void testCreatePortfolioFixedStrategy() {

    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      StringBuilder sb = new StringBuilder();
      sService = new MockService(sb);
      modal = new AdvancedPortfolioModelImpl(new MockDataStoreException(log), sService);
      String testPortfolio = this.generateRandomString();
      String testUser = this.generateRandomString();
      Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
      Date endDate = new Date(System.currentTimeMillis());
      Map<Stock, Double> stock = new LinkedHashMap<>();
      double commissionFee = 10.4;
      long period = -1;
      Double moneyInvested = Math.random() * 10000 + 1;
      double percent = 100;
      String out = Constant.PORTFOLIO_STORE + testPortfolio + "," + testUser + ","
          + Constant.convertDateToString(startDate) + "," + "FLEXIBLE";
      out += Constant.PORTFOLIO_TRANSACTIONS_STORE;
      while (percent > 0) {
        double num = Math.random() * 100 + 1;
        if (percent - num < 0) {
          num = percent;
        }
        String stockName = this.generateRandomString();
        stock.put(new StockImpl(stockName), num);
        percent -= num;
        out += testPortfolio + "," + testUser
            + ",FLEXIBLE," + stockName + "," + num + "," + "buy" + ","
            + Constant.convertDateToString(startDate) + "," + commissionFee + ","
            + "FIXED_VALUE" + "," + moneyInvested + "," + Constant.convertDateToString(endDate)
            + "," + period + ","
            + "0.0";
      }

      try {
        modal.createStrategyPortfolio(new UserImpl(testUser), testPortfolio, stock, commissionFee,
            startDate,
            endDate, "FIXED_VALUE", period, moneyInvested, "FLEXIBLE");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      assertEquals(out, log.toString());
    }
  }

  @Test
  public void testCreatePortfolioDollarStrategy() {

    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      StringBuilder sb = new StringBuilder();
      sService = new MockService(sb);
      modal = new AdvancedPortfolioModelImpl(new MockDataStoreException(log), sService);
      String testPortfolio = this.generateRandomString();
      String testUser = this.generateRandomString();
      Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
      Date endDate = new Date(System.currentTimeMillis());
      Map<Stock, Double> stock = new LinkedHashMap<>();
      double commissionFee = 10.4;
      long period = (long) Math.random() * 100 + 1;
      Double moneyInvested = Math.random() * 10000 + 11;
      double percent = 100;
      String out = Constant.PORTFOLIO_STORE + testPortfolio + "," + testUser + ","
          + Constant.convertDateToString(startDate) + "," + "FLEXIBLE";
      out += Constant.PORTFOLIO_TRANSACTIONS_STORE;
      while (percent > 0) {
        double num = Math.random() * 100 + 1;
        if (percent - num < 0) {
          num = percent;
        }
        String stockName = this.generateRandomString();
        stock.put(new StockImpl(stockName), num);
        percent -= num;
        out += testPortfolio + "," + testUser
            + ",FLEXIBLE," + stockName + "," + num + "," + "buy" + ","
            + Constant.convertDateToString(startDate) + "," + commissionFee + ","
            + "DOLLAR_STRATEGY" + "," + moneyInvested + "," + Constant.convertDateToString(endDate)
            + "," + period + ","
            + "0.0";
      }

      try {
        modal.createStrategyPortfolio(new UserImpl(testUser), testPortfolio, stock, commissionFee,
            startDate,
            endDate, "DOLLAR_STRATEGY", period, moneyInvested, "FLEXIBLE");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      assertEquals(out, log.toString());
    }
  }

  @Test
  public void testBuyStocksFixedStrategy() {

    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      StringBuilder sb = new StringBuilder();
      sService = new MockService(sb);
      modal = new AdvancedPortfolioModelImpl(new MockDataStoreException(log), sService);
      String testPortfolio = this.generateRandomString();
      String testUser = this.generateRandomString();
      Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
      Date endDate = new Date(System.currentTimeMillis());
      Map<Stock, Double> stock = new LinkedHashMap<>();
      double commissionFee = 10.4;
      long period = -1;
      Double moneyInvested = Math.random() * 10000 + 1;
      double percent = 100;
      String out = Constant.PORTFOLIO_STORE + testPortfolio + "," + testUser + ","
          + Constant.convertDateToString(startDate) + "," + "FLEXIBLE";
      out += Constant.PORTFOLIO_TRANSACTIONS_STORE;
      while (percent > 0) {
        double num = Math.random() * 100 + 1;
        num = Double.parseDouble(String.format("%.2f", num));
        if (percent - num < 0) {
          num = percent;
        }
        String stockName = this.generateRandomString();
        stock.put(new StockImpl(stockName), num);
        out += testPortfolio + "," + testUser
            + ",FLEXIBLE," + stockName + "," + num + ",buy,"
            + Constant.convertDateToString(startDate) + "," + commissionFee + ","
            + "FIXED_VALUE" + "," + moneyInvested + "," + Constant.convertDateToString(endDate)
            + "," + period + ","
            + "0.0";
        percent -= num;
      }

      try {
        modal.createStrategyPortfolio(new UserImpl(testUser), testPortfolio, stock, commissionFee,
            startDate,
            endDate, "FIXED_VALUE", period, moneyInvested, "FLEXIBLE");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      assertEquals(out, log.toString());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyStocksFixedStrategyCommissionFeeGreater() {

    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      StringBuilder sb = new StringBuilder();
      sService = new MockService(sb);
      modal = new AdvancedPortfolioModelImpl(new MockDataStoreException(log), sService);
      String testPortfolio = this.generateRandomString();
      String testUser = this.generateRandomString();
      Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
      Date endDate = new Date(System.currentTimeMillis());
      Map<Stock, Double> stock = new HashMap<Stock, Double>();
      double commissionFee = 10000;
      long period = -1;
      Double moneyInvested = Math.random() * 100 + 1;
      double percent = 100;
      String out = Constant.PORTFOLIO_STORE + testPortfolio + "," + testUser + ","
          + Constant.convertDateToString(startDate) + "," + "FLEXIBLE";
      out += Constant.PORTFOLIO_TRANSACTIONS_STORE;
      while (percent > 0) {
        double num = Math.random() * 100 + 1;
        if (percent - num < 0) {
          num = percent;
        }
        String stockName = this.generateRandomString();
        stock.put(new StockImpl(stockName), num);
        percent -= num;
        out += testPortfolio + "," + testUser + ","
            + ",FLEXIBLE," + "," + stockName + "," + "0.0" + "," + "buy" + ","
            + Constant.convertDateToString(startDate) + "," + "," + commissionFee + ","
            + "FIXED_VALUE" + "," + moneyInvested + "," + Constant.convertDateToString(endDate)
            + "," + period + ","
            + "0.0";
      }

      try {
        modal.createStrategyPortfolio(new UserImpl(testUser), testPortfolio, stock, commissionFee,
            startDate,
            endDate, "FIXED_VALUE", period, moneyInvested, "FLEXIBLE");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    }
  }

  class MockDataStore implements DataStore {

    private final StringBuilder log;
    private final Portfolio rand;

    MockDataStore(StringBuilder log) {
      this.log = log;
      this.rand = null;
    }

    protected String generateRandomString() {
      StringBuilder s = new StringBuilder();
      for (int i = 0; i < 10; i++) {
        int t = (int) (Math.random() * 26);
        s.append(chars.charAt(t));
      }
      return s.toString();
    }

    @Override
    public <R> List<R> filter(Map<String, String> filters, String store,
        Function<List<Map<String, String>>, List<R>> f)
        throws NoSuchElementException, IOException, IllegalArgumentException {
      if (store == null) {
        throw new NoSuchElementException("No Such element found");
      }
      this.log.append(store);
      for (Map.Entry<String, String> fil : filters.entrySet()) {
        this.log.append(fil.getKey()).append(":").append(fil.getValue());
      }
      Map<String, String> hm = new HashMap<>();
      List<Map<String, String>> o = new ArrayList<>();

      if (store.equals(Constant.PORTFOLIO_TRANSACTIONS_STORE)) {
        return f.apply(oo);
      } else {
        hm.put("Portfolio", "reitrement portfolio");
        hm.put("OwnedUser", "testing");
        hm.put("CreationDate", "11/15/2021");
        hm.put("Type", "FLEXIBLE");
      }
      o.add(hm);
      return f.apply(o);
    }

    @Override
    public <R> void addData(List<R> v, String store, Function<List<R>, List<String>> f)
        throws IOException {
      List<String> o = f.apply(v);
      this.log.append(store);
      for (String s : o) {
        this.log.append(s);
      }
    }

    @Override
    public <R> void updateData(List<R> v, String store, Function<List<R>, List<String>> f)
        throws IOException {
      //perform nothing
    }
  }

  class MockService implements Service {

    private final StringBuilder sb;

    public MockService(StringBuilder sb) {
      this.sb = sb;
    }

    @Override
    public boolean checkStockExists(String s) throws IOException {
      this.sb.append(s);
      return false;
    }

    @Override
    public Double getStockValue(Stock s, Date d) throws NoSuchElementException, IOException {
      this.sb.append(s);
      this.sb.append(d);
      return 123.1d;
    }

    @Override
    public String getStockData(Stock s) throws NoSuchElementException, IOException {
      this.sb.append(s);
      return "";
    }
  }

  class MockDataStoreException implements DataStore {

    private final StringBuilder log;

    MockDataStoreException(StringBuilder log) {
      this.log = log;
    }

    @Override
    public <R> List<R> filter(Map<String, String> filters, String store,
        Function<List<Map<String, String>>, List<R>> f)
        throws NoSuchElementException, IOException, IllegalArgumentException {
      throw new NoSuchElementException("");
    }

    @Override
    public <R> void addData(List<R> v, String store, Function<List<R>, List<String>> f)
        throws IOException {
      List<String> o = f.apply(v);
      this.log.append(store);
      for (String s : o) {
        String[] oo = s.split(",");
        if (oo.length == 13) {
          this.log.append(String.join(",", Arrays.copyOf(oo, oo.length - 1)) + ",0.0");
        } else {
          this.log.append(s);
        }
      }
    }

    @Override
    public <R> void updateData(List<R> v, String store, Function<List<R>, List<String>> f)
        throws IOException {
      // perform nothing
    }
  }

  class MockDataStoreExceptionTwo implements DataStore {

    private StringBuilder log;

    public MockDataStoreExceptionTwo(StringBuilder bb) {
      this.log = bb;
    }

    @Override
    public <R> List<R> filter(Map<String, String> filters, String store,
        Function<List<Map<String, String>>, List<R>> f)
        throws NoSuchElementException, IOException, IllegalArgumentException {
      if (store == null) {
        throw new NoSuchElementException("No Such element found");
      }
      this.log.append(store);
      for (Map.Entry<String, String> fil : filters.entrySet()) {
        this.log.append(fil.getKey()).append(":").append(fil.getValue());
      }
      Map<String, String> hm = new HashMap<>();
      List<Map<String, String>> o = new ArrayList<>();

      if (store.equals(Constant.PORTFOLIO_TRANSACTIONS_STORE)) {
        return f.apply(oo);
      } else {
        hm.put("Portfolio", "reitrement portfolio");
        hm.put("OwnedUser", "testing");
        hm.put("CreationDate", "11/15/2021");
        hm.put("Type", "FLEXIBLE");
      }
      o.add(hm);
      throw new NoSuchElementException("");
    }

    @Override
    public <R> void addData(List<R> v, String store, Function<List<R>, List<String>> f)
        throws IOException {
      // perform nothing
    }

    @Override
    public <R> void updateData(List<R> v, String store, Function<List<R>, List<String>> f)
        throws IOException {
      // perform nothing
    }
  }
}