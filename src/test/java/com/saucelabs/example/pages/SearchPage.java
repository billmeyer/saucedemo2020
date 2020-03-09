package com.saucelabs.example.pages;

import com.saucelabs.example.Util;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchPage extends AbstractPage
{
    @FindBy(css = "#refine-results-template > button")
    private WebElement refineResults;

    @FindBy(css = "#search-controls-readonly-template > div:nth-child(1) > span > button")
    private WebElement editSearch;

    @FindBy(css = "#search-controls-template > div.button-container.col-sm-3.col-xs-12 > button.search-button.btn.btn-default")
    private WebElement searchButton;

    @FindBy(css = "#name-search-input")
    private WebElement nameSearch;

    @FindBy(xpath = "//div[@class = 'agent-title']")
//    @FindBy(css = "#result-template-small > div > div.agent-main.col-lg-9.col-md-9.col-sm-9.col-xs-9 > div:nth-child(1) > div")
    private WebElement agentTitle;

    @FindBy(xpath = "//a[@class = 'phone-small']")
//    @FindBy(css = "#result-template-small > div > div.agent-main.col-lg-9.col-md-9.col-sm-9.col-xs-9 > div.left-column > a")
    private WebElement agentPhone;

    public SearchPage(WebDriver driver)
    {
        super(driver);
        PageFactory.initElements(driver, this);
//
//        wait = new MyFluentWait<WebDriver>(driver)
//                .withTimeout(60, ChronoUnit.SECONDS)
//                .pollingEvery(2, ChronoUnit.SECONDS)
//                .ignoring(NoSuchElementException.class);
    }

    @Override
    public WebElement getPageLoadedTestElement()
    {
        return editSearch;
    }

    public void clickEditSearch()
    {
        editSearch.click();
    }

    public void setAgentName(String agentName)
    {
        nameSearch.clear();
        nameSearch.sendKeys(agentName);
    }

    public void clickSearch()
    {
        searchButton.click();
    }

    public String getAgentTitle()
    {
        return agentTitle.getText();
    }

    public String getPhoneNumber()
    {
        return agentPhone.getText();
    }
}
