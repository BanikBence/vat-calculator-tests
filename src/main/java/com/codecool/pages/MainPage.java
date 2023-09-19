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
    static By priceWithoutVatInput = By.xpath("//*[@id='VATpct2']");
    static By valueAddedTax = By.xpath("//*[@id='VATsum']");
    static By priceIncludedVat = By.xpath("//*[@id='Price']");
    static By consentBtn = By.xpath("//*[@class='fc-button fc-cta-consent fc-primary-button']");
    static By priceWithoutVatRadioBtn = By.xpath("//*[@id='F1']/following-sibling::label");
    static By negativeInputErrorMessage = By.xpath(("//*[@id='chart_div']/div/div/span"));

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

    public String getActualPriceIncludedVatValue() {
        return driver.findElement(priceIncludedVat).getAttribute("value");
    }

    public String getActualValueAddedTaxValue() {
        return driver.findElement(valueAddedTax).getAttribute("value");
    }

    public static String getActualPriceWithoutVatValue() {
        return driver.findElement(priceWithoutVatInput).getAttribute("value");
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

        waitAndClick(driver.findElement(priceWithoutVatInput));
        driver.findElement(priceWithoutVatInput).sendKeys(input);

        assertEquals(expectedValueAddedTaxValue, getActualValueAddedTaxValue());
        assertEquals(expectedPriceIncludedVatValue, getActualPriceIncludedVatValue());
    }

    public void assertValueAddedTaxCalculations(String input, String expectedPriceWithoutVatValue, String expectedPriceIncludedVatValue) {
        WebElement valueAddedTaxInput = driver.findElement(By.xpath("//*[@id='VATsum']"));
        waitAndClick(valueAddedTaxInput);
        valueAddedTaxInput.sendKeys(input);

        String actualPriceWithoutVatValue = driver.findElement(By.xpath("//*[@id='NetPrice']")).getAttribute("value");

        String actualPriceIncludedVatValue = driver.findElement(By.xpath("//*[@id='Price']")).getAttribute("value");

        assertEquals(expectedPriceWithoutVatValue, actualPriceWithoutVatValue);
        assertEquals(expectedPriceIncludedVatValue, actualPriceIncludedVatValue);
    }

    public void assertPriceIncVatCalculations(String input, String expectedPriceWithoutVatValue, String expectedPriceIncludedVatValue) {
        WebElement priceIncludedVatInput = driver.findElement(By.xpath("//*[@id='Price']"));
        waitAndClick(priceIncludedVatInput);
        priceIncludedVatInput.sendKeys(input);

        String actualPriceWithoutVatValue = driver.findElement(By.xpath("//*[@id='NetPrice']")).getAttribute("value");

        String actualValueAddedTaxValue = driver.findElement(By.xpath("//*[@id='VATsum']")).getAttribute("value");

        assertEquals(expectedPriceWithoutVatValue, actualPriceWithoutVatValue);
        assertEquals(expectedPriceIncludedVatValue, actualValueAddedTaxValue);
    }

    public static void quitDriver() {
        WebdriverManager.quitDriver();
    }

}
