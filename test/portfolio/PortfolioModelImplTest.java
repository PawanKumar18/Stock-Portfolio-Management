package portfolio;

import static org.junit.Assert.assertEquals;

import constant.Constant;
import datastore.DataStore;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Function;
import org.junit.Test;
import services.Service;
import stock.Stock;
import stock.StockImpl;
import user.UserImpl;

/**
 * Junit tests for PortfolioModelImpl class. It mocks the datastore class and performs the tests on
 * model.
 */
public class PortfolioModelImplTest extends AbstractTest {

  private PortfolioModel modal;
  private Service sService;

  @Test
  public void testcheckPortfolioExists() throws IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      modal = new PortfolioModelImpl(new MockDataStore(log), null);
      String testName = this.generateRandomString();
      String testUser = this.generateRandomString();
      String out = Constant.PORTFOLIO_STORE + "Portfolio:" + testName + "OwnedUser:" + testUser;
      modal.checkPortfolioExists(testName, testUser);
      assertEquals(out, log.toString());
    }
  }

  @Test
  public void testCheckStockExists() throws IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder sb = new StringBuilder();
      sService = new MockService(sb);
      StringBuilder log = new StringBuilder();
      modal = new PortfolioModelImpl(new MockDataStore(log), sService);
      String testStock = this.generateRandomString();
      String out = testStock;
      modal.checkStockExists(new StockImpl(testStock));
      assertEquals(out, sb.toString());
    }
  }

  @Test
  public void testCreatePortfolio() throws IOException {
    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      StringBuilder sb = new StringBuilder();
      sService = new MockService(sb);
      modal = new PortfolioModelImpl(new MockDataStoreException(log), sService);
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
  public void testGetPortfolioComposition() throws IOException,
      ParseException, NoSuchElementException {
    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      modal = new PortfolioModelImpl(new MockDataStore(log), null);
      String testPortfolio = this.generateRandomString();
      String testUser = this.generateRandomString();
      String out =
          Constant.PORTFOLIO_STORE + "Portfolio:" + testPortfolio + "OwnedUser:" + testUser;
      out += out;
      out += Constant.PORTFOLIO_TRANSACTIONS_STORE + "Portfolio:nullOwnedUser:null";
      try {
        modal.getPortfolioComposition(new UserImpl(testUser), testPortfolio);
      } catch (Exception e) {
        // perform nothing
      }
      assertEquals(out, log.toString());
    }
  }

  @Test
  public void testGetPortfolioValue() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      StringBuilder sb = new StringBuilder();
      sService = new MockService(sb);
      modal = new PortfolioModelImpl(new MockDataStore(log), sService);
      String testPortfolio = this.generateRandomString();
      String testUser = this.generateRandomString();
      Date d = new Date(System.currentTimeMillis());
      StringBuilder out = new StringBuilder(
          Constant.PORTFOLIO_STORE + "Portfolio:" + testPortfolio + "OwnedUser:" + testUser);
      out.append(out);
      out.append(Constant.PORTFOLIO_TRANSACTIONS_STORE + "Portfolio:nullOwnedUser:null");
      try {
        modal.getPortfolioValue(new UserImpl(testUser), testPortfolio, d);
        Map<Stock, Double> temp = modal.getPortfolioComposition(new UserImpl(testUser),
            testPortfolio);
      } catch (NullPointerException ex) {
        // perform nothing
      }
      assertEquals(out.toString(), log.toString());
    }
  }

  @Test
  public void testGetPortfolioValueNoSuchElementFound() throws IOException, ParseException {
    for (int i = 0; i < 200; i++) {
      StringBuilder log = new StringBuilder();
      StringBuilder sb = new StringBuilder();
      sService = new MockService(sb);
      modal = new PortfolioModelImpl(new MockDataStoreException(log), sService);
      String testPortfolio = this.generateRandomString();
      String testUser = this.generateRandomString();
      Date d = new Date(System.currentTimeMillis());
      String out = String.format(Constant.PORTFOLIO_STORE
              + "Portfolio:%sOwnedUser:%sPortfolio %s does not exist for the user %s",
          testPortfolio, testUser, testPortfolio, testUser);
      try {
        modal.getPortfolioValue(new UserImpl(testUser), testPortfolio, d);
      } catch (NoSuchElementException ex) {
        log.append(ex.toString().replace("java.util.NoSuchElementException: ",
            ""));
      }

      assertEquals(out, log.toString());
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

  class MockDataStoreException implements DataStore {

    private final StringBuilder log;

    MockDataStoreException(StringBuilder log) {
      this.log = log;
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