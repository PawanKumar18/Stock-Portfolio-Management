package portfolio;

import java.util.Date;
import java.util.Map;
import stock.Stock;
import user.User;

/**
 * The PortfolioImpl class implements Portfolio. It contains data members to store the portfolio
 * name, stocks and date created. It has functions to create and fetch a portfolio and fetch the
 * value of a portfolio.
 */
public class PortfolioImpl implements Portfolio {

  private final String name;
  private final Map<Stock, Double> stocks;
  private final Date date;
  private final User user;

  /**
   * Constructor for the PortfolioImpl class. It initializes the data variables to given values.
   *
   * @param name   name of the portfolio.
   * @param user   creator of the portfolio.
   * @param stocks stocks in the portfolio along with count.
   * @param d      date of creation of the portfolio.
   */
  public PortfolioImpl(String name, User user, Map<Stock, Double> stocks, Date d) {
    this.name = name;
    this.stocks = stocks;
    this.date = d;
    this.user = user;
  }

  /**
   * This is a getter method used to fetch the name of the portfolio.
   *
   * @return the name of the portfolio.
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * This is a getter method used to fetch the stocks in the portfolio.
   *
   * @return the stocks in the portfolio.
   */
  @Override
  public Map<Stock, Double> getStocks() {
    return this.stocks;
  }

  /**
   * This is a getter method used to fetch the creation date of the portfolio.
   *
   * @return the date of the portfolio creation.
   */
  @Override
  public Date getCreationDate() {
    return this.date;
  }

  /**
   * This is a getter method used to fetch the creator of the portfolio.
   *
   * @return the creator of the portfolio.
   */
  @Override
  public User getOwnedUser() {
    return this.user;
  }
}
