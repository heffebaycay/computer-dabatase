package fr.heffebaycay.cdb.controller;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import fr.heffebaycay.cdb.model.Computer;

public class UITestAddComputerController {

  WebDriver driver;
  
  @Before
  public void setUp() {
    driver = new FirefoxDriver();
  }

  @After
  public void tearDown() {
    driver.close();
  }

  @Test
  public void testAddLegitComputer() {

    String computerName = "SeleniumTestingComputer";
    String dateIntroduced = "1985-12-14";
    String dateDiscontinued = "2000-05-03";
    String companyId;

    driver.get("http://localhost:8080/computer-database/computers/add");

    // Fill in the computer name
    WebElement computerNameElement = driver.findElement(By.id("computerName"));
    computerNameElement.sendKeys(computerName);

    WebElement computerIntroducedElement = driver.findElement(By.id("introduced"));
    computerIntroducedElement.sendKeys(dateIntroduced);

    WebElement computerDiscontinuedElement = driver.findElement(By.id("discontinued"));
    computerDiscontinuedElement.sendKeys(dateDiscontinued);

    WebElement computerCompanyElement = driver.findElement(By.id("companyId"));
    List<WebElement> companies = computerCompanyElement.findElements(By.tagName("option"));

    // Generating a random int between 0 and companies.size() - 1
    Random rand = new Random();
    int randomIndex = rand.nextInt(companies.size());

    WebElement companyOption = companies.get(randomIndex);
    companyId = companyOption.getAttribute("value");
    // Selecting the company option
    companyOption.click();

    // Click on "Add"
    driver.findElement(By.id("submit")).click();

    List<WebElement> successElement = driver.findElements(By.id("msgComputerAdded"));

    assertEquals(1, successElement.size());

    // All Done!
  }
  
  
  @Test
  public void testAddEmptyComputer() {
    driver.get("http://localhost:8080/computer-database/computers/add");

    // Click on "Add"
    driver.findElement(By.id("submit")).click();

    List<WebElement> errorElement = driver.findElements(By.id("msgErrors"));

    assertEquals(1, errorElement.size());
  }

}
