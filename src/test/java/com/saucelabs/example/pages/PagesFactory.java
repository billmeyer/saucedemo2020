package com.saucelabs.example.pages;

import org.openqa.selenium.remote.RemoteWebDriver;

public class PagesFactory
{
    private static ThreadLocal<PagesFactory> pagesFactories = new ThreadLocal<>();
    private RemoteWebDriver driver;

    private CheckOutStepOnePage checkOutStepOnePage;
    private CheckOutStepTwoPage checkOutStepTwoPage;
    private InventoryPage inventoryPage;
    private RegisterPage registerPage;
    private LoginPage loginPage;
    private CartPage cartPage;

    public static void start(RemoteWebDriver driver)
    {
        PagesFactory instance = new PagesFactory(driver);
        pagesFactories.set(instance);
    }

    public static PagesFactory getInstance()
    {
        return pagesFactories.get();
    }

    private PagesFactory(RemoteWebDriver driver)
    {
        this.driver = driver;
        checkOutStepOnePage = new CheckOutStepOnePage(driver);
        checkOutStepTwoPage = new CheckOutStepTwoPage(driver);
        inventoryPage = new InventoryPage(driver);
        loginPage = new LoginPage(driver);
        cartPage = new CartPage(driver);
    }

    public RemoteWebDriver getDriver()
    {
        return driver;
    }

    public CheckOutStepOnePage getCheckOutStepOnePage()
    {
        return checkOutStepOnePage;
    }

    public CheckOutStepTwoPage getCheckOutStepTwoPage()
    {
        return checkOutStepTwoPage;
    }

    public InventoryPage getInventoryPage()
    {
        return inventoryPage;
    }

    public RegisterPage getRegisterPage()
    {
        return registerPage;
    }

    public LoginPage getLoginPage()
    {
        return loginPage;
    }

    public CartPage getCartPage()
    {
        return cartPage;
    }
}
