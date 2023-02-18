package constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * This class stores all the Constants and message variables that are used across the application.
 * This improves readability and message re-usability.
 */
public class Constant {

  public static final String ERROR_TAG = "[ERROR]: ";
  public static final String CREATE_PORTFOLIO = "Create Portfolio";
  public static final String PORTFOLIO_COMPOSITION = "View Portfolio Composition";
  public static final String PORTFOLIO_VALUE = "Get Portfolio Value";
  public static final String PORTFOLIO_ADD_STOCK = "Buy Portfolio Stocks";
  public static final String PORTFOLIO_REMOVE_STOCK = "Sell Portfolio Stocks";

  public static final String EXIT = "Exit";
  public static final String BACK = "Back";
  public static final String COMM_FEE_ERR = "Commission Fee cannot be negative";
  public static final String PERIOD_ERR = "Period can be a positive number";
  public static final String MONEY_INV_ERR = "Money Invested cannot be "
      + "negative or less than commission fee";
  public static final String PORTFOLIO_EMPTY_ERR = "Portfolio name cannot be empty!";
  public static final String PORTFOLIO_ALREADY_EXIST = "Portfolio already exists!";
  public static final String EXIT_MESSAGE = "Exiting from the portfolio app!\nBye Bye ";
  public static final String OP_VALUES_ERR = "Value entered is out of the specified numbers!";
  public static final String STOCK_PROMPT = "Please enter a valid stock ticker";
  public static final String STOCK_ERR = "Stock Ticker is invalid!";
  public static final String OP_NUMB = "Enter the number of the operation";
  public static final String OP_PERFORM = "Operations that can be performed:";
  public static final String ADD_STOCK_MSG = "1. Add Stock ?\n2. Create Portfolio with"
      + " current stocks ?";
  public static final String STOCK_COMP_HEADER = "Composition of Portfolio:\nStocks\t|\t"
      + "Stock Counts\n";
  public static final String DATE_INVALID = "Entered Date is invalid!";
  public static final String DATE_FUTURE_ERR = "Date cannot be in future!";
  public static final String DATE_FORMAT = "MM/dd/yyyy";
  public static final String USER_EMPTY_ERR = "Username cannot be empty!";
  public static final String STOCK_COUNT_EMPTY_ERR = "Stock count must be "
      + "greater than zero and non-fractional!";
  public static final String STOCK_DUPLICATE_ERR = "Value must be Y/N";
  public static final String ADD_STOCK_OP_ERR = "Value must be either 1 or 2!";
  public static final String DELETE_ADD_STOCK_OP_ERR = "Value must be either 1 or 2 or 3!";
  public static final String DATE_PROMPT = "Enter the date in mm/dd/yyyy";
  public static final String PORTFOLIO_SUCCESS = "Portfolio Successfully Created!";

  public static final String STOCK_ADD_SUCCESS = "Stock Successfully Bought to the Portfolio!";
  public static final String STOCK_DELETE_SUCCESS = "Stock Successfully removed "
      + "from the Portfolio!";

  public static final String PORTFOLIO_EXISTS = "Portfolio already exists!";
  public static final String PORTFOLIO_STORE = "data/portfolios.csv";

  public static final String PORTFOLIO_TRANSACTIONS_STORE = "data/portfolio_transactions.csv";

  public static final String STOCK_VALUE_NOT_FOUND = "Stock value for stock "
      + "%s does not exist on given date %s";
  public static final String PORTFOLIO_NOT_FOUND = "Portfolio %s does not "
      + "exist for the user %s";

  public static final String PORTFOLIO_NOT_FOUND_DATE = PORTFOLIO_NOT_FOUND + " on date %s";
  public static final String PORTFOLIO_VALUE_MSG = "Portfolio %s value: $%.10f\n";
  public static final String PORTFOLIO_PROMPT_MSG = "Enter the name of portfolio";
  public static final String USR_PROMPT_MSG = "Please Enter username";
  public static final String WELCOME_MESSAGE = "Hello, %s\n";
  public static final String STOCK_TICKER_MSG = "Stock %d Ticker";

  public static final String STOCK_TICKER_MSG_ENTER = "Enter the Stock Ticker";
  public static final String STOCK_CNT_MSG = "Stock %d Count";

  public static final String STOCK_CNT_MSG_ENTER = "Enter the Stock Count";
  public static final String STOCK_EXIST_PROMPT = "Stock Value already added to "
      + "portfolio, Would you like to replace it ? (Y/N)";
  public static final String USER_MESSAGE = "1. Enter Username ? \n2. Exit";

  public static final String ALPHA_VANTAGE_API = "https://www.alphavantage.co/query?datatype=csv&";
  public static final String ALPHA_TIME_INTRADAY = "TIME_SERIES_INTRADAY";
  public static final String ALPHA_TIME_DAILY = "TIME_SERIES_DAILY";
  public static final String ALPHA_API_KEY = "147LGXMNKZFU94BN";
  public static final String ALPHA_INVALID_API = "\"Error Message\": \"Invalid API call.";
  public static final String STOCK_SELL_ERR = "Given shares %d of the Stock %s are not "
      + "present on the date %s in the portfolio %s";
  public static final String COMMISSION_FEE_PROMPT = "Commission Fee for transaction (in $)";
  public static final String COMMISSION_FEE_ERR = "Commission Fee can't be empty or negative";
  public static final String INVESTED_VALUE = "Get Portfolio Invested Amount";
  public static final String PORTFOLIO_INVESTED_AMNT = "Total invested amount in portfolio %s "
      + "till date %s is $%.6f";
  public static final String PLOT_BAR = "Draw Bar Plot";
  public static final String START_DATE_PROMPT = "Enter the start date (MM/DD/YYYY format)";
  public static final String END_DATE_PROMPT = "Enter the End date (MM/DD/YYYY format)";
  public static final String PORTFOLIO_TYPE_MESSAGE = "Please select one of Portfolio Types:"
      + "\n1. Flexible\n2. UnFlexibile\n3. Exit";
  public static final String PORTFOLIO_TYPE_INPUT = "Enter Portfolio type";
  public static final String STOCK_EMPTY = "Please add Stocks before portfolio Creation";


  public static final Map<String, String> DATA_HEADERS = Map.of(
      Constant.PORTFOLIO_STORE, "Portfolio,OwnedUser,CreationDate,Type",
      Constant.PORTFOLIO_TRANSACTIONS_STORE,
      "Portfolio,OwnedUser,Type,StockName,Count,Operation,Date,CommissionFee"
  );

  public static String convertDateToString(Date d) {
    return new SimpleDateFormat(DATE_FORMAT).format(d);
  }

  public static Date convertToDate(String d) throws ParseException {
    return new SimpleDateFormat(DATE_FORMAT).parse(d);
  }
}

