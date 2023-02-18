package stock;

import java.util.Date;
import java.util.Objects;

/**
 * StockImpl implements Stock interface. It has getter methods to fetch ticker value of a stock.
 */
public class StockImpl implements Stock {

  private final String ticker;
  private Date date;
  private Double value;

  /**
   * Constructs a StockImpl class by taking ticker as the input.
   *
   * @param ticker ticker of the stock
   */
  public StockImpl(String ticker) {
    this.ticker = ticker;
  }


  @Override
  public String getTicker() {
    return this.ticker;
  }

  @Override
  public void setDate(Date d) {
    this.date = d;
  }

  @Override
  public void setValue(Double d) {
    this.value = d;
  }

  @Override
  public Double getValue() {
    return this.value;
  }

  @Override
  public Date getDate() {
    return this.date;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Stock)) {
      return false;
    }
    return Objects.equals(this.ticker, ((Stock) obj).getTicker());
  }

  @Override
  public int hashCode() {
    return this.ticker.hashCode();
  }

  @Override
  public String toString() {
    return this.ticker;
  }

}
