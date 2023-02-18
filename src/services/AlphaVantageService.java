package services;

import constant.Constant;
import file.FileIO;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import stock.Stock;

/**
 * This class implements the Service interface. It fetches stock values using the AlphaVantage API.
 */
public class AlphaVantageService implements Service {

  private final FileIO fileIO;
  private final Map<String, Boolean> stockCheck;
  private final Map<String, String> stockData;

  /**
   * Constructor for the AlphaVantageService class. It initializes the fileIO value;
   *
   * @param fileIO fileIO object.
   */
  public AlphaVantageService(FileIO fileIO) {
    this.fileIO = fileIO;
    this.stockCheck = new HashMap<>();
    this.stockData = new HashMap<>();
  }


  private String apiRequest(String function, String symbol, String interval, String outputSize)
      throws IOException {
    String endpoint =
        Constant.ALPHA_VANTAGE_API + "function=" + function
            + "&symbol=" + symbol + "&apikey="
            + Constant.ALPHA_API_KEY;
    if (interval != null) {
      endpoint += "&interval=" + interval;
    }
    if (outputSize != null) {
      endpoint += "&outputsize=" + outputSize;
    }

    URL url = new URL(endpoint);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    con.connect();

    InputStream is = con.getInputStream();
    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      response.append(line).append("\n");
    }
    rd.close();
    if (response.toString().contains(Constant.ALPHA_INVALID_API)) {
      return null;
    }
    return response.toString();
  }

  @Override
  public boolean checkStockExists(String s) throws IOException {
    if (this.stockCheck.containsKey(s.toLowerCase())) {
      return this.stockCheck.get(s);
    }
    String out = this.apiRequest(Constant.ALPHA_TIME_INTRADAY, s,
        "1min", null);
    if (out == null) {
      return false;
    }
    boolean oo = this.fileIO.read(new HashMap<>(),
        new InputStreamReader(new ByteArrayInputStream(out.getBytes()))).size() > 0;
    if (oo) {
      this.stockCheck.put(s.toLowerCase(), true);
      return true;
    } else {
      this.stockCheck.put(s.toLowerCase(), false);
      return false;
    }
  }

  @Override
  public Double getStockValue(Stock s, Date d)
      throws NoSuchElementException, IOException {
    List<Map<String, String>> oo;
    if (d.compareTo(new Date(System.currentTimeMillis())) > 0) {
      throw new IllegalArgumentException(Constant.DATE_FUTURE_ERR);
    }
    if (this.checkStockExists(s.getTicker())) {
      if (Constant.convertDateToString(d)
          .equals(Constant.convertDateToString(new Date(System.currentTimeMillis())))) {
        String out = this.apiRequest(Constant.ALPHA_TIME_INTRADAY,
            s.getTicker(), "1min", null);
        oo = this.fileIO.read(new HashMap<>(),
            new InputStreamReader(new ByteArrayInputStream(out.getBytes())));

      } else {
        String out;
        if (this.stockData.containsKey(s.getTicker().toLowerCase())) {
          out = this.stockData.get(s.getTicker().toLowerCase());
        } else {
          out = this.apiRequest(Constant.ALPHA_TIME_DAILY,
              s.getTicker(), null, "full");
          this.stockData.put(s.getTicker().toLowerCase(), out);
        }
        Map<String, String> filter = new HashMap<>();
        filter.put("timestamp", new SimpleDateFormat("YYYY-MM-dd").format(d));
        oo = this.fileIO.read(filter,
            new InputStreamReader(new ByteArrayInputStream(out.getBytes())));
      }
      if (oo.size() > 0) {
        return Double.parseDouble(oo.get(0).get("close"));
      }
      return this.getStockValue(s, new Date(d.getTime() + 1000L * 60 * 60 * 24));
    }
    throw new NoSuchElementException(String.format(Constant.STOCK_VALUE_NOT_FOUND,
        s.getTicker(), Constant.convertDateToString(d)));
  }

  @Override
  public String getStockData(Stock s) throws NoSuchElementException, IOException {
    if (!this.stockData.containsKey(s.getTicker())) {
      this.stockData.put(s.getTicker(),
          this.apiRequest(Constant.ALPHA_TIME_DAILY, s.getTicker(), null, "full"));
    }
    return this.stockData.get(s.getTicker());
  }

}
