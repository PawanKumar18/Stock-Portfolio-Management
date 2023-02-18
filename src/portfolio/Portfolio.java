package portfolio;

import java.util.Date;
import java.util.Map;
import stock.Stock;
import user.User;

/**
 * This interface represents the Portfolio. A portfolio constitutes of a group of stocks and it is
 * created by a user.
 */
public interface Portfolio {

  /**
   * This is a getter method that returns the name of the given portfolio.
   *
   * @return name of Portfolio as a String.
   */
  String getName();

  /**
   * This is a getter method that returns the list of Stocks and their counts in a given Portfolio.
   *
   * @return list of Stocks and it's count as a map
   */
  Map<Stock, Double> getStocks();

  /**
   * This is a getter method that returns the date of creation of the given portfolio.
   *
   * @return date of creation of the portfolio.
   */
  Date getCreationDate();

  /**
   * This getter method that returns the user who is the creator of the Portfolio.
   *
   * @return creator of the portfolio
   */
  User getOwnedUser();
}
