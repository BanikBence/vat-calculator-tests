package com.codecool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VatCalculator {


    private WebDriver driver = new ChromeDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    private void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    private void selectCountry(String country) {
        Select countrySelection = new Select(driver.findElement(By.xpath("//select[@name='Country']")));
        countrySelection.selectByVisibleText(country);
    }


    private void click18VatRadioBtn() {
        WebElement fivePercentVatRadioBtn = driver.findElement(By.xpath("//*[@id='VAT_18']/following-sibling::label"));
        waitAndClick(fivePercentVatRadioBtn);
    }

    private void click27VatRadioBtn() {
        WebElement fivePercentVatRadioBtn = driver.findElement(By.xpath("//*[@id='VAT_27']/following-sibling::label"));
        waitAndClick(fivePercentVatRadioBtn);
    }

    private void click5VatRadioBtn() {
        WebElement fivePercentVatRadioBtn = driver.findElement(By.xpath("//*[@id='VAT_5']/following-sibling::label"));
        waitAndClick(fivePercentVatRadioBtn);
    }

    private void assertPriceWithoutTaxCalculations(String input, String expectedValueAddedTaxValue, String expectedPriceIncludedVatValue) {
        WebElement priceWithoutVatInput = driver.findElement(By.xpath("//*[@id='NetPrice']"));
        waitAndClick(priceWithoutVatInput);
        priceWithoutVatInput.sendKeys(input);

        String actualValueAddedTaxValue = driver.findElement(By.xpath("//*[@id='VATsum']")).getAttribute("value");

        String actualPriceIncludedVatValue = driver.findElement(By.xpath("//*[@id='Price']")).getAttribute("value");

        assertEquals(expectedValueAddedTaxValue, actualValueAddedTaxValue);
        assertEquals(expectedPriceIncludedVatValue, actualPriceIncludedVatValue);
    }

    private void assertValueAddedTaxCalculations(String input, String expectedPriceWithoutVatValue, String expectedPriceIncludedVatValue) {
        WebElement valueAddedTaxInput = driver.findElement(By.xpath("//*[@id='VATsum']"));
        waitAndClick(valueAddedTaxInput);
        valueAddedTaxInput.sendKeys(input);

        String actualPriceWithoutVatValue = driver.findElement(By.xpath("//*[@id='NetPrice']")).getAttribute("value");

        String actualPriceIncludedVatValue = driver.findElement(By.xpath("//*[@id='Price']")).getAttribute("value");

        assertEquals(expectedPriceWithoutVatValue, actualPriceWithoutVatValue);
        assertEquals(expectedPriceIncludedVatValue, actualPriceIncludedVatValue);
    }

    private void assertPriceIncVatCalculations(String input, String expectedPriceWithoutVatValue, String expectedPriceIncludedVatValue) {
        WebElement priceIncludedVatInput = driver.findElement(By.xpath("//*[@id='Price']"));
        waitAndClick(priceIncludedVatInput);
        priceIncludedVatInput.sendKeys(input);

        String actualPriceWithoutVatValue = driver.findElement(By.xpath("//*[@id='NetPrice']")).getAttribute("value");

        String actualValueAddedTaxValue = driver.findElement(By.xpath("//*[@id='VATsum']")).getAttribute("value");

        assertEquals(expectedPriceWithoutVatValue, actualPriceWithoutVatValue);
        assertEquals(expectedPriceIncludedVatValue, actualValueAddedTaxValue);
    }

    @BeforeEach
    void setup() {
        driver.get("https://www.calkoo.com/en/vat-calculator");
        WebElement countrySelector = driver.findElement(By.xpath("//*[@class='fc-button fc-cta-consent fc-primary-button']"));
        waitAndClick(countrySelector);
        selectCountry("Hungary");
    }

    @AfterEach
    void tearDown() {
        //  driver.quit();
    }

    @Test
    public void expectedTaxOptionsAppearAfterCountrySelection() {
        List<WebElement> vatRates = driver.findElements(By.xpath("div[@class='mainarea container-fluid py-0 px-2' and contains(@for, 'VAT_')]"));

        for (WebElement element : vatRates) {
            assertTrue(element.isDisplayed());
        }
    }

    @Test
    public void chosenVatRateAppearsBehindNetpriceLabel() {
        click5VatRadioBtn();

        String expectedNetPriceValue = "× 0.050000";
        String actualNetPriceValue = driver.findElement(By.xpath("//*[@id='VATpct2']")).getAttribute("value");

        assertEquals(expectedNetPriceValue, actualNetPriceValue);
    }

    @Test
    public void fillingThePriceWithoutVatFieldCalculatesOtherFields() {

        WebElement priceWithoutVatRadioBtn = driver.findElement(By.xpath("//*[@id='F1']/following-sibling::label"));
        waitAndClick(priceWithoutVatRadioBtn);

        click5VatRadioBtn();
        assertPriceWithoutTaxCalculations("5", "0.25", "5.25");

        click18VatRadioBtn();
        assertPriceWithoutTaxCalculations("5", "0.90", "5.90");

        click27VatRadioBtn();
        assertPriceWithoutTaxCalculations("5", "1.35", "6.35");

    }

    @Test
    public void fillingTheValueAddedTaxFieldCalculatesOtherFields() {
        WebElement valueAddedTaxBtn = driver.findElement(By.xpath("//*[@id='F2']/following-sibling::label"));
        waitAndClick(valueAddedTaxBtn);

        click5VatRadioBtn();
        assertValueAddedTaxCalculations("5", "100.00", "105.00");

        click18VatRadioBtn();
        assertValueAddedTaxCalculations("5", "27.78", "32.78");

        click27VatRadioBtn();
        assertValueAddedTaxCalculations("5", "18.52", "23.52");

    }

    @Test
    public void fillingThePriceIncVatFieldsCalculatesOtherFields() {
        WebElement priceInclVattn = driver.findElement(By.xpath("//*[@id='F3']/following-sibling::label"));
        waitAndClick(priceInclVattn);

        click5VatRadioBtn();
        assertPriceIncVatCalculations("5", "4.76", "0.24");

        click18VatRadioBtn();
        assertPriceIncVatCalculations("5", "4.24", "0.76");

        click27VatRadioBtn();
        assertPriceIncVatCalculations("5", "3.94", "1.06");
    }

    @Test
    public void negativeInputInAnyFieldsProducesErrorMessage() {
        WebElement priceWithoutVatRadioBtn = driver.findElement(By.xpath("//*[@id='F1']/following-sibling::label"));
        waitAndClick(priceWithoutVatRadioBtn);

        click5VatRadioBtn();
        assertPriceWithoutTaxCalculations("-5", "-0.25", "-5.25");

        String expectedErrorMessage = "Negative values are invalid for a pie chart.×";
        String actualErrorMessage = driver.findElement(By.xpath(("//*[@id='chart_div']/div/div/span"))).getText();

        assertEquals(expectedErrorMessage, actualErrorMessage);

        assertValueAddedTaxCalculations("-5", "-5", "-5.25");
        assertEquals(expectedErrorMessage, actualErrorMessage);

        assertPriceIncVatCalculations("-5", "-5", "-0.25");
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }


}