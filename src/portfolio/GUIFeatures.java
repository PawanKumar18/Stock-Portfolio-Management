package portfolio;

import generics.Pair;
import java.util.Map;

/**
 * GUIFeatures class is the controller for the GUI. All the action listeners are defined in this
 * interface.
 */
public interface GUIFeatures extends PortfolioController {

  /**
   * Get username from the user.
   *
   * @param user name of the user.
   * @return boolean value if the username is accepted.
   */
  boolean getUser(String user);

  /**
   * This method interacts with the model to create a portfolio from the given inputs.
   *
   * @param name   name of the portfolio.
   * @param stocks stocks in the portfolio.
   * @param date   date of creation of the portfolio.
   * @return boolean value of successful creation of the portfolio.
   */
  boolean operateCreatePortfolio(String name, Map<String, Pair<String, String>> stocks,
      String date);

  /**
   * This method interacts with the model to create a portfolio based on a strategy from the given
   * inputs.
   *
   * @param name     name of the portfolio.
   * @param stocks   stocks in the portfolio.
   * @param stDate   start date of the portfolio.
   * @param enDate   end date of the portfolio.
   * @param commFee  commission fee for the transaction.
   * @param bc       indicator to create portfolio or buy stocks.
   * @param moneyInv total amount of money invested in a strategy.
   * @param period   time period of the strategy.
   * @param strategy type of strategy - fixed/ dollar cost strategy.
   * @return boolean value of successful creation of the portfolio/ addition of stocks.
   */
  boolean operateStrategyCreateOrBuyPortfolio(String name, Map<String, String> stocks,
      String stDate, String enDate, String commFee, String bc, String moneyInv, String period,
      PortfolioStrategy strategy);

  /**
   * This method interacts with the model to fetch portfolio composition from the model.
   *
   * @param portfolioName name of portfolio.
   * @param date          date at which portfolio composition needs to be fetched.
   */
  void portfolioComposition(String portfolioName, String date);

  /**
   * This method interacts with the model to fetch portfolio value from the model.
   *
   * @param portfolioName name of portfolio.
   * @param date          date at which portfolio value needs to be fetched.
   */
  void portfolioValue(String portfolioName, String date);

  /**
   * This method interacts with the model to buy stocks and add stocks to portfolio from the model.
   *
   * @param portfolioName portfolio name.
   * @param stockName     name of stock.
   * @param count         count of stock.
   * @param date          date on which portfolio needs to be bought.
   * @param commissionFee commission fee for the transaction.
   * @return boolean value of successful addition of stocks.
   */
  boolean operateFlexibleBuyInputs(String portfolioName, String stockName, String count,
      String date, String commissionFee);

  /**
   * This method interacts with the model to sell stocks and remove stocks from portfolio from the
   * model.
   *
   * @param portfolioName portfolio name.
   * @param stockName     name of stock.
   * @param count         count of stock.
   * @param date          date on which portfolio needs to be sold.
   * @param commissionFee commission fee for the transaction.
   */
  void operateFlexibleSellInputs(String portfolioName, String stockName,
      String count, String date, String commissionFee);

  /**
   * This method interacts with the model to fetch the total amount of money invested until a given
   * date.
   *
   * @param portfolioName name of the portfolio.
   * @param date          date on which amount needs to be fetched.
   */
  void operateInvestedAmount(String portfolioName, String date);

  /**
   * This method interacts with the model to fetch the performance graph of a portfolio over a
   * period of time.
   *
   * @param portfolioName name of the portfolio.
   * @param stDate        start date of graph.
   * @param enDate        end date of the graph.
   */
  void drawGraph(String portfolioName, String stDate, String enDate);

}
