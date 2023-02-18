package portfolio;

import constant.Constant;
import generics.Pair;
import generics.VFunction;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;
import stock.Stock;
import stock.StockImpl;
import user.User;
import user.UserImpl;

/**
 * This class implements PortfolioController interface. The controller receives all its inputs from
 * an InputStream object and transmits all outputs to a PrintStream object.
 */
public class PortfolioControllerImpl extends AbstractPortfolioController implements
    CommandController {

  private final PortfolioView view;
  private final PortfolioFacadeModel model;
  private final InputStream in;

  /**
   * Constructor for the PortfolioControllerImpl class. It initialiszes the data variables to given
   * values.
   *
   * @param view  the view of portfolio.
   * @param model the model of portfolio.
   * @param in    the Input Stream for the controller.
   */
  public PortfolioControllerImpl(PortfolioView view, PortfolioFacadeModel model, InputStream in) {
    super(model);
    this.model = model;
    this.view = view;
    this.in = in;
  }

  private String takeValueInput(Scanner s, String message, VFunction<String, Void> p)
      throws IOException {
    String pName;
    while (true) {
      try {
        pName = this.takeInput(s, message);
        p.apply(pName);
        break;
      } catch (IllegalArgumentException ex) {
        this.view.showErrorMessage(
            ex.toString().replace("java.lang.IllegalArgumentException:", ""));
      }
    }
    return pName;
  }

  protected <T> T validateOperationInput(Scanner s, String message,
      Predicate<T> p, Function<String, T> f, String error) {
    T out;
    try {
      this.view.prompt(message);
      out = f.apply(s.nextLine());
      if (!p.test(out)) {
        throw new IllegalArgumentException("");
      }
    } catch (IllegalArgumentException e) {
      this.view.showErrorMessage(error);
      return this.validateOperationInput(s, message, p, f, error);
    }
    return out;
  }

  private int getPortfolioType(Scanner s, PortfolioView view) {
    view.returnOutput(Constant.PORTFOLIO_TYPE_MESSAGE);
    return this.validateOperationInput(s, Constant.PORTFOLIO_TYPE_INPUT,
        x -> x > 0 && x < 4,
        x -> Integer.parseInt(x),
        Constant.OP_VALUES_ERR);
  }

  private Stock takeStockInput(Scanner s, int count) throws IOException {
    try {
      return new StockImpl(
          this.takeValueInput(s, String.format(Constant.STOCK_TICKER_MSG, count + 1),
              x -> this.validateStock(x)));
    } catch (IllegalArgumentException ex) {
      this.view.showErrorMessage(ex.toString());
      return this.takeStockInput(s, count);
    }
  }

  private void createStrategyPortfolio(Scanner s, String portfolioName, User user,
      PortfolioType type, PortfolioStrategy strategyType, int cb)
      throws IOException, ParseException {
    double money = Double.parseDouble(
        this.takeValueInput(s, "Enter the total money (in $)", x -> this.validateMoney(x)));
    Double percent = 100d;
    int count = 0;
    Map<Stock, Double> stocks = new HashMap<>();
    double commissionFee = 0d;
    if (Objects.equals(type, PortfolioType.FLEXIBLE)) {
      commissionFee = Double.parseDouble(
          this.takeValueInput(s, "Enter the commission fee (in $)",
              x -> this.validateCommission(x)));
    }
    String st_date;
    String en_date = "";
    int period = -1;
    if (strategyType == PortfolioStrategy.DOLLAR_AVERAGE) {
      st_date = this.takeValueInput(s, "Enter Start Date (in MM/DD/YYYY format)",
          x -> this.callValidateDateInput(x));
      en_date = this.takeValueInput(s, "Enter end Date (in MM/DD/YYYY format)",
          x -> this.callValidateDateParseInput(x));
      period = Integer.parseInt(
          this.takeValueInput(s, "Enter the recurring period (in days)",
              x -> this.validatePeriod(x)));
    } else {
      st_date = this.takeValueInput(s, "Enter Date (in MM/DD/YYYY format)",
          x -> this.callValidateDateInput(x));
    }

    while (percent > 0) {
      Stock stock = this.takeStockInput(s, count);
      if (stocks.containsKey(stock)) {
        this.view.returnOutput("Stock name already added!!");
        continue;
      }
      Double finalPercent = percent;
      Double stockPercent = Double.valueOf(this.takeValueInput(s, "Enter the Stock Percentage",
          x -> this.validateStockPercent(x, finalPercent)));
      percent = (double) Math.round((percent - stockPercent) * 100) / 100;
      stocks.put(stock, stockPercent);
      count += 1;
    }
    if (cb == 0) {
      this.callCreateStrategyPortfolio(user.getName(), portfolioName, stocks, commissionFee,
          st_date, en_date, strategyType.name(), period, money, type);
      this.view.returnOutput(Constant.PORTFOLIO_SUCCESS);
    } else {
      this.callBuyStrategyStock(user.getName(), portfolioName, stocks, commissionFee, st_date,
          en_date, strategyType.name(), period, money);
      this.view.returnOutput(Constant.STOCK_ADD_SUCCESS);
    }
  }

  private User getUser(Scanner s, PortfolioView view) throws IOException, IllegalArgumentException {
    view.enterUserMessage();
    int inp = this.validateOperationInput(s, Constant.OP_NUMB,
        x -> x == 1 || x == 2,
        x -> Integer.parseInt(x),
        Constant.ADD_STOCK_OP_ERR);
    if (inp == 2) {
      view.exitMessage("");
      System.exit(0);
    }

    User user = new UserImpl(this.takeValueInput(s,
        Constant.USR_PROMPT_MSG,
        this::validateUserName));
    view.displayWelcomeMessage(user.getName());
    return user;
  }

  private int getOperation(Scanner s, Predicate<Integer> p) {
    return this.validateOperationInput(s, Constant.OP_NUMB,
        p, x -> Integer.parseInt(x),
        Constant.OP_VALUES_ERR);
  }

  private String takeInput(Scanner s, String message) {
    this.view.prompt(message);
    return s.nextLine();
  }


  private void createSharePortfolio(Scanner s, User user, PortfolioType type, String name)
      throws IOException, ParseException {
    if (this.model.checkPortfolioExists(name, user.getName(), new Date(System.currentTimeMillis()),
        type)) {
      view.showErrorMessage(Constant.PORTFOLIO_ALREADY_EXIST);
      return;
    }
    Map<Stock, Pair<Double, Double>> stockList = new HashMap<>();
    int count = 0;
    String dateInp = "";
    if (type == PortfolioType.FLEXIBLE) {
      dateInp = this.takeValueInput(s, Constant.DATE_PROMPT, x -> this.callValidateDateInput(x));
    }
    while (true) {
      Stock stock = this.takeStockInput(s, count);
      Double value = Math.floor(
          Double.parseDouble(
              this.takeValueInput(s, String.format(Constant.STOCK_CNT_MSG, count + 1),
                  x -> this.validateStockCount(x))));
      Double commissionFee = 0d;

      if (type == PortfolioType.FLEXIBLE) {
        commissionFee = Double.valueOf(this.takeValueInput(s, Constant.COMMISSION_FEE_PROMPT,
            x -> this.validateCommission(x)));
      }
      if (stockList.containsKey(stock)) {
        String containsCheck = this.validateOperationInput(s, Constant.STOCK_EXIST_PROMPT,
            x -> x.equalsIgnoreCase("y") || x.equalsIgnoreCase("n"),
            x -> x,
            Constant.STOCK_DUPLICATE_ERR);
        if (containsCheck.equalsIgnoreCase("y")) {
          stockList.put(stock, new Pair<>(value, commissionFee));
          count -= 1;
        }
      } else {
        stockList.put(stock, new Pair<>(value, commissionFee));
      }
      count += 1;
      this.view.addStockMessage();
      long crenew = this.validateOperationInput(s, Constant.OP_NUMB,
          x -> x == 1 || x == 2,
          x -> Long.parseLong(x),
          Constant.ADD_STOCK_OP_ERR);
      if (crenew == 2) {
        break;
      }
    }
    this.callCreateSharePortfolio(name, stockList, user.getName(), type, dateInp);
    this.view.returnOutput(Constant.PORTFOLIO_SUCCESS);
  }

  private void operateCreatePortfolioOrBuyStock(Scanner s, User user, PortfolioType type,
      String name, int cb)
      throws IOException, ParseException {
    if (cb == 0 && this.model.checkPortfolioExists(name, user.getName(),
        new Date(System.currentTimeMillis()), type)) {
      view.showErrorMessage(Constant.PORTFOLIO_ALREADY_EXIST);
      return;
    }
    this.view.displayStrategy();
    int st = this.validateOperationInput(s, "Enter the strategy type", x -> x > 0 && x < 4,
        x -> Integer.parseInt(x), "Enter a valid strategy type");
    switch (st) {
      case 1:
        this.createStrategyPortfolio(s, name, user, type, PortfolioStrategy.FIXED_VALUE, cb);
        break;
      case 2:
        this.createStrategyPortfolio(s, name, user, type, PortfolioStrategy.DOLLAR_AVERAGE, cb);
        break;
      case 3:
        if (cb == 0) {
          this.createSharePortfolio(s, user, type, name);
        } else {
          this.operateFLexibleBuyStock(s, user, name);
        }
        break;
      default:
        this.view.returnOutput("Input not valid!");
    }

  }

  private Map<Date, Map<Stock, Pair<Long, Double>>> operateFlexibleBuySellInputs(Scanner s)
      throws IOException, ParseException {
    Stock ss = new StockImpl(
        this.takeValueInput(s, Constant.STOCK_TICKER_MSG_ENTER, x -> this.validateStock(x)));
    long value = (long) Math.floor(
        Double.parseDouble(this.takeValueInput(s, Constant.STOCK_CNT_MSG_ENTER,
            x -> this.validateStockCount(x))));
    String nDate = this.takeValueInput(s, Constant.DATE_PROMPT, x -> this.callValidateDateInput(x));
    Double commissionFee = Double.valueOf(this.takeValueInput(s, Constant.COMMISSION_FEE_PROMPT,
        x -> this.validateCommission(x)));
    HashMap<Stock, Pair<Long, Double>> hm = new HashMap<>();
    hm.put(ss, new Pair<>(value, commissionFee));
    HashMap<Date, Map<Stock, Pair<Long, Double>>> out = new HashMap<>();
    out.put(Constant.convertToDate(nDate), hm);
    return out;
  }

  private void operateFLexibleSellStock(Scanner s, User user, String portfolioName)
      throws IOException, ParseException {
    Map.Entry<Date, Map<Stock, Pair<Long, Double>>> inp =
        this.operateFlexibleBuySellInputs(s).entrySet().iterator().next();
    Map.Entry<Stock, Pair<Long, Double>> st = inp.getValue().entrySet().iterator().next();
    this.callSellStock(portfolioName, user.toString(), st.getKey(),
        Double.valueOf(st.getValue().gete()), Constant.convertDateToString(inp.getKey()),
        st.getValue().gett());
    this.view.returnOutput(Constant.STOCK_DELETE_SUCCESS);
  }

  private void operateFLexibleBuyStock(Scanner s, User user, String portfolioName)
      throws IOException, ParseException {
    Map.Entry<Date, Map<Stock, Pair<Long, Double>>> inp = this.operateFlexibleBuySellInputs(s)
        .entrySet().iterator().next();
    Map.Entry<Stock, Pair<Long, Double>> st = inp.getValue().entrySet().iterator().next();
    this.callBuyStock(portfolioName, user.getName(), st.getKey(),
        Double.valueOf(st.getValue().gete()), Constant.convertDateToString(inp.getKey()),
        st.getValue().gett());
    this.view.returnOutput(Constant.STOCK_ADD_SUCCESS);
  }

  private void operateFlexiblePerformanceGraph(Scanner s, User user, String pfName)
      throws ParseException, IOException, NoSuchElementException {
    String startDate = this.takeValueInput(s, Constant.START_DATE_PROMPT,
        x -> this.callValidateDateInput(x));
    String endDate = this.takeValueInput(s, Constant.END_DATE_PROMPT,
        x -> this.callValidateDateInput(x));
    Map<Long, Double> out = this.callGetPerformanceGraph(user.getName(), pfName, startDate,
        endDate);
    Double baseAmount = (Collections.max(out.values()) / 100) * 2.1;
    Map<String, String> disp = new LinkedHashMap<>();
    for (Map.Entry<Long, Double> entry : out.entrySet()) {
      StringBuilder val = new StringBuilder();
      int t = (int) (entry.getValue() / baseAmount);
      if (t == 0) {
        val.append("*");
      } else {
        val.append("*".repeat(Math.max(0, t)));
      }
      disp.put(new SimpleDateFormat("dd MMM yyyy").format(new Date(entry.getKey())),
          val.toString());
    }
    this.view.displayBarGraph(pfName, disp,
        new SimpleDateFormat("MMM yyyy").format(Constant.convertToDate(startDate)),
        new SimpleDateFormat("MMM yyyy").format(Constant.convertToDate(endDate)),
        (int) Math.floor(baseAmount));
  }


  private void operateInvestedAmount(Scanner s, User user, String ppName)
      throws IOException, NoSuchElementException, ParseException {
    String ppDate = this.takeValueInput(s, Constant.DATE_PROMPT,
        x -> this.callValidateDateInput(x));
    this.view.displayInvestedAmount(ppName,
        ppDate, this.callGetInvestedAmount(
            ppName, user.getName(), ppDate));
  }

  private void operateFlexibleMenu(Scanner s, User user)
      throws IOException, ParseException, NoSuchElementException {
    main:
    while (true) {
      this.view.promptFlexibleMenu();
      int op = this.getOperation(s,
          x -> x > 0 && x < 9);
      String portfolioName = null;
      if (op != 8) {
        portfolioName = this.takeValueInput(s, Constant.PORTFOLIO_PROMPT_MSG,
            x -> this.validatePortfolioName(x));
      }
      switch (op) {
        case 1:
          this.operateCreatePortfolioOrBuyStock(s, user, PortfolioType.FLEXIBLE, portfolioName, 0);
          break;
        case 2:
          this.view.displayComposition(
              this.callViewPortfolioComposition(user.getName(), portfolioName,
                  this.takeValueInput(s, Constant.DATE_PROMPT, x -> this.callValidateDateInput(x)),
                  PortfolioType.FLEXIBLE));
          break;
        case 3:
          this.view.displayStockValue(portfolioName,
              this.callGetPortfolioValue(user.getName(), portfolioName,
                  this.takeValueInput(s, Constant.DATE_PROMPT, x -> this.callValidateDateInput(x)),
                  PortfolioType.FLEXIBLE));
          break;
        case 4:
          this.operateCreatePortfolioOrBuyStock(s, user, PortfolioType.FLEXIBLE, portfolioName, 1);
          break;
        case 5:
          this.operateFLexibleSellStock(s, user, portfolioName);
          break;
        case 6:
          this.operateFlexiblePerformanceGraph(s, user, portfolioName);
          break;
        case 7:
          this.operateInvestedAmount(s, user, portfolioName);
          break;
        case 8:
          break main;
        default:
          break;
      }
    }
  }

  private void operateUnflexibleMenu(Scanner s, User user)
      throws IOException, ParseException, NoSuchElementException {
    main:
    while (true) {
      this.view.promptMenu();
      int op = this.getOperation(s,
          x -> x > 0 && x < 5);
      String portfolioName = null;
      if (op != 4) {
        portfolioName = this.takeValueInput(s, Constant.PORTFOLIO_PROMPT_MSG,
            x -> this.validatePortfolioName(x));
      }
      switch (op) {
        case 1:
          this.createSharePortfolio(s, user, PortfolioType.UNFLEXIBLE, portfolioName);
          break;
        case 2:
          this.view.displayComposition(
              this.callViewPortfolioComposition(user.getName(), portfolioName, null,
                  PortfolioType.UNFLEXIBLE));
          break;
        case 3:
          this.view.displayStockValue(portfolioName,
              this.callGetPortfolioValue(user.getName(),
                  portfolioName,
                  this.takeValueInput(s, Constant.DATE_PROMPT, x -> this.callValidateDateInput(x)),
                  PortfolioType.UNFLEXIBLE));
          break;
        case 4:
          break main;
        default:
          break main;
      }
    }
  }


  @Override
  public void operate() {
    Scanner s = new Scanner(this.in);
    User user = null;
    try {
      user = this.getUser(s, this.view);
    } catch (IOException ex) {
      this.view.showErrorMessage(Constant.USER_EMPTY_ERR);
      return;
    }
    boolean goInto = false;
    int type = 0;
    loop:
    while (true) {
      try {
        if (!goInto) {
          type = this.getPortfolioType(s, this.view);
        } else {
          goInto = false;
        }
        switch (type) {
          case 1:
            this.operateFlexibleMenu(s, user);
            break;
          case 2:
            this.operateUnflexibleMenu(s, user);
            break;
          case 3:
            this.view.exitMessage(user.getName());
            break loop;
          default:
            this.view.returnOutput("Invalid Operation for type");
        }
      } catch (NoSuchElementException nse) {
        goInto = true;
        this.view.showErrorMessage(nse.toString().replace("java.util.NoSuchElementException:", ""));
      } catch (IOException ioe) {
        goInto = true;
        this.view.showErrorMessage(ioe.toString().replace("java.io.IOException:", ""));
      } catch (IllegalArgumentException iae) {
        goInto = true;
        this.view.showErrorMessage(iae.toString().replace("java.lang.IllegalArgumentException:",
            ""));
      } catch (ParseException pe) {
        goInto = true;
        this.view.showErrorMessage(pe.toString().replace("java.text.ParseException:", ""));
      }
    }
  }

}
