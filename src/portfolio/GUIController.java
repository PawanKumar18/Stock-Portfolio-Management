package portfolio;

import constant.Constant;
import generics.Pair;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import stock.Stock;
import stock.StockImpl;
import user.User;
import user.UserImpl;

/**
 * GUIController class extends the AbstractPortfolioController class and implements GUIFeatures
 * interface. It is the controller for the GUI based UI.
 */
public class GUIController extends AbstractPortfolioController implements GUIFeatures {

  PortfolioFacadeModel model;
  GUIView jView;
  User user;

  /**
   * Constructor for GUIController class. It initializes the model and view.
   *
   * @param model model of the project.
   * @param view  graphical user interface of the project.
   */
  public GUIController(PortfolioFacadeModel model, GUIView view) {
    super(model);
    this.model = model;
    this.jView = view;

    jView.addFeatures(this);
  }

  @Override
  public boolean getUser(String userName) {
    try {
      this.validateUserName(userName);
      this.user = new UserImpl(userName);
      this.jView.displayWelcomeMessage(this.user.getName());
      this.jView.promptFlexibleMenu();
      return true;
    } catch (IllegalArgumentException ex) {
      this.throwException(ex.toString());
      return false;
    }
  }

  @Override
  public boolean operateCreatePortfolio(String name, Map<String, Pair<String, String>> stocks,
      String date) {
    try {
      this.validatePortfolioName(name);
      this.callValidateDateInput(date);
      if (stocks.isEmpty()) {
        this.jView.showErrorMessage(Constant.STOCK_EMPTY);
        return false;
      }
      Map<Stock, Pair<Double, Double>> stockList = new HashMap<>();
      for (Map.Entry<String, Pair<String, String>> entry : stocks.entrySet()) {
        this.validateStock(entry.getKey());
        this.validateStockCount(entry.getValue().gete());
        this.validateCommission(entry.getValue().gett());
        stockList.put(new StockImpl(entry.getKey()),
            new Pair<>(Double.valueOf(entry.getValue().gete()),
                Double.valueOf(entry.getValue().gett())));
      }
      this.callCreateSharePortfolio(name, stockList, user.getName(), PortfolioType.FLEXIBLE, date);
      this.jView.returnOutput(Constant.PORTFOLIO_SUCCESS);
      return true;
    } catch (IOException | IllegalArgumentException | NoSuchElementException | ParseException ex) {
      this.throwException(ex.toString());
      return false;
    }
  }

  @Override
  public boolean operateStrategyCreateOrBuyPortfolio(String name, Map<String, String> stocks,
      String stDate, String enDate, String commFee, String bc, String moneyInv, String period,
      PortfolioStrategy strategy) {
    try {
      System.out.println(name);
      System.out.println(stocks);
      System.out.println(stDate);
      System.out.println(enDate);
      System.out.println(commFee);
      System.out.println(moneyInv);
      System.out.println(bc);
      System.out.println(period);
      System.out.println(strategy);

      this.validatePortfolioName(name);
      this.callValidateDateInput(stDate);
      this.callValidateDateParseInput(enDate);
      this.validateCommission(commFee);
      this.validateMoney(moneyInv);
      if (strategy == PortfolioStrategy.DOLLAR_AVERAGE) {
        this.validatePeriod(period);
      } else {
        period = "-1";
      }
      double percent = 0d;
      Map<Stock, Double> stks = new HashMap<>();
      System.out.println(stocks.entrySet());
      for (Map.Entry<String, String> stock : stocks.entrySet()) {
        this.validateStock(stock.getKey());
        this.validateStockPercent(stock.getValue(), 100d);
        stks.put(new StockImpl(stock.getKey()), Double.valueOf(stock.getValue()));
        percent += (double) Math.round(Double.parseDouble(stock.getValue()) * 100) / 100;
        System.out.println(percent);
      }
      if (percent != 100d) {
        throw new IllegalArgumentException("Stocks specified for " + percent + " of 100 percent");
      }
      if (bc.equals("create")) {
        this.callCreateStrategyPortfolio(user.getName(), name, stks, Double.valueOf(commFee),
            stDate, enDate, strategy.name(), Integer.parseInt(period), Double.parseDouble(moneyInv),
            PortfolioType.FLEXIBLE);
        this.jView.returnOutput(Constant.PORTFOLIO_SUCCESS);
      } else {
        this.callBuyStrategyStock(user.getName(), name, stks, Double.valueOf(commFee), stDate,
            enDate, strategy.name(), Long.parseLong(period), Double.parseDouble(moneyInv));
        this.jView.returnOutput("Stock Successfully purchased");
      }
      return true;
    } catch (IOException | IllegalArgumentException | NoSuchElementException | ParseException ex) {
      this.throwException(ex.toString());
      return false;
    }
  }

  @Override
  public void portfolioComposition(String portfolioName, String date) {
    try {
      this.validatePortfolioName(portfolioName);
      this.callValidateDateInput(date);

      this.jView.displayComposition(
          this.callViewPortfolioComposition(user.getName(), portfolioName,
              date, PortfolioType.FLEXIBLE));
    } catch (NoSuchElementException | IllegalArgumentException | IOException | ParseException ex) {
      this.throwException(ex.toString());
    }
  }

  private void throwException(String excep) {
    this.jView.showErrorMessage(excep
        .replace("java.util.NoSuchElementException:", "")
        .replace("java.io.IOException:", "")
        .replace("java.lang.IllegalArgumentException:", "")
        .replace("java.text.ParseException:", "")
    );
  }

  @Override
  public void portfolioValue(String portfolioName, String date) {
    try {
      this.validatePortfolioName(portfolioName);
      this.callValidateDateInput(date);

      this.jView.displayStockValue(portfolioName,
          this.callGetPortfolioValue(user.getName(), portfolioName, date, PortfolioType.FLEXIBLE));
    } catch (IOException | ParseException | NoSuchElementException | IllegalArgumentException ex) {
      this.throwException(ex.toString());
    }
  }

  @Override
  public boolean operateFlexibleBuyInputs(String portfolioName, String stockName,
      String count, String date, String commissionFee) {
    try {
      this.operateFlexibleBuySellInputs(portfolioName,
          stockName, count, date, commissionFee);
      this.callBuyStock(portfolioName, user.getName(), new StockImpl(stockName),
          Double.parseDouble(count),
          date, Double.parseDouble(commissionFee));
      this.jView.returnOutput(Constant.STOCK_ADD_SUCCESS);
      return true;
    } catch (NoSuchElementException | IOException | ParseException | IllegalArgumentException ex) {
      this.throwException(ex.toString());
      return false;
    }
  }

  @Override
  public void operateFlexibleSellInputs(String portfolioName, String stockName,
      String count, String date, String commissionFee) {
    try {
      this.operateFlexibleBuySellInputs(portfolioName,
          stockName, count, date, commissionFee);
      this.callSellStock(portfolioName, user.getName(), new StockImpl(stockName),
          Double.parseDouble(count),
          date, Double.parseDouble(commissionFee));
      this.jView.returnOutput(Constant.STOCK_DELETE_SUCCESS);
    } catch (IOException | NoSuchElementException | IllegalArgumentException | ParseException ex) {
      this.throwException(ex.toString());
    }
  }

  private void operateFlexibleBuySellInputs(
      String portfolioName,
      String stockName,
      String count, String date,
      String commissionFee) throws IOException {
    this.validatePortfolioName(portfolioName);
    this.validateStock(stockName);
    this.validateStockCount(count);
    this.callValidateDateInput(date);
    this.validateCommission(commissionFee);
  }

  @Override
  public void operateInvestedAmount(String portfolioName, String date) {
    try {
      this.validatePortfolioName(portfolioName);
      this.callValidateDateInput(date);

      this.jView.displayInvestedAmount(portfolioName,
          date, this.callGetInvestedAmount(
              portfolioName, user.getName(), date));
    } catch (IOException | NoSuchElementException | ParseException | IllegalArgumentException ex) {
      this.throwException(ex.toString());
    }
  }

  @Override
  public void drawGraph(String portfolioName, String stDate, String enDate) {
    try {
      Map<Long, Double> out = this.callGetPerformanceGraph(user.getName(), portfolioName, stDate,
          enDate);
      Map<String, String> re = new LinkedHashMap<>();
      for (Map.Entry<Long, Double> o : out.entrySet()) {
        re.put(new SimpleDateFormat("dd MMM yyyy").format(new Date(o.getKey())),
            String.valueOf(o.getValue()));
      }
      System.out.println("came in here...");
      this.jView.displayBarGraph(portfolioName, re, stDate, enDate, 0);
    } catch (IOException | NoSuchElementException | ParseException | IllegalArgumentException ex) {
      this.throwException(ex.toString());
    }
  }

}
