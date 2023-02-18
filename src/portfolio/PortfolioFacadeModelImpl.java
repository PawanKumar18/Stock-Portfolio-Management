package portfolio;

import constant.Constant;
import datastore.DataStore;
import generics.Pair;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import services.Service;
import stock.Stock;
import user.User;
import user.UserImpl;

/**
 * The PortfolioFacadeModelImpl class implements the PortfolioFacadeModel interface. It delegates
 * the the input received to either the flexible or inflexible portfolio as the case may be to get
 * the appropriate output.
 */
public class PortfolioFacadeModelImpl implements PortfolioFacadeModel {

  private PortfolioModel inFlexible;
  private AdvancedPortfolioModel flexible;
  private DataStore ds;
  private Service stockService;

  /**
   * Constructor for the PortfolioFacadeModelImpl class. It initializes its data members to the
   * given values.
   *
   * @param inFlexible   object of Inflexible portfolio.
   * @param flexible     object of flexible portfolio.
   * @param ds           datastore to store the portfolio.
   * @param stockService stock service to fetch the stock value.
   */
  public PortfolioFacadeModelImpl(PortfolioModel inFlexible, AdvancedPortfolioModel flexible,
      DataStore ds, Service stockService) {
    this.inFlexible = inFlexible;
    this.flexible = flexible;
    this.ds = ds;
    this.stockService = stockService;
  }

  @Override
  public PortfolioType getPortfolioType(String name, User user)
      throws IOException, NoSuchElementException {
    Map<String, String> hm = new HashMap<>();
    hm.put("Portfolio", name);
    hm.put("OwnedUser", user.getName());
    try {
      List<Map<String, String>> out = this.ds.filter(hm, Constant.PORTFOLIO_STORE, x -> x);
      if (out.size() == 0) {
        return null;
      }
      return PortfolioType.valueOf(out.get(0).get("Type").toUpperCase());
    } catch (NoSuchElementException ex) {
      return null;
    }

  }

  @Override
  public void createFlexiblePortfolio(String name, Map<Stock,
      Pair<Double, Double>> s, User user, Date d)
      throws IOException, NoSuchElementException {
    this.flexible.createPortfolio(name, user, s, d);
  }

  @Override
  public void createUnFlexiblePortfolio(String name, Map<Stock, Double> s,
      User user)
      throws IOException, NoSuchElementException {
    this.inFlexible.createPortfolio(name, s, user);
  }

  @Override
  public Map<Stock, Double> getFlexiblePortfolioComposition(User user,
      String portfolioName, Date d)
      throws IOException, ParseException, NoSuchElementException {
    PortfolioType type = this.getPortfolioType(portfolioName, user);
    if (type == null || type == PortfolioType.FLEXIBLE) {
      return this.flexible.getPortfolioComposition(user, portfolioName, d);
    }
    throw new IllegalArgumentException("Flexible Portfolio can only perform "
        + "FlexiblePortfolio Operations");
  }

  @Override
  public Map<Stock, Double> getUnFlexiblePortfolioComposition(User user,
      String portfolioName)
      throws IOException, ParseException, NoSuchElementException {
    PortfolioType type = this.getPortfolioType(portfolioName, user);
    if (type == null || type == PortfolioType.UNFLEXIBLE) {
      return this.inFlexible.getPortfolioComposition(user, portfolioName);
    }
    throw new IllegalArgumentException("UnFlexible Portfolio can only perform "
        + "UnFlexiblePortfolio Operations");
  }

  @Override
  public Double getFlexiblePortfolioValue(User user, String portfolioName, Date date)
      throws IOException, ParseException, NoSuchElementException {
    PortfolioType type = this.getPortfolioType(portfolioName, user);
    if (type == null || type == PortfolioType.FLEXIBLE) {
      return this.flexible.getPortfolioValue(user, portfolioName, date);
    }
    throw new IllegalArgumentException("Flexible Portfolio can only perform "
        + "FlexiblePortfolio Operations");
  }

  @Override
  public Double getUnFlexiblePortfolioValue(User user, String portfolioName,
      Date date)
      throws IOException, ParseException, NoSuchElementException {
    PortfolioType type = this.getPortfolioType(portfolioName, user);
    if (type == null || type == PortfolioType.UNFLEXIBLE) {
      return this.inFlexible.getPortfolioValue(user, portfolioName, date);
    }
    throw new IllegalArgumentException("UnFlexible Portfolio can only perform "
        + "UnFlexiblePortfolio Operations");

  }

  @Override
  public boolean checkStockExists(Stock stock) throws IOException {
    return this.stockService.checkStockExists(stock.getTicker());
  }

  @Override
  public boolean checkPortfolioExists(String name, String user, Date d,
      PortfolioType type)
      throws IOException, ParseException {
    try {
      if (this.getPortfolioType(name, new UserImpl(user)) == PortfolioType.FLEXIBLE) {
        return this.flexible.checkPortfolioExists(name, user, d);
      } else {
        return this.inFlexible.checkPortfolioExists(name, user);
      }
    } catch (NoSuchElementException ex) {
      return false;
    }
  }

  @Override
  public void buyStock(String name, User user, Stock stock, Double count, Date d,
      Double commissionFee) throws IOException, NoSuchElementException,
      ParseException {
    PortfolioType type = this.getPortfolioType(name, user);
    if (type == null || type == PortfolioType.FLEXIBLE) {
      this.flexible.buyStock(name, user, stock, count, d,
          commissionFee);
    } else {
      throw new IllegalArgumentException("This Portfolio cannot perform "
          + "buyStock operation!");
    }
  }

  @Override
  public void sellStock(String name, User user, Stock stock, Double count, Date d,
      Double commissionFee) throws IOException, ParseException,
      NoSuchElementException {
    PortfolioType type = this.getPortfolioType(name, user);
    if (type == null || type == PortfolioType.FLEXIBLE) {
      this.flexible.sellStock(name, user, stock, count, d, commissionFee);
    } else {
      throw new IllegalArgumentException("Invalid Portfolio for Non-Flexible type");
    }
  }

  @Override
  public double moneyInvested(String name, String user, Date d)
      throws IOException, ParseException, NoSuchElementException {
    PortfolioType type = this.getPortfolioType(name, new UserImpl(user));
    if (type == null || type == PortfolioType.FLEXIBLE) {
      return this.flexible.moneyInvested(name, user, d);
    } else {
      throw new IllegalArgumentException("Invalid Portfolio for Non-flexible type");
    }
  }

  @Override
  public Map<Long, Double> getPortfolioPerformance(User user, String name, Date sDate, Date eDate)
      throws ParseException, IOException, NoSuchElementException {
    PortfolioType type = this.getPortfolioType(name, user);
    if (type == null || type == PortfolioType.FLEXIBLE) {
      return this.flexible.getPortfolioPerformance(user, name, sDate, eDate);
    }
    throw new IllegalArgumentException("Invalid method for Non-flexible Portfolio");
  }

  @Override
  public boolean createStrategyPortfolio(User user, String portfolioName, Map<Stock, Double> stocks,
      Double commissionFee, Date st, Date en, String strategyType, long period,
      double moneyInvested, PortfolioType type) throws IOException {
    if (type == PortfolioType.FLEXIBLE) {
      return this.flexible.createStrategyPortfolio(user, portfolioName, stocks, commissionFee, st,
          en, strategyType, period, moneyInvested, PortfolioType.FLEXIBLE.name());
    }
    throw new IllegalArgumentException("Invalid method for flexible Portfolio");
  }

  @Override
  public void buyStrategyStock(User user, String portfolioName, Map<Stock, Double> stocks,
      Double commissionFee, Date st, Date en, String strategyType, long period,
      double moneyInvested) throws IOException, ParseException {
    PortfolioType ptype = this.getPortfolioType(portfolioName, user);
    if (ptype == null || ptype == PortfolioType.FLEXIBLE) {
      this.flexible.buyStrategyStock(user, portfolioName, stocks, commissionFee, st, en,
          strategyType, period, moneyInvested, PortfolioType.FLEXIBLE.name());
    } else {
      throw new IllegalArgumentException("Invalid method for flexible Portfolio");
    }
  }
}
