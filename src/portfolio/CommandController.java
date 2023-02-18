package portfolio;

/**
 * CommandController class extends the PortfolioController interface. It is the controller for the
 * console based UI.
 */
public interface CommandController extends PortfolioController {

  /**
   * This method delegates work to the model and view of portfolio as required.
   */
  void operate();
}
