package com.Automation.Keywords;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface IseleniumCommandManager {

	WebElement presenceOfElementLocator(WebDriver driver, String xpathName);

	WebElement elementToBeClickableLocator(WebDriver driver, String xpathName);

	WebElement visiblityOfElementLocator(WebDriver driver, String xpathName);

	WebElement elementToBeClickableLocator(WebDriver driver, WebElement xpathName);

	WebElement visiblityOfElementLocator(WebDriver driver, WebElement xpathName);

	WebElement findElement(WebDriver driver, String xpathName);

	List<WebElement> findElements(WebDriver driver, String xpathName);

	List<WebElement> visiblityOfElementLocatorList(WebDriver driver, String xpathName);

	boolean inVisiblityOfElementLocator(WebDriver driver, WebElement xpathName);

}
