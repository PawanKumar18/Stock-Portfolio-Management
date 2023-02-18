package portfolio;

import generics.Pair;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;
import stock.Stock;
import user.User;

/**
 * PortfolioFacadeModel is a facade interface for the flexible and inflexible portfolios. It has
 * methods to delegate operations to both flexible and inflexible portfolios as the case may be.
 */
public interface PortfolioFacadeModel {

  /**
   * This methods gets the type of a portfolio given a portfolio name and the name of the user.
   *
   * @param name name of the portfolio.
   * @param user creator of the portfolio.
   * @return enum of the type of portfolio.
   * @throws IOException if datastore file is not found.
   */
  PortfolioType getPortfolioType(String name, User user) throws IOException;

  /**
   * This method creates an inflexible portfolio from the given stocks and it's count for a given
   * user.
   *
   * @param name name of the portfolio.
   * @param s    stocks with it's counts.
   * @param user creator of the portfolio.
   * @throws IOException if datastore file is not found.
   */
  void createUnFlexiblePortfolio(String name, Map<Stock, Double> s, User user)
      throws IOException;

  /**
   * This method creates an flexible portfolio from the given stocks and it's count for a given
   * user.
   *
   * @param name name of the portfolio.
   * @param s    stocks with its count and the commission value.
   * @param user creator of the portfolio.
   * @param date date of creation of portfolio
   * @throws IOException if datastore file is not found.
   */
  void createFlexiblePortfolio(String name, Map<Stock, Pair<Double, Double>> s, User user,
      Date date)
      throws IOException;

  /**
   * This method fetches the composition of a flexible portfolio. It fetches the stocks and the
   * count of those stocks in a portfolio till a given date.
   *
   * @param user          creator of the portfolio.
   * @param portfolioName name of the portfolio.
   * @param d             date
   * @return a map of the stocks and its count.
   * @throws IOException if datastore file is not found.
   */
  Map<Stock, Double> getFlexiblePortfolioComposition(User user, String portfolioName, Date d)
      throws IOException, ParseException, NoSuchElementException;

  /**
   * This method fetches the composition of a inflexible portfolio. It fetches the stocks and the
   * count of those stocks in a portfolio.
   *
   * @param user          creator of the portfolio.
   * @param portfolioName name of the portfolio.
   * @return a map of the stocks and its count.
   * @throws IOException if datastore file is not found.
   */
  Map<Stock, Double> getUnFlexiblePortfolioComposition(User user, String portfolioName)
      throws IOException, ParseException, NoSuchElementException;


  /**
   * This method fetches the value of a flexible portfolio. It fetches the stocks and the count of
   * those stocks in a portfolio on a given date and computes its value.
   *
   * @param user          creator of the portfolio.
   * @param portfolioName name of the portfolio.
   * @param date          date
   * @return the value of a portfolio on given date.
   * @throws IOException if datastore file is not found.
   */
  Double getFlexiblePortfolioValue(User user, String portfolioName, Date date)
      throws IOException, ParseException;

  /**
   * This method fetches the value of a inflexible portfolio. It fetches the stocks and the count of
   * those stocks in a portfolio and computes its value on a given date.
   *
   * @param user          creator of the portfolio.
   * @param portfolioName name of the portfolio.
   * @param date          date
   * @return the value of a portfolio on given date.
   * @throws IOException if datastore file is not found.
   */
  Double getUnFlexiblePortfolioValue(User user, String portfolioName, Date date)
      throws IOException, ParseException;

  /**
   * This method checks if a stock ticker exists.
   *
   * @param stock stock value.
   * @return a boolean value of the existance of a stock.
   * @throws IOException if API fetch fails.
   */
  boolean checkStockExists(Stock stock) throws IOException;

  /**
   * This method checks if a portfolio exists for a given user.
   *
   * @param name name of a portfolio.
   * @param d    date on which portfolio should exist.
   * @param type the portfolio type enum - flexible, inflexible
   * @return a boolean value of the existence of a portfolio on a given date.
   * @throws IOException    if API fetch fails.
   * @throws ParseException if date parsing fails - incorrect date
   */
  boolean checkPortfolioExists(String name, String user, Date d, PortfolioType type)
      throws IOException, ParseException;

  /**
   * This method allows to buy more stocks for a flexible portfolio.
   *
   * @param name          name of a portfolio.
   * @param user          creator of the portfolio.
   * @param stock         Stock
   * @param count         the number of stocks.
   * @param d             date on which stocks need to be bought.
   * @param commissionFee the commission fee for buying stocks.
   * @throws IOException            if API fetch fails.
   * @throws NoSuchElementException when stock or portfolio doesn't exist.
   * @throws ParseException         if date parsing fails - incorrect date
   */
  void buyStock(String name, User user, Stock stock, Double count, Date d, Double commissionFee)
      throws IOException, NoSuchElementException, ParseException;

  /**
   * This method allows to sell stocks from a flexible portfolio.
   *
   * @param name          name of a portfolio.
   * @param user          creator of the portfolio.
   * @param stock         Stock
   * @param count         the number of stocks.
   * @param d             date on which stocks need to be sold.
   * @param commissionFee the commission fee for buying stocks.
   * @throws IOException    if API fetch fails.
   * @throws ParseException if date parsing fails - incorrect date
   */
  void sellStock(String name, User user, Stock stock, Double count, Date d, Double commissionFee)
      throws IOException, ParseException;


  /**
   * This method gets the cost basis of a flexible portfolio (i.e the total amount of money invested
   * to buy the stocks + commission fee for buying and selling the stocks till a given date )
   *
   * @param name name of the portfolio.
   * @param user creator of the portfolio.
   * @param d    date
   * @return the total money invested in the portfolio while buying stocks + commission fees.
   * @throws IOException    if API call fails.
   * @throws ParseException if date parsing fails - incorrect date
   */
  double moneyInvested(String name, String user, Date d) throws IOException, ParseException;

  /**
   * This method gets the  performance of a flexible portfolio within a given time period. User has
   * to provide the start date and end date between which performance graph needs to be plotted.
   *
   * @param name  name of the portfolio.
   * @param user  creator of the portfolio.
   * @param sDate start date.
   * @param eDate end date.
   * @return a map of the dates and value of portfolio on those dates.
   * @throws IOException    if API call fails.
   * @throws ParseException f date parsing fails - incorrect date
   */
  Map<Long, Double> getPortfolioPerformance(User user, String name, Date sDate, Date eDate)
      throws ParseException, IOException;

  /**
   * calls the flexible model to create a strategy portfolio.
   *
   * @param user          creator of the portfolio
   * @param portfolioName name of the portfolio
   * @param stocks        list of stocks with which portfolio is created
   * @param commissionFee commission fee of the transaction
   * @param st            start date of the strategy
   * @param en            end date of the strategy
   * @param strategyType  type of the strategy
   * @param period        period in which the money has to be invested
   * @param moneyInvested total money invested into the strategy
   * @return true on successful creation of the portfolio
   * @throws IOException if faced some I/O related exceptions with the files.
   */
  boolean createStrategyPortfolio(User user, String portfolioName, Map<Stock, Double> stocks,
      Double commissionFee, Date st, Date en, String strategyType, long period,
      double moneyInvested, PortfolioType type)
      throws IOException;

  /**
   * This method calls the flexible model to add a strategy to the existing portfolio.
   *
   * @param user          user who wants to create the strategy
   * @param portfolioName portfolio name on which the strategy has to be created
   * @param stocks        stocks to by brought on the portfolio
   * @param commissionFee commission fee of the transaction
   * @param st            start date of the strategy
   * @param en            end date of the strategy
   * @param strategyType  type of the strategy
   * @param period        period in which the money has to be invested
   * @param moneyInvested total money invested into the strategy
   * @throws IOException    if faced some I/O related exceptions with the files
   * @throws ParseException if not able to parse the date
   */
  void buyStrategyStock(User user, String portfolioName, Map<Stock, Double> stocks,
      Double commissionFee, Date st, Date en, String strategyType, long period,
      double moneyInvested)
      throws IOException, ParseException;

}
