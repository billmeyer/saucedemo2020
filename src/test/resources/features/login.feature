@login
Feature: Test Entering Credentials
  As an online shopper, users must sign in with valid credentials

  Background: Navigate to the home page
    Given The user is on the Home Page

  ###############################################################################################
  @smoke1
  @regression1
  Scenario Outline: Verify valid users can sign in
    And The user provides the username as "<username>" and password as "<password>"
    And The user clicks the 'Login' button
    Then The user should login successfully and is brought to the inventory page
  Examples:
    |username       |password     |
    |standard_user  |secret_sauce |

  ###############################################################################################
  @regression1
  Scenario Outline: Verify valid users can sign in over a slow 3G connection
    And The network speed is "REGULAR_3G"
    And The user provides the username as "<username>" and password as "<password>"
    And The user clicks the 'Login' button
    Then The user should login successfully and is brought to the inventory page
    Examples:
      |username       |password     |
      |standard_user  |secret_sauce |

  ###############################################################################################
  @regression1
  Scenario Outline: Verify valid users can sign in
    And The user provides the username as "<username>" and password as "<password>"
    And The user clicks the 'Login' button
    Then The user should login successfully and is brought to the inventory page
    Examples:
      |username       |password     |
      |standard_user  |secret_sauce |
      |problem_user   |secret_sauce |

  ###############################################################################################
  @regression1
  Scenario Outline: Verify locked out user gets locked out message
    And The user provides the username as "<username>" and password as "<password>"
    And The user clicks the 'Login' button
    Then The user should be shown a locked out message
    Examples:
      |username       |password     |
      |locked_out_user|secret_sauce |

  ###############################################################################################
  @regression1
  Scenario Outline: Verify invalid users cannot sign in
    And The user provides the username as "<username>" and password as "<password>"
    And The user clicks the 'Login' button
    Then The user should be shown an invalid username/password message
    Examples:
      |username       |password     |
      |fake_user      |bogus        |

  ###############################################################################################
  @performance1
  Scenario: Test Page Load Time
    Given The user is on the Home Page
    Then The Page Load Time should be less than "5000" msecs

