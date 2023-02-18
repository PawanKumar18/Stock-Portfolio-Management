package portfolio;

import constant.Constant;
import generics.Pair;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import stock.Stock;


/**
 * GUIViewImpl class extends JFrame interface and implements GUIView interface. It has all the
 * methods to display windows and interact with the user.
 */
public class GUIViewImpl extends JFrame implements GUIView {

  private JTextField userNameInput;
  private JButton submitUser;
  private final JButton getPortCompBtn;
  private final JButton getPortfolioValueBtn;
  private final JButton sellStockBtn;
  private final JButton getPortfAmtInvstedValBtn;
  private JTextField portfolioNameCreatePort;
  private JLabel getPortfolioNamePortComp;
  private JTextField portfolioNamePortComp;
  private JLabel getDatePortComp;
  private JTextField datePortComp;
  private JTextField portfolioNamePortVal;
  private JTextField datePortVal;
  private JTextField dateSellPort;
  private JTextField portfolioNameSellPort;
  private JTextField stockNameSellPort;
  private JTextField stockCountSellPort;
  private JTextField commissionFeeSellPort;

  private JTextField portfolioNamePortAmtInv;
  private JTextField datePortAmtInv;
  private final JButton addStockBtnMain;
  private final JButton buyStockBtnMain;
  private final JButton addStockBtnSec;
  private final JButton createPortfolioBtn;
  private JFrame userPrompt;
  private JFrame addStockFrame;
  private JPanel createPortfolio;
  private JPanel viewPortfolioComp;
  private JPanel viewPortfolioVal;
  private JPanel addStockPortfolio;
  private JPanel sellStocksPortfolio;
  private JPanel viewPortfolioAmtInvst;
  private JPanel cards;
  private JPanel panel1;
  private List<List<JTextField>> stockInputs;
  private final JComboBox strateList;
  private final JComboBox buystrateList;
  private JTextField strategyCommission;
  private JTextField strategyStart;
  private JTextField strategyEnd;
  private JTextField strategyInvested;
  private JTextField strategyPeriod;
  private JTextField buyStockName;
  private JTextField buyStockCount;
  private final JButton createPortfolioStrategyBtn;
  private final JButton buyPortfolioBtn;
  private final JButton buyPortfolioStrategyBtn;
  private JTextField portfolioNameBuyPort;
  private JPanel viewGraph;
  private JTextField graphPortfolioName;
  private JTextField graphStDate;
  private JTextField graphEnDate;
  private final JButton graphBtn;

  JComboBox menu;

  /**
   * Constructor for the GUIViewImpl class. It initializes all the data members and buttons.
   */
  public GUIViewImpl() {

    //setSize(500, 300);
    setLocation(600, 300);
    stockInputs = new ArrayList<>();
    String[] strategyTypes = {"Fixed Value Strategy", "Dollar Cost Averaging Strategy",
        "Buy Stock shares"};
    strateList = new JComboBox(strategyTypes);
    buystrateList = new JComboBox(strategyTypes);
    menu = new JComboBox();
    menu.setActionCommand("flexPortOption");

    graphBtn = new JButton("Get Graph");
    graphBtn.setActionCommand("getGraph");
    createPortfolioStrategyBtn = new JButton("Create Portfolio");
    createPortfolioStrategyBtn.setActionCommand("createStrategyPortfolio");
    buyPortfolioBtn = new JButton("Buy Stock");
    buyPortfolioBtn.setActionCommand("buyPortfolioStocks");
    buyPortfolioStrategyBtn = new JButton("Buy Stocks");
    buyPortfolioStrategyBtn.setActionCommand("buyPortfolioStrategy");
    getPortCompBtn = new JButton("View Composition");
    getPortCompBtn.setActionCommand("FlexComposition");
    addStockBtnMain = new JButton("Add stocks");
    addStockBtnMain.setActionCommand("addMoreStocks");
    buyStockBtnMain = new JButton("Add stocks");
    buyStockBtnMain.setActionCommand("addMoreStocks");
    createPortfolioBtn = new JButton("Create Portfolio");
    createPortfolioBtn.setActionCommand("createPortfolio");
    addStockBtnSec = new JButton("Add Stocks");
    addStockBtnSec.setActionCommand("Add Stocks");
    getPortfolioValueBtn = new JButton("Get Value");
    getPortfolioValueBtn.setActionCommand("getValue");
    sellStockBtn = new JButton("Sell Stock");
    sellStockBtn.setActionCommand("SellStock");
    getPortfAmtInvstedValBtn = new JButton("Get Amount Invested");
    getPortfAmtInvstedValBtn.setActionCommand("amtInvested");
    this.enterUserMessage();
  }

  @Override
  public void addFeatures(GUIFeatures features) {
    graphBtn.addActionListener(evt -> {
      features.drawGraph(graphPortfolioName.getText(), graphStDate.getText(),
          graphEnDate.getText());
    });

    submitUser.addActionListener(evt -> {
      if (features.getUser(userNameInput.getText())) {
        userPrompt.dispose();
      } else {
        userNameInput.setFocusable(true);
        userNameInput.requestFocus();
      }
    });
    menu.addItemListener(this::itemStateChanged);

    createPortfolioBtn.addActionListener(evt -> {
      Map<String, Pair<String, String>> stocks = new HashMap<>();
      for (List<JTextField> fields : this.stockInputs) {
        if (!Objects.equals(fields.get(0).getText(), "")
            && !Objects.equals(fields.get(0).getText(), "") && !Objects.equals(
            fields.get(0).getText(), "")) {
          stocks.put(fields.get(0).getText(),
              new Pair(fields.get(1).getText(), fields.get(2).getText()));
        }
      }
      if (features.operateCreatePortfolio(portfolioNameCreatePort.getText(), stocks,
          strategyStart.getText())) {
        addStockFrame.dispose();
      }
      this.resetFocus();
    });

    createPortfolioStrategyBtn.addActionListener(evt -> {
      Map<String, String> stocks = new HashMap<>();
      for (List<JTextField> fields : this.stockInputs) {
        if (!Objects.equals(fields.get(0).getText(), "")
            && !Objects.equals(fields.get(0).getText(), "")) {
          stocks.put(fields.get(0).getText(), fields.get(1).getText());
        }
      }
      PortfolioStrategy strategy =
          strateList.getSelectedIndex() == 0 ? PortfolioStrategy.FIXED_VALUE
              : PortfolioStrategy.DOLLAR_AVERAGE;
      if (features.operateStrategyCreateOrBuyPortfolio(portfolioNameCreatePort.getText(), stocks,
          strategyStart.getText(),
          strategyEnd.getText(), strategyCommission.getText(), "create", strategyInvested.getText(),
          strategyPeriod.getText(), strategy)) {
        addStockFrame.dispose();
      }
      this.resetFocus();
    });

    buyPortfolioStrategyBtn.addActionListener(evt -> {
      Map<String, String> stocks = new HashMap<>();
      for (List<JTextField> fields : this.stockInputs) {
        if (!Objects.equals(fields.get(0).getText(), "")
            && !Objects.equals(fields.get(0).getText(), "")) {
          stocks.put(fields.get(0).getText(), fields.get(1).getText());
        }
      }
      PortfolioStrategy strategy =
          buystrateList.getSelectedIndex() == 0 ? PortfolioStrategy.FIXED_VALUE
              : PortfolioStrategy.DOLLAR_AVERAGE;
      if (features.operateStrategyCreateOrBuyPortfolio(portfolioNameBuyPort.getText(), stocks,
          strategyStart.getText(),
          strategyEnd.getText(), strategyCommission.getText(), "buy", strategyInvested.getText(),
          strategyPeriod.getText(), strategy)) {
        addStockFrame.dispose();
      }
      this.resetFocus();
    });

    buyPortfolioBtn.addActionListener(evt -> {
      if (features.operateFlexibleBuyInputs(portfolioNameBuyPort.getText(), buyStockName.getText(),
          buyStockCount.getText(), strategyStart.getText(), strategyCommission.getText())) {
        addStockFrame.dispose();
      }
      this.resetFocus();
    });

    getPortCompBtn.addActionListener(evt -> {
      features.portfolioComposition(portfolioNamePortComp.getText(), datePortComp.getText());
      this.resetFocus();
    });

    getPortfolioValueBtn.addActionListener(evt -> {
      features.portfolioValue(portfolioNamePortVal.getText(),
          datePortVal.getText());
      this.resetFocus();
    });

    sellStockBtn.addActionListener(evt -> {
      features.operateFlexibleSellInputs(portfolioNameSellPort.getText(),
          stockNameSellPort.getText(), stockCountSellPort.getText(),
          dateSellPort.getText(), commissionFeeSellPort.getText());
      this.resetFocus();
    });

    getPortfAmtInvstedValBtn.addActionListener(evt -> {
      features.operateInvestedAmount(portfolioNamePortAmtInv.getText(), datePortAmtInv.getText());
      this.resetFocus();
    });

  }

  @Override
  public void returnOutput(String out) {
    JOptionPane.showMessageDialog(this,
        out, "Success!!", JOptionPane.PLAIN_MESSAGE);
    resetFocus();
  }

  @Override
  public void promptMenu() {
    // perform nothing
  }

  @Override
  public void displayWelcomeMessage(String user) {
    JOptionPane.showMessageDialog(this,
        "Welcome  " + user, "Welcome!", JOptionPane.PLAIN_MESSAGE);
  }

  private JFrame setGridLayout(JFrame frame) {
    panel1 = new JPanel();
    frame.setSize(500, 300);
    frame.setLocation(500, 200);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    panel1.setLayout(new GridBagLayout());
    panel1.setBorder(new EmptyBorder(30, 30, 30, 30));
    frame.add(panel1);
    return frame;
  }

  private void displayWindow(JFrame frame) {
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  @Override
  public void enterUserMessage() {
    userPrompt = new JFrame("Portfolio Portal");

    userPrompt = setGridLayout(userPrompt);
    GridBagConstraints gridConstraints = new GridBagConstraints();
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 0;

    panel1.add(new JLabel("Enter your name"), gridConstraints);
    gridConstraints.gridy++;
    userNameInput = new JTextField(10);
    panel1.add(userNameInput, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.anchor = GridBagConstraints.PAGE_END;
    submitUser = new JButton("Login");
    submitUser.setActionCommand("Login");
    panel1.add(submitUser, gridConstraints);
    displayWindow(userPrompt);
  }

  private void graphForm() {
    GridBagConstraints gridConstraints = new GridBagConstraints();
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 0;
    gridConstraints.anchor = GridBagConstraints.LINE_START;

    viewGraph.setLayout(new BoxLayout(viewGraph, BoxLayout.PAGE_AXIS));
    JPanel form = new JPanel(new GridBagLayout());
    graphPortfolioName = new JTextField(10);
    graphStDate = new JTextField(10);
    graphEnDate = new JTextField(10);

    form.add(new JLabel("Portfolio"), gridConstraints);
    gridConstraints.gridx++;
    form.add(graphPortfolioName, gridConstraints);
    gridConstraints.gridx--;
    gridConstraints.gridy++;
    form.add(new JLabel("Start date (MM/DD/YYYY)"), gridConstraints);
    gridConstraints.gridx++;
    form.add(graphStDate, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.gridx--;
    form.add(new JLabel("End date (MM/DD/YYYY)"), gridConstraints);
    gridConstraints.gridx++;
    form.add(graphEnDate, gridConstraints);
    gridConstraints.gridy++;
    form.add(graphBtn, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.gridy++;
    gridConstraints.anchor = GridBagConstraints.PAGE_END;
    viewGraph.add(this.addHR());
    JLabel jl = new JLabel("<html><h3>View Graph</h3></html>");
    jl.setHorizontalAlignment(0);
    viewGraph.add(jl);
    viewGraph.add(form);
  }

  @Override
  public void displayInvestedAmount(String name, String date, Double amount) {
    String msg = String.format(Constant.PORTFOLIO_INVESTED_AMNT + "\n", name, date, amount);
    JOptionPane.showMessageDialog(this,
        msg, "Success!!", JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public void displayBarGraph(String pfName, Map<String, String> data, String start, String end,
      int base) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    for (Map.Entry<String, String> da : data.entrySet()) {
      dataset.addValue(Double.parseDouble(da.getValue()), "Value", da.getKey());
    }
    JFreeChart jchart = ChartFactory.createBarChart(
        "Performance of " + pfName + " from " + start + " to " + end,
        "Dates",
        "Money Invested",
        dataset,
        PlotOrientation.HORIZONTAL,
        true,
        true,
        false
    );
    ChartFrame cFrame = new ChartFrame("Portfolio Performance", jchart, true);
    cFrame.setVisible(true);
    cFrame.setSize(800, 800);
  }

  @Override
  public void displayStrategy() {
    GridBagConstraints gridConstraints = new GridBagConstraints();
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 0;
    gridConstraints.anchor = GridBagConstraints.LINE_START;

    viewPortfolioComp.setLayout(new BoxLayout(viewPortfolioComp, BoxLayout.PAGE_AXIS));
    JPanel form = new JPanel(new GridBagLayout());
    getPortfolioNamePortComp = new JLabel("Portfolio Name");
    portfolioNamePortComp = new JTextField(10);
    getDatePortComp = new JLabel("Date (MM/dd/YYYY)");
    datePortComp = new JTextField(10);

    form.add(getPortfolioNamePortComp, gridConstraints);
    gridConstraints.gridx++;
    form.add(portfolioNamePortComp, gridConstraints);
    gridConstraints.gridx--;
    gridConstraints.gridy++;
    form.add(getDatePortComp, gridConstraints);
    gridConstraints.gridx++;
    form.add(datePortComp, gridConstraints);
    gridConstraints.gridy++;
    form.add(getPortCompBtn, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.gridy++;
    gridConstraints.anchor = GridBagConstraints.PAGE_END;
    viewPortfolioComp.add(this.addHR());
    JLabel jl = new JLabel("<html><h3>View Composition</h3></html>");
    jl.setHorizontalAlignment(0);
    viewPortfolioComp.add(jl);
    viewPortfolioComp.add(form);
  }

  private JLabel addHR() {
    JLabel jl = new JLabel(
        "<html><div style='width:10000px;display:inline-block;margin:1px;border:"
            + " 1px solid black;height: 0'></div></html>");
    jl.setHorizontalAlignment(SwingConstants.LEFT);
    return jl;
  }

  @Override
  public void addStocks() {
    if (addStockFrame != null && addStockFrame.isVisible()) {
      addStockFrame.toFront();
      addStockFrame.requestFocus();
      return;
    }
    this.stockInputs = new ArrayList<>();
    addStockFrame = new JFrame("Add Stocks");
    JPanel dateInp = new JPanel(new FlowLayout());
    JPanel pHeaders = new JPanel(new FlowLayout());
    List<JTextField> txt = new ArrayList<>();

    pHeaders.add(new JLabel("Stock Name"));
    pHeaders.add(new JSeparator(SwingConstants.VERTICAL));
    pHeaders.add(new JSeparator(SwingConstants.VERTICAL));
    pHeaders.add(new JSeparator(SwingConstants.VERTICAL));
    pHeaders.add(new JLabel("Stock Count"));
    pHeaders.add(new JSeparator(SwingConstants.VERTICAL));
    pHeaders.add(new JSeparator(SwingConstants.VERTICAL));
    pHeaders.add(new JSeparator(SwingConstants.VERTICAL));
    pHeaders.add(new JLabel("Commission Fee"));
    Box center = Box.createVerticalBox();
    JScrollPane jsp = new JScrollPane(center);
    dateInp.add(new Label("Date (MM/DD/YYYY): "));
    strategyStart = new JTextField(10);
    dateInp.add(strategyStart);
    center.add(dateInp);
    center.add(pHeaders);

    JPanel firstRow = new JPanel(new FlowLayout());
    JTextField txt1 = new JTextField(10);
    JTextField txt2 = new JTextField(10);
    JTextField txt3 = new JTextField(10);
    firstRow.add(txt1);
    firstRow.add(txt2);
    firstRow.add(txt3);
    txt.add(txt1);
    txt.add(txt2);
    txt.add(txt3);

    this.stockInputs.add(txt);

    center.add(firstRow);

    addStockFrame.getContentPane().add(jsp);
    JPanel addBtn = new JPanel(new FlowLayout());
    addBtn.add(addStockBtnSec);
    addBtn.add(createPortfolioBtn);
    addStockFrame.getContentPane().add(addBtn, BorderLayout.SOUTH);
    addStockFrame.setSize(500, 400);

    addStockBtnSec.addActionListener(e -> {
      JPanel inputs = new JPanel(new FlowLayout());
      List<JTextField> ttxt = new ArrayList<>();
      JTextField stock = new JTextField(10);
      JTextField sc = new JTextField(10);
      JTextField cmfee = new JTextField(10);
      inputs.add(stock);
      inputs.add(sc);
      inputs.add(cmfee);
      ttxt.add(stock);
      ttxt.add(sc);
      ttxt.add(cmfee);
      this.stockInputs.add(ttxt);
      center.add(inputs);
      addStockFrame.validate();
      addStockFrame.repaint();
    });
    displayWindow(addStockFrame);

  }


  public void itemStateChanged(ItemEvent evt) {
    CardLayout cl = (CardLayout) (cards.getLayout());
    cl.show(cards, (String) evt.getItem());
  }

  private void resetFocus() {
    menu.setFocusable(true);
    menu.requestFocus();
  }

  private void addSingleStock() {
    if (addStockFrame != null && addStockFrame.isVisible()) {
      addStockFrame.toFront();
      addStockFrame.requestFocus();
      return;
    }
    addStockFrame = new JFrame("Add Stocks");
    addStockFrame.setSize(400, 400);
    JPanel jp = new JPanel();
    GridBagConstraints gridConstraints = new GridBagConstraints();
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 0;
    gridConstraints.ipadx = 0;
    gridConstraints.ipady = 0;
    gridConstraints.weightx = 0;
    gridConstraints.weighty = 0;
    gridConstraints.anchor = GridBagConstraints.LINE_START;
    jp.setLayout(new GridBagLayout());
    buyStockName = new JTextField(10);
    buyStockCount = new JTextField(10);
    strategyCommission = new JTextField(10);
    strategyStart = new JTextField(10);

    jp.add(new JLabel("Stock Name"));
    gridConstraints.gridx++;
    jp.add(buyStockName, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.gridx--;
    jp.add(new JLabel("Stock Share Count"), gridConstraints);
    gridConstraints.gridx++;
    jp.add(buyStockCount, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.gridx--;
    jp.add(new JLabel("Commission Fee"), gridConstraints);
    gridConstraints.gridx++;
    jp.add(strategyCommission, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.gridx--;
    jp.add(new JLabel("Date (MM/DD/YYYY)"), gridConstraints);
    gridConstraints.gridx++;
    jp.add(strategyStart, gridConstraints);
    gridConstraints.gridy++;
    jp.add(buyPortfolioBtn, gridConstraints);
    addStockFrame.getContentPane().add(jp);
    displayWindow(addStockFrame);
  }

  private void manageAction(JComboBox combo, int type) {
    if (combo.getSelectedIndex() == 2) {
      if (type == 0) {
        this.addStocks();
      } else {
        this.addSingleStock();
      }
    } else {
      this.addStrategyStocks(type);
    }
    this.resetFocus();
  }

  private void createoRBuyPortfolioCard(int type) {
    GridBagConstraints gridConstraints = new GridBagConstraints();
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 0;
    gridConstraints.ipadx = 0;
    gridConstraints.ipady = 0;
    gridConstraints.weightx = 0;
    gridConstraints.weighty = 0;
    gridConstraints.anchor = GridBagConstraints.LINE_START;
    if (type == 0) {
      createPortfolio.setLayout(new BoxLayout(createPortfolio, BoxLayout.PAGE_AXIS));
      strateList.setSelectedIndex(2);
    } else {
      addStockPortfolio.setLayout(new BoxLayout(addStockPortfolio, BoxLayout.PAGE_AXIS));
      buystrateList.setSelectedIndex(2);
    }
    JPanel form = new JPanel(new GridBagLayout());

    form.add(new JLabel("Portfolio Name"), gridConstraints);
    gridConstraints.gridx++;
    if (type == 0) {
      portfolioNameCreatePort = new JTextField(10);
      form.add(portfolioNameCreatePort, gridConstraints);
    } else {
      portfolioNameBuyPort = new JTextField(10);
      form.add(portfolioNameBuyPort, gridConstraints);
    }
    gridConstraints.gridx--;
    gridConstraints.gridy++;
    form.add(new JLabel("Strategy"), gridConstraints);
    gridConstraints.gridx++;
    if (type == 0) {
      form.add(strateList, gridConstraints);
    } else {
      form.add(buystrateList, gridConstraints);
    }
    gridConstraints.gridy++;
    if (type == 0) {
      form.add(addStockBtnMain, gridConstraints);
      addStockBtnMain.addActionListener(evt -> this.manageAction(strateList, type));
    } else {
      form.add(buyStockBtnMain, gridConstraints);
      buyStockBtnMain.addActionListener(evt -> this.manageAction(buystrateList, type));
    }

    gridConstraints.gridy++;
    gridConstraints.anchor = GridBagConstraints.PAGE_END;
    if (type == 0) {
      createPortfolio.add(this.addHR());
      JLabel jl = new JLabel("<html><h3 style='margin-left: 10px'>Create Portfolio</h3></html>");
      jl.setHorizontalAlignment(0);
      createPortfolio.add(jl);
      createPortfolio.add(form);
    } else {
      addStockPortfolio.add(this.addHR());
      JLabel jl = new JLabel(
          "<html><h3 style='margin-left: 10px'>Add Stocks to Portfolio</h3></html>");
      jl.setHorizontalAlignment(0);
      addStockPortfolio.add(jl);
      addStockPortfolio.add(form);
    }
  }

  private void addStrategyStocks(int type) {
    if (addStockFrame != null && addStockFrame.isVisible()) {
      addStockFrame.toFront();
      addStockFrame.requestFocus();
      return;
    }
    addStockFrame = new JFrame("Add Stocks");
    this.stockInputs = new ArrayList<>();
    JPanel pHeaders = new JPanel(new FlowLayout());
    List<JTextField> txt = new ArrayList<>();
    pHeaders.add(new JLabel("Stock Name"));
    pHeaders.add(new JSeparator(SwingConstants.VERTICAL));
    pHeaders.add(new JSeparator(SwingConstants.VERTICAL));
    pHeaders.add(new JSeparator(SwingConstants.VERTICAL));
    pHeaders.add(new JLabel("Stock Percent"));
    Box center = Box.createVerticalBox();
    JScrollPane jsp = new JScrollPane(center);
    JPanel jpinputs = new JPanel(new FlowLayout());
    jpinputs.add(new JLabel("Invested Amt."));
    strategyInvested = new JTextField(10);
    jpinputs.add(strategyInvested);
    jpinputs.add(new JSeparator(SwingConstants.VERTICAL));
    jpinputs.add(new JLabel("Comm. Fee"));
    strategyCommission = new JTextField(10);
    jpinputs.add(strategyCommission);
    JPanel dateInp = new JPanel();
    JPanel period = new JPanel();
    JComboBox choose = type == 0 ? this.strateList : this.buystrateList;
    if (choose.getSelectedIndex() == 0) {
      dateInp.add(new JLabel("Date (MM/DD/YYYY)"));
      strategyStart = new JTextField(10);
      dateInp.add(strategyStart);
      strategyEnd = new JTextField();
      strategyPeriod = new JTextField();
    } else {
      dateInp.add(new JLabel("Start Date (MM/DD/YYYY)"));
      strategyStart = new JTextField(10);
      dateInp.add(strategyStart);
      dateInp.add(new JSeparator(SwingConstants.VERTICAL));
      dateInp.add(new JLabel("End Date (MM/DD/YYYY)"));
      strategyEnd = new JTextField(10);
      strategyPeriod = new JTextField(10);
      period.add(new JLabel("Time Period (in days)"));
      period.add(strategyPeriod);
      dateInp.add(strategyEnd);
    }

    center.add(dateInp);
    center.add(period);
    center.add(jpinputs);
    center.add(pHeaders);

    JPanel firstRow = new JPanel(new FlowLayout());
    JTextField txt1 = new JTextField(10);
    JTextField txt2 = new JTextField(10);
    firstRow.add(txt1);
    firstRow.add(txt2);
    txt.add(txt1);
    txt.add(txt2);

    this.stockInputs.add(txt);

    center.add(firstRow);

    addStockFrame.getContentPane().add(jsp);
    JPanel addBtn = new JPanel(new FlowLayout());
    addBtn.add(addStockBtnSec);
    addBtn.add(type == 0 ? createPortfolioStrategyBtn : buyPortfolioStrategyBtn);
    addStockFrame.getContentPane().add(addBtn, BorderLayout.SOUTH);
    addStockFrame.setSize(650, 400);

    addStockBtnSec.addActionListener(e -> {
      JPanel inputs = new JPanel(new FlowLayout());
      List<JTextField> ttxt = new ArrayList<>();
      JTextField stock = new JTextField(10);
      JTextField sc = new JTextField(10);
      inputs.add(stock);
      inputs.add(sc);
      ttxt.add(stock);
      ttxt.add(sc);
      this.stockInputs.add(ttxt);
      center.add(inputs);
      addStockFrame.validate();
      addStockFrame.repaint();
    });
    displayWindow(addStockFrame);
  }

  private void createPortfolioCompositionCard() {

    GridBagConstraints gridConstraints = new GridBagConstraints();
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 0;
    gridConstraints.anchor = GridBagConstraints.LINE_START;

    viewPortfolioComp.setLayout(new BoxLayout(viewPortfolioComp, BoxLayout.PAGE_AXIS));
    JPanel form = new JPanel(new GridBagLayout());
    getPortfolioNamePortComp = new JLabel("Portfolio Name");
    portfolioNamePortComp = new JTextField(10);
    getDatePortComp = new JLabel("Date (MM/dd/YYYY)");
    datePortComp = new JTextField(10);

    form.add(getPortfolioNamePortComp, gridConstraints);
    gridConstraints.gridx++;
    form.add(portfolioNamePortComp, gridConstraints);
    gridConstraints.gridx--;
    gridConstraints.gridy++;
    form.add(getDatePortComp, gridConstraints);
    gridConstraints.gridx++;
    form.add(datePortComp, gridConstraints);
    gridConstraints.gridy++;
    form.add(getPortCompBtn, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.gridy++;
    gridConstraints.anchor = GridBagConstraints.PAGE_END;
    viewPortfolioComp.add(this.addHR());
    JLabel jl = new JLabel("<html><h3>View Composition</h3></html>");
    jl.setHorizontalAlignment(0);
    viewPortfolioComp.add(jl);
    viewPortfolioComp.add(form);
  }

  private void createPortfolioValueCard() {

    GridBagConstraints gridConstraints = new GridBagConstraints();
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 0;

    viewPortfolioVal.setLayout(new BoxLayout(viewPortfolioVal, BoxLayout.PAGE_AXIS));
    JPanel form = new JPanel(new GridBagLayout());
    portfolioNamePortVal = new JTextField(20);
    datePortVal = new JTextField(20);

    form.add(new JLabel("Portfolio Name"), gridConstraints);
    gridConstraints.gridx++;
    form.add(portfolioNamePortVal, gridConstraints);
    gridConstraints.gridx--;
    gridConstraints.gridy++;
    form.add(new JLabel("Date (MM/dd/YYYY)"), gridConstraints);
    gridConstraints.gridx++;
    form.add(datePortVal, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.anchor = GridBagConstraints.LINE_END;
    form.add(getPortfolioValueBtn, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.anchor = GridBagConstraints.LINE_END;
    viewPortfolioVal.add(this.addHR());
    JLabel jl = new JLabel("<html><h3 style='margin-left: 10px'>View Portfolio Value</h3></html>");
    jl.setHorizontalAlignment(0);
    viewPortfolioVal.add(jl);
    viewPortfolioVal.add(form);
  }

  private void createPortfolioAmtInvestedCard() {

    GridBagConstraints gridConstraints = new GridBagConstraints();
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 0;

    viewPortfolioAmtInvst.setLayout(new BoxLayout(viewPortfolioAmtInvst, BoxLayout.PAGE_AXIS));
    JPanel form = new JPanel(new GridBagLayout());
    portfolioNamePortAmtInv = new JTextField(20);
    datePortAmtInv = new JTextField(20);

    form.add(new JLabel("Portfolio Name"), gridConstraints);
    gridConstraints.gridx++;
    form.add(portfolioNamePortAmtInv, gridConstraints);
    gridConstraints.gridx--;
    gridConstraints.gridy++;
    form.add(new JLabel("Date (MM/dd/YYYY)"), gridConstraints);
    gridConstraints.gridx++;
    form.add(datePortAmtInv, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.anchor = GridBagConstraints.LINE_END;
    form.add(getPortfAmtInvstedValBtn, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.anchor = GridBagConstraints.LINE_END;
    viewPortfolioAmtInvst.add(this.addHR());
    JLabel jl = new JLabel("<html><h3 style='margin-left: 10px'>Amount Invested</h3></html>");
    jl.setHorizontalAlignment(0);
    viewPortfolioAmtInvst.add(jl);
    viewPortfolioAmtInvst.add(form);
  }

  private void createSellStockCard() {

    GridBagConstraints gridConstraints = new GridBagConstraints();
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 0;

    sellStocksPortfolio.setLayout(new BoxLayout(sellStocksPortfolio, BoxLayout.PAGE_AXIS));
    JPanel form = new JPanel(new GridBagLayout());
    portfolioNameSellPort = new JTextField(20);
    stockNameSellPort = new JTextField(20);
    stockCountSellPort = new JTextField(20);
    dateSellPort = new JTextField(20);
    commissionFeeSellPort = new JTextField(20);

    form.add(new JLabel("Portfolio Name"), gridConstraints);
    gridConstraints.gridx++;
    form.add(portfolioNameSellPort, gridConstraints);
    gridConstraints.gridx--;
    gridConstraints.gridy++;
    form.add(new JLabel("Stock Name"), gridConstraints);
    gridConstraints.gridx++;
    form.add(stockNameSellPort, gridConstraints);
    gridConstraints.gridx--;
    gridConstraints.gridy++;
    form.add(new JLabel("Stock Count"), gridConstraints);
    gridConstraints.gridx++;
    form.add(stockCountSellPort, gridConstraints);
    gridConstraints.gridx--;
    gridConstraints.gridy++;
    form.add(new JLabel("Date (MM/dd/YYYY)"), gridConstraints);
    gridConstraints.gridx++;
    form.add(dateSellPort, gridConstraints);
    gridConstraints.gridx--;
    gridConstraints.gridy++;
    form.add(new JLabel("Commission Fee"), gridConstraints);
    gridConstraints.gridx++;
    form.add(commissionFeeSellPort, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.anchor = GridBagConstraints.LINE_END;
    form.add(sellStockBtn, gridConstraints);
    gridConstraints.gridy++;
    gridConstraints.anchor = GridBagConstraints.LINE_END;

    sellStocksPortfolio.add(this.addHR());
    JLabel jl = new JLabel("<html><h3 style='margin-left: 10px'>Sell Stocks</h3></html>");
    jl.setHorizontalAlignment(0);
    sellStocksPortfolio.add(jl);
    sellStocksPortfolio.add(form);
  }

  private void createFrame() {
    JFrame inFlexPortFolio = new JFrame("Portfolio Operations");
    inFlexPortFolio.setSize(400, 400);
    inFlexPortFolio.setLocation(500, 200);
    inFlexPortFolio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel comboBoxPane = new JPanel();
    comboBoxPane.setLayout(new FlowLayout());
    String[] options = {"-", Constant.CREATE_PORTFOLIO, Constant.PORTFOLIO_COMPOSITION,
        Constant.PORTFOLIO_VALUE, Constant.PORTFOLIO_ADD_STOCK, Constant.PORTFOLIO_REMOVE_STOCK,
        Constant.PLOT_BAR, Constant.INVESTED_VALUE};

    cards = new JPanel(new CardLayout());
    for (String option : options) {
      menu.addItem(option);
    }
    Dimension dd = new Dimension(5, 100);
    JSeparator sp = new JSeparator();

    sp.setBackground(Color.red);
    JLabel selectOp = new JLabel("Select an Operation: ");
    comboBoxPane.add(selectOp);
    comboBoxPane.add(sp);
    comboBoxPane.add(menu);

    JPanel emptyPanel = new JPanel();
    JLabel emptyL = new JLabel("Please select an operation");
    emptyL.setHorizontalAlignment(JLabel.CENTER);
    emptyL.setVerticalAlignment(JLabel.CENTER);
    emptyPanel.add(this.addHR());
    emptyPanel.add(emptyL);
    createPortfolio = new JPanel();
    createoRBuyPortfolioCard(0);
    viewPortfolioComp = new JPanel();
    createPortfolioCompositionCard();
    viewPortfolioVal = new JPanel();
    createPortfolioValueCard();
    viewPortfolioAmtInvst = new JPanel();
    createPortfolioAmtInvestedCard();
    addStockPortfolio = new JPanel();
    createoRBuyPortfolioCard(1);
    sellStocksPortfolio = new JPanel();
    createSellStockCard();
    viewGraph = new JPanel();
    graphForm();

    cards.add(emptyPanel, "-");
    cards.add(createPortfolio, Constant.CREATE_PORTFOLIO);
    cards.add(viewPortfolioComp, Constant.PORTFOLIO_COMPOSITION);
    cards.add(viewPortfolioVal, Constant.PORTFOLIO_VALUE);
    cards.add(addStockPortfolio, Constant.PORTFOLIO_ADD_STOCK);
    cards.add(sellStocksPortfolio, Constant.PORTFOLIO_REMOVE_STOCK);
    cards.add(viewGraph, Constant.PLOT_BAR);
    cards.add(viewPortfolioAmtInvst, Constant.INVESTED_VALUE);

    inFlexPortFolio.getContentPane().add(comboBoxPane, BorderLayout.PAGE_START);
    inFlexPortFolio.getContentPane().add(cards);
    inFlexPortFolio.setVisible(true);

  }

  @Override
  public void promptFlexibleMenu() {
    createFrame();
  }

  @Override
  public void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(this,
        message, "Error!!", JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public void exitMessage(String username) {
    // perform nothing
  }

  @Override
  public void addStockMessage() {
    // perform nothing
  }

  @Override
  public void displayComposition(Map<Stock, Double> stocks) {

    GridBagConstraints gridConstraints = new GridBagConstraints();
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 0;
    gridConstraints.anchor = GridBagConstraints.CENTER;
    Object[][] tableData = new Object[stocks.keySet().size() + 1][2];
    int index = 0;
    JTable j;
    for (Map.Entry<Stock, Double> entry : stocks.entrySet()) {
      tableData[index][0] = entry.getKey();
      tableData[index][1] = String.format("%.2f", entry.getValue());
      index++;
    }
    String[] columnNames = {"Stock", "Count"};

    // Initializing the JTable
    j = new JTable(tableData, columnNames);

    // adding it to JScrollPane
    JScrollPane sp = new JScrollPane(j);
    JPanel pp = new JPanel();
    pp.add(sp, gridConstraints);
    // Frame Size
    JOptionPane.showMessageDialog(
        this.getParent(),
        pp,
        "Portfolio Composition",
        JOptionPane.INFORMATION_MESSAGE,
        new ImageIcon()
    );
  }

  @Override
  public void displayStockValue(String portfolio, Double value) {
    CardLayout cl = (CardLayout) (cards.getLayout());
    cl.show(cards, Constant.PORTFOLIO_VALUE);
    JOptionPane.showMessageDialog(this,
        String.format(Constant.PORTFOLIO_VALUE_MSG, portfolio, value), "Success!!",
        JOptionPane.PLAIN_MESSAGE);
  }


  @Override
  public void prompt(String s) {
    // perform nothing
  }

}
