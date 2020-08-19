package com.saucelabs.example;

import cucumber.api.Scenario;
import cucumber.api.java8.En;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DriverFactory implements En
{
    private static final String userName = System.getenv("SAUCE_USERNAME");
    private static final String accessKey = System.getenv("SAUCE_ACCESS_KEY");
    private static final String headlessUserName = System.getenv("SAUCE_HEADLESS_USERNAME");
    private static final String headlessAccessKey = System.getenv("SAUCE_HEADLESS_ACCESS_KEY");

    private static URL SAUCE_EU_URL;
    private static URL SAUCE_US_URL;
    private static URL HEADLESS_URL;

    static
    {
        try
        {
            SAUCE_US_URL = new URL("https://" + userName + ":" + accessKey + "@ondemand.us-west-1.saucelabs.com/wd/hub");
        }
        catch (MalformedURLException e)
        {
            System.err.printf("Malformed SAUCE_US_URL: %s\n", e.getMessage());
            System.exit(-1);
        }

        try
        {
            SAUCE_EU_URL = new URL("https://ondemand.eu-central-1.saucelabs.com:443/wd/hub");
        }
        catch (MalformedURLException e)
        {
            System.err.printf("Malformed SAUCE_EU_URL: %s\n", e.getMessage());
            System.exit(-1);
        }

        try
        {
            HEADLESS_URL = new URL("http://ondemand.us-east-1.saucelabs.com/wd/hub");
        }
        catch (MalformedURLException e)
        {
            System.err.printf("Malformed HEADLESS_URL: %s\n", e.getMessage());
            System.exit(-1);
        }
    }

    public static RemoteWebDriver getDriverInstance(TestPlatform tp, Scenario scenario)
    {
        RemoteWebDriver driver = null;

        String platform = tp.getPlatformName();
        if (tp.getPlatformContainer() == PlatformContainer.HEADLESS)
        {
            driver = getHeadlessDriverInstance(tp, scenario);
        }
        else if (platform.startsWith("Windows ") || platform.startsWith("macOS ") || platform.startsWith("OS X") || platform.equalsIgnoreCase("linux"))
        {
            driver = getDesktopDriverInstance(tp, scenario);
        }
        else if (platform.equals("iOS") || platform.equals("Android"))
        {
            driver = DriverFactory.getMobileDriverInstance(tp, scenario, null);
        }

        return driver;
    }

    private static RemoteWebDriver getHeadlessDriverInstance(TestPlatform tp, Scenario scenario)
    {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("browserName", tp.getBrowser().toString());
        caps.setCapability("version", tp.getBrowserVersion());
        caps.setCapability("platform", tp.getPlatformName());
        caps.setCapability("name", scenario.getName());

        caps.setCapability("username", headlessUserName);
        caps.setCapability("accesskey", headlessAccessKey);

//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--headless");
//        HashMap<String, Object> googOpts = new HashMap<String, Object>();
//        googOpts.put("w3c", true);
//        chromeOptions.setCapability("goog:chromeOptions", googOpts);
//
//        caps.merge(chromeOptions);

        addJenkinsBuildInfo(caps);

        RemoteWebDriver driver = new RemoteWebDriver(HEADLESS_URL, caps);

        String sessionId = driver.getSessionId().toString();
        Util.log("Started %s", new Date().toString());
        Util.log("Test Results: https://app.us-east-1.saucelabs.com/tests/%s", sessionId);
        Util.log("SauceOnDemandSessionID=%s job-name=%s", sessionId, scenario.getName());

        DesiredCapabilities capabilities = new DesiredCapabilities();
// ...
        capabilities.setCapability("build", System.getenv("JOB_NAME") + "__" + System.getenv("BUILD_NUMBER"));

        // Set reasonable page load and script timeouts
//        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
//        driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);

        return driver;
    }

    private static RemoteWebDriver getDesktopDriverInstance(TestPlatform tp, Scenario scenario)
    {
        MutableCapabilities caps = new MutableCapabilities();

        // Set the W3C Specific Values
        // See https://www.w3.org/TR/webdriver1/#capabilities
        caps.setCapability("browserName", tp.getBrowser().toString());
        caps.setCapability("browserVersion", tp.getBrowserVersion());
        caps.setCapability("platformName", tp.getPlatformName());
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        String resultsURL = "";
        RemoteWebDriver driver = null;

        // Set the Sauce Labs-specific Values
        // See https://wiki.saucelabs.com/display/DOCS/Test+Configuration+Options#TestConfigurationOptions-OptionalSeleniumCapabilitiesforSauceLabsTests
        // See https://wiki.saucelabs.com/display/DOCS/Test+Configuration+Options#TestConfigurationOptions-SauceLabsCustomTestingOptions
        // See https://wiki.saucelabs.com/display/DOCS/Test+Configuration+Options#TestConfigurationOptions-OptionalSauceLabsTestingFeatures

        MutableCapabilities sauceOpts = new MutableCapabilities();
        sauceOpts.setCapability("seleniumVersion", "3.141.59");
        sauceOpts.setCapability("name", scenario.getName());
        sauceOpts.setCapability("username", userName);
        sauceOpts.setCapability("accesskey", accessKey);
        sauceOpts.setCapability("recordVideo", "true");
        sauceOpts.setCapability("recordMp4", "true");
        sauceOpts.setCapability("recordScreenshots", "true");
//        sauceOpts.setCapability("tunnelIdentifier", "SOME_TUNNEL_NAME");
//        sauceOpts.setCapability("parentTunnel", "USERNAME_OF_TUNNEL_OWNER");
//        sauceOpts.setCapability("screenResolution", "1600x1200");

        if (tp.getExtendedDebugging())
        {
            sauceOpts.setCapability("extendedDebugging", true);
        }

        if (tp.getCapturePerformance())
        {
            sauceOpts.setCapability("capturePerformance", true);
        }

        // Add Jenkins Build Info...
        addJenkinsBuildInfo(sauceOpts);
        caps.merge(sauceOpts);

        if (tp.getDataCenter().equals(DataCenter.US))
        {
            driver = new RemoteWebDriver(SAUCE_US_URL, caps);
            resultsURL = "https://app.saucelabs.com/tests";
        }
        else
        {
            driver = new RemoteWebDriver(SAUCE_EU_URL, caps);
            resultsURL = "https://app.eu-central-1.saucelabs.com/tests";
        }

        String sessionId = driver.getSessionId().toString();
        Util.log("Started %s", new Date().toString());
        Util.log("Test Results: %s/%s", resultsURL, sessionId);
        Util.log("SauceOnDemandSessionID=%s job-name=%s", sessionId, scenario.getName());

        // Set reasonable page load and script timeouts
//        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
//        driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);

        return driver;
    }

    private static MutableCapabilities addJenkinsBuildInfo(MutableCapabilities sauceOpts)
    {
        // Pull the Job Name and Build Number from Jenkins if available...
        String jenkinsBuildNumber = System.getenv("JENKINS_BUILD_NUMBER");
        if (jenkinsBuildNumber != null)
        {
            sauceOpts.setCapability("build", jenkinsBuildNumber);
        }
        else
        {
            String jobName = System.getenv("JOB_NAME");
            String buildNumber = System.getenv("BUILD_NUMBER");

            if (jobName != null && buildNumber != null)
            {
                sauceOpts.setCapability("build", String.format("%s__%s", jobName, buildNumber));
            }
            else
            {
                sauceOpts.setCapability("build", Util.buildTag);
            }
        }

        return sauceOpts;
    }

    private static RemoteWebDriver getMobileDriverInstance(TestPlatform tp, Scenario scenario, MutableCapabilities addlCaps)
    {
        URL url = null;
        RemoteWebDriver driver;
        String resultsURL = "";

        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("name", scenario.getName());

        if (addlCaps != null)
        {
            caps.merge(addlCaps);
        }

        caps.setCapability("platformName", tp.getPlatformName());

        if (tp.getPlatformVersion() != null)
        {
            caps.setCapability("platformVersion", tp.getPlatformVersion());
        }

        if (tp.getDeviceName() != null)
        {
            caps.setCapability("deviceName", tp.getDeviceName());
        }

//        caps.setCapability("appiumVersion", "1.9.1");
        caps.setCapability("deviceOrientation", "portrait");
        caps.setCapability("recordMp4", "true");

//        if (Util.useUnifiedPlatform == true)
        {
            caps.setCapability("username", userName);
            caps.setCapability("accesskey", accessKey);

            url = SAUCE_US_URL;
        }
//        else if (tp.getDeviceName() != null && (tp.getDeviceName().endsWith(" Simulator") || tp.getDeviceName().endsWith(" Emulator")))
//        {
//            caps.setCapability("username", userName);
//            caps.setCapability("accesskey", accessKey);
//            url = SAUCE_US_URL;
//        }
//        else
//        {
//            caps.setCapability("username", userName);
//            caps.setCapability("accesskey", accessKey);
//            url = SAUCE_US_URL;
//        }

        Util.log("Loading driver...");
        long start = System.currentTimeMillis();

        Platform platform = Platform.fromString(tp.getPlatformName());
        try
        {
            switch (platform)
            {
                case ANDROID:
                    caps.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.CHROME);
                    caps.setCapability("automationName", "UIAutomator2");
                    driver = new AndroidDriver(url, caps);
                    break;

                case IOS:
                    caps.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.SAFARI);
                    caps.setCapability("automationName", "XCUITest");
                    caps.setCapability("sendKeyStrategy", "setValue");
                    driver = new IOSDriver(url, caps);
                    break;

                default:
                    return null;
            }
        }
        catch (SessionNotCreatedException e)
        {
            Util.log("Failed to create a new mobile session: %s", e.getMessage());
            Util.log("-- tried creating a session for %s", tp.toString());
            return null;
        }

        long stop = System.currentTimeMillis();
        Util.log("Driver loaded in %.2f seconds", ((stop - start) / 1000f));

        resultsURL = (String) driver.getCapabilities().getCapability("testobject_test_report_url");

        String sessionId = driver.getSessionId().toString();
        Util.log("Started %s", new Date().toString());
        Util.log("Test Results: %s", resultsURL);
        Util.log("SauceOnDemandSessionID=%s job-name=%s", sessionId, scenario.getName());

        // Set reasonable page load and script timeouts
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);

        return driver;
    }
}
