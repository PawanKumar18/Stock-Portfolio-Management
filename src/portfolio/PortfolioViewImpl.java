package portfolio;

import constant.Constant;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import stock.Stock;

/**
 * PortfolioViewImpl implements PortfolioView interface. It has the outstream object and has
 * functions to display different messages.
 */
public class PortfolioViewImpl implements PortfolioView {

  private final PrintStream out;

  /**
   * Constructor for the PortfolioViewImpl class. It initializes the output stream to the given
   * stream.
   *
   * @param p print stream object
   */
  public PortfolioViewImpl(PrintStream p) {
    this.out = p;
  }

  @Override
  public void returnOutput(String out) {
    this.out.println(out);
  }

  @Override
  public void promptMenu() {
    this.out.println(Constant.OP_PERFORM);
    Map<Integer, String> cmds = new HashMap<>();
    cmds.put(1, Constant.CREATE_PORTFOLIO);
    cmds.put(2, Constant.PORTFOLIO_COMPOSITION);
    cmds.put(3, Constant.PORTFOLIO_VALUE);
    cmds.put(4, Constant.BACK);
    for (Map.Entry<Integer, String> cmd : cmds.entrySet()) {
      this.out.println(cmd.getKey() + ". " + cmd.getValue());
    }
  }

  @Override
  public void promptFlexibleMenu() {
    this.out.println(Constant.OP_PERFORM);
    Map<Integer, String> cmds = new HashMap<>();
    cmds.put(1, Constant.CREATE_PORTFOLIO);
    cmds.put(2, Constant.PORTFOLIO_COMPOSITION);
    cmds.put(3, Constant.PORTFOLIO_VALUE);
    cmds.put(4, Constant.PORTFOLIO_ADD_STOCK);
    cmds.put(5, Constant.PORTFOLIO_REMOVE_STOCK);
    cmds.put(6, Constant.PLOT_BAR);
    cmds.put(7, Constant.INVESTED_VALUE);
    cmds.put(8, Constant.BACK);
    for (Map.Entry<Integer, String> cmd : cmds.entrySet()) {
      this.out.println(cmd.getKey() + ". " + cmd.getValue());
    }
  }

  @Override
  public void showErrorMessage(String error) {
    this.out.println(Constant.ERROR_TAG + error);
  }

  @Override
  public void exitMessage(String username) {
    this.out.println(Constant.EXIT_MESSAGE + username);
  }

  @Override
  public void addStockMessage() {
    this.out.println(Constant.ADD_STOCK_MSG);
  }

  @Override
  public void displayComposition(Map<Stock, Double> portfolio) {
    StringBuilder out = new StringBuilder(Constant.STOCK_COMP_HEADER);
    for (Map.Entry<Stock, Double> entry : portfolio.entrySet()) {
      out.append(entry.getKey().getTicker()).append("\t|\t")
          .append(String.format("%.2f", entry.getValue())).append("\n");
    }
    this.out.printf(out.toString());
  }

  @Override
  public void displayStockValue(String portfolio, Double value) {
    this.out.printf(Constant.PORTFOLIO_VALUE_MSG, portfolio, value);
  }

  @Override
  public void prompt(String s) {
    this.out.print(s + ": ");
  }

  @Override
  public void displayWelcomeMessage(String user) {
    this.out.printf(Constant.WELCOME_MESSAGE, user);
  }

  @Override
  public void enterUserMessage() {
    this.out.println(Constant.USER_MESSAGE);
  }

  @Override
  public void displayInvestedAmount(String name, String d, Double a) {
    this.out.printf(Constant.PORTFOLIO_INVESTED_AMNT + "\n", name, d, a);
  }

  @Override
  public void displayBarGraph(String pfName, Map<String, String> data, String start, String end,
      int base) {
    this.out.println("Performance of " + pfName + " from " + start + " to " + end);
    for (Map.Entry<String, String> dat : data.entrySet()) {
      this.out.println(dat.getKey() + " : " + dat.getValue());
    }
    this.out.println("Scale: * = $" + base);
  }

  @Override
  public void displayStrategy() {
    this.out.print("1. Fixed Value Strategy\n2. Dollar Cost Averaging\n3. Buy Stock shares\n");
  }
}
