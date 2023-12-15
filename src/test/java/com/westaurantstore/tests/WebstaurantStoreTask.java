package com.westaurantstore.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WebstaurantStoreTask {

    @Test
    public void task(){

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://www.webstaurantstore.com/");

        WebElement searchBox = driver.findElement(By.xpath("//input[@type='text']"));

        searchBox.sendKeys("stainless work table"+ Keys.ENTER);

        List<String> tableTextList = new ArrayList<>();
        List<WebElement> addToCartButtons = new ArrayList<>();

        while(true){

            List<WebElement> tableResult = driver.findElements(By.xpath("//span[@class='block font-semibold text-sm-1/2 leading-none mt-4 mb-0 max-h-10 hover:max-h-full min-h-10 overflow-hidden hover:overflow-visible text-center']"));
            for (WebElement table : tableResult) {
                String tableText = table.getText();
                tableTextList.add(tableText.toLowerCase());
            }
            List<WebElement> allItems = driver.findElements(By.xpath("//input[@value='Add to Cart']"));
            addToCartButtons.addAll(allItems);

            try {
                WebElement nextPageButton = driver.findElement(By.xpath("//a[@class='border-gray-400 hover:border-green-800 border-solid border border-r-0 box-border flex items-center justify-center font-semibold h-7-1/2 text-sm leading-none p-0 fill-current hover:text-white w-7-1/2 hover:shadow-inner-green bg-gray-gradient hover:bg-green-gradient rounded-r-md text-gray-800 border-r pagerLink']"));

                if (nextPageButton.isDisplayed() && nextPageButton.isEnabled()) {
                    nextPageButton.click();
                } else {
                    break;
                }
            }catch (NoSuchElementException e){
                System.out.println("last Page");
                break;
            }

        }
       SoftAssert softAssert = new SoftAssert();
        for (String eachItem : tableTextList) {
            softAssert.assertTrue(eachItem.contains("table"));
        }

        try{
            WebElement lastItem = addToCartButtons.get(addToCartButtons.size()-1);
            lastItem.click();
        }catch (IndexOutOfBoundsException e){
            System.out.println("Nothing found");
        }

        WebElement cartButton = driver.findElement(By.xpath("//a[@data-testid='cart-button']"));

        WebElement popUp = driver.findElement(By.xpath("//button[@class='close p-2 mr-2 top-1']"));
        popUp.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        cartButton.click();

        WebElement deleteButton = driver.findElement(By.xpath("//button[@class='deleteCartItemButton itemDelete__link itemDelete--positioning']"));

        deleteButton.click();

        WebElement cartEmptyText = driver.findElement(By.xpath("//p[@class=\'header-1\']"));
        String expectedResult = "Your cart is empty.";
        String actualResult = cartEmptyText.getText();

        Assert.assertEquals(actualResult,expectedResult);

        softAssert.assertAll();

        driver.close();





    }
}
