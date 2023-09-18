package com.codecool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

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

    private void assertCalculations(String input, String expectedValueAddedTaxValue, String expectedPriceIncludedVatValue) {
        WebElement priceWithoutVatInput = driver.findElement(By.xpath("//*[@id='NetPrice']"));
        waitAndClick(priceWithoutVatInput);
        priceWithoutVatInput.sendKeys(input);


        String actualValueAddedTaxValue = driver.findElement(By.xpath("//*[@id='VATsum']")).getAttribute("value");

        String actualPriceIncludedVatValue = driver.findElement(By.xpath("//*[@id='Price']")).getAttribute("value");

        assertEquals(expectedValueAddedTaxValue, actualValueAddedTaxValue);
        assertEquals(expectedPriceIncludedVatValue, actualPriceIncludedVatValue);
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
        driver.quit();
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
        assertCalculations("5", "0.25", "5.25");

        click18VatRadioBtn();
        assertCalculations("5", "0.90", "5.90");

        click27VatRadioBtn();
        assertCalculations("5", "1.35", "6.35");

    }

}