import datastore.DataStore;
import datastore.FileDataStore;
import file.CSVFileIO;
import file.FileIO;
import java.util.Scanner;
import portfolio.AdvancedPortfolioModel;
import portfolio.AdvancedPortfolioModelImpl;
import portfolio.CommandController;
import portfolio.GUIController;
import portfolio.GUIFeatures;
import portfolio.GUIView;
import portfolio.GUIViewImpl;
import portfolio.PortfolioControllerImpl;
import portfolio.PortfolioFacadeModel;
import portfolio.PortfolioFacadeModelImpl;
import portfolio.PortfolioModel;
import portfolio.PortfolioModelImpl;
import portfolio.PortfolioView;
import portfolio.PortfolioViewImpl;
import services.AlphaVantageService;
import services.Service;

/**
 * This is the main class through which the application is being run. The main method of this class
 * is the trigger point to start the application.
 */
public class PortfolioRunner {

  /**
   * The main method calls the creates the model, view and controller objects and calls hands over
   * the control to the go method in the controller.
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    FileIO fi = new CSVFileIO();
    DataStore d = new FileDataStore(fi);
    Service serv = new AlphaVantageService(fi);
    AdvancedPortfolioModel adModel = new AdvancedPortfolioModelImpl(
        new FileDataStore(fi), serv);
    PortfolioModel pModel = new PortfolioModelImpl(new FileDataStore(fi), serv);
    PortfolioFacadeModel facadeModel = new PortfolioFacadeModelImpl(pModel, adModel, d, serv);
    Scanner s = new Scanner(System.in);
    String ui;
    while (true) {
      System.out.print("Select the UI:\n1. Text UI\n2. GUI\nEnter the operation: ");
      ui = s.nextLine();
      if (ui.equals("1") || ui.equals("2")) {
        break;
      }
      System.out.println("Enter a valid operation!");
    }
    if (ui.equals("1")) {
      PortfolioView view = new PortfolioViewImpl(System.out);
      CommandController controller = new PortfolioControllerImpl(view, facadeModel, System.in);
      controller.operate();
    } else {
      GUIView view = new GUIViewImpl();
      GUIFeatures guiView = new GUIController(facadeModel, view);
    }


  }

}
