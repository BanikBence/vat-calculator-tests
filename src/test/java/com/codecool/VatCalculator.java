package com.codecool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VatCalculator {


    private WebDriver driver = new ChromeDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


    private void selectCountry(String country) {
        Select countrySelection = new Select(driver.findElement(By.xpath("//select[@name='Country']")));
        countrySelection.selectByVisibleText(country);
    }

    @BeforeEach
    void setup() {
        driver.get("https://www.calkoo.com/en/vat-calculator");
        driver.findElement(By.xpath("//*[@class='fc-button fc-cta-consent fc-primary-button']")).click();
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
        WebElement fivePercentVatRadioBtn = driver.findElement(By.xpath("//*[@id='VAT_5']/following-sibling::label"));
        fivePercentVatRadioBtn.click();

        String expectedNetPriceValue = "× 0.050000";
        String actualNetPriceValue = driver.findElement(By.xpath("//*[@id='VATpct2']")).getAttribute("value");

        assertEquals(expectedNetPriceValue, actualNetPriceValue);
    }
}