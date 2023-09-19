package com.codecool.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebdriverManager {
    private static WebDriver driver;
    public static WebDriver getInstance() {
        //ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless=new");
        return driver == null ? driver = new ChromeDriver() : driver;
    }

    public static void quitDriver() {
        driver.quit();
        driver = null;
    }
}
