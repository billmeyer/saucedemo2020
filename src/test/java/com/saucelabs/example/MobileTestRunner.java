package com.saucelabs.example;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.testng.TestNGCucumberRunner;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

// @formatter:off
@CucumberOptions(
    // Features
    features = "src/test/resources/features",

    // Glue
    glue = {"com/saucelabs/example/stepdefs"},
    snippets = SnippetType.CAMELCASE,

    // Plugins
    plugin = {
        // Cucumber report location
        "json:target/json-reports/cucumber-mobile.json",
        "usage:target/usage-reports/cucumber-usage-mobile.json"
//        "html:target/cucumber-html-report"
    }
)
// @formatter:on
public class MobileTestRunner extends AbstractTestRunner
{
    @Parameters({"deviceName", "platformName", "platformVersion", "appKey"})
    @BeforeClass(alwaysRun = true)
    public void setUpMobileProfile(@Optional("") String deviceName, String platformName, String platformVersion, @Optional("") String appKey)
    {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());

        TestPlatform.Builder builder = new TestPlatform.Builder();

        if (appKey.startsWith("$ENV{"))
        {
            String envVar = appKey.substring(5, appKey.length()-1);
            appKey = System.getenv(envVar);
        }
//        System.err.printf("appKey=%s\n", appKey);

        PlatformContainer pc;

        if (deviceName.endsWith(" Emulator"))
            pc = PlatformContainer.EMULATOR;
        else if (deviceName.endsWith(" Simulator"))
            pc = PlatformContainer.SIMULATOR;
        else
            pc = PlatformContainer.MOBILE;

        // @formatter:off
        TestPlatform tp = builder
                .deviceName(deviceName)
                .platformName(platformName)
                .platformVersion(platformVersion)
                .platformContainer(pc)
                .appKey(appKey)
                .build();
        // @formatter:on

        Util.setTestPlatform(tp);
    }
}
