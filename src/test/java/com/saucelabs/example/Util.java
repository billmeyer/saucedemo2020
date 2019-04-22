package com.saucelabs.example;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

public class Util
{
    /**
     * If true, the tests will be run on the local desktop.  If false, the tests will run on Sauce Labs.
     */
    public static final boolean runLocal = false;

    public static final String buildTag = "Build " + new Date();

    public static boolean isMobile;
    public static boolean isEmuSim;

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

        if (runLocal || isMobile)
        {
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

        if (runLocal || isMobile)
        {
            return;
        }

        String msg = String.format(format, args);
        ((JavascriptExecutor) driver).executeScript("sauce:job-name=" + msg);
    }

    public static void reportSauceLabsResult(WebDriver driver, boolean status)
    {
        if (!isMobile || isEmuSim)
        {
            ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + status);
        }
    }

    /**
     * Uses the Appium V2 RESTful API to report test result status to the Sauce Labs dashboard.
     *
     * @param sessionId The session ID we want to set the status for
     * @param status    TRUE if the test was successful, FALSE otherwise
     */
    public static void reportTestObjectResult(String sessionId, boolean status)
    {
        if (runLocal || isEmuSim)
        {
            return;
        }

        // The Appium REST Api expects JSON payloads...
        MediaType[] mediaType = new MediaType[]{MediaType.APPLICATION_JSON_TYPE};

        // Construct the new REST client...
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target("https://app.testobject.com/api/rest/v2/appium");

        // Construct the REST body payload...
        Entity entity = Entity.json(Collections.singletonMap("passed", status));

        // Build a PUT request to /v2/appium/session/{:sessionId}/test
        Invocation.Builder request = resource.path("session").path(sessionId).path("test").request(mediaType);

        // Execute the PUT request...
        request.put(entity);
    }

    public static void log(String format, Object... args)
    {
        System.out.printf(format, args);
        System.out.println();
    }

//    public static void log(Object instance, String format, Object... args)
//    {
//        String mergedFormat = "[%s][%s] " + format;
//        Object[] mergedArgs = new Object[args.length + 2];
//        mergedArgs[0] = Thread.currentThread().getName();
//        mergedArgs[1] = instance.getClass().getSimpleName();
//        System.arraycopy(args, 0, mergedArgs, 2, args.length);
//
//        System.out.printf(mergedFormat, mergedArgs);
//        System.out.println();
//    }
//
//    protected void log(Class instance, String output)
//    {
//        System.out.printf("[%s][%s] %s\n", Thread.currentThread().getName(), instance.getClass().getSimpleName(),
//        output);
//    }

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
}
