package portfolio;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import stock.Stock;
import user.User;

/**
 * This interface represents the model of a Portfolio. It queries / stores all the data related to
 * the portfolio, performs data validations pre/post querying.
 */
public interface PortfolioModel {

  /**
   * This method creates a Portfolio from the portfolio name, user name and stocks.
   *
   * @param name name of the portfolio.
   * @param s    all the stocks belonging to the portfolio.
   * @param user the use creating the portfolio.
   * @return if the portfolio is successfully created or not.
   * @throws IOException thrown when error in accessing a file
   */
  boolean createPortfolio(String name, Map<Stock, Double> s, User user) throws IOException;

  /**
   * this method returns the composition of a Portfolio.
   *
   * @param user          the use creating the portfolio.
   * @param portfolioName name of the portfolio.
   * @return A map of Stock and the count of the stocks in a given portfolio.
   * @throws NoSuchElementException thrown no user exists
   * @throws IOException            thrown when file isn't accessible
   * @throws ParseException         thrown when error in parsing file
   */
  Map<Stock, Double> getPortfolioComposition(User user, String portfolioName)
      throws IOException, ParseException, NoSuchElementException;

  /**
   * Returns the value of the portfolio on a given date.
   *
   * @param user          the use creating the portfolio.
   * @param portfolioName name of the portfolio.
   * @param date          date at which the value of the portfolio is required.
   * @return the total value of all the stocks in the porfolio.
   * @throws IOException    thrown when file isn't accessible
   * @throws ParseException thrown when error in parsing file
   */
  Double getPortfolioValue(User user, String portfolioName, Date date)
      throws IOException, ParseException;

  /**
   * this method checks if the stock exists.
   *
   * @param stock the stock Objects whose existance needs to be determined.
   * @return boolean value of the stock's existance.
   * @throws IOException thrown when file isn't accessible.
   */
  boolean checkStockExists(Stock stock) throws IOException;

  /**
   * this method checks if the portfolio exists under a given user.
   *
   * @param name name of the portfolio.
   * @param user the use creating the portfolio.
   * @return boolean value of the portfolio's existance.
   * @throws IOException thrown when file isn't accessible.
   */
  boolean checkPortfolioExists(String name, String user) throws IOException;

  /**
   * Get the Portfolio with the given inputs.
   *
   * @param name name of the portfolio to be brought
   * @param user user who owns the portfolio
   * @return portfolio if exist
   * @throws IOException if error in reading the files
   */
  Portfolio getPortfolio(String name, String user) throws IOException;

  /**
   * Mapper that maps the given CSV lines to the portfolio.
   *
   * @param dbInput list of csv lines to be mapped
   * @return list of portfolio objects upon mapping
   * @throws ParseException when error in parsing date
   * @throws IOException    when error in reading files
   */
  List<Portfolio> mapCSVToPortfolio(List<Map<String, String>> dbInput)
      throws ParseException, IOException;
}
