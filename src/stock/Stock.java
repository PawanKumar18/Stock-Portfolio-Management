package stock;

import java.util.Date;

/**
 * This interface represents an individual stock. A stock has a ticker which is unique to itself.
 */
public interface Stock {

  /**
   * This is a getter method that return a ticker value of the given stock.
   *
   * @return ticker value of the stock as a String.
   */
  String getTicker();

  /**
   * This is a setter method that sets the date of the stock.
   *
   * @param d date
   */
  void setDate(Date d);

  /**
   * This is a setter method that sets the value of the stock.
   *
   * @param d value of the stock.
   */
  void setValue(Double d);

  /**
   * This is a getter method to get the value of a stock.
   *
   * @return value of stock.
   */
  Double getValue();

  /**
   * This is a getter method to get the date of a stock.
   *
   * @return date of a stock.
   */
  Date getDate();
}
