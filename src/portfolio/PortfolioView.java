package portfolio;

import java.util.Map;
import stock.Stock;

/**
 * This interface represents the view of a Portfolio. All the view functions of the view are managed
 * by the Portfolio Controller
 */
public interface PortfolioView {

  /**
   * This method redirects and displays the given string to the required output source.
   *
   * @param out the String to be displayed.
   */
  void returnOutput(String out);

  /**
   * Prompts the initial menu that displays the operations to be performed.
   */
  void promptMenu();

  void promptFlexibleMenu();

  /**
   * show the error message to the user.
   *
   * @param message error message to be displayed
   */
  void showErrorMessage(String message);

  /**
   * Exit message that gets displayed when exiting from the application.
   *
   * @param username name of the user exiting from the application
   */
  void exitMessage(String username);

  /**
   * Message displayed to prompt for stock addition or creation of the portfolio.
   */
  void addStockMessage();

  /**
   * view to display the stocks and their counts in a portfolio.
   *
   * @param stocks list of stocks to be displayed
   */
  void displayComposition(Map<Stock, Double> stocks);

  /**
   * display value of a portfolio when queried for on a particular date.
   *
   * @param portfolio name of the portfolio
   * @param value     value of the portfolio
   */
  void displayStockValue(String portfolio, Double value);

  /**
   * prompt message that displays a message when querying something from the user.
   *
   * @param s message to be displayed when querying
   */
  void prompt(String s);

  /**
   * display the welcome message when entered the application.
   *
   * @param user name of the user to which the welcome message need to be displayed
   */
  void displayWelcomeMessage(String user);

  /**
   * Message to be displayed when a user enters the application.
   */
  void enterUserMessage();

  /**
   * Method to display the amount of money invested in a given portfolio.
   *
   * @param name   name of the portfolio
   * @param date   date at which portfolio needs to be fetched
   * @param amount the amount of money invested in the portfolio
   */
  void displayInvestedAmount(String name, String date, Double amount);


  /**
   * This method displays a graph of the performance of the portfolio within a given date range.
   *
   * @param pfName name of the portfolio
   * @param data   date and value of the portfolio on the given date.
   * @param start  start date of the graph
   * @param end    end date of the graph
   * @param base   the base value of the "*"
   */
  void displayBarGraph(String pfName, Map<String, String> data, String start, String end, int base);

  /**
   * Display the strategy to be chosen.
   */
  void displayStrategy();
}
