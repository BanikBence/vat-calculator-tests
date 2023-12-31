package com.codecool;

import com.codecool.pages.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

class VatCalculatorTest {

    private MainPage mainPage;  //

    @BeforeEach
    void setup() {
        mainPage = new MainPage();
        mainPage.navigateToSite();
        mainPage.clickConsentBtn();
        mainPage.selectCountry("Hungary");
    }

    @AfterEach
    void tearDown() {
        mainPage.quitDriver();
    }

    @Test
    public void countrySelectionDropdownTest() {
        mainPage.selectCountry("Austria");
        mainPage.selectCountry("Saint Vincent and the Grenadines");
        mainPage.selectCountry("France");

    }

    @Test

    public void expectedTaxOptionsAppearAfterCountrySelection() {
        for (WebElement element : mainPage.getVatRates()) {
            assertTrue(element.isDisplayed());
        }
    }

    @Test
    public void chosenVatRateAppearsBehindNetpriceLabel() {
        MainPage.click5VatRadioBtn();

        String expectedNetPriceValue = "× 0.050000";

        assertEquals(expectedNetPriceValue, mainPage.getActualPriceWithoutVatValue());
    }

    @Test
    public void fillingThePriceWithoutVatFieldCalculatesOtherFields() {
        mainPage.click5VatRadioBtn();
        mainPage.assertPriceWithoutVatCalculations("5", "0.25", "5.25");

        mainPage.click18VatRadioBtn();
        mainPage.assertPriceWithoutVatCalculations("5", "0.90", "5.90");

        mainPage.click27VatRadioBtn();
        mainPage.assertPriceWithoutVatCalculations("5", "1.35", "6.35");
    }

    @Test
    public void fillingTheValueAddedTaxFieldCalculatesOtherFields() {
        mainPage.click5VatRadioBtn();
        mainPage.assertValueAddedTaxCalculations("5", "100.00", "105.00");

        mainPage.click18VatRadioBtn();
        mainPage.assertValueAddedTaxCalculations("5", "27.78", "32.78");

        mainPage.click27VatRadioBtn();
        mainPage.assertValueAddedTaxCalculations("5", "18.52", "23.52");
    }

    @Test
    public void fillingThePriceIncVatFieldsCalculatesOtherFields() {

        mainPage.click5VatRadioBtn();
        mainPage.assertPriceIncVatCalculations("5", "4.76", "0.24");

        mainPage.click18VatRadioBtn();
        mainPage.assertPriceIncVatCalculations("5", "4.24", "0.76");

        mainPage.click27VatRadioBtn();
        mainPage.assertPriceIncVatCalculations("5", "3.94", "1.06");
    }

    @Test
    public void negativeInputInAnyFieldsProducesErrorMessage() {
        mainPage.click5VatRadioBtn();

        mainPage.assertPriceWithoutVatCalculations("-5", "-0.25", "-5.25");
        mainPage.assertNegativeErrorMessage();

        mainPage.assertValueAddedTaxCalculations("-5", "-100.00", "-105.00");
        mainPage.assertNegativeErrorMessage();

        mainPage.assertPriceIncVatCalculations("-5", "-4.76", "-0.24");
        mainPage.assertNegativeErrorMessage();
    }

    @Test
    // There is no way to put too big of a number to any of the input fields
    // The error we expect will never appear
    // it's not automatable because selenium can't handle it, it just has a bug
    public void anyInputFieldWithMoreThanABillionProducesAnErrorMessage() {
        mainPage.click5VatRadioBtn();

        mainPage.assertPriceWithoutVatCalculations("10000000000000000000000", "5000000.00", "105000000");
        mainPage.assertTooBigInputErrorMessage();
    }
}