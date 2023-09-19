package com.codecool.pages;

import com.codecool.util.WebdriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class MainPage {

    private static WebDriver driver;
    private static WebDriverWait wait;

    static By vatRates = By.xpath("//label[contains(@for,'VAT_')]");
    static By countrySelector = By.xpath("//select[@name='Country']");
    static By fivePercentVatRadioBtn = By.xpath("//*[@id='VAT_5']/following-sibling::label");
    static By eighteenPercentVatRadioBtn = By.xpath("//*[@id='VAT_18']/following-sibling::label");
    static By twentySevenPercentVatRadioBtn = By.xpath("//*[@id='VAT_27']/following-sibling::label");
    static By priceWithoutVatInputLabel = By.xpath("//*[@id='VATpct2']");
    static By consentBtn = By.xpath("//*[@class='fc-button fc-cta-consent fc-primary-button']");
    static By priceWithoutVatRadioBtn = By.xpath("//*[@id='F1']/following-sibling::label");
    static By valueAddedTaxRadioBtn = By.xpath("//*[@id='F2']/following-sibling::label");
    static By priceIncludedVatRadioBtn = By.xpath("//*[@id='F3']/following-sibling::label");
    static By negativeInputErrorMessage = By.xpath(("//*[@id='chart_div']/div/div/span"));
    static By priceWithoutVatInput = By.xpath("//*[@id='NetPrice']");
    static By valueAddedTaxInput = By.xpath("//*[@id='VATsum']");
    static By priceIncludedVatInput = By.xpath("//*[@id='Price']");

    public MainPage() {
        driver = WebdriverManager.getInstance();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // PageFactory.initElements(this.driver, this);
    }

    public void assertNegativeErrorMessage() {
        assertEquals("Negative values are invalid for a pie chart.×", driver.findElement(negativeInputErrorMessage));

    }

    public void clickConsentBtn() {
        waitAndClick(driver.findElement(consentBtn));
    }
//
//    public String getActualPriceIncludedVatValue() {
//        return driver.findElement(priceIncludedVatInput).getAttribute("value");
//    }
//
//    public String getActualValueAddedTaxValue() {
//        return driver.findElement(valueAddedTax).getAttribute("value");
//    }

    public static String getActualPriceWithoutVatValue() {
        return driver.findElement(priceWithoutVatInputLabel).getAttribute("value");
    }

    public List<WebElement> getVatRates() {
        return driver.findElements(vatRates);
    }

    public static void navigateToSite() {
        driver.get("https://www.calkoo.com/en/vat-calculator");
    }

    public static void selectCountry(String country) {
        Select asd = new Select(driver.findElement(countrySelector));
        asd.selectByVisibleText(country);
    }

    private static void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public static void click5VatRadioBtn() {
        waitAndClick(driver.findElement(fivePercentVatRadioBtn));
    }

    public void click18VatRadioBtn() {
        waitAndClick(driver.findElement(eighteenPercentVatRadioBtn));
    }

    public void click27VatRadioBtn() {
        waitAndClick(driver.findElement(twentySevenPercentVatRadioBtn));
    }

    public void assertPriceWithoutVatCalculations(String input, String expectedValueAddedTaxValue, String expectedPriceIncludedVatValue) {
        waitAndClick(driver.findElement(priceWithoutVatRadioBtn));
        waitAndClick(driver.findElement(priceWithoutVatInput));
        driver.findElement(priceWithoutVatInput).sendKeys(input);

        assertEquals(expectedValueAddedTaxValue, driver.findElement(valueAddedTaxInput).getAttribute("value"));
        assertEquals(expectedPriceIncludedVatValue, driver.findElement(priceIncludedVatInput).getAttribute("value"));
    }

    public void assertValueAddedTaxCalculations(String input, String expectedPriceWithoutVatValue, String expectedPriceIncludedVatValue) {
        waitAndClick(driver.findElement(valueAddedTaxRadioBtn));
        waitAndClick(driver.findElement(valueAddedTaxInput));
        driver.findElement(valueAddedTaxInput).sendKeys(input);

        assertEquals(expectedPriceWithoutVatValue, driver.findElement(priceWithoutVatInput).getAttribute("value"));
        assertEquals(expectedPriceIncludedVatValue, driver.findElement(priceIncludedVatInput).getAttribute("value"));
    }

    public void assertPriceIncVatCalculations(String input, String expectedPriceWithoutVatValue, String expectedValueAddedTax) {
        waitAndClick(driver.findElement(priceIncludedVatRadioBtn));
        waitAndClick(driver.findElement(priceIncludedVatInput));
        driver.findElement(priceIncludedVatInput).sendKeys(input);

        assertEquals(expectedPriceWithoutVatValue, driver.findElement(priceWithoutVatInput).getAttribute("value"));
        assertEquals(expectedValueAddedTax, driver.findElement(valueAddedTaxInput).getAttribute("value"));
    }

    public static void quitDriver() {
        WebdriverManager.quitDriver();
    }

}
