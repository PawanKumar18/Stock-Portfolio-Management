package portfolio;

import static org.junit.Assert.assertEquals;

import constant.Constant;
import datastore.DataStore;
import generics.Pair;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.function.Function;
import org.junit.Test;
import services.Service;
import stock.Stock;
import stock.StockImpl;
import user.User;
import user.UserImpl;

/**
 * Junit tests for Facade model. This class tests the functianlity of the facade model that takes in
 * a Flexible and InFlexible mock models as input.
 */
public class PortfolioFacadeModelImplTest extends AbstractTest {

  private AdvancedPortfolioModel adv;
  private PortfolioModel pm;
  private Service sv;
  private DataStore ds;

  private PortfolioFacadeModel pfm;

  @Test
  public void testGetPortfolioType() throws IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();
      pfm.getPortfolioType(name, new UserImpl(user));

      assertEquals(Constant.PORTFOLIO_STORE + "Portfolio:" + name + "OwnedUser:" + user,
          pds.toString());
    }
  }

  @Test
  public void testCreateFlexiblePortfolio() throws IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();
      String stock = this.generateRandomString();
      out += name + "\n" + user + "\n" + stock + "\n";
      Map<Stock, Pair<Double, Double>> dd = new HashMap<>();
      Double cnt = Math.random() * 100;
      Double cmFee = Math.random() * 100;
      dd.put(new StockImpl(stock), new Pair<>(cnt, cmFee));
      pfm.createFlexiblePortfolio(name, dd, new UserImpl(user),
          new Date(System.currentTimeMillis()));
      out += cmFee + "\n" + cnt + "\n";

      assertEquals(out, padv.toString());
    }
  }

  @Test
  public void testCreateUnFlexiblePortfolio() throws IOException {

    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();
      String stock = this.generateRandomString();
      out += name + "\n" + user + "\n" + stock + "\n";
      Map<Stock, Double> dd = new HashMap<>();
      Double cnt = Math.random() * 100;
      dd.put(new StockImpl(stock), cnt);
      pfm.createUnFlexiblePortfolio(name, dd, new UserImpl(user));
      out += cnt + "\n";

      assertEquals(out, ppm.toString());
    }
  }

  @Test
  public void testGetFlexiblePortfolioComposition() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();
      Date dd = new Date(System.currentTimeMillis());
      out += user + name + dd;
      pfm.getFlexiblePortfolioComposition(new UserImpl(user), name, dd);

      assertEquals(out, padv.toString());
    }
  }

  @Test
  public void testGetUnFlexiblePortfolioComposition() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockTwoDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();
      out += user + name;
      pfm.getUnFlexiblePortfolioComposition(new UserImpl(user), name);

      assertEquals(out, ppm.toString());
    }
  }

  @Test
  public void testGetFlexiblePortfolioValue() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();
      Date dd = new Date(System.currentTimeMillis());
      out += user + name + dd;
      pfm.getFlexiblePortfolioValue(new UserImpl(user), name, dd);

      assertEquals(out, padv.toString());
    }
  }

  @Test
  public void testGetUnFlexiblePortfolioValue() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockTwoDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();
      Date dd = new Date(System.currentTimeMillis());
      out += user + name + dd;
      pfm.getUnFlexiblePortfolioValue(new UserImpl(user), name, dd);

      assertEquals(out, ppm.toString());
    }
  }

  @Test
  public void testCheckStockExists() throws IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockTwoDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      out += name;
      pfm.checkStockExists(new StockImpl(name));

      assertEquals(out, psv.toString());
    }
  }

  @Test
  public void testCheckPortfolioExists() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockTwoDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();
      Date dd = new Date(System.currentTimeMillis());
      out += name + "\n" + user + "\n";
      pfm.checkPortfolioExists(name, user, dd, PortfolioType.FLEXIBLE);
      assertEquals(out, ppm.toString());
    }
  }

  @Test
  public void testBuyStock() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();
      String stock = this.generateRandomString();
      Date dd = new Date(System.currentTimeMillis());
      Double cnt = Math.floor(Math.random() * 100);
      Double cmFee = Math.random() * 100;
      out += name + user + stock + cnt + dd + cmFee;
      pfm.buyStock(name, new UserImpl(user), new StockImpl(stock), cnt, dd, cmFee);
      assertEquals(out, padv.toString());
    }
  }

  @Test
  public void testSellStock() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();
      String stock = this.generateRandomString();
      Date dd = new Date(System.currentTimeMillis());
      Double cnt = Math.floor(Math.random() * 100);
      Double cmFee = Math.random() * 100;
      out += name + user + stock + cnt + dd + cmFee;
      pfm.sellStock(name, new UserImpl(user), new StockImpl(stock), cnt, dd, cmFee);
      assertEquals(out, padv.toString());
    }
  }

  @Test
  public void testMoneyInvested() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();

      Date dd = new Date(System.currentTimeMillis());

      out += name + user + dd;
      pfm.moneyInvested(name, user, dd);
      assertEquals(out, padv.toString());
    }
  }

  @Test
  public void testGetPortfolioPerformance() throws ParseException, IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder padv = new StringBuilder();
      StringBuilder ppm = new StringBuilder();
      StringBuilder psv = new StringBuilder();
      StringBuilder pds = new StringBuilder();
      String out = "";
      adv = new MockFlexiblePortfolio(padv);
      pm = new MockInFlexibleModel(ppm);
      sv = new MockService(psv);
      ds = new MockDataStore(pds);
      pfm = new PortfolioFacadeModelImpl(pm, adv, ds, sv);

      String name = this.generateRandomString();
      String user = this.generateRandomString();

      Date dd = new Date(System.currentTimeMillis() - 1000L * 24 * 60 * 60 * 30);
      Date ed = new Date(System.currentTimeMillis() - 1000L * 24 * 60 * 60 * 30);

      out += user + name + dd + ed;
      pfm.getPortfolioPerformance(new UserImpl(user), name, dd, ed);
      assertEquals(out, padv.toString());
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
      if (filters.containsKey("StockName")) {
        hm.put("Portfolio", "reitrement portfolio");
        hm.put("OwnedUser", "testing");
        hm.put("Type", "FLEXIBLE");
        hm.put("StockName", "tsla");
        hm.put("Operation", "buy");
        hm.put("Count", "100.0");
        hm.put("Date", "11/15/2022");
        hm.put("Count", "121.20");
        o.add(hm);
        hm = new HashMap<>();

        hm.put("Portfolio", "reitrement portfolio");
        hm.put("OwnedUser", "testing");
        hm.put("Type", "FLEXIBLE");
        hm.put("StockName", "googl");
        hm.put("Operation", "buy");
        hm.put("Count", "100.0");
        hm.put("Date", "11/15/2022");
        hm.put("Count", "121.20");
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
      // perform nothing
    }
  }

  class MockTwoDataStore implements DataStore {

    private final StringBuilder log;
    private final Portfolio rand;

    MockTwoDataStore(StringBuilder log) {
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
      if (filters.containsKey("StockName")) {
        hm.put("Portfolio", "reitrement portfolio");
        hm.put("OwnedUser", "testing");
        hm.put("Type", "UNFLEXIBLE");
        hm.put("StockName", "tsla");
        hm.put("Operation", "buy");
        hm.put("Count", "100.0");
        hm.put("Date", "11/15/2022");
        hm.put("Count", "121.20");
        o.add(hm);
        hm = new HashMap<>();

        hm.put("Portfolio", "reitrement portfolio");
        hm.put("OwnedUser", "testing");
        hm.put("Type", "UNFLEXIBLE");
        hm.put("StockName", "googl");
        hm.put("Operation", "buy");
        hm.put("Count", "100.0");
        hm.put("Date", "11/15/2022");
        hm.put("Count", "121.20");
      } else {
        hm.put("Portfolio", "reitrement portfolio");
        hm.put("OwnedUser", "testing");
        hm.put("CreationDate", "11/15/2021");
        hm.put("Type", "UNFLEXIBLE");
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
      // perform nothing
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
      return 12.4d;
    }

    @Override
    public String getStockData(Stock s) throws NoSuchElementException, IOException {
      this.sb.append(s);
      return "";
    }
  }

  class MockInFlexibleModel implements PortfolioModel {

    private final StringBuilder mfp;

    public MockInFlexibleModel(StringBuilder sb) {
      this.mfp = sb;
    }

    @Override
    public boolean createPortfolio(String name, Map<Stock, Double> s, User user)
        throws IOException {
      this.mfp.append(name).append("\n");
      this.mfp.append(user.getName()).append("\n");
      for (Entry<Stock, Double> ent : s.entrySet()) {
        this.mfp.append(ent.getKey().getTicker()).append("\n");
        this.mfp.append(ent.getValue()).append("\n");
      }
      return false;
    }

    @Override
    public Map<Stock, Double> getPortfolioComposition(User user, String portfolioName)
        throws IOException, ParseException, NoSuchElementException {
      this.mfp.append(user.getName());
      this.mfp.append(portfolioName);
      return null;
    }

    @Override
    public Double getPortfolioValue(User user, String portfolioName, Date date)
        throws IOException, ParseException {
      this.mfp.append(user.getName());
      this.mfp.append(portfolioName);
      this.mfp.append(date);
      return 1.2345d;
    }

    @Override
    public boolean checkStockExists(Stock stock) throws IOException {
      return false;
    }

    @Override
    public boolean checkPortfolioExists(String name, String user) throws IOException {
      this.mfp.append(name).append("\n");
      this.mfp.append(user).append("\n");
      return true;
    }

    @Override
    public Portfolio getPortfolio(String name, String user) throws IOException {
      return null;
    }

    @Override
    public List<Portfolio> mapCSVToPortfolio(List<Map<String, String>> dbInput)
        throws ParseException, IOException {
      return null;
    }
  }

  class MockFlexiblePortfolio extends MockInFlexibleModel implements AdvancedPortfolioModel {

    private final StringBuilder mfp;

    public MockFlexiblePortfolio(StringBuilder sb) {
      super(sb);
      this.mfp = sb;
    }

    @Override
    public boolean createPortfolio(String name, User user, Map<Stock, Pair<Double, Double>> s,
        Date d)
        throws IOException {
      this.mfp.append(name).append("\n");
      this.mfp.append(user.getName()).append("\n");
      for (Map.Entry<Stock, Pair<Double, Double>> ent : s.entrySet()) {
        this.mfp.append(ent.getKey().getTicker()).append("\n");
        this.mfp.append(ent.getValue().gett()).append("\n");
        this.mfp.append(ent.getValue().gete()).append("\n");
      }
      return false;
    }

    @Override
    public void buyStock(String name, User user, Stock stock, Double count, Date d,
        Double commissionFee) throws IOException, NoSuchElementException, ParseException {
      this.mfp.append(name);
      this.mfp.append(user.getName());
      this.mfp.append(stock.getTicker());
      this.mfp.append(count);
      this.mfp.append(d);
      this.mfp.append(commissionFee);
    }

    @Override
    public void sellStock(String name, User user, Stock stock, Double count, Date d,
        Double commissionFee) throws IOException, ParseException {
      this.mfp.append(name);
      this.mfp.append(user.getName());
      this.mfp.append(stock.getTicker());
      this.mfp.append(count);
      this.mfp.append(d);
      this.mfp.append(commissionFee);
    }

    @Override
    public boolean checkPortfolioExists(String name, String user, Date d)
        throws IOException, ParseException {
      this.mfp.append(name);
      this.mfp.append(user);
      this.mfp.append(d);
      return true;
    }

    @Override
    public double moneyInvested(String name, String user, Date d)
        throws IOException, ParseException {
      this.mfp.append(name);
      this.mfp.append(user);
      this.mfp.append(d);
      return 0;
    }

    @Override
    public Map<Long, Double> getPortfolioPerformance(User user, String name, Date sDate, Date eDate)
        throws ParseException, IOException {
      this.mfp.append(user.getName());
      this.mfp.append(name);
      this.mfp.append(sDate);
      this.mfp.append(eDate);
      return null;
    }

    @Override
    public Map<Stock, Double> getPortfolioComposition(User user, String portfolioName, Date d)
        throws IOException, ParseException {
      this.mfp.append(user.getName());
      this.mfp.append(portfolioName);
      this.mfp.append(d);
      return null;
    }

    @Override
    public boolean createStrategyPortfolio(User user, String portfolioName,
        Map<Stock, Double> stocks, Double commissionFee, Date st, Date en, String strategyType,
        long period, double moneyInvested, String portfolioType) throws IOException {
      return false;
    }

    @Override
    public void addRecurringEntries(User user, String portfolioName, Date d)
        throws IOException, ParseException {
      // perform nothing
    }

    @Override
    public void buyStrategyStock(User user, String portfolioName, Map<Stock, Double> stocks,
        Double commissionFee, Date st, Date en, String strategyType, long period,
        double moneyInvested, String portfolioType) throws IOException, ParseException {
      // perform nothing
    }

    @Override
    public Double getPortfolioValue(User user, String portfolioName, Date date)
        throws IOException, ParseException {
      this.mfp.append(user.getName());
      this.mfp.append(portfolioName);
      this.mfp.append(date);
      return 1.2345d;
    }

    @Override
    public boolean checkStockExists(Stock stock) throws IOException {
      return false;
    }

    @Override
    public Portfolio getPortfolio(String name, String user) throws IOException {
      return null;
    }

    @Override
    public List<Portfolio> mapCSVToPortfolio(List<Map<String, String>> dbInput)
        throws ParseException, IOException {
      return null;
    }
  }

}