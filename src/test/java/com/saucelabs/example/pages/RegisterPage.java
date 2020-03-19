package com.saucelabs.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage extends AbstractPage
{
    public static final String PAGE_URL = "https://www.saucedemo.com/register.html";

    @FindBy(css = "#register_container")
    private WebElement registerContainerElem;

    public RegisterPage(WebDriver driver)
    {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    public WebElement getPageLoadedTestElement()
    {
        return registerContainerElem;
    }
}
