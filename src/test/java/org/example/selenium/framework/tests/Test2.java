package org.example.selenium.framework.tests;

import org.example.selenium.framework.core.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.example.selenium.framework.assertions.HamcrestAssertions.assertThat;
import static org.hamcrest.Matchers.*;

public class Test2 {

    @Test
    public void test_1(WebDriver driver) {
//        assertThat("hello").isEqualTo("hellp");
        driver.get("https://opensource-demo.orangehrmlive.com/");
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Admin");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("admin123");
        driver.findElement(By.cssSelector("button")).click();
        WebElement el = driver.findElement(By.cssSelector("#app > div.oxd-layout.orangehrm-upgrade-layout > div.oxd-layout-navigation > header > div.oxd-topbar-header > div.oxd-topbar-header-title > span > h6"));
        // First assertion - should pass
        assertThat(el.getText(), equalTo("Dashboard"), "Dashboard header text");

        // Second assertion - should fail (intentionally wrong expectation)
        assertThat(el.getText(), equalTo("Dashboad"), "Dashboard header text (intentionally wrong)");

        // This line will not be reached due to the failing assertion above
        driver.findElement(By.cssSelector("jsafklas"));
    }

    @Test
    public void test_2(WebDriver driver) {
        driver.get("https://opensource-demo.orangehrmlive.com/");

        // Demonstrate various Hamcrest assertions

        // String assertions
        String pageTitle = driver.getTitle();
        assertThat(pageTitle, containsString("Orange"), "Page title contains 'Orange'");
        assertThat(pageTitle, not(containsString("Apple")), "Page title does not contain 'Apple'");

        // Boolean assertions
        boolean isLoginPageDisplayed = !driver.findElements(By.cssSelector("input[name='username']")).isEmpty();
        assertThat(isLoginPageDisplayed, is(true), "Login page is displayed");

        // Collection assertions
        java.util.List<WebElement> inputFields = driver.findElements(By.cssSelector("input"));
        assertThat(inputFields, hasSize(greaterThan(1)), "Page has multiple input fields");

        // Compound assertions
        assertThat(pageTitle, allOf(
            startsWith("Orange"),
            containsString("HRM"),
            not(containsString("Login"))
        ), "Page title matches complex pattern");

        // Element assertions
        WebElement usernameField = driver.findElement(By.cssSelector("input[name='username']"));
        assertThat(usernameField.isDisplayed(), is(true), "Username field is displayed");
        assertThat(usernameField.getAttribute("placeholder"), equalTo("Username"), "Username field has correct placeholder");
    }

    @Test
    public void test_3(WebDriver driver) {
        driver.get("https://www.google.com");

        // Simple assertions to demonstrate error reporting
        assertThat(driver.getTitle(), equalTo("Google"), "Google page title");

        // This assertion will fail intentionally to demonstrate error reporting
        assertThat(driver.getCurrentUrl(), equalTo("https://www.bing.com"), "Current URL should be Bing (will fail)");
    }

}
