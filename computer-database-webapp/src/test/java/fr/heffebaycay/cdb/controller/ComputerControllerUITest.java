package fr.heffebaycay.cdb.controller;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

@RunWith(MockitoJUnitRunner.class)
public class ComputerControllerUITest {

  WebDriver driver;
  
  @Before
  public void setUp() {
    driver = new FirefoxDriver();
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void test() {

    driver.get("http://localhost:8080/computer-database-webapp/");

    WebElement element = driver.findElement(By.id("homeTitle"));

    String homeTitle = element.getText();

    if (homeTitle == null || !homeTitle.matches("\\d+ Computers found")) {
      fail("Computer list page is broken");
    }

  }

}
