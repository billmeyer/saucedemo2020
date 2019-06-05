# Sauce Demo Test Suite

## Full regressions

To execute the full test suite, simply run:

    $ mvn test
    
And each scenario will execute.

## Smoke tests

Smoke tests can be run by specifying the `@smoke1` tag:

    $ mvn clean test "-Dcucumber.options=--tags '@smoke1'"

Tags can be combined to further filter the set of tests run.  For example, to run only the login smoke tests, run:

    $ mvn clean test "-Dcucumber.options=--tags '@login and @smoke1'"

Similarly, to run the order smoke tags, run:

    $ mvn clean test "-Dcucumber.options=--tags '@order and @smoke1'"
    
## Performance tests

    $ mvn clean test -Dtestng.file=testng-performance.xml
    
## Run tests across all platforms

    $ mvn clean test -Dtestng.file=testng-allplatforms.xml
    
## Scenario breakdown

|Feature|Scenario|Smoke Test|Regression Test|End to End|
|---|---|---|---|---|
|login|Verify valid users can sign in|Yes|Yes|No|
|login|Verify locked out user gets locked out message|No|Yes|No|
|login|Verify invalid users cannot sign in|No|Yes|No|
|orders|Place a single item in the shopping cart|Yes|Yes|No|
|orders|Place multiple items in the shopping cart|No|Yes|No|
|orders|Validate Order Totals|No|Yes|No|

## Tags

Tags are defined to allow execution of specific Features and Scenarios.

__Example__: Execute smoke tests for tests related to `orders`:

    $ mvn test "-Dcucumber.options=--tags '@orders and @smoke1'" -f pom.xml   

__Example__: Execute regression tests for tests related to `login`:

    $ mvn test "-Dcucumber.options=--tags '@login and @regression1'" -f pom.xml   
