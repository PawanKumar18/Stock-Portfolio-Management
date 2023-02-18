package portfolio;

import constant.Constant;
import generics.Pair;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import stock.Stock;
import stock.StockImpl;
import user.UserImpl;

/**
 * AbstractPortfolioController is an abstract class that contains methods that are common to the
 * console based UI and graphical UI.
 */
public abstract class AbstractPortfolioController {

  private PortfolioFacadeModel model;

  /**
   * Initializes the model so that Abstract portfolio could use it in it's further interactions.
   *
   * @param model model to be initialized
   */
  public AbstractPortfolioController(PortfolioFacadeModel model) {
    this.model = model;
  }

  protected <T> T callValidateInput(String value,
      Predicate<T> p, Function<String, T> f, String error) {
    T out;
    try {
      out = f.apply(value);
      if (!p.test(out)) {
        throw new IllegalArgumentException(error);
      }
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(error);
    }
    return out;
  }

  protected void callValidateDateParseInput(String da) {
    try {
      if (da.length() != 0) {
        new SimpleDateFormat(Constant.DATE_FORMAT).parse(da);
      }
    } catch (ParseException pe) {
      throw new IllegalArgumentException(Constant.DATE_INVALID);
    }
  }

  protected void validatePortfolioName(String name) {
    this.callValidateInput(name, x -> x.length() > 0, x -> x, Constant.PORTFOLIO_EMPTY_ERR);
  }

  protected void validateUserName(String name) {
    this.callValidateInput(name, x -> x != null && x.length() > 0, x -> x, Constant.USER_EMPTY_ERR);
  }

  protected void validateCommission(String comm) {
    this.callValidateInput(comm, x -> x >= 0, x -> Double.parseDouble(x), Constant.COMM_FEE_ERR);
  }

  protected void validatePeriod(String period) {
    this.callValidateInput(period, x -> x > 0, x -> Integer.parseInt(x), Constant.PERIOD_ERR);
  }

  protected void validateMoney(String money) {
    this.callValidateInput(money, x -> x > 0, x -> Double.parseDouble(x), Constant.MONEY_INV_ERR);
  }

  protected void validateStockPercent(String percent, Double mxp) {
    this.callValidateInput(percent, x -> x > 0d && x <= mxp, x -> Double.parseDouble(x),
        "Total portfolio percent should be between (0, " + mxp + "]");
  }

  protected void validateStock(String name) throws IOException {
    this.callValidateInput(name, x -> x.length() > 0, x -> x, Constant.STOCK_ERR);
    if (!this.model.checkStockExists(new StockImpl(name))) {
      throw new IllegalArgumentException(Constant.STOCK_ERR);
    }
  }

  protected void validateStockCount(String cnt) throws IOException {
    this.callValidateInput(cnt, x -> x > 0 && x - Math.floor(x) == 0, x -> Double.valueOf(x),
        "Stock count has to be positive and non-fractional!");
  }

  protected void validateStockCountDouble(String cnt) throws IOException {
    this.callValidateInput(cnt, x -> x > 0, x -> Double.valueOf(x),
        "Stock count has to be positive!");
  }

  protected void callValidateDateInput(String fd) {
    try {
      Date d = new SimpleDateFormat(Constant.DATE_FORMAT).parse(fd);
      if (d.compareTo(new Date(System.currentTimeMillis())) > 0) {
        throw new IllegalArgumentException(Constant.DATE_FUTURE_ERR);
      }
    } catch (ParseException pe) {
      throw new IllegalArgumentException(Constant.DATE_INVALID);
    }
  }

  protected void callCreateSharePortfolio(String name, Map<Stock, Pair<Double, Double>> stocks,
      String user, PortfolioType type, String date)
      throws IOException, ParseException {
    if (this.model.getPortfolioType(name, new UserImpl(user)) != null) {
      throw new IllegalArgumentException("Portfolio already exists!");
    }

    if (type == PortfolioType.FLEXIBLE) {
      this.model.createFlexiblePortfolio(name, stocks, new UserImpl(user),
          Constant.convertToDate(date));
    } else {
      Map<Stock, Double> stocksList = new HashMap<>();
      for (Map.Entry<Stock, Pair<Double, Double>> ent : stocks.entrySet()) {
        stocksList.put(ent.getKey(), ent.getValue().gete());
      }
      this.model.createUnFlexiblePortfolio(name, stocksList, new UserImpl(user));
    }
  }

  protected void callCreateStrategyPortfolio(
      String user, String portfolioName, Map<Stock, Double> stocks,
      Double commissionFee, String st, String en, String strategyType, int period,
      double moneyInvested, PortfolioType type)
      throws IOException, IllegalArgumentException, ParseException {
    Date end = null;
    if (en.length() != 0) {
      end = Constant.convertToDate(en);
    }
    if (commissionFee > moneyInvested) {
      throw new IllegalArgumentException("Commission Fee cannot be greater than money Invested!");
    }
    this.model.createStrategyPortfolio(new UserImpl(user),
        portfolioName,
        stocks,
        commissionFee,
        new SimpleDateFormat(Constant.DATE_FORMAT).parse(st), end,
        strategyType, period, moneyInvested, type);
  }

  protected Map<Stock, Double> callViewPortfolioComposition(String user, String name, String d,
      PortfolioType t)
      throws ParseException, IOException {

    if (t == PortfolioType.FLEXIBLE) {
      return this.model.getFlexiblePortfolioComposition(new UserImpl(user), name,
          Constant.convertToDate(d));
    } else {
      return this.model.getUnFlexiblePortfolioComposition(new UserImpl(user), name);
    }
  }

  protected void callBuyStock(String name, String user, Stock stock, Double count, String d,
      Double commissionFee) throws IOException, ParseException {
    this.model.buyStock(name, new UserImpl(user), stock, count, Constant.convertToDate(d),
        commissionFee);
  }

  protected void callBuyStrategyStock(String user, String portfolioName, Map<Stock, Double> stocks,
      Double commissionFee, String st, String en, String strategyType, long period,
      double moneyInvested) throws IOException, ParseException {

    Date end = null;
    if (en.length() != 0) {
      end = Constant.convertToDate(en);
    }
    if (commissionFee > moneyInvested) {
      throw new IllegalArgumentException("Commission Fee cannot be greater than money Invested!");
    }
    this.model.buyStrategyStock(new UserImpl(user),
        portfolioName,
        stocks,
        commissionFee,
        Constant.convertToDate(st), end,
        strategyType, period, moneyInvested);
  }

  protected void callSellStock(String name, String user, Stock stock, Double count, String d,
      Double commissionFee) throws IOException, ParseException {
    if (!this.model.checkStockExists(stock)) {
      throw new IllegalArgumentException(Constant.STOCK_ERR);
    }
    this.model.sellStock(name, new UserImpl(user), stock, count, Constant.convertToDate(d),
        commissionFee);

  }

  protected double callGetPortfolioValue(String user, String portfolioName, String date,
      PortfolioType type)
      throws ParseException, IOException {
    if (type == PortfolioType.FLEXIBLE) {
      return this.model.getFlexiblePortfolioValue(new UserImpl(user), portfolioName,
          Constant.convertToDate(date));
    } else {
      return this.model.getUnFlexiblePortfolioValue(new UserImpl(user), portfolioName,
          Constant.convertToDate(date));
    }
  }

  protected Map<Long, Double> callGetPerformanceGraph(String user, String name, String st,
      String en)
      throws ParseException, IOException {
    return this.model.getPortfolioPerformance(new UserImpl(user), name, Constant.convertToDate(st),
        Constant.convertToDate(en));
  }

  protected double callGetInvestedAmount(String name, String user, String d)
      throws ParseException, IOException {
    return this.model.moneyInvested(name, user, Constant.convertToDate(d));
  }

}
