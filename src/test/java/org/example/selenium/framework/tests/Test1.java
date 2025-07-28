package org.example.selenium.framework.tests;

import org.example.selenium.framework.core.Ignore;
import org.example.selenium.framework.core.SingleThreaded;
import org.example.selenium.framework.core.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class Test1 {

    @Test
    @Ignore
    public void test_1(WebDriver driver) throws InterruptedException {
        throw new RuntimeException();
    }

    @Test
    @SingleThreaded
    @Ignore
    public void test_2(WebDriver driver) {
        driver.get("https://opensource-demo.orangehrmlive.com/");
        driver.findElement(By.cssSelector("button")).click();
    }

    @Ignore
    @Test
    public void test_3(WebDriver driver) throws InterruptedException {
        driver.get("https://www.google.com");
    }

}
