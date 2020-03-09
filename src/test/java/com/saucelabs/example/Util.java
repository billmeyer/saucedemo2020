package com.saucelabs.example;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Util
{
    public static final boolean useUnifiedPlatform = false;
    public static final String buildTag = "saucedemo-java-cucumber-" + new Date().getTime();

    private static ThreadLocal<TestPlatform> testPlatformThreadLocal = new ThreadLocal<>();

    // https://wiki.saucelabs.com/display/DOCS/Annotating+Tests+with+the+Sauce+Labs+REST+API

    /**
     * Puts a Sauce breakpoint in the test. Test execution will pause at this point, waiting for manual control
     * by clicking in the test’s live video.  A space must be included between sauce: and break.
     *
     * @param driver The WebDriver instance we use to execute the Javascript command
     */
    public static void breakpoint(WebDriver driver)
    {
        ((JavascriptExecutor) driver).executeScript("sauce: break");
    }

    /**
     * Logs the given line in the job’s Selenium commands list. No spaces can be between sauce: and context.
     *
     * @param driver The WebDriver instance we use to log the info
     */
    public static void info(WebDriver driver, String format, Object... args)
    {
        System.out.printf(format, args);
        System.out.println();

        PlatformContainer pc = getTestPlatform().getPlatformContainer();

        switch (pc)
        {
            case DESKTOP:
            case EMULATOR:
            case SIMULATOR:
            case HEADLESS:
                break;

            default:
                // All others... not supported.
                return;
        }

        String msg = String.format(format, args);
        ((JavascriptExecutor) driver).executeScript("sauce:context=" + msg);
    }

    /**
     * Sets the job name
     *
     * @param driver The WebDriver instance we use to log the info
     */
    public static void name(WebDriver driver, String format, Object... args)
    {
        System.out.printf(format, args);
        System.out.println();

        PlatformContainer pc = getTestPlatform().getPlatformContainer();

        switch (pc)
        {
            case DESKTOP:
            case EMULATOR:
            case SIMULATOR:
            case HEADLESS:
                break;

            default:
                // All others... not supported.
                return;
        }

        String msg = String.format(format, args);
        ((JavascriptExecutor) driver).executeScript("sauce:job-name=" + msg);
    }

    public static void reportSauceLabsResult(WebDriver driver, boolean status)
    {
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + status);
    }

    public static void log(String format, Object... args)
    {
        System.out.printf(format, args);
        System.out.println();
    }

    public static void sauceThrottle(WebDriver driver, SauceThrottle condition)
    {
        PlatformContainer pc = getTestPlatform().getPlatformContainer();

        switch (pc)
        {
            case DESKTOP:
                break;

            default:
                // All others... not supported.
                return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("condition", condition.toValue());
        try
        {
            ((JavascriptExecutor) driver).executeScript("sauce:throttleNetwork", map);
        }
        catch (JavascriptException e)
        {
            RemoteWebDriver rwd = (RemoteWebDriver) driver;
            Capabilities caps = rwd.getCapabilities();
            System.err.printf(">>> Failed to set Sauce Throttle: %s\n(%s %s on %s)\n", e.getMessage());
            System.err.printf(">>> (%s %s on %s)\n", caps.getBrowserName(), caps.getVersion(), caps.getPlatform());
        }
    }

    public static Map<String, Object> getSaucePerformance(WebDriver driver)
    {
        PlatformContainer pc = getTestPlatform().getPlatformContainer();
        switch (pc)
        {
            case DESKTOP:
            case EMULATOR:
            case SIMULATOR:
            case HEADLESS:
                break;

            default:
                // All others... not supported.
                return null;
        }

        Browser browser = getTestPlatform().getBrowser();
        if (browser == null)
        {
            System.err.printf("Browser is null in getSaucePerformance()!\n");
            return null;
        }

        switch (browser)
        {
            case CHROME:
                break;

            default:
                // All others... not supported.
                return null;
        }

        Map<String, Object> results = null;

        try
        {
            Map<String, Object> map = new HashMap<>();
            map.put("type", "sauce:performance");

            try
            {
                Object object = ((JavascriptExecutor) driver).executeScript("sauce:log", map);
                results = (Map<String, Object>) object;
            }
//            catch (JavascriptException e)
            catch (Exception e)
            {
                RemoteWebDriver rwd = (RemoteWebDriver) driver;
                Capabilities caps = rwd.getCapabilities();
                System.err.printf(">>> Failed to retrieve Sauce Performance Log: %s\n(%s %s on %s)\n", e.getMessage());
                System.err.printf(">>> (%s %s on %s)\n", caps.getBrowserName(), caps.getVersion(), caps.getPlatform());
            }

            // Sample return value:
            // {load=3595, speedIndex=759.3125, pageWeight=3208724, firstMeaningfulPaint=595, timeToFirstInteractive=3586, timeToFirstByte=22, firstPaint=595, firstContentfulPaint=595, pageWeightEncoded=150159,
            // perceptualSpeedIndex=863.4155969137146, domContentLoaded=3586}
        }
        catch (org.openqa.selenium.UnsupportedCommandException ignored)
        {
            ignored.printStackTrace();
        }

        return results;
    }

    public static void sleep(long msecs)
    {
        try
        {
            Thread.sleep(msecs);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static void takeScreenShot(WebDriver driver)
    {
        PlatformContainer pc = getTestPlatform().getPlatformContainer();

        switch (pc)
        {
            case DESKTOP:
            case EMULATOR:
            case SIMULATOR:
            case HEADLESS:
                break;

            default:
                // All others... not supported.
                return;
        }

        WebDriver augDriver = new Augmenter().augment(driver);
        File file = ((TakesScreenshot) augDriver).getScreenshotAs(OutputType.FILE);

        long time = new Date().getTime();
        String outputName = time + ".png";
        try
        {
            FileUtils.copyFile(file, new File(outputName));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static TestPlatform getTestPlatform()
    {
        return testPlatformThreadLocal.get();
    }

    public static void setTestPlatform(TestPlatform tp)
    {
        testPlatformThreadLocal.set(tp);
    }

    /*
     * Hack to trigger events because React doesn't properly on iOS
     * See https://github.com/saucelabs/sample-app-web/issues/10
     */
    public static void triggerOnChange(WebDriver driver, String elementId)
    {
        TestPlatform tp = getTestPlatform();
        if (!tp.getPlatformName().equalsIgnoreCase("ios"))
            return;

        JavascriptExecutor executor = (JavascriptExecutor)driver;

        // @formatter:off
        String jscode =
                String.format(
                    "var input = document.getElementById('%s'); var lastValue = '';" +
                    "let event = new Event('input', { bubbles: true });" +
                    "let tracker = input._valueTracker; if (tracker) { tracker.setValue(lastValue); }" +
                    "input.dispatchEvent(event);", elementId);
        // @formatter:on

        executor.executeScript(jscode);
    }
}
