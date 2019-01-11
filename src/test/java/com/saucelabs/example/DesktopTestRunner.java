package com.saucelabs.example;

import com.saucelabs.Browser;
import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.testng.TestNGCucumberRunner;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

//@ExtendedCucumberOptions(
//        customTemplatesPath = "/Users/bmeyer/github/billmeyer/saucedemo-java-cucumber/src/test/resources/templates/templates.json",
//        // Location of the JSON report...
//        jsonReport = "target/cucumber-report/cucumber.json",
//        // Location of the json usage report...
//        jsonUsageReport = "target/cucumber-report/cucumber-usage.json.save",
//        // Location to write the reports...
//        outputFolder = "target/extendedcucumber", retryCount = 3,
//        // We want the usage report...
//        usageReport = true,
//        // We want the overview report...
//        overviewReport = true,
//        // We want the overview charts report...
//        overviewChartsReport = true,
//        // We want the detailed report...
//        detailedReport = true,
//        // We want the detailed aggregate report...
//        detailedAggregatedReport = true,
//        // We want the coverage report...
//        coverageReport = true,
//        // No PDF output...
//        toPDF = false,
//        // We want the breakdown report...
//        breakdownReport = true,
//        // We want the feature map report...
//        featureMapReport = true,
//        //
//        featureOverviewChart = true,
//        //
//        knownErrorsReport = false,
//        //
//        consolidatedReport = true,
//        //
//        systemInfoReport = true,
//        //
//        benchmarkReport = true
////        excludeCoverageTags = {"@flaky" },
////        includeCoverageTags = {"@passed" },
//)
@CucumberOptions(
        // Features
        features = "src/test/resources/features",
        // Glue
        glue = {"com/saucelabs/example/stepdefs"},
//        tags = {"@Regression1"},
//        tags = {"@SignOn"},
//          tags = {"@Orders"},
//        tags = {"@Orders,@SignOn"},
        snippets = SnippetType.CAMELCASE,
        // Plugins
        plugin = {
                // Cucumber report location
                "json:target/cucumber-report/cucumber.json",
//            "rerun:target/cucumber-reports/rerun.txt",
                "usage:target/cucumber-report/cucumber-usage.json",
//            "pretty:target/pretty",
//            "progress:target/progress",
//            "timeline:target/timeline",
//            "testng:target/testng",
                "html:target/cucumber-html-report",
//            "com.saucelabs.cucumber.ExtentReportsFormatter:target/my-extent-reports"
//                "com.vimalselvam.cucumber.listener.ExtentCucumberFormatter:target/extent-reports"
}
)
public class DesktopTestRunner extends AbstractTestRunner
{
//    private TestNGCucumberRunner testNGCucumberRunner;

    @Parameters({"browser", "version", "platform"})
    @BeforeClass(alwaysRun = false)
    public void setUpDesktopProfile(@Optional String browser, String version, String platform)
    {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());

        TestPlatform.Builder builder = new TestPlatform.Builder();

        TestPlatform tp = builder.browser(Browser.valueOf(browser)).browserVersion(version).platformName(platform).build();
        Util.setTestPlatform(tp);
    }

//    @AfterClass(alwaysRun = true)
//    public void tearDownClass()
//    {
//        if (testNGCucumberRunner == null)
//        {
//            return;
//        }
//
//        testNGCucumberRunner.finish();
//
//    }
//
//    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
//    public void feature(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper cucumberFeatureWrapper)
//    throws Throwable
//    {
//        PickleEvent event = pickleWrapper.getPickleEvent();
//        Pickle pickle = event.pickle;
//        System.out.printf("Running scenario: %s\n", pickle.getName());
//
//        // the 'featureWrapper' parameter solely exists to display the feature file in a test report
//        testNGCucumberRunner.runScenario(event);
//    }
//
//    @DataProvider
//    public Object[][] scenarios()
//    {
//        if (testNGCucumberRunner == null)
//        {
//            return null;
//        }
//
//        return testNGCucumberRunner.provideScenarios();
//    }

}
