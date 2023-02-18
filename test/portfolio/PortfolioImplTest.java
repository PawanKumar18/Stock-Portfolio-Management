package portfolio;

import static org.junit.Assert.assertEquals;

import constant.Constant;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import stock.Stock;
import stock.StockImpl;
import user.UserImpl;

/**
 * Junit tests for PortfolioImpl class.
 */
public class PortfolioImplTest extends AbstractTest {


  @Test
  public void testPortfolio() throws ParseException {
    Portfolio p;
    List<String> s = new ArrayList<>();
    String name;
    String date = "12/21/2021";
    String user;
    for (int i = 0; i < 100; i++) {
      name = this.generateRandomString();
      user = this.generateRandomString();
      Map<Stock, Double> stocks = new HashMap<>();
      for (int k = 0; k < (int) (Math.random() * 100); k++) {
        stocks.put(new StockImpl(this.generateRandomString()), (Math.random() * 100));
      }
      p = new PortfolioImpl(name, new UserImpl(user), stocks,
          new SimpleDateFormat(Constant.DATE_FORMAT).parse(date));

      assertEquals(name, p.getName());
      assertEquals(user, p.getOwnedUser().getName());
      assertEquals(date, new SimpleDateFormat(Constant.DATE_FORMAT).format(p.getCreationDate()));
      assertEquals(stocks.size(), p.getStocks().size());
      for (var entry : stocks.entrySet()) {
        assertEquals(stocks.get(entry.getValue()), p.getStocks().get(entry.getValue()));
      }

    }
  }


}