package com.saucelabs.example.stepdefs;

import com.saucelabs.example.SauceThrottle;
import com.saucelabs.example.Util;
import com.saucelabs.example.pages.InventoryPage;
import com.saucelabs.example.pages.LoginPage;
import com.saucelabs.example.pages.PagesFactory;
import cucumber.api.java8.En;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.util.Map;

public class LoginPageSteps implements En
{
    public LoginPageSteps()
    {
        Given("^The user is on the Home Page$", () -> {
            PagesFactory pf = PagesFactory.getInstance();
            RemoteWebDriver driver = pf.getDriver();
            Util.info(driver, ">>> The user is on the Home Page");

            LoginPage loginPage = pf.getLoginPage();
            loginPage.navigateTo(LoginPage.PAGE_URL);
        });

        And("^The network speed is \"([^\"]*)\"$", (String networkSpeed) -> {
            PagesFactory pf = PagesFactory.getInstance();
            RemoteWebDriver driver = pf.getDriver();
            Util.info(driver, ">>> The network speed is %s", networkSpeed);
            Util.sauceThrottle(driver, SauceThrottle.fromValue(networkSpeed));
        });

        And("^The Page Load Time should be less than \"([^\"]*)\" msecs$", (String pageLoadTime) -> {
            // We need to give the capture routines time to capture the performance data before querying it
            Util.sleep(5000);
            PagesFactory pf = PagesFactory.getInstance();
            RemoteWebDriver driver = pf.getDriver();
            Util.info(driver, ">>> The Page Load Time should be less than %s msecs", pageLoadTime);

            Map<String, Object> performance = Util.getSaucePerformance(driver);
            if (performance != null)
            {
                Long expected = Long.parseLong(pageLoadTime);
                Long actual;

                Object load = performance.get("load");
                if (load != null)
                {
                    if (load instanceof Double)
                    {
                        actual = ((Double) load).longValue();
                    }
                    else
                    {
                        actual = (Long) load;
                    }

                    Assert.assertTrue(actual < expected);
                }
            }
        });

        And("^The user provides the username as \"([^\"]*)\" and password as \"([^\"]*)\"$", (String username, String password) -> {
            PagesFactory pf = PagesFactory.getInstance();
            RemoteWebDriver driver = pf.getDriver();
            Util.info(driver, ">>> The user provides the username as \"username\" and password as \"password\"");

            LoginPage loginPage = pf.getLoginPage();
            loginPage.enterUsername(username);
            loginPage.enterPassword(password);
            Util.sleep(2000);
        });

        And("^The user clicks the 'Login' button$", () -> {
            PagesFactory pf = PagesFactory.getInstance();
            RemoteWebDriver driver = pf.getDriver();
            Util.info(driver, ">>> The user clicks the 'Login' button");

            LoginPage loginPage = pf.getLoginPage();
            loginPage.clickLogin();
//            Util.sleep(3000);
        });

        Then("^The user should login successfully and is brought to the inventory page$", () -> {
            PagesFactory pf = PagesFactory.getInstance();
            RemoteWebDriver driver = pf.getDriver();
            Util.info(driver, ">>> The user should login successfully and is brought to the inventory page");

            InventoryPage inventoryPage = pf.getInventoryPage();
            inventoryPage.waitForPageLoad();

            String currentUrl = PagesFactory.getInstance().getDriver().getCurrentUrl();
            Assert.assertEquals(currentUrl, InventoryPage.PAGE_URL);

//            Util.takeScreenShot(driver);
        });

        Then("^The user should be shown a locked out message$", () -> {
            PagesFactory pf = PagesFactory.getInstance();
            RemoteWebDriver driver = pf.getDriver();
            Util.info(driver, ">>> The user should be shown a locked out message");

            LoginPage loginPage = pf.getLoginPage();
            Assert.assertTrue(loginPage.hasLockedOutError());
        });

        Then("^The user should be shown an invalid username/password message$", () -> {
            PagesFactory pf = PagesFactory.getInstance();
            RemoteWebDriver driver = pf.getDriver();
            Util.info(driver, ">>> The user should be shown an invalid username/password message");

            LoginPage loginPage = pf.getLoginPage();
            Assert.assertTrue(loginPage.hasUsernamePasswordError());
        });
    }
}
