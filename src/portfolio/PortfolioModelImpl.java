package portfolio;

import constant.Constant;
import datastore.DataStore;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import services.Service;
import stock.Stock;
import stock.StockImpl;
import user.User;
import user.UserImpl;

/**
 * PortfolioModelImpl class implements the PortfolioModel. It access dataStore to fetch data of the
 * portfolio and stockModel to fetch stock value. It creates a portfolio, fetches value and
 * composition of the portfolio.
 */
public class PortfolioModelImpl implements PortfolioModel {

  private final DataStore dataStore;
  private final Service sModal;

  /**
   * Constructor for PortfolioModelImpl class. It initializes the datastore and sModel to the given
   * values
   *
   * @param d Datastore where the query/update operations to be performed
   */
  public PortfolioModelImpl(DataStore d, Service serv) {
    dataStore = d;
    sModal = serv;
  }

  @Override
  public boolean checkStockExists(Stock stock) throws IOException {
    return this.sModal.checkStockExists(stock.getTicker());
  }

  private Map<String, String> getPortfolioDefaultFilters(String name, String user) {
    Map<String, String> out = new HashMap<>();
    out.put("Portfolio", name);
    out.put("OwnedUser", user);
    return out;
  }

  @Override
  public boolean checkPortfolioExists(String name, String user) throws IOException {
    try {
      return this.dataStore.filter(this.getPortfolioDefaultFilters(name, user),
          Constant.PORTFOLIO_STORE, x -> x) != null;
    } catch (NoSuchElementException ex) {
      return false;
    }
  }

  @Override
  public Portfolio getPortfolio(String name, String user)
      throws IOException, NoSuchElementException {
    return this.dataStore.filter(this.getPortfolioDefaultFilters(name, user),
        Constant.PORTFOLIO_STORE, x -> {
          try {
            return this.mapCSVToPortfolio(x);
          } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
          }
        }).get(0);
  }

  @Override
  public List<Portfolio> mapCSVToPortfolio(List<Map<String, String>> dbInput)
      throws ParseException, IOException, NoSuchElementException {
    List<Portfolio> out = new ArrayList<>();
    for (Map<String, String> inp : dbInput) {
      Map<Stock, Double> values = this.dataStore.filter(
          this.getPortfolioDefaultFilters(inp.get("Portfolio"), inp.get("OwnedUser")),
          Constant.PORTFOLIO_TRANSACTIONS_STORE, x -> {
            Map<Stock, Double> v = new HashMap<>();
            for (Map<String, String> st : x) {
              v.put(new StockImpl(st.get("StockName")), Double.parseDouble(st.get("Count")));
            }
            List<Map<Stock, Double>> o = new ArrayList<>();
            o.add(v);
            return o;
          }).get(0);
      out.add(new PortfolioImpl(inp.get("Portfolio"), new UserImpl(inp.get("OwnedUser")), values,
          Constant.convertToDate(inp.get("CreationDate"))));
    }
    return out;
  }

  @Override
  public boolean createPortfolio(String name, Map<Stock, Double> stocks, User user)
      throws IOException, NoSuchElementException {
    if (this.checkPortfolioExists(name, user.getName())) {
      throw new IllegalArgumentException("Portfolio already exists!");
    }
    List<String> out = new ArrayList<>();
    out.add(name + "," + user.getName() + "," + Constant.convertDateToString(
        new Date(System.currentTimeMillis())) + "," + PortfolioType.UNFLEXIBLE.name());
    this.dataStore.addData(out, Constant.PORTFOLIO_STORE, x -> x);
    List<Map<Stock, Double>> l = new ArrayList<>();
    l.add(stocks);
    this.dataStore.addData(l, Constant.PORTFOLIO_TRANSACTIONS_STORE, x -> {
      List<String> o = new ArrayList<>();
      for (Map.Entry<Stock, Double> stock : x.get(0).entrySet()) {
        o.add(
            name + "," + user.getName() + "," + PortfolioType.UNFLEXIBLE.name()
                + "," + stock.getKey().getTicker() + "," + stock.getValue()
                + ",buy,"
                + Constant.convertDateToString(new Date(System.currentTimeMillis()))
                + ",0.0");
      }
      return o;
    });
    return true;
  }

  @Override
  public Map<Stock, Double> getPortfolioComposition(User user, String portfolioName)
      throws IOException, NoSuchElementException, ParseException {
    if (!this.checkPortfolioExists(portfolioName, user.getName())) {
      throw new NoSuchElementException(
          String.format(Constant.PORTFOLIO_NOT_FOUND, portfolioName, user.getName()));
    }
    return this.getPortfolio(portfolioName, user.getName()).getStocks();
  }

  @Override
  public Double getPortfolioValue(User user, String portfolioName, Date date)
      throws IOException, NoSuchElementException, ParseException {
    if (!this.checkPortfolioExists(portfolioName, user.getName())) {
      throw new NoSuchElementException(
          String.format(Constant.PORTFOLIO_NOT_FOUND, portfolioName, user.getName()));
    }
    Map<Stock, Double> out;
    Portfolio portfolio = this.getPortfolio(portfolioName, user.getName());
    out = portfolio.getStocks();
    double value = 0d;

    for (Map.Entry<Stock, Double> entry : out.entrySet()) {
      value += (entry.getValue() * this.sModal.getStockValue(entry.getKey(), date));
    }
    return value;
  }
}