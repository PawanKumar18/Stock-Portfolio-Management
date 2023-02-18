package portfolio;


import constant.Constant;
import datastore.DataStore;
import file.CSVFileIO;
import file.FileIO;
import generics.Pair;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import services.Service;
import stock.Stock;
import stock.StockImpl;
import user.User;
import user.UserImpl;

/**
 * AdvancedPortfolioModelImpl extends the PortfolioModelImpl class.
 */
public class AdvancedPortfolioModelImpl extends PortfolioModelImpl implements
    AdvancedPortfolioModel {

  private final DataStore dataStore;
  private final Service sModal;

  /**
   * Constructor for PortfolioModelImpl class. It initializes the datastore and sModel to the given
   * values
   *
   * @param d Datastore where the query/update operations to be performed
   */
  public AdvancedPortfolioModelImpl(DataStore d, Service serv) {
    super(d, serv);
    this.dataStore = d;
    this.sModal = serv;
  }

  @Override
  public boolean checkPortfolioExists(String name, String user, Date d)
      throws IOException, ParseException {
    try {
      Map<String, String> hm = new HashMap<>();
      hm.put("Portfolio", name);
      hm.put("OwnedUser", user);
      List<Map<String, String>> pp = this.dataStore.filter(hm, Constant.PORTFOLIO_STORE, x -> x);
      return pp != null && d.compareTo(Constant.convertToDate(pp.get(0).get("CreationDate"))) >= 0;
    } catch (NoSuchElementException ex) {
      return false;
    }
  }

  @Override
  public double moneyInvested(String name, String user, Date d) throws IOException, ParseException {
    if (!this.checkPortfolioExists(name, user, d)) {
      throw new NoSuchElementException(
          String.format(Constant.PORTFOLIO_NOT_FOUND_DATE, name, user,
              Constant.convertDateToString(d)));
    }
    this.addRecurringEntries(new UserImpl(user), name, d);
    Map<String, String> hm = new HashMap<>();
    hm.put("Portfolio", name);
    hm.put("OwnedUser", user);
    List<Map<String, String>> data = this.dataStore.filter(hm,
        Constant.PORTFOLIO_TRANSACTIONS_STORE, x -> x);
    double out = 0f;
    List<String> groupCounted = new ArrayList<>();
    for (Map<String, String> datum : data) {
      if (d.compareTo(Constant.convertToDate(datum.get("Date"))) >= 0) {
        if (datum.get("StrategyType") != null) {
          if (!groupCounted.contains(datum.get("GroupName") + "," + datum.get("Date"))) {
            out += Double.parseDouble(datum.get("MoneyInvested"));
            groupCounted.add(datum.get("GroupName") + "," + datum.get("Date"));
          }
          continue;
        }
        if (datum.get("Operation").equals("sell")) {
          out += Double.parseDouble(datum.get("CommissionFee"));
        } else {
          out += (Double.parseDouble(datum.get("Count")) * this.sModal.getStockValue(
              new StockImpl(datum.get("StockName")), Constant.convertToDate(datum.get("Date"))))
              + Double.parseDouble(datum.get("CommissionFee"));
        }
      }
    }
    return out;
  }

  @Override
  public Map<Long, Double> getPortfolioPerformance(User user, String name, Date sDate, Date eDate)
      throws ParseException, IOException {
    if (!this.checkPortfolioExists(name, user.toString(), sDate)) {
      throw new NoSuchElementException(
          String.format(Constant.PORTFOLIO_NOT_FOUND_DATE, name, user,
              Constant.convertDateToString(sDate)));
    }
    FileIO csvRead = new CSVFileIO();

    long time_diff = eDate.getTime() - sDate.getTime();
    int days_span = (int) (time_diff / (1000d * 60 * 60 * 24));
    if (days_span < 5) {
      throw new IllegalArgumentException("Cannot Plot graph with less than 5 bars!");
    }
    int numb = -1;

    int delta_days = -1;
    for (int i = 5; i < 30; i++) {
      if (days_span / i >= 1) {
        numb = i;
      } else if (days_span / i < 1 && numb == -1) {
        break;
      } else {
        delta_days = days_span / numb;
        break;
      }

      if (i == 29) {
        delta_days = days_span / numb;
      }
    }

    if (delta_days == -1 || eDate.getTime() - sDate.getTime() < 1000d * 60 * 60 * 24) {
      throw new IllegalArgumentException("End date should be greater than start!");
    }

    Map<Long, Double> out = new LinkedHashMap<>();

    long end_time_stamp = eDate.getTime();
    long iterator = sDate.getTime();

    main:
    for (int i = 0; i < numb; i++) {
      if (iterator >= end_time_stamp) {
        break;
      }

      Map<Stock, Double> stk = this.getPortfolioComposition(user, name, new Date(iterator));

      double value = 0d;
      for (int k = 0; k < stk.keySet().size(); k++) {
        if (iterator >= end_time_stamp) {
          break main;
        }
        Double val = stk.get(stk.keySet().toArray()[k]);
        Stock st = (Stock) stk.keySet().toArray()[k];

        Double d;
        try {
          d = this.sModal.getStockValue(st, new Date(iterator));
          value += val * d;
        } catch (NoSuchElementException ex) {
          k -= 1;
          iterator += 1000d * 60 * 60 * 24;
        }
      }
      out.put(iterator, value);
      iterator += (delta_days * 1000d * 60 * 60 * 24);
    }

    if (out.size() < 5) {
      throw new IllegalArgumentException("Cannot plot the graph with less than 5 bars!");
    }
    return out;
  }

  @Override
  public Map<Stock, Double> getPortfolioComposition(User user, String portfolioName, Date d)
      throws NoSuchElementException, IOException, ParseException {
    if (!this.checkPortfolioExists(portfolioName, user.getName(), d)) {
      throw new NoSuchElementException(String.format(Constant.PORTFOLIO_NOT_FOUND_DATE,
          portfolioName, user.getName(), Constant.convertDateToString(d)));
    }
    this.addRecurringEntries(user, portfolioName, d);
    Map<Stock, Double> out = new HashMap<>();
    Map<String, String> filters = new HashMap<>();
    filters.put("Portfolio", portfolioName);
    filters.put("OwnedUser", user.getName());
    List<Map<String, String>> data = this.dataStore.filter(filters,
        Constant.PORTFOLIO_TRANSACTIONS_STORE, x -> x);
    if (data.size() > 0) {
      for (Map<String, String> datum : data) {
        Stock temp = new StockImpl(datum.get("StockName"));
        if (d.compareTo(Constant.convertToDate(datum.get("Date"))) < 0) {
          continue;
        }
        if (!out.containsKey(temp)) {
          out.put(new StockImpl(datum.get("StockName")), 0d);
        }
        Double shareCount = Double.parseDouble(datum.get("Count"));
        if (datum.get("StrategyType") != null) {
          Double shareMoney = ((Double.parseDouble(datum.get("MoneyInvested")) - Double.parseDouble(
              datum.get("CommissionFee"))) * Double.parseDouble(datum.get("Count"))) / 100;
          shareCount = shareMoney / this.sModal.getStockValue(new StockImpl(datum.get("StockName")),
              Constant.convertToDate(datum.get("Date")));
        }

        if (datum.get("Operation").equalsIgnoreCase("buy")) {
          out.put(temp, out.get(temp) + shareCount);
        } else {
          out.put(temp, out.get(temp) - shareCount);
        }
      }
    }
    return out;
  }

  @Override
  public void buyStrategyStock(User user, String portfolioName, Map<Stock, Double> stocks,
      Double commissionFee, Date st, Date en, String strategyType, long period,
      double moneyInvested, String portfolioType) throws IOException, ParseException {
    if (!this.checkPortfolioExists(portfolioName, user.getName())) {
      throw new NoSuchElementException(
          String.format(Constant.PORTFOLIO_NOT_FOUND, portfolioName, user.getName()));
    }

    if (commissionFee >= moneyInvested) {
      throw new IllegalArgumentException("Commission Fee cannot be greater than Money Invested!");
    }

    if (!this.checkPortfolioExists(portfolioName, user.getName(), st)) {
      List<String> updates = new ArrayList<>();
      updates.add(
          portfolioName + "," + user.getName() + ",;" + portfolioName + "," + user.getName() + ","
              + Constant.convertDateToString(st) + "," + PortfolioType.FLEXIBLE.name());
      this.dataStore.updateData(updates, Constant.PORTFOLIO_STORE, x -> x);
    }

    String value = this.sModal.getStockData(stocks.entrySet().iterator().next().getKey());
    if (Objects.equals(strategyType, PortfolioStrategy.DOLLAR_AVERAGE.name())) {
      while (!value.contains(new SimpleDateFormat("yyyy-MM-dd").format(st))) {
        st = this.addDays(st, 1);
      }
    }

    List<String> stockData = new ArrayList<>();
    long randv = new Random(System.currentTimeMillis()).nextInt(1000000);
    for (Map.Entry<Stock, Double> stock : stocks.entrySet()) {
      String enConverted = "";
      if (en != null) {
        enConverted = Constant.convertDateToString(en);
      }
      stockData.add(portfolioName + "," + user.getName() + "," + portfolioType + ","
          + stock.getKey().getTicker() + "," + stock.getValue() + ",buy,"
          + Constant.convertDateToString(st)
          + "," + commissionFee + "," + strategyType + "," + moneyInvested + "," + enConverted + ","
          + period + "," + randv);
    }
    this.dataStore.addData(stockData, Constant.PORTFOLIO_TRANSACTIONS_STORE, x -> x);
  }

  @Override
  public List<Portfolio> mapCSVToPortfolio(List<Map<String, String>> dbInput)
      throws ParseException, IOException {
    List<Portfolio> out = new ArrayList<>();
    for (Map<String, String> inp : dbInput) {
      Map<Stock, Double> values = this.getPortfolioComposition(
          new UserImpl(inp.get("OwnedUser")), inp.get("PortfolioName"),
          new Date(System.currentTimeMillis()));
      out.add(new PortfolioImpl(inp.get("Portfolio"), new UserImpl(inp.get("OwnedUser")), values,
          Constant.convertToDate(inp.get("CreationDate"))));
    }
    return out;
  }

  @Override
  public boolean createPortfolio(String name, User user, Map<Stock, Pair<Double, Double>> stocks,
      Date d)
      throws IOException, NoSuchElementException {
    if (this.checkPortfolioExists(name, user.getName())) {
      throw new IllegalArgumentException("Portfolio already exists!");
    }
    List<String> out = new ArrayList<>();
    out.add(name + "," + user.getName() + "," + Constant.convertDateToString(
        d) + "," + PortfolioType.FLEXIBLE.name());

    this.dataStore.addData(out, Constant.PORTFOLIO_STORE, x -> x);
    List<Map<Stock, Pair<Double, Double>>> l = new ArrayList<>();
    l.add(stocks);
    this.dataStore.addData(l, Constant.PORTFOLIO_TRANSACTIONS_STORE, x -> {
      List<String> o = new ArrayList<>();
      for (Map.Entry<Stock, Pair<Double, Double>> stock : x.get(0).entrySet()) {
        o.add(
            name + "," + user.getName() + "," + PortfolioType.FLEXIBLE.name()
                + "," + stock.getKey().getTicker() + "," + stock.getValue().gete()
                + ",buy,"
                + Constant.convertDateToString(d)
                + "," + stock.getValue().gett());
      }
      return o;
    });
    return true;
  }

  @Override
  public Double getPortfolioValue(User user, String portfolioName, Date date)
      throws IOException, NoSuchElementException, ParseException {
    if (!this.checkPortfolioExists(portfolioName, user.getName())) {
      throw new NoSuchElementException(
          String.format(Constant.PORTFOLIO_NOT_FOUND, portfolioName, user.getName()));
    }
    double value = 0d;
    Map<Stock, Double> out;
    try {
      out = this.getPortfolioComposition(user, portfolioName, date);
    } catch (NoSuchElementException e) {
      return value;
    }
    for (Map.Entry<Stock, Double> entry : out.entrySet()) {
      value += (entry.getValue() * this.sModal.getStockValue(entry.getKey(), date));
    }
    return value;
  }

  @Override
  public void buyStock(String name, User user, Stock stock, Double count,
      Date d, Double cmFee)
      throws IOException, NoSuchElementException, ParseException {
    if (cmFee < 0) {
      throw new IllegalArgumentException("Commission Fee cannot be negative!");
    }
    if (!this.checkPortfolioExists(name, user.getName())) {
      throw new NoSuchElementException(
          String.format(Constant.PORTFOLIO_NOT_FOUND, name, user.getName()));
    }

    if (!this.checkPortfolioExists(name, user.getName(), d)) {
      List<String> updates = new ArrayList<>();
      updates.add(name + "," + user.getName() + ",;" + name + "," + user.getName() + ","
          + Constant.convertDateToString(d) + "," + PortfolioType.FLEXIBLE.name());
      this.dataStore.updateData(updates, Constant.PORTFOLIO_STORE, x -> x);
    }

    if (d.compareTo(new Date(System.currentTimeMillis())) > 0) {
      throw new IllegalArgumentException(Constant.DATE_FUTURE_ERR);
    }

    List<Map<Stock, Double>> l = new ArrayList<>();
    Map<Stock, Double> tt = new HashMap<>() {
      {
        put(stock, count);
      }
    };
    l.add(tt);
    this.dataStore.addData(l, Constant.PORTFOLIO_TRANSACTIONS_STORE, x -> {
      List<String> o = new ArrayList<>();
      for (Map.Entry<Stock, Double> stt : x.get(0).entrySet()) {
        o.add(
            name + "," + user.getName() + "," + PortfolioType.FLEXIBLE.name()
                + "," + stt.getKey().getTicker() + "," + stt.getValue()
                + ",buy,"
                + Constant.convertDateToString(d) + "," + cmFee);
      }
      return o;
    });
  }

  @Override
  public void sellStock(String name, User user, Stock stock, Double count,
      Date d, Double cmFee) throws IOException,
      ParseException, NoSuchElementException {
    if (cmFee < 0) {
      throw new IllegalArgumentException("Commission Fee cannot be negative!");
    }
    if (!this.checkPortfolioExists(name, user.getName(), d)) {
      throw new NoSuchElementException(
          String.format(Constant.PORTFOLIO_NOT_FOUND_DATE, name, user.getName(),
              Constant.convertDateToString(d)));
    }

    if (d.compareTo(new Date(System.currentTimeMillis())) > 0) {
      throw new IllegalArgumentException(Constant.DATE_FUTURE_ERR);
    }

    Map<Stock, Double> stocks = this.getPortfolioComposition(user, name, d);
    if (!stocks.containsKey(stock) || stocks.get(stock) < count) {
      throw new IllegalArgumentException(
          String.format(Constant.STOCK_SELL_ERR, (long) Math.floor(count), stock.getTicker(),
              Constant.convertDateToString(d), name));
    }

    List<String> out = new ArrayList<>();
    out.add(name + "," + user.getName() + "," + PortfolioType.FLEXIBLE.name() + ","
        + stock.getTicker() + "," + count + ",sell,"
        + Constant.convertDateToString(d) + "," + cmFee);
    this.dataStore.addData(out, Constant.PORTFOLIO_TRANSACTIONS_STORE, x -> x);
  }

  @Override
  public boolean createStrategyPortfolio(User user, String portfolioName, Map<Stock, Double> stocks,
      Double commissionFee, Date st, Date en, String strategyType, long period,
      double moneyInvested, String portfolioType)
      throws IOException, IllegalArgumentException {
    if (this.checkPortfolioExists(portfolioName, user.getName())) {
      throw new IllegalArgumentException("Portfolio already exists!");
    }
    if (commissionFee >= moneyInvested) {
      throw new IllegalArgumentException("Commission Fee cannot be greater than Money Invested!");
    }

    String value = this.sModal.getStockData(stocks.entrySet().iterator().next().getKey());
    if (Objects.equals(strategyType, PortfolioStrategy.DOLLAR_AVERAGE.name())) {
      while (!value.contains(new SimpleDateFormat("yyyy-MM-dd").format(st))) {
        st = this.addDays(st, 1);
      }
    }

    Date sDate = new Date(System.currentTimeMillis());
    if (Objects.equals(portfolioType, PortfolioType.FLEXIBLE.name())) {
      sDate = st;
    }
    List<String> out = new ArrayList<>();
    out.add(portfolioName + "," + user.getName() + "," + Constant.convertDateToString(
        sDate) + "," + portfolioType);

    this.dataStore.addData(out, Constant.PORTFOLIO_STORE, x -> x);
    List<String> stockData = new ArrayList<>();
    long randv = new Random(System.currentTimeMillis()).nextInt(1000000);
    for (Map.Entry<Stock, Double> stock : stocks.entrySet()) {
      String enConverted = "";
      if (en != null) {
        enConverted = Constant.convertDateToString(en);
      }
      stockData.add(portfolioName + "," + user.getName() + "," + portfolioType + ","
          + stock.getKey().getTicker() + "," + stock.getValue() + ",buy,"
          + Constant.convertDateToString(st)
          + "," + commissionFee + "," + strategyType + "," + moneyInvested + "," + enConverted + ","
          + period + "," + randv);
    }
    this.dataStore.addData(stockData, Constant.PORTFOLIO_TRANSACTIONS_STORE, x -> x);
    return true;
  }

  private Date addDays(Date inp, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(inp);
    cal.add(Calendar.DATE, days);
    return cal.getTime();
  }

  @Override
  public void addRecurringEntries(User user, String portfolioName, Date d)
      throws IOException, ParseException {
    Map<String, String> filters = new HashMap<>();
    filters.put("Portfolio", portfolioName);
    filters.put("OwnedUser", user.getName());
    filters.put("StrategyType", PortfolioStrategy.DOLLAR_AVERAGE.name());
    List<Map<String, String>> outData;
    try {
      outData = this.dataStore.filter(filters, Constant.PORTFOLIO_TRANSACTIONS_STORE, x -> x);
    } catch (NoSuchElementException ex) {
      return;
    }
    Comparator<Map<String, String>> compareByDate = new Comparator<Map<String, String>>() {
      public int compare(Map<String, String> m1, Map<String, String> m2) {
        try {
          return Constant.convertToDate(m1.get("Date"))
              .compareTo(Constant.convertToDate(m2.get("Date")));
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }
      }
    };

    outData.sort(compareByDate.reversed());
    String stockName = "";
    boolean temp = false;
    Map<String, String> groupName = new HashMap<>();
    for (Map<String, String> entry : outData) {
      if (groupName.containsKey(entry.get("GroupName")) && !groupName.get(entry.get("GroupName"))
          .equals(entry.get("Date"))) {
        continue;
      }
      if (!temp) {
        stockName = entry.get("StockName");
        temp = true;
      }
      Date sDate = Constant.convertToDate(entry.get("Date"));
      Date eDate;
      if (entry.get("EndDate") == null || Objects.equals(entry.get("EndDate"), "")) {
        eDate = d;
      } else if (Constant.convertToDate(entry.get("EndDate")).compareTo(d) >= 0) {
        eDate = d;
      } else {
        eDate = Constant.convertToDate(entry.get("EndDate"));
      }

      int period = -1;
      try {
        period = Integer.parseInt(entry.get("Period"));
      } catch (NumberFormatException ex) {
        // perform nothing
      }
      sDate = this.addDays(sDate, period);
      while (sDate.compareTo(eDate) <= 0) {
        if (!this.sModal.getStockData(new StockImpl(stockName))
            .contains(new SimpleDateFormat("yyyy-MM-dd").format(sDate))) {
          sDate = this.addDays(sDate, 1);
          continue;
        }

        List<String> recurData = new ArrayList<>();
        String reData =
            entry.get("Portfolio") + "," + entry.get("OwnedUser") + "," + entry.get("Type") + ","
                + entry.get("StockName") + "," + entry.get("Count") + "," + entry.get("Operation")
                + ","
                + Constant.convertDateToString(sDate) + "," + entry.get("CommissionFee") + ","
                + entry.get("StrategyType") + ","
                + entry.get("MoneyInvested") + "," + (entry.get("EndDate") != null ? entry.get(
                "EndDate") : "") + "," + entry.get("Period") + ","
                + entry.get("GroupName");
        recurData.add(reData);
        this.dataStore.addData(recurData, Constant.PORTFOLIO_TRANSACTIONS_STORE, x -> x);
        sDate = this.addDays(sDate, Integer.parseInt(entry.get("Period")));
      }
      if (!groupName.containsKey(entry.get("GroupName"))) {
        groupName.put(entry.get("GroupName"), entry.get("Date"));
      }
    }
  }
}
