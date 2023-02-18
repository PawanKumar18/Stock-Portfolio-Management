package portfolio;

/**
 * GUIView class extends the PortfolioView interface. It has methods specific to the Graphical User
 * Interface.
 */
public interface GUIView extends PortfolioView {


  /**
   * Method to display window to add stocks to the portfolio.
   */
  void addStocks();

  /**
   * This method maps all the action listeners between view and controller.
   *
   * @param features object of the action listeners.
   */
  void addFeatures(GUIFeatures features);
}
