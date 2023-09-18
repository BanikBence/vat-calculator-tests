package com.codecool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

class VatCalculator {


    private WebDriver driver = new ChromeDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @BeforeEach
    void setup() {
        driver.get("https://www.calkoo.com/en/vat-calculator");
        driver.findElement(By.xpath("//*[@class='fc-button fc-cta-consent fc-primary-button']")).click();
    }

    @AfterEach
    void tearDown() {
        //   driver.quit();
    }

    @Test
    public void expectedTaxOptionsAppearAfterCountrySelection() {
        Select countrySelection = new Select(driver.findElement(By.xpath("//select[@name='Country']")));
        String option = "Hungary";
        countrySelection.selectByVisibleText(option);

        List<WebElement> vatRates = driver.findElements(By.xpath("div[@class='mainarea container-fluid py-0 px-2' and contains(@for, 'VAT_')]"));

        vatRates.forEach(WebElement::isDisplayed);

        for (WebElement element : vatRates) {
            Assertions.assertTrue(element.isDisplayed());
        }
    }
}