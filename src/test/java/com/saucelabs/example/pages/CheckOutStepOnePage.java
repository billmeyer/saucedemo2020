package com.saucelabs.example.pages;

import com.saucelabs.example.Util;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckOutStepOnePage extends AbstractPage
{
    public static final String PAGE_URL = "https://www.saucedemo.com/checkout-step-one.html";

//    @FindBy(xpath = "//input[@data-test='firstName']")
    @FindBy(id = "first-name")
    private WebElement firstNameElem;

//    @FindBy(xpath = "//input[@data-test='lastName']")
    @FindBy(id = "last-name")
    private WebElement lastNameElem;

//    @FindBy(xpath = "//input[@data-test='postalCode']")
    @FindBy(id = "postal-code")
    private WebElement postalCodeElem;

    @FindBy(css = "div.checkout_buttons > input")
    private WebElement continueElem;

    public CheckOutStepOnePage(WebDriver driver)
    {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    public WebElement getPageLoadedTestElement()
    {
        return firstNameElem;
    }

    public void enterFirstName(String firstName)
    {
        firstNameElem.clear();
        firstNameElem.sendKeys(firstName);
        Util.triggerOnChange(getDriver(), "first-name");
    }

    public void enterLastName(String lastName)
    {
        lastNameElem.clear();
        lastNameElem.sendKeys(lastName);
        Util.triggerOnChange(getDriver(), "last-name");
    }

    public void enterPostalCode(String postalCode)
    {
        postalCodeElem.clear();
        postalCodeElem.sendKeys(postalCode);
        Util.triggerOnChange(getDriver(), "postal-code");
    }

    public void clickContinue()
    {
        continueElem.submit();
    }
}

