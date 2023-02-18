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
 * AdvancedPortfolioModel extends the PortfolioModel interface. It has additional functions to buy
 * individual stocks and sell stocks under a portfolio.
 */
public interface AdvancedPortfolioModel extends PortfolioModel {


  /**
   * Creates a Flexible portfolio and adds the stocks provided to that portfolio.
   *
   * @param name name of the portfolio to be created
   * @param user user creating the portfolio
   * @param s    list of stocks, commissionFee and shares of stocks
   * @param d    date on which the stocks are being created.
   * @return true if successfully created else false
   * @throws IOException if error in writing / reading from file
   */
  boolean createPortfolio(String name, User user, Map<Stock, Pair<Double, Double>> s, Date d)
      throws IOException;


  /**
   * This method allows the user to add a stock to an already existing portfolio.
   *
   * @param name          name of the portfolio.
   * @param user          creator of the portfolio.
   * @param stock         stock to be added to the portfolio.
   * @param count         count of the stock.
   * @param d             date.
   * @param commissionFee commission fee for each transaction.
   * @throws IOException            if file not present.
   * @throws NoSuchElementException if stock not present.
   * @throws ParseException         file parsing fails.
   */
  void buyStock(String name, User user, Stock stock, Double count, Date d, Double commissionFee)
      throws IOException, NoSuchElementException, ParseException;

  /**
   * This method allows the user to remove stock from a portfolio.
   *
   * @param name          name of the portfolio.
   * @param user          creator of the portfolio.
   * @param stock         stock to be added to the portfolio.
   * @param count         count of the stock.
   * @param d             date.
   * @param commissionFee commission fee for each transaction.
   * @throws IOException    if file not present.
   * @throws ParseException file parsing fails.
   */
  void sellStock(String name, User user, Stock stock, Double count, Date d, Double commissionFee)
      throws IOException, ParseException;

  /**
   * This method checks if a portfolio exists for a given user.
   *
   * @param name name of the portfolio.
   * @param user creator of the portfolio.
   * @param d    date.
   * @return boolean value of existance of portfolio.
   * @throws IOException    if file not present.
   * @throws ParseException file parsing fails.
   */
  boolean checkPortfolioExists(String name, String user, Date d) throws IOException, ParseException;

  /**
   * This method gives the cost basis (i.e. the total amount of money invested in a portfolio) by a
   * specific date. This would include all the purchases made in that portfolio till that date.
   *
   * @param name name of the portfolio.
   * @param user creator of the portfolio.
   * @param d    date.
   * @return boolean value of existance of portfolio.
   * @throws IOException    if file not present.
   * @throws ParseException file parsing fails.
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
   * This method gets the composition of a portfolio until a given date. It fetches the stocks and
   * it's count.
   *
   * @param user          creator of the portfolio.
   * @param portfolioName name of the portfolio.
   * @param d             date.
   * @return a map of the stocks and it's count.
   * @throws IOException    if file not present.
   * @throws ParseException file parsing fails.
   */
  Map<Stock, Double> getPortfolioComposition(User user, String portfolioName, Date d)
      throws IOException, ParseException;

  /**
   * This method create a portfolio strategy by taking portfolio details along with the strategy as
   * the inputs.
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
   * @param portfolioType type of the portfolio
   * @return true on successful creation of the portfolio
   * @throws IOException if faced some I/O related exceptions with the files.
   */
  boolean createStrategyPortfolio(User user, String portfolioName, Map<Stock, Double> stocks,
      Double commissionFee, Date st, Date en, String strategyType, long period,
      double moneyInvested, String portfolioType)
      throws IOException;

  /**
   * This method adds the recurring portfolio entries when queried on any latest date.
   *
   * @param user          owner of the portfolio
   * @param portfolioName name of the portfolio
   * @param d             date on which recurring entries to be added
   * @throws IOException    if faced some I/O related exceptions with the files
   * @throws ParseException if not able to parse the date
   */
  void addRecurringEntries(User user, String portfolioName, Date d)
      throws IOException, ParseException;

  /**
   * This method adds a strategy to a portfolio by taking the details along with the strategy as the
   * inputs.
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
   * @param portfolioType type of the portfolio
   * @throws IOException    if faced some I/O related exceptions with the files
   * @throws ParseException if not able to parse the date
   */
  void buyStrategyStock(User user, String portfolioName, Map<Stock, Double> stocks,
      Double commissionFee, Date st, Date en, String strategyType, long period,
      double moneyInvested, String portfolioType)
      throws IOException, ParseException;

}
