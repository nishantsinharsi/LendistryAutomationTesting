package com.Automation.Keywords;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumFunctions implements IseleniumCommandManager {

	static final String XPATH_NOT_FOUNDE_EXCEPTION = " xpath is not present in web application: ";
	static final String XPATH_NOT_INTERACT_EXCEPTION = " xpath is correct but not interact with web application: ";

	public static String fontColorType(colorType color, String message) {
		return "<font color=" + color + ">" + message + "</font>";

	}

	/*
	 * Test data: MAGENTA writeText Action: LIME ERROR: RED Validation: YELLOW
	 * 
	 */
	public enum colorType {
		RED, YELLOW, ORANGE, MAGENTA, LIME

	}

	public void calculateTimeDiff() {
		Instant start = Instant.now();

		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds");
	}

	@Override
	public WebElement presenceOfElementLocator(WebDriver driver, String xpathName) {
		return new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathName)));

	}

	@Override
	public WebElement elementToBeClickableLocator(WebDriver driver, String xpathName) {
		return new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(By.xpath(xpathName)));

	}

	@Override
	public WebElement visiblityOfElementLocator(WebDriver driver, String xpathName) {
		return new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathName)));

	}

	@Override
	public WebElement elementToBeClickableLocator(WebDriver driver, WebElement xpathName) {
		return new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(xpathName));

	}

	@Override
	public WebElement visiblityOfElementLocator(WebDriver driver, WebElement xpathName) {
		return new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(xpathName));

	}

	@Override
	public WebElement findElement(WebDriver driver, String xpathName) {
		return driver.findElement(By.xpath(xpathName));
	}

	@Override
	public List<WebElement> findElements(WebDriver driver, String xpathName) {
		return driver.findElements(By.xpath(xpathName));
	}

	@Override
	public List<WebElement> visiblityOfElementLocatorList(WebDriver driver, String xpathName) {
		return new WebDriverWait(driver, 30)
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpathName)));
	}

	@Override
	public boolean inVisiblityOfElementLocator(WebDriver driver, WebElement xpathName) {
		return new WebDriverWait(driver, 30).until(ExpectedConditions.invisibilityOf(xpathName));
	}

}
