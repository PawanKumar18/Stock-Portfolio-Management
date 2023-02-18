Portfolio
=========

------------------------------------------------------------------------

A portfolio is a collection of stocks. This project lets a user manage his/her Flexible / InFlexible
portfolios by creating portfolios, updating portfolios, retrieving the composition of a portfolio,
plotting performances, getting money invested and fetch a value of a portfolio on a given date.

Changes from Previous Iteration:

- An abstract controller that has common functions of both GUI based and console based controller.
- A GUI features , a controller for GUI based operations
- A GUIView interface that extends existing GUIView interface and has functions that are specific to GUI view.


Design
------

The project has a total of 6 entities. Each of them is represented by an
interface

1. Portfolio
2. Stock
3. User
4. File
4. DataStore
5. Services

A user can have multiple Portfolio\'s.

A portfolio has 3 modals, a view and controller which helps in managing the
portfolio.

- A PortfolioController is what a user interacts with in a text based manner
- A GUIController interacts with the user through GUI.
- A Facade Model is created that directly interacts with the controller. It further redirects the
  request to Flexible/InFlexible model based on the input given.
- The Controller requests the FacadePortfolioModal to perform any task, the model takes inputs from
  the controller and redirects it to the corresponding Flexible / InFlexible model. It gets the
  response from the model and gives the data back to the controller.
- The returned data from the modal is returned to View by the Controller
- All the input validations are done by the abstract controller class.
- All the data validations are done by the modal
- All the switch cases that help interact with the user now call private methods to
  accept input and henceforth perform the required operation.
- **Note:**
    - This design is less prone to edit existing code when new
      features implemented. If a new UI is asked to develop, a new java
      class can extend PortfolioView and implement all those methods. This
      does not change anything from the controller or the model.
    - If a new type of Portfolio is introduced tomorrow, and interface and implementation are
      created to it if all its implementations are unique or else existing ones from
      Flexible/InFlexible can be re-used by extending them. It is then attached to facade model for
      communication. This design is open for addition and closed for modofication.

A portfolio has a group of stocks. Currently all stocks supported by Alpha Vintage API
are supported in the program.

A service is created that fetches all the required stock related data from the API. As per the
requirement, a stock can exist in the application only as part of a portfolio. A stock cannot exist
independently. There is no functionalities that a user can perform directly with the stock. So, a
separate Model/View/Controller have not been created for the stock.

### User

*User* interface represents a User. Currently, a user can only get his
name. *User* is implemented by the class *UserImpl*

- A userImpl has a *\'name\'* variable.
- A userImpl can get the name of the user through getName() method.

### Stock

*Stock* is an interface that represents a stock. It is implemented by
the class *StockImpl*

- A StockImpl has a *\'ticker\'* variable which is the unique
  representation of a stock.
- A StockImpl can get the name of the ticker through getTicker()
  method.
- StockImpl overrides the equals and hash methods of an object. This
  could be helpful to hashmap while managing the multiple stocks.
- StockImpl has methods to set and get value of a stock.
- StockImpl has methods to set and get date of a stock.
- The setter and getter methods of value and date and not being used currently,
  but are added for future use.

### File

*FileIO* interface provides methods to read and write data from files. It is
implemented by the *FileIO* class. The purpose of FileIO is to be able
provide abstraction for the type of data to be read and written. Today, we are using
CSV data for read and write operation. Tomorrow, we might use a JSON data.
The FileIO interface helps us to switch to a new data format without changing the
existing architecture.
FIleIO is implemented by the CSVFileIO class to perform the read and write operation
of csv data.

- FileIO has the read method that takes the filters and CSV data and applies the
  filters. It returns filtered data as as map of key, value pairs.
- FileIO has the write method that takes the input as the data to be written and
  filewriter object and write data to it.

### DataStore

*DataStore* is an interface that provides methods to filter,add and delete Portfolio data.
It is implemented by the FileDataStore class that works with csv data.
It interacts with the CSVFileIO class to read and write csv data.

- FileDataStore has filter function that accepts filters and name of csv file.
  It reads the csv files and applies filters on that data through CSVFileIO class.
- FileDataStore has addData function that takes a list of input, a Function and
  file name as input. It applies the function to this list to convert it to string
  values and writes to the csv file using the CSVFIleIO class.

### Portfolio {#portfolio}

*Portfolio* is an interface that is a portfolio. It is implemented by
the class *PortfolioImpl*

- A PortfolioImpl has a *\'name\'* variable which is the name of the
  portfolio.

- A PortfolioImpl has a *\'stocks\'* variable which is a hashmap of
  stock and count of the stock.

- A PortfolioImpl has a *\'date\'* variable which is the date at which
  the portfolio was created.

- A PortfolioImpl has a *\'user\'* variable which is the name of the
  creator of the portfolio.

- A PortfolioImpl can get the name of the portfolio through
  getPortfolio() method.

- A PortfolioImpl can get the stock and it\'s count through
  getStocks() method.

- A PortfolioImpl can get the date of creation of the portfolio
  through getCreationDate() method.

- A PortfolioImpl can get the name of the creator of the portfolio
  through getOwnedUser() method.

A portfolio has a view, model and controller as follows:

#### View
The Portfolio has two views. Console based view and Graphical user interface.
Since both have functions in common, we have kept the PortfolioView interface as is.
But GUI based view has some additional functions. Hence, GUIView extends the PortfolioView interface
and adds additional functionality to it.

##### PortfolioView
The console based view of the Portfolio is defined through the PortfolioView
Interface. It is implemented by the PortfolioViewImpl class.

- PortfolioViewImpl has a *\'out\'* variable that stores the output
  stream.
- PortfolioViewImpl has returnOutput method to display the output.
- PortfolioViewImpl has promptMenu method to display the prompt.
- PortfolioViewImpl has showErrorMessage method to display the error
  message.
- PortfolioViewImpl has ExitMessage method to display the exit
  message.
- PortfolioViewImpl has ExitMessage method to display the exit
  message.
- PortfolioViewImpl has addStockMessage method to display the status
  message of adding a stock.
- PortfolioViewImpl has displayComposition method to display the
  composition of a portfolio.
- PortfolioViewImpl has displayStockValue method to display the value
  of a portfolio.
- PortfolioViewImpl has prompt method to display the prompt message.
- PortfolioViewImpl has displayWelcomeMessage method to display the
  welcome message.
- PortfolioViewImpl has enterUserMessage(); method to display a message
  when user enters the program.
- PortfolioViewImpl has displayInvestedAmount(); method to display the amount of
  money invested in a flexible portfolio.
- PortfolioViewImpl has displayBarGraph(); method to display the graph of the performance
  of a portfolio within a given period of time.

##### GUIView
The GUI based view of the Portfolio is defined through the GUIView
Interface.It extends the PortfolioView interface. It is implemented by the GUIViewImpl class.
It has the following methods:
- addStocks method displays the window to add stocks to the portfolio.
- addFeatures method registers the action listeners with the methods in the controller.
- 

#### Controller

We have two different controllers for GUI based view and console based view.

##### AbstractPortfolioController
All the common functions required by GUI based view and console based view are present 
in this abstract class.
It has the following methods:
- callValidateInput method that checks the if the user input is valid
- callValidateDateParseInput methods validates the date provided by the user.
- validatePortfolioName method validates the portfolio name
- validateUserName method validates the user name.
- validatePeriod method validates period.
- validateMoney method validates money.
- validateStockPercent method validates the percentage of stock.
- validateStock method checks if the stock name provided exists.
- validateStockCount method checks if the stock count is valid.
- validateStockCountDouble method checks if stock count is valid.
- callValidateDateInput method calls method to validate date.
- callCreateSharePortfolio method delegates creation of shared portfolio to the model.
- callCreateStrategyPortfolio method delegates strategy portfolio creation to model.
- callViewPortfolioComposition method delegates fetching portfolio composition to model.
- callBuyStock method delegates adding stocks to the portfolio to the model.
- callBuyStrategyStock delegates adding strategy stocks to the portfolio to the model.
- callSellStock method delegates removing stocks from the portfolio to the model.
- callGetPortfolioValue method delegates fetching portfolio value to the model.
- callGetPerformanceGraph method delegates fetching portfolio performance to the model.
- callGetInvestedAmount method delegates fetching portfolio value to the model.

##### CommandController
The controller of the Portfolio is defined through the
PortfolioController Interface. It is implemented by the
PortfolioControllerImpl class.

- PortfolioViewImpl has a *\'model\'* variable that stores model
  object.
- PortfolioViewImpl has a *\'view\'* variable that stores view object.
- PortfolioViewImpl has a *\'in\'* variable that stores Input Stream
  object.
- PortfolioViewImpl has a operate method that takes input and delegates
  processing to model and the display to view.

#### GUIFeatures
The controller for GUI based view. It is implemented by GUIController class.
It has the following methods: 
- getUser method to fetch the username.
- operateCreatePortfolio method to delegate portfolio creation to model.
- operateStrategyCreateOrBuyPortfolio method to delegate strategy portfolio creation
 to model.
- portfolioComposition method to delegate fetching of composition of the portfolio to the model.
- portfolioValue method to delegate fetching of portfolio value to the portfolio model.
- operateFlexibleBuyInputs method to delegate adding portfolio stocks to model.
- operateFlexibleSellInputs methods to delegate removing stocks from portfolio to the model.
- operateInvestedAmount method to delegate fetching of amount invested in portfolio to the model.
- drawGraph method to delegate fetching of portfolio performance to the model.

#### Model

Model needs to represent both flexible and inflexible portfolio.
Thus we have two model: Portfolio Model and Advanced Portfolio Model.\
Portfolio Model is the one that we created for the previous assignment
for creating a immutable portfolio. (This ensures backward compatibility)\
AdvancedPortfolioModel is introduced to create a mutable Portfolio where
we can add,sell stocks with a commission fee.\
Since, in a good design,the controller needs to interact with
only one model, we have created a Facade Portfolio model that is a
single point of contact to both Portfolio model and AvancedPortfolioModel.

##### PortfolioFacadeModel

PortfolioFacadeModel is an interface that contains methods to interact with both
flexible and inflexible portfolio models. It is implemented by the PortfolioFacadeModelImpl
class.

- PortfolioFacadeModelImpl has Portfolio object data member to call
  methods of the inflexible model. It is instantiated during PortfolioFacadeModelImpl class
  instantiation.
- PortfolioFacadeModelImpl has AdvancedPortfolio object data member to call
  methods of the flexible model. It is instantiated during PortfolioFacadeModelImpl class
  instantiation.
- PortfolioFacadeModelImpl has Datastore object to write and read portfolio data. It is instantiated
  during PortfolioFacadeModelImpl class instantiation.
- PortfolioFacadeModelImpl has Service object to fetch stock value. It is instantiated during
  PortfolioFacadeModelImpl class instantiation.
- PortfolioFacadeModelImpl has getPortfolioType method to fetch the type of portfolio when provided
  with user name and portfolio name.
- PortfolioFacadeModelImpl has createUnFlexiblePortfolio method that delegates portfolio creation
  function to PortfolioImpl class.
- PortfolioFacadeModelImpl has createFlexiblePortfolio method that delegates portfolio creation
  function to AdvancedPortfolioImpl class.
- PortfolioFacadeModelImpl has getUnFlexiblePortfolioComposition method that delegates function of
  fetching portfolio composition to PortfolioImpl class.
- PortfolioFacadeModelImpl has getFlexiblePortfolioComposition method that delegates function of
  fetching portfolio composition to AdvancedPortfolioImpl class.
- PortfolioFacadeModelImpl has getUnFlexiblePortfolioValue method that delegates function of
  fetching portfolio value to PortfolioImpl class.
- PortfolioFacadeModelImpl has getFlexiblePortfolioValue method that delegates function of fetching
  portfolio value to AdvancedPortfolioImpl class.
- PortfolioFacadeModelImpl has checkStockExists method that checks if a given stock exists.
- PortfolioFacadeModelImpl has checkPortfolioExists method that checks if a given portfolio exists
  for a given user.
- PortfolioFacadeModelImpl has buyStock method that delegates function of buying a new stock to
  AdvancedPortfolioImpl class.
- PortfolioFacadeModelImpl has sellStock method that delegates function of selling a stock to
  AdvancedPortfolioImpl class.
- PortfolioFacadeModelImpl has moneyInvested method that delegates function of getting the total
  money invested in a portfolio
  to AdvancedPortfolioImpl class.
- PortfolioFacadeModelImpl has getPortfolioPerformance method that delegates function of getting the
  performance of a portfolio over a period of time
  to AdvancedPortfolioImpl class.

##### PortfolioModel

The inflexible Portfolio is defined through the PortfolioModel
Interface. It is implemented by the PortfolioModelImpl class.

- PortfolioModelImpl has a *\'dataStore\'* variable that stores
  DataStore object.
- PortfolioModelImpl has a *\'sModel\'* variable that stores
  Service object to fetch stock value.
- PortfolioModelImpl has a checkStockExists method that checks if a
  given stock exists.
- PortfolioModelImpl has a checkPortfolioExists method that checks if
  a given portfolio exists.
- PortfolioModelImpl has a createPortfolio method that creates a
  portfolio from the given stocks.
- PortfolioModelImpl has a getPortfolioComposition method that fetches
  the composition a portfolio.
- PortfolioModelImpl has a getPortfolioValue method that fetches the
  value a portfolio.

##### AdvancedPortfolioModel

The flexible Portfolio is defined through the PortfolioModel
Interface. It is implemented by the AdvancedPortfolioModelImpl class.

- AdvancedPortfolioModelImpl has a *\'dataStore\'* variable that stores
  DataStore object.
- AdvancedPortfolioModelImpl has a *\'sModel\'* variable that stores
  Service object to fetch stock value.
- AdvancedPortfolioModelImpl has a checkPortfolioExists method that checks if
  a given portfolio exists.
- AdvancedPortfolioModelImpl has a createPortfolio method that creates a flexible
  portfolio from the given stocks.
- AdvancedPortfolioModelImpl has a getPortfolioComposition method that fetches
  the composition a portfolio on a given date
- AdvancedPortfolioModelImpl has a getPortfolioValue method that fetches the
  value a portfolio on a given date
- AdvancedPortfolioModelImpl has a buyStock method that adds stock to a given portfolio on a given
  date.
- AdvancedPortfolioModelImpl has a sellStock method that removes a stock from a given portfolio on a
  given date.
- AdvancedPortfolioModelImpl has a moneyInvested method that fetches the total amount of money
  invested in
  the portfolio including commissions until the given date.
- AdvancedPortfolioModelImpl has a getPortfolioPerformance method that fetches the performance
  values os stocks
  over a given period of time.

The model of the Stock is defined through the StockModel Interface. It
is implemented by the StockModelImpl class.

- StockModelImpl has a *getStockValue* method that returns value of a
  stock on particular date.