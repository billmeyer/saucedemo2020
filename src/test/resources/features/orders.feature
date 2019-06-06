@orders
  Feature: Validate Web Ordering

    ###############################################################################################
    @smoke1
    @regression1
    Scenario Outline: Place a single item in the shopping cart
      Given The user is on the Home Page
      And The user provides the username as "<username>" and password as "<password>"
      And The user clicks the 'Login' button
      And The user chooses a "<item>" by clicking 'Add To Cart'
      And The user clicks on the shopping cart
      Then There should be "1" items in the shopping cart
    Examples:
      |username|password|item|
      |standard_user  |secret_sauce |Sauce Labs Backpack|

    ###############################################################################################
    @regression1
    Scenario Outline: Place multiple items in the shopping cart
      Given The user is on the Home Page
      And The user provides the username as "<username>" and password as "<password>"
      And The user clicks the 'Login' button
      And The user selects
        |Sauce Labs Backpack    |
        |Sauce Labs Bolt T-Shirt|
        |Sauce Labs Onesie      |
      And The user clicks on the shopping cart
      Then There should be "3" items in the shopping cart
      Examples:
        |username|password|
        |standard_user  |secret_sauce |

    ###############################################################################################
    @regression1
    Scenario Outline: Validate Order Totals
      Given The user is on the Home Page
      And The user provides the username as "<username>" and password as "<password>"
      And The user clicks the 'Login' button
      And The user selects
        |Sauce Labs Backpack    |
        |Sauce Labs Bolt T-Shirt|
        |Sauce Labs Onesie      |
        |Test.allTheThings() T-Shirt (Red)|
        |Sauce Labs Fleece Jacket         |
        |Sauce Labs Bike Light            |
      And The user clicks on the shopping cart
      And The user clicks 'Checkout'
      And The user provides the first name as "Tom" and last name as "Jones" and zip code as "12345"
      And The user clicks 'Continue'
      Then The item total should be "Item total: $129.94"
      And The tax should be "Tax: $10.40"
      And The total should be "Total: $140.34"
      Examples:
        |username|password|
        |standard_user  |secret_sauce |

    ###############################################################################################
    @performance1
    Scenario Outline: Validate Order Totals Over Slow 3G Connection
      Given The user is on the Home Page
      And The network speed is "REGULAR_3G"
      And The user provides the username as "<username>" and password as "<password>"
      And The user clicks the 'Login' button
      And The user selects
        |Sauce Labs Backpack    |
        |Sauce Labs Bolt T-Shirt|
        |Sauce Labs Onesie      |
        |Test.allTheThings() T-Shirt (Red)|
        |Sauce Labs Fleece Jacket         |
        |Sauce Labs Bike Light            |
      And The user clicks on the shopping cart
      And The user clicks 'Checkout'
      And The user provides the first name as "Tom" and last name as "Jones" and zip code as "12345"
      And The user clicks 'Continue'
      Then The item total should be "Item total: $129.94"
      And The tax should be "Tax: $10.40"
      And The total should be "Total: $140.34"
      Examples:
        |username|password|
        |standard_user  |secret_sauce |
