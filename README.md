# Stock-Portfolio-Management
Portfolio
=========

------------------------------------------------------------------------

A portfolio of stocks is simply a collection of stocks. This project
lets a user create multiple portfolio of stocks, retrieve the
composition of the portfolio and fetch a value of the portfolio on a
given date.

### Working Features

- Create a Portfolio that is either flexible or inflexible with single / multiple stocks (A
  portfolio must
  have atleast one stock!)
- View the composition of a inflexible portfolio
- View the composition of a flexible portfolio on a given date (i.e it includes the composition of
  the portfolio only until the date provided)
- View the value of a inflexible portfolio on a specific date (i.e. it is the sum
  of total values of stocks multiplied by their counts on a particular
  date)
- View the value of a flexible portfolio on a specific date (i.e. it is the sum
  of total values of stocks multiplied by their counts till a particular
  date)
- Add stocks to a flexible portfolio
- Sell stocks from a flexible portfolio
- View performance of a flexible portfolio within a given time period (user has to provide the start
  date and end date date between which performance graph needs to be plotted)
- Get the cost basis of a flexible portfolio (i.e the total amount of money invested to buy the
  stocks + commission fee for buying and selling the stocks till a given date )
- GUI based interaction for flexible portfolio.
- Performance graph in GUI.
- Strategy based Portfolio creation:
  * fixed amount strategy
  * dollar-cost averaging (It works when both end date is specified and not specified)
- Buying stocks and adding to an existing portfolio based on a strategy:
  * fixed amount strategy
  * dollar-cost averaging (It works when both end date is specified and not specified)
- ReBalance Portfolio for a given date for flexible portfolio
  * ReBalance Portfolio Equally (weight is split equally amount all the stocks)
  * ReBalance Portfolio Unequally (weight for each stock is provided)
- Exit the program

Screen Shot:
<img width="398" alt="GUI_sample" src="https://user-images.githubusercontent.com/36084170/235487144-8eaab87d-ec63-4985-a542-4b6d21ded806.png">
<img width="1148" alt="performance_graph" src="https://user-images.githubusercontent.com/36084170/235487176-2c54a94d-2503-4fac-9bb5-b29a5f67a56b.png">

