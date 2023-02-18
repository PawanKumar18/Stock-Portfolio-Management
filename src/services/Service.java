package services;

import java.io.IOException;
import java.util.Date;
import java.util.NoSuchElementException;
import stock.Stock;

/**
 * Service is an interface that contains methods that interact with stocks. It has methods to check
 * if a stock exists and to fetch a value of a stock on a given date.
 */
public interface Service {

  /**
   * This method checks if a stock exists.
   *
   * @param s name of a stock.
   * @return boolean value indicating the presence of a portfolio.
   * @throws IOException error while reading a file.
   */
  boolean checkStockExists(String s) throws IOException;

  /**
   * this method gets the value of a stock on a particular date.
   *
   * @param s name of a stock.
   * @param d date
   * @return value of a stock on a given date.
   * @throws NoSuchElementException error when stock doesn't exist.
   * @throws IOException            error while reading a file.
   */
  Double getStockValue(Stock s, Date d) throws NoSuchElementException, IOException;

  String getStockData(Stock s) throws NoSuchElementException, IOException;


}
