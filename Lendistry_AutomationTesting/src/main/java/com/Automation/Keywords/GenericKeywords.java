package com.Automation.Keywords;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.api.SoftAssertions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import com.Automation.reports.ExtentManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.framework.exception.FrameworkException;

import io.github.bonigarcia.wdm.WebDriverManager;

//@author Nishant.Sinha Nov 18, 2021

public class GenericKeywords extends SeleniumFunctions {

	public Properties envProp;
	public Properties prop;
	public String ObjectKey;
	public String dataKey;
	public String proceedOnFail;
	public Hashtable<String, String> data;
	public WebDriver driver;
	private static ThreadLocal<WebDriver> driverInstance = new ThreadLocal<>();
	private static ThreadLocal<WebElement> threadElement = new ThreadLocal<>();
	List<WebElement> elementList;

	public ExtentTest test;
	public SoftAssert softAssert = new SoftAssert();
	static List<String> filesListInDir = new ArrayList<>();
	public String Description;
	public String numeric;
	static ThreadLocal<String> copyText = new ThreadLocal<>();
	public int COUNTx = 0;
	public final int RETRY = 4;
	public boolean bool;
	public String popUpText;

	public static WebDriver getDriver() {
		return driverInstance.get();
	}

	public static WebElement getElement() {
		return threadElement.get();
	}

	public static void setDriver(WebDriver driverref) {
		driverInstance.set(driverref);
	}

	public static void unload() {
		driverInstance.remove();
	}

	public static void unloadElement() {
		threadElement.remove();
	}

	public void openBrowser() {
		try {
			if (driver == null) {
				System.out.println("Opening Browser" + " | " + data.get(dataKey));
				String browser = data.get(dataKey);

				test.log(Status.INFO, "Opening Browser " + " | " + browser);
				if (browser.equals("Mozilla")) {
					WebDriverManager.firefoxdriver().setup();
					FirefoxOptions options = new FirefoxOptions();
					options.setProfile(new FirefoxProfile());
					options.addPreference("dom.webnotifications.enabled", false);
					driver = new FirefoxDriver(options);
					
				} else if (browser.equals("Chrome")) {
					WebDriverManager.chromedriver().setup();
					ChromeOptions options = new ChromeOptions();
					// options.setHeadless(true);
					options.addArguments("--disable-notifications");
					options.addArguments("--allow-geolocation");
					options.addArguments("--disable-gpu");
					options.setExperimentalOption("useAutomationExtension", false);
					HashMap<String, Object> prefs = new HashMap<>();
					prefs.put("download.prompt_for_download", false);
					prefs.put("plugins.always_open_pdf_externally", true);
					options.setExperimentalOption("prefs", prefs);
					driver = new ChromeDriver(options);

				} else if (browser.equals("IE")) {
					WebDriverManager.iedriver().setup();
					driver = new InternetExplorerDriver();

				} else if (browser.equals("Edge")) {
					WebDriverManager.edgedriver().setup();
					driver = new EdgeDriver();
				} else if (browser.equals("Headless")) {
					WebDriverManager.chromiumdriver().setup();
					ChromeOptions chromeOptions = new ChromeOptions();
					chromeOptions.setHeadless(true);
					chromeOptions.addArguments("--disable-notifications");
					chromeOptions.addArguments("--disable-gpu");
					chromeOptions.addArguments("window-size=1980,1080");
					driver = new ChromeDriver(chromeOptions);

				}

			} else {
				navigate();
			}

			driverInstance.set(driver);
			new WebDriverWait(getDriver(), 60).until(webDriver -> ((JavascriptExecutor) webDriver)
					.executeScript("return document.readyState").equals("complete"));
			getDriver().manage().window().maximize();
			getDriver().manage().deleteAllCookies();
		} catch (Exception e) {
			reportFailure(Description, " Unable to Launch Browser ", e);

		}

	}

	/*****************************
	 * click method Keywords
	 *****************************/
	public synchronized WebElement retryingElement(String locator, String message) {
		WebElement returnElement = null;
		getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		elementList = findElements(getDriver(), (locator));
		while (elementList.isEmpty() && COUNTx < RETRY) {
			System.out.println("-> Retry Log: " + COUNTx + " | " + message);
			COUNTx++;
			getDriver().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			elementList = findElements(getDriver(), (locator));

		}

		if (!elementList.isEmpty()) {
			COUNTx = 0;
			System.out.println("INFO: " + message);
			returnElement = presenceOfElementLocator(getDriver(), (locator));

		} else {
			test.log(Status.FAIL,
					fontColorType(colorType.LIME, "ERROR: at retryingElement xpath not found in application"));
			takeScreenShot();
		}

		return returnElement;
	}

	public void click() {
		sleepThreadSec();
		Actions action = new Actions(getDriver());
		threadElement.set(retryingElement(prop.getProperty(ObjectKey), Description));
		try {
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({ block: 'center' });",
					getElement());
			visiblityOfElementLocator(getDriver(), getElement());
			action.moveToElement(getElement()).click().build().perform();
			test.log(Status.INFO, " " + Description);
			sleepThread1Sec();
		} catch (Exception e) {
			test.log(Status.WARNING, "ERROR: at click unable to perform click action");
			clickScript();

		}

	}

	public void clickScript() {
		sleepThreadSec();
		threadElement.set(retryingElement(prop.getProperty(ObjectKey), Description));
		try {
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({ block: 'center' });",
					getElement());
			visiblityOfElementLocator(getDriver(), getElement());
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", getElement());
			test.log(Status.INFO, "  " + Description);
			sleepThread1Sec();
		} catch (Exception e) {
			reportFailure(Description,
					" ERROR: at clickscript where" + XPATH_NOT_INTERACT_EXCEPTION + prop.getProperty(ObjectKey), e);
		}
	}

	public void doubleClick() {
		sleepThread1Sec();
		Actions action = new Actions(getDriver());
		WebElement element = retryingElement(prop.getProperty(ObjectKey), Description);

		try {
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({ block: 'center' });",
					element);
			action.moveToElement(element).doubleClick(element).click().build().perform();
			test.log(Status.INFO, "  " + Description);
		} catch (ElementClickInterceptedException e) {

			StackTraceElement[] a = e.getStackTrace();
			a[0] = new StackTraceElement("com.Automation.Keywords", "doubleClick", "GenericKeywords", 253);
			e.setStackTrace(a);
			throw new FrameworkException("Please check double click method: ", e);
		}
	}

//	***********  writeText method ***************

	public void writeText() {
		sleepThreadSec();
		Actions action = new Actions(getDriver());
		threadElement.set(retryingElement(prop.getProperty(ObjectKey), Description + " | " + data.get(dataKey)));
		try {
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({ block: 'center' });",
					getElement());
			visiblityOfElementLocator(getDriver(), getElement());
			getElement().clear();
			action.moveToElement(getElement()).doubleClick(getElement()).sendKeys(data.get(dataKey)).build().perform();
			copyText.set(data.get(dataKey));
			test.log(Status.INFO, Description + " | " + fontColorType(colorType.MAGENTA, data.get(dataKey)));
			sleepThread1Sec();
		} catch (Exception e) {
			System.out.println("ERROR: at writeText, render to writescript");
			writeScript();
		}

	}

	public void writeScript() {
		sleepThread1Sec();
		threadElement.set(retryingElement(prop.getProperty(ObjectKey), Description + " | " + data.get(dataKey)));
		try {

			((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({ block: 'center' });",
					getElement());
			visiblityOfElementLocator(getDriver(), getElement());
			getElement().clear();
			((JavascriptExecutor) driver).executeScript("arguments[0].value='" + data.get(dataKey) + "';",
					getElement());
			copyText.set(data.get(dataKey));
			test.log(Status.INFO, Description + " | " + fontColorType(colorType.MAGENTA, data.get(dataKey)));
			sleepThread1Sec();
		} catch (Exception e) {
			reportFailure(Description,
					" ERROR: at writescript where" + XPATH_NOT_INTERACT_EXCEPTION + prop.getProperty(ObjectKey), e);

		}

	}

	public void write() {
		sleepThread2Sec();
		try {
			threadElement.set(retryingElement(prop.getProperty(ObjectKey), Description + " | " + data.get(dataKey)));
			getElement().clear();
			getElement().sendKeys(data.get(dataKey));
			test.log(Status.INFO, Description + " | " + fontColorType(colorType.MAGENTA, data.get(dataKey)));
		} catch (Exception e) {
			reportFailure(Description,
					" ERROR: at writescript where" + XPATH_NOT_INTERACT_EXCEPTION + prop.getProperty(ObjectKey), e);
		}

	}

	public void selectFromDropDown() {
		sleepThread1Sec();
		threadElement.set(retryingElement(prop.getProperty(ObjectKey), Description + " | " + data.get(dataKey)));

		if ((getElement().isDisplayed()) || (getElement().isEnabled())) {
			List<WebElement> listPresence = findElements(getDriver(), prop.getProperty(ObjectKey));
			for (int i = 0; i < listPresence.size(); i++) {
				if (listPresence.get(i).getText().equalsIgnoreCase(data.get(dataKey))) {
					try {
						((JavascriptExecutor) getDriver()).executeScript(
								"arguments[0].scrollIntoView({ block: 'center' });", listPresence.get(i));
						takeScreenShot();
						listPresence.get(i).click();
						test.log(Status.INFO,
								Description + " | " + fontColorType(colorType.MAGENTA, data.get(dataKey)));
						System.out.println("INFO: " + Description + " | " + data.get(dataKey));
						break;
					} catch (Exception e) {

						reportFailure(Description, " at selectFromDropDown value is not selected: " + data.get(dataKey)
								+ " | " + prop.getProperty(ObjectKey));

					} finally {
						sleepThread1Sec();
					}

				}

			}

		} else {
			reportFailure(Description, "ERROR:  at selectFromDropDown" + XPATH_NOT_FOUNDE_EXCEPTION + data.get(dataKey)
					+ " | " + prop.getProperty(ObjectKey));

		}

	}

	public void enterDIR(String mode, String xpath) {
		if (mode != null) {
			String read = fileReader(mode.toUpperCase(), "dirLogs.txt");
			String[] lastemail = read.split(":");
			String dir = lastemail[1].strip();

			try {
				elementToBeClickableLocator(getDriver(), xpath).sendKeys(dir);
				test.log(Status.PASS, "Test id: " + mode + " | DIR id: " + fontColorType(colorType.YELLOW, dir));
				System.out.println("INFO-1: Enter Dir " + mode + " DIR id: " + dir);
				sleepThread1Sec();
			} catch (ElementNotInteractableException e) {
				System.out.println("ERROR: xpath not working for search DIR: " + mode + " | " + xpath);
				reportFailure("xpath not working! " + xpath + " | " + e);

			}

		} else {
			System.out.println("ERROR: mode not found!" + Description);
			reportFailure("mode not found! " + Description);
		}

	}

	public void waitForPageLoad(int timeOut) {

		long endTime = System.currentTimeMillis() + timeOut;
		while (System.currentTimeMillis() < endTime) {
			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			String state = js.executeScript("return document.readyState").toString();
			System.out.println("page loding status: " + state);
			if (state.equals("complete")) {
				System.out.println("page is fully loaded now....");
				break;
			}

		}

	}

	public void executeUnderWriter(String mode) {
		System.out.println("INFO: Opening UnderWriter URL");
		test.log(Status.INFO,
				"Opening UnderWriter URL: " + fontColorType(colorType.MAGENTA, envProp.getProperty("UnderWriter_URL")));
		getDriver().get(envProp.getProperty("UnderWriter_URL"));
		retryingElement(prop.getProperty("Lendistry_cameoEmail_xpath"),
				"Typing Email: " + prop.getProperty("UWUsername")).sendKeys(prop.getProperty("UWUsername"));
		test.log(Status.INFO,
				"  " + "Typing Email: " + fontColorType(colorType.MAGENTA, prop.getProperty("UWUsername")));
		retryingElement(prop.getProperty("Lendistry_cameoPassword_xpath"),
				"Typing Password: " + prop.getProperty("UWPassword")).sendKeys(prop.getProperty("UWPassword"));
		test.log(Status.INFO,
				"  " + "Typing Password: " + fontColorType(colorType.MAGENTA, prop.getProperty("UWPassword")));
		retryingElement(prop.getProperty("Lendistry_cameoSignIn_xpath"), "Click on Sign In button").click();
		test.log(Status.INFO, "  " + "Click on Sign In button");

		retryingElement(prop.getProperty("UW_Application_xpath"), "Click on All Application").click();
		sleepThread2Sec();
		retryingElement(prop.getProperty("UW_Application_xpath"), "Click on All Application").click();
		test.log(Status.INFO, "  " + "Click on All Application");
		enterDIR(mode, prop.getProperty("UW_searchApplication_xpath")); // Enter DIR ID
		retryingElement(prop.getProperty("UW_DIR_xpath"), "Click on DIR number").click();
		test.log(Status.INFO, "  " + "Click on DIR id");
	}

	public void loginToUnderWriter() {
		String mode = data.get(dataKey);
		executeUnderWriter(mode);
		waitForPageLoad(30);
		test.log(Status.INFO, "Waiting for page load!");
		bool = isTextPresent("//*[contains(text(),'OWNER DETAILS')]");

	}

	private boolean isTextPresent(String xpath) {
		List<WebElement> isPresent = findElements(getDriver(), xpath);
		while (isPresent.isEmpty() && COUNTx < RETRY) {
			System.out.println("-> Retry Log: " + COUNTx + " | Waiting for page load | " + Description);
			COUNTx++;
			getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			isPresent = findElements(getDriver(), xpath);
		}

		if (!isPresent.isEmpty()) {
			COUNTx = 0;
			try {
				String txt = findElement(getDriver(), xpath).getText();
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				bool = wait.until(ExpectedConditions.textToBePresentInElement(findElement(getDriver(), xpath), txt));
				test.log(Status.PASS, "Page loaded sucessfully! " + fontColorType(colorType.MAGENTA, txt));
			} catch (Exception e) {
				System.out.println("ERROR: isTextPresent-> xpath not working: " + " | " + e);
				reportFailure("isTextPresent: Xpath not working: " + " | " + e);
			}

		} else {
			System.out.println("WARNING: Unable to find text by isTextPresent method!");
		}
		return bool;

	}

	public void reload() {

		getDriver().navigate().refresh();
		getDriver().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		((JavascriptExecutor) getDriver())
				.executeScript("return (document.readyState === 'complete' || document.readyState === 'interactive')");
		System.out.println("INFO: Refreshing element . . . ");
	}

	public void scrollToBottom() {
		sleepThread1Sec();
		try {
			((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight)");
			System.out.println("INFO: SCROLL TO BOTTOM OF THE PAGE");
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Unexpected exception is thrown: Unable to scrolling to bottom of a page " + e);
			System.out.println("ERROR: PLEASE CHECK SCROLL TO BOTTOM METHOD" + e);
		}
	}

	public void scrollToEnd() {
		sleepThread1Sec();
		try {
			getDriver().findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
			System.out.println("INFO: SCROLL TO BOTTOM OF THE PAGE");
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Unexpected exception is thrown: Unable to scrolling to bottom of a page " + e);
			System.out.println("ERROR: PLEASE CHECK SCROLL TO BOTTOM METHOD" + e);
		}
	}

	public void scrollToElement() {
		try {

			WebElement element = visiblityOfElementLocator(getDriver(), prop.getProperty(ObjectKey));
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({ block: 'center' });",
					element);
			sleepThread1Sec();
			System.out.println("INFO-1: SCROLL TO ELEMENT: ");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: PLEASE CHECK SCROLL TO ELEMENT METHOD: " + Description);
			reportFailure("<font color=red>" + "Unexpected exception is thrown: " + "</font>" + e);

		}
	}

// esc method
	public void esc() {
		try {
			sleepThread1Sec();
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ESCAPE);
			robot.keyRelease(KeyEvent.VK_ESCAPE);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getCalender() {
		String date = data.get(dataKey);
		String[] calender = date.split("-");
		selectDate(calender[0], calender[1], calender[2]);
		test.log(Status.INFO, Description + " | " + "Date: " + calender[0] + " " + calender[1] + " " + calender[2]);

	}

	public void selectDate(String exDay, String exMonth, String exYear) {
		String monthVal = getDriver().findElement(By.xpath("(//*[@class='dl-datepicker-title']/span)[1]")).getText();
		String yearVal = getDriver().findElement(By.xpath("(//*[@class='dl-datepicker-title']/span)[2]")).getText();
		sleepThread2Sec();
		while (!(monthVal.equalsIgnoreCase(exMonth) && yearVal.equalsIgnoreCase(exYear))) {
			getDriver().findElement(By.xpath("//*[@title='To select next month']//span")).click();
			monthVal = getDriver().findElement(By.xpath("(//*[@class='dl-datepicker-title']/span)[1]")).getText();
			yearVal = getDriver().findElement(By.xpath("(//*[@class='dl-datepicker-title']/span)[2]")).getText();
			sleepThread1Sec();
		}
		getDriver().findElement(By.xpath("(//td[contains(.," + exDay + ")])[1]")).click();
		getDriver().findElement(By.xpath("//*[text()='done']")).click();
		sleepThread1Sec();

	}

	public List<WebElement> getElementList(String xpathElement) {// String xpathElement
		List<WebElement> element = null;
		try {
			element = findElements(getDriver(), xpathElement);
			for (int i = 0; i < element.size(); i++) {
				System.out.println(element.get(i).getText());
			}

		} catch (Exception e) {
			e.printStackTrace();
			reportLog("ERROR: PLEASE CHECK Get Element List METHOD: " + e);
		}
		return element;

	}

	public void ownerStateDropdown() {
		sleepThread1Sec();
		SoftAssertions soft = new SoftAssertions();

		try {
			List<String> jsonStateList = readJson("ownerState", "dropdownData.json");
			for (int i = 0; i < jsonStateList.size(); i++) {
				String stateName = jsonStateList.get(i);

				elementToBeClickableLocator(getDriver(), "//mat-select[@formcontrolname='owner_state']").click(); // state
				sleepThread2Sec();
				List<WebElement> stateList = getElementList(
						"//div[@class='mat-select-content ng-trigger ng-trigger-fadeInContent']//span");
				for (int j = i; j < stateList.size(); j++) {
					if (stateList.get(j).getText().equalsIgnoreCase(stateName)) {
						stateList.get(j).click();
						break;
					}
				}
				elementToBeClickableLocator(getDriver(), "//mat-select[@formcontrolname='owner_county']").click(); // county
				sleepThread2Sec();
				List<WebElement> countryList = getElementList(
						"//div[@class='mat-select-content ng-trigger ng-trigger-fadeInContent']//span");
				List<String> exceptedList = new ArrayList<>();
				for (int k = 0; k < countryList.size(); k++) {
					String expected = countryList.get(k).getText();
					exceptedList.add(expected);
				}
				esc();
				List<String> countryJsonList = readJson(stateName, "dropdownData.json");
				System.out.println("INFO: " + stateName + " ACTUAL SIZE: " + countryJsonList.size()
						+ " | EXPECTED SIZE: " + exceptedList.size());
				System.out.println("INFO: ACTUAL RESULT: " + countryJsonList);
				System.out.println("INFO: EXPECTED RESULT:" + exceptedList);
				System.out.println();
				soft.assertThat(countryJsonList).as(stateName).containsAll(exceptedList);
				test.log(Status.PASS, Description + " | " + stateName + " | " + "ACTUAL SIZE: " + countryJsonList.size()
						+ " | " + "EXPECTED SIZE: " + exceptedList.size());
			}
			soft.assertAll();
		} catch (SoftAssertionError softAssertionError) {
			List<String> errors = softAssertionError.getErrors();
			for (int i = 0; i < errors.size(); i++) {
				System.out.println("ERROR: PLEASE CHECK DROP DOWN METHOD: " + errors.get(i));
				reportLog(Description + " | " + "<font color=red> Unexpected exception is thrown: </font>"
						+ errors.get(i));
			}
		}
	}

	public void businessStateDropdown() {
		sleepThread1Sec();
		SoftAssertions soft = new SoftAssertions();
		try {
			List<String> jsonStateList = readJson("ownerState", "dropdownData.json");
			for (int i = 0; i < jsonStateList.size(); i++) {
				String stateName = jsonStateList.get(i);
				elementToBeClickableLocator(getDriver(), "//mat-select[@formcontrolname='business_state']").click(); // state
				sleepThread2Sec();
				List<WebElement> stateList = getElementList(
						"//div[@class='mat-select-content ng-trigger ng-trigger-fadeInContent']//span");
				for (int j = i; j < stateList.size(); j++) {
					if (stateList.get(j).getText().equalsIgnoreCase(stateName)) {
						stateList.get(j).click();
						break;
					}
				}
				elementToBeClickableLocator(getDriver(), "//mat-select[@formcontrolname='business_county']").click(); // county
				sleepThread2Sec();
				List<WebElement> countryList = getElementList(
						"//div[@class='mat-select-content ng-trigger ng-trigger-fadeInContent']//span");
				List<String> exceptedList = new ArrayList<>();
				for (int k = 0; k < countryList.size(); k++) {
					String expected = countryList.get(k).getText();
					exceptedList.add(expected);
				}
				esc();
				List<String> countryJsonList = readJson(stateName, "dropdownData.json");
				System.out.println("INFO: " + stateName + " ACTUAL SIZE: " + countryJsonList.size()
						+ " | EXPECTED SIZE: " + exceptedList.size());
				System.out.println("INFO: ACTUAL RESULT: " + countryJsonList);
				System.out.println("INFO: EXPECTED RESULT:" + exceptedList);
				System.out.println();
				soft.assertThat(countryJsonList).as(stateName).containsAll(exceptedList);
				test.log(Status.PASS, Description + " | " + stateName + " | " + "ACTUAL SIZE: " + countryJsonList.size()
						+ " | " + "EXPECTED SIZE: " + exceptedList.size());
			}
			soft.assertAll();
		} catch (SoftAssertionError softAssertionError) {
			List<String> errors = softAssertionError.getErrors();
			for (int i = 0; i < errors.size(); i++) {
				System.out.println("ERROR: PLEASE CHECK DROP DOWN METHOD: " + errors.get(i));
				reportLog(Description + " | " + "<font color=red> Unexpected exception is thrown: </font>"
						+ errors.get(i));
			}
		}
	}

	public void businessDoDropdown() {
		sleepThread1Sec();
		SoftAssertions soft = new SoftAssertions();
		try {
			List<String> jsonStateList = readJson("businessDo", "dropdownData.json");
			for (int i = 0; i < jsonStateList.size(); i++) {
				String stateName = jsonStateList.get(i);
				elementToBeClickableLocator(getDriver(), "//mat-select[@formcontrolname='what_business_do']").click(); // state
				sleepThread2Sec();
				List<WebElement> stateList = getElementList(
						"//div[@class='mat-select-content ng-trigger ng-trigger-fadeInContent']//span");
				for (int j = i; j < stateList.size(); j++) {
					if (stateList.get(j).getText().equalsIgnoreCase(stateName)) {
						stateList.get(j).click();
						break;
					}
				}
				elementToBeClickableLocator(getDriver(), "(//mat-select[@formcontrolname='business_type'])[2]").click(); // county
				sleepThread2Sec();
				List<WebElement> countryList = getElementList(
						"//div[@class='mat-select-content ng-trigger ng-trigger-fadeInContent']//span");
				List<String> exceptedList = new ArrayList<>();
				for (int k = 0; k < countryList.size(); k++) {
					String expected = countryList.get(k).getText();

					exceptedList.add(expected);
					System.out.println(expected);
				}
				esc();
				List<String> countryJsonList = readJson(stateName, "dropdownData.json");
				System.out.println("INFO: " + stateName + " ACTUAL SIZE: " + countryJsonList.size()
						+ " | EXPECTED SIZE: " + exceptedList.size());
				System.out.println("INFO: ACTUAL RESULT: " + countryJsonList);
				System.out.println("INFO: EXPECTED RESULT:" + exceptedList);
				System.out.println();
				soft.assertThat(countryJsonList).as(stateName).containsAll(exceptedList);
				test.log(Status.PASS, Description + " | " + stateName + " | " + "ACTUAL SIZE: " + countryJsonList.size()
						+ " | " + "EXPECTED SIZE: " + exceptedList.size());
			}
			soft.assertAll();
		} catch (SoftAssertionError softAssertionError) {
			List<String> errors = softAssertionError.getErrors();
			for (int i = 0; i < errors.size(); i++) {
				System.out.println("ERROR: PLEASE CHECK DROP DOWN METHOD: " + errors.get(i));
				reportLog(Description + " | " + "<font color=red> Unexpected exception is thrown: </font>"
						+ errors.get(i));
			}
		}
	}

	public void tellUsMoreDropdown() {
		sleepThread1Sec();
		SoftAssertions soft = new SoftAssertions();
		try {
			List<String> jsonStateList = readJson("businessDo", "dropdownData.json");
			for (int i = 0; i < jsonStateList.size(); i++) {
				String stateName = jsonStateList.get(i);
				elementToBeClickableLocator(getDriver(), "//mat-select[@formcontrolname='what_business_do']").click(); // state
				sleepThread1Sec();
				List<WebElement> stateList = getElementList(
						"//div[@class='mat-select-content ng-trigger ng-trigger-fadeInContent']//span");
				for (int j = i; j < stateList.size(); j++) {
					if (stateList.get(j).getText().equalsIgnoreCase(stateName)) {
						stateList.get(j).click();
						break;
					}
				}
				elementToBeClickableLocator(getDriver(), "(//mat-select[@formcontrolname='business_type'])[2]").click(); // county
				List<WebElement> countryList = getElementList(
						"//div[@class='mat-select-content ng-trigger ng-trigger-fadeInContent']//span");
				esc();
				for (int l = 0; l < countryList.size(); l++) {
					sleepThread1Sec();
					elementToBeClickableLocator(getDriver(), "(//mat-select[@formcontrolname='business_type'])[2]")
							.click(); // county
					sleepThread2Sec();
					countryList.get(l).click();

					String businessIs = countryList.get(l).getText();
					elementToBeClickableLocator(getDriver(), "//mat-select[@formcontrolname='tell_us_more']").click(); // county
					List<WebElement> tellUsMoreList = getElementList(
							"//div[@class='mat-select-content ng-trigger ng-trigger-fadeInContent']//span");
					System.out.println("\"" + businessIs + "\",");
					System.out.println();
					List<String> tellMoreList = new ArrayList<>();
					for (int k = 0; k < tellUsMoreList.size(); k++) {
						String text = tellUsMoreList.get(k).getText();
						System.out.println("\"" + text + "\",");
						tellMoreList.add(text);

					}
					sleepThread1Sec();
					esc();
					System.out.println();
					List<String> countryJsonList = readJson(businessIs, "dropdownData.json");
					soft.assertThat(countryJsonList).as(stateName + " >> " + businessIs).containsAll(tellMoreList);

					test.log(Status.PASS,
							Description + " | " + stateName + " >> " + businessIs + " | " + "ACTUAL SIZE: "
									+ countryJsonList.size() + " | " + "EXPECTED SIZE: " + tellMoreList.size());

				}

			}
			soft.assertAll();
		} catch (SoftAssertionError softAssertionError) {
			List<String> errors = softAssertionError.getErrors();
			for (int i = 0; i < errors.size(); i++) {
				System.out.println("ERROR: PLEASE CHECK DROP DOWN METHOD: " + errors.get(i));
				reportLog(Description + " | " + "<font color=red> Unexpected exception is thrown: </font>"
						+ errors.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportLog(Description + " | " + e);
		}

	}

	public void verifyDropDown() {
		sleepThread2Sec();
		SoftAssertions soft = new SoftAssertions();
		List<String> exceptedList = new ArrayList<>();
		try {

			List<WebElement> isPresent = visiblityOfElementLocatorList(getDriver(), prop.getProperty(ObjectKey));
			for (int i = 0; i < isPresent.size(); i++) {
				String expected = isPresent.get(i).getText();
				System.out.println(Description + " " + expected);
				exceptedList.add(expected);
			}
			esc();
			List<String> actual = readJson(data.get(dataKey), "dropdownData.json");
			soft.assertThat(actual).as(Description).containsOnlyOnceElementsOf(exceptedList);
			test.log(Status.PASS, Description + " | " + data.get(dataKey) + " | " + "Actual size: [" + actual.size()
					+ "] Expected size: [" + exceptedList.size() + "] ");
			System.out.println("Actual Size: " + actual.size() + " Actual result: " + actual + "\n" + "expected Size: "
					+ exceptedList.size() + " Excepted result: " + exceptedList);
			soft.assertAll();
		} catch (SoftAssertionError softAssertionError) {
			List<String> errors = softAssertionError.getErrors();
			reportLog(Description + " | " + "<font color=red> Unexpected exception is thrown: </font>" + errors);
			System.out.println("ERROR: PLEASE CHECK: " + Description + " | " + errors);

		}

	}

	public static List<String> readJson(String jsonKey, String filePath) {

		List<String> jsonList = new ArrayList<>();
		try {
			JSONParser jsonParser = new JSONParser();
			File file = new File(".\\Document\\" + filePath);
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(file, StandardCharsets.UTF_8));// "dropdownData.json"
			JSONArray jsonArray = (JSONArray) jsonObject.get(jsonKey);
			for (int i = 0; i < jsonArray.size(); i++) {
				String array = jsonArray.get(i).toString();
				jsonList.add(array);
			}

		} catch (Exception e) {
			System.out.println("INFO: PLEASE CHECK READ JSON METHOD: " + e);
			e.printStackTrace();

		}
		return jsonList;

	}

	public void selectLendistrySalesyMode() {

		try {
			WebElement textElement = visiblityOfElementLocator(getDriver(), "(//*[@class='slds-truncate'])[1]");
			String modeText = textElement.getText();
			if (modeText.equalsIgnoreCase("Lendistry Sales")) {
				System.out.println("INFO: ALEARY IN LENDISTRY SLAES!");

			} else {
				elementToBeClickableLocator(getDriver(), "//*[@class='slds-icon-waffle']").click();
				sleepThread3Sec();
				elementToBeClickableLocator(getDriver(), "//*[@placeholder='Search apps and items...']")
						.sendKeys("Lendistry Sales");
				enterKey();
				sleepThread5Sec();
				esc();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Write data to file
	public void fileWriter(String text, String fileName) {
		SimpleDateFormat timestamp = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
		Date dates = new Date();
		String date = timestamp.format(dates);

		File file = new File(System.getProperty("user.dir") + "/LendistryLog/" + fileName);
		try (FileWriter fw = new FileWriter(file, true); BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write(date + " " + text);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @author Nishant.Sinha - Nov 18, 2021
	 */
	public String fileReader(String text, String filename) {

		String readLine = null;
		String line = null;

		try {

			File file = new File(System.getProperty("user.dir") + "/LendistryLog/" + filename);
			LineIterator lineIterator = FileUtils.lineIterator(file, "UTF-8");

			while (lineIterator.hasNext()) {
				readLine = lineIterator.nextLine();
				if (readLine.contains(" " + text + " ")) {
					line = readLine;

				}
			}
			if (line == null) {
				System.out.println("Error: at fileReader email not found. Please check " + filename);
				reportFailure(
						"<font color=red> Error: at fileReader email not found. Please check " + filename + " </font>");
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return line;

	}

	public synchronized void generateEmail() {
		String mode = data.get(dataKey);
		long time = System.nanoTime();
		String nanoSec = String.valueOf(time).substring(8, 12);

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyymm" + nanoSec);
			Date dates = new Date();
			String date = dateFormat.format(dates);
			copyText.set("test" + date + "@yopmail.com");

			WebElement ele = elementToBeClickableLocator(getDriver(), prop.getProperty(ObjectKey));
			ele.clear();
			ele.sendKeys(copyText.get());
			fileWriter(mode.toUpperCase() + " - " + "email: " + copyText.get(), "emaillog.txt");
			test.log(Status.INFO, Description + " | " + fontColorType(colorType.MAGENTA, copyText.get()));
			System.out.println("INFO: RANDOM EMAIL IS: " + copyText.get());
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Unexpected exception is thrown: " + e);
			System.out.println("ERROR: PLEASE CHECK GENERATE EMAIL METHOD: " + e);
		}

	}

	public synchronized void readEmailFromTextFile() {
		sleepThread1Sec();
		String mode = data.get(dataKey);
		Actions action = new Actions(getDriver());

		if (mode == null) {
			reportFailure(Description + " | " + "<font color=red> Email mode Not found!: </font>" + mode);
			throw new NullPointerException("Error: Email mode Not found! " + mode);
		}
		try {
			String read = fileReader(mode.toUpperCase(), "emaillog.txt");
			String[] lastemail = read.split(":");
			String email = lastemail[1].strip();
			WebElement ele = elementToBeClickableLocator(getDriver(), prop.getProperty(ObjectKey));
			action.moveToElement(ele).doubleClick(ele).sendKeys(email).build().perform();
			System.out.println("INFO: Email log for: " + mode + " : " + email);
			test.log(Status.INFO,
					Description + fontColorType(colorType.MAGENTA, " | Test id: " + mode + " : " + email));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: PLEASE CHECK EMAIL SEARCH METHOD: ");
			reportLog(Description + " | " + "<font color=red> Unexpected exception is thrown: </font>" + e);
		}

	}

	public synchronized void copyDIR() {
		try {
			String mode = data.get(dataKey);

			if (mode != null) {

				String outputText = visiblityOfElementLocator(getDriver(), prop.getProperty(ObjectKey)).getText();
				String read = fileReader(mode.toUpperCase(), "emaillog.txt");
				String[] lastemail = read.split(":");
				String email = lastemail[1].strip();
				fileWriter(mode.toUpperCase() + " " + email + ":" + outputText, "dirLogs.txt");
				test.log(Status.INFO, Description + " | " + fontColorType(colorType.MAGENTA, outputText));
				System.out.println("INFO: COPY METHOD: " + outputText);
			} else {

				System.out.println("Error: DIR module not found!");
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			reportFailure("Unexpected exception is thrown: " + e);
			System.out.println("ERROR: PLEASE CHECK COPY METHOD: " + e.getMessage());

		}

	}

	public String closeBrowser() {
		test.log(Status.INFO, "Closing the browser");
		try {
			getDriver().close();
			getDriver().quit();
			unload();

			softAssert.assertAll();
			System.out.println("INFO: CLOSING THE BROWSER!");
			System.out.println();

		} catch (Exception e) {
			reportFailure("Unable to closing the browser");
			return "FAIL <br>" + " Unable to close browser" + e.getMessage().substring(0, 40) + "</br>";
		}
		return "PASS <br> Browser closed successfully" + "</br>";
	}

	public void maximizeScreen() {
		test.log(Status.INFO, Description);
		getDriver().manage().window().maximize();
	}

	public void navigate() {
		try {
			test.log(Status.INFO, Description + " " + " | " + envProp.getProperty(ObjectKey));
			getDriver().get(envProp.getProperty(ObjectKey));
			System.out.println("Navigate to: " + envProp.getProperty(ObjectKey));
		} catch (Exception e) {
			reportFailure("Unable to navigate to Website " + envProp.getProperty(ObjectKey));
		}

	}

	public void dismissAlert() {

		try {

			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

		} catch (Exception e) {

			Actions browser = new Actions(getDriver());
			browser.sendKeys(Keys.TAB).perform();
			browser.sendKeys(Keys.ENTER).perform();

		}

	}

	public void tab() {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);

		} catch (Exception e) {
			Actions builder = new Actions(getDriver());
			builder.sendKeys(Keys.TAB).perform();

		}

	}

	public synchronized void copyEmailPassword() {
		String mode = data.get(dataKey);

		try {
			WebElement element = visiblityOfElementLocator(getDriver(), prop.getProperty(ObjectKey));
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({ block: 'center' });",
					element);
			String sourceText = element.getText();
			String[] source = sourceText.split(": ");
			String username = source[1].substring(0, 28);
			String password = source[2];
			fileWriter(mode.toUpperCase() + " - " + "email: " + username + " - password: " + password,
					"yopMailLog.txt");

			System.out.println("INFO: COPY EMAIL: " + username + " | " + "INFO: COPY PASSWORD: " + password);

		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Unexpected exception is thrown: " + e);

		}

	}

	public synchronized void loginToPortal() {
		String mode = data.get(dataKey);
		String pwd = "Nishant@123#";
		try {
			String read = fileReader(mode.toUpperCase(), "emaillog.txt");
			String getPwd = fileReader(mode.toUpperCase(), "yopMailLog.txt");
			String[] lastemail = read.split(":");
			String email = lastemail[1].strip();
			String[] splitPwd = getPwd.split(":", 3);
			String password = splitPwd[2].strip();

			presenceOfElementLocator(getDriver(), "//*[@formcontrolname='username' or@ id='inputEmail']")
					.sendKeys(email);
			sleepThread1Sec();
			presenceOfElementLocator(getDriver(), "//*[@id='inputPassword']").sendKeys(password);
			presenceOfElementLocator(getDriver(), "//*[@id='login']").click();

			System.out.println("INFO: Email log for: " + mode + " : " + email);
			test.log(Status.INFO, Description + fontColorType(colorType.MAGENTA, " Test id: " + mode + " : " + email));

			System.out.println("INFO: Password log for: " + mode + " : " + password);
			test.log(Status.INFO,
					Description + fontColorType(colorType.MAGENTA, " Test id: " + mode + " : " + password));

			sleepThread1Sec();
			List<WebElement> isPresent = findElements(getDriver(), "//*[text()='User name or Password is wrong!']");

			if (!isPresent.isEmpty()) {
				presenceOfElementLocator(getDriver(), "//*[@id='inputPassword']").clear();
				presenceOfElementLocator(getDriver(), "//*[@id='inputPassword']").sendKeys(pwd);
				presenceOfElementLocator(getDriver(), "//*[@id='login']").click();
				System.out.println("INFO: Retry with default password: " + pwd);
				test.log(Status.INFO, "Retry with default password: " + fontColorType(colorType.MAGENTA, pwd));

			} else {

				presenceOfElementLocator(getDriver(), "//*[@formcontrolname='resetpassword']").sendKeys(pwd);
				presenceOfElementLocator(getDriver(), "//*[@formcontrolname='resetconformpassword']").sendKeys(pwd);
				presenceOfElementLocator(getDriver(), "//*[@id='login']").click();
				System.out.println("INFO: Set default password: " + pwd);
				test.log(Status.INFO, "Set default password: " + fontColorType(colorType.MAGENTA, pwd));

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: PLEASE CHECK Login To Portal METHOD: " + e.getMessage());
			reportFailure(Description + " - Unexpected exception is thrown: " + e);

		}

	}

	// Copy Method | Past Method

	public void copy() {
		try {
			String outputText = visiblityOfElementLocator(getDriver(), prop.getProperty(ObjectKey)).getText();
			copyText.set(outputText);
			test.log(Status.INFO, Description + " | " + fontColorType(colorType.MAGENTA, copyText.get()));
			System.out.println("INFO: COPY METHOD: " + copyText.get());
		} catch (NullPointerException e) {
			e.printStackTrace();
			reportFailure("Unexpected exception is thrown: " + e);
			System.out.println("ERROR: PLEASE CHECK COPY METHOD: " + e.getMessage());

		}

	}

	public void scrollToUp() {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("window.scrollBy(0,-350)", "");
	}

	public synchronized void past() {

		try {
			if (copyText.get() != null) {

				WebElement ele = elementToBeClickableLocator(getDriver(), prop.getProperty(ObjectKey));
				ele.clear();
				ele.sendKeys(copyText.get());
				System.out.println("INFO-1: past: " + copyText.get());
				test.log(Status.INFO, Description + " | " + fontColorType(colorType.MAGENTA, copyText.get()));
			} else {
				System.out.println("INFO: Past method text is empty...!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Unexpected exception is thrown: " + e);
			System.out.println("ERROR: PLEASE CHECK PAST METHOD: " + e);
		}
	}

	public void randomNum() {
		try {
			int str = (int) Double.parseDouble(data.get(dataKey));
			numeric = RandomStringUtils.randomNumeric(str);
			WebElement ele = elementToBeClickableLocator(getDriver(), prop.getProperty(ObjectKey));
			ele.clear();
			ele.sendKeys(numeric);
			test.log(Status.INFO, Description + fontColorType(colorType.MAGENTA, " | " + numeric));
			System.out.println("INFO-1: RANDOM NUM IS: " + numeric);

		} catch (Exception e) {
			Actions builder = new Actions(getDriver());
			try {
				int str = (int) Double.parseDouble(data.get(dataKey));
				numeric = RandomStringUtils.randomNumeric(str);

				WebElement ele = elementToBeClickableLocator(getDriver(), prop.getProperty(ObjectKey));
				ele.clear();
				builder.moveToElement(ele).doubleClick(ele).sendKeys(data.get(dataKey)).build().perform();
				test.log(Status.INFO, Description + fontColorType(colorType.MAGENTA, " | " + numeric));
				System.out.println("INFO-2: RANDOM NUM IS: " + numeric);

			} catch (Exception e2) {
				e2.printStackTrace();
				reportFailure("Unexpected exception is thrown: randomNum: " + e2);
				System.out.println("ERRO: PLEASE CHECK RANDOM NUM METHOD: " + e2.getMessage());

			}

		}

	}

	public void getNumber() {

		try {
			test.log(Status.INFO, Description + fontColorType(colorType.MAGENTA, " | " + numeric));
			System.out.println("INFO: GET NUMBER IS: " + numeric);
			WebElement ele = getDriver().findElement(By.xpath(prop.getProperty(ObjectKey)));
			ele.clear();
			ele.sendKeys(numeric);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: PLEASE CHECK GET NUMBER METHOD:" + e);
			reportFailure("Unexpected exception is thrown: " + e);
		}
	}

	public void scrollDown() {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("scroll(0," + data.get(dataKey) + ")");

	}

	/**
	 * Upload file with element
	 */
	public void uploadFile() {

		try {
			sleepThread2Sec();
			if (data.get(dataKey) != null) {
				String filePath = System.getProperty("user.dir") + "/Document/" + data.get(dataKey);
				presenceOfElementLocator(getDriver(), prop.getProperty(ObjectKey));
				WebElement element = findElement(getDriver(), prop.getProperty(ObjectKey));
				element.sendKeys(filePath);
				test.log(Status.INFO, Description + fontColorType(colorType.LIME, " | " + filePath));
				System.out.println("INFO: Select File: " + filePath);

			} else {
				System.out.println("INFO:FILE NOT FOUND...");
				reportFailure(Description + " | " + "<font color=red>" + "FILE NOT FOUND" + "</font>");
			}

		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(Description + " | " + "<font color=red>" + "Unexpected exception is thrown: " + "</font>"
					+ prop.getProperty(ObjectKey) + " " + e);
		}
	}

	public void assertCheckBox() {
		try {
			System.out.println("check box");
			boolean enabled = getDriver().findElement(By.cssSelector(data.get(dataKey))).isSelected();
			System.out.println(enabled);

		} catch (ElementNotSelectableException e) {
			e.printStackTrace();
		}

	}

	// *********** Switch Tab method**************************************

	public void alertHandle() {
		try {
			Optional.ofNullable(getDriver().switchTo().alert()).ifPresent(Alert::dismiss);
			System.out.println("INFO: ALERT DISMISS");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("INFO: ALERT NOT PRESENT");

		}

	}

	public void newTab() {
		try {
			((JavascriptExecutor) getDriver()).executeScript("window.open()");
			ArrayList<String> tabs = new ArrayList<>(getDriver().getWindowHandles());
			getDriver().switchTo().window(tabs.get(1));
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Unable to open new tab  " + e);
		}
	}

	public void switchTabFirst() {
		try {
			ArrayList<String> tabs = new ArrayList<>(getDriver().getWindowHandles());
			getDriver().switchTo().window(tabs.get(0));
			System.out.println("INFO: SWITCH TAB FIRST! " + Description);
			test.log(Status.INFO, fontColorType(colorType.LIME, Description));
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Unable to switch first tab  " + e);
		} finally {
			System.out.println("INFO: at switchTabFirst executing finally block!");
			sleepThread1Sec();

		}
	}

	public void switchTabSecond() {

		try {
			ArrayList<String> tabs = new ArrayList<>(getDriver().getWindowHandles());
			getDriver().switchTo().window(tabs.get(1));
			test.log(Status.INFO, fontColorType(colorType.LIME, Description));
			System.out.println(Description);
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Unable to switch second tab  " + e);
		} finally {
			System.out.println("INFO: at switchTabSecond executing finally block!");
			sleepThreadSec();

		}

	}

	public void enterKey() {

		try {
			if (prop.getProperty(ObjectKey) == null) {
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				System.out.println("INFO-1: PRESING ENTER KEY");
			} else {

				WebElement ele = elementToBeClickableLocator(getDriver(), prop.getProperty(ObjectKey));
				System.out.println("INFO-2: PRESING  ENTER KEY");
				ele.sendKeys(Keys.ENTER);
			}

		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Unable to press enter: " + e);
		}

	}

	public void checkBox() {

		boolean checkbox = getDriver().findElement(By.tagName("Plaid_Integration_Complete__c")).isSelected();
		System.out.println(checkbox);
	}

	public void switchToFrameByXpath() {
		retryingElement(prop.getProperty(ObjectKey), Description);
		sleepThread2Sec();
		try {
			WebElement element = presenceOfElementLocator(getDriver(), prop.getProperty(ObjectKey));
			getDriver().switchTo().frame(element);

			test.log(Status.INFO, Description);
			System.out.println("INFO: switch to iframe");

		} catch (Exception e) {
			reportFailure(Description, "ERROR: at switchToFrameByXpath where switch to ifram fail!", e);
			e.printStackTrace();

		}
	}

	public void switchToIframe() {

		try {
			getDriver().switchTo().frame(prop.getProperty(ObjectKey));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: PLEASE CHECK SWITCH TO FRAME METHOD: " + e);
			reportFailure("Unexpected exception is thrown: " + e);
		}

	}

	public void defaultframe() {

		try {
			getDriver().switchTo().defaultContent();

		} catch (Exception e) {
			reportFailure("Unexpected exception is thrown: " + e);
			e.printStackTrace();
		}

	}

	public synchronized void disapearLoder() {
		int count = 0;
		List<WebElement> spin = findElements(getDriver(), prop.getProperty(ObjectKey));
		if (!spin.isEmpty()) {
			while (!spin.isEmpty() && count < 30) {
				sleepThread4Sec();
				spin = findElements(getDriver(), prop.getProperty(ObjectKey));
				System.out.println("-> Loading: Waiting for disapear loader . . . " + count);
				count++;
			}
			System.out.println("INFO: Loading sucessfully!");

		} else {

			sleepThread3Sec();
			System.out.println("INFO: at disapearLoder loading icon not found!");

		}

	}

	// >> getText method <<

	public void attributeLink() {

		retryingElement(prop.getProperty(ObjectKey), Description);

		try {
			WebElement element = findElement(getDriver(), prop.getProperty(ObjectKey));
			if (element.getAttribute("src") != null) {
				copyText.set(element.getAttribute("src"));
				takeScreenShot();
				getDriver().get(copyText.get());
				test.log(Status.INFO, Description
						+ fontColorType(colorType.ORANGE, " | Consumer credit report download sucessfully"));

			} else if (element.getAttribute("href") != null) {

				copyText.set(element.getAttribute("href"));
				takeScreenShot();
				getDriver().get(copyText.get());
				test.log(Status.INFO,
						Description + fontColorType(colorType.ORANGE, " | URL open sucessfully -> " + copyText.get()));
				System.out.println("INFO: URL open sucessfully | " + Description + copyText.get());

			}
		} catch (Exception e) {
			reportFailure(Description,
					" ERROR: at attributeLink where" + XPATH_NOT_INTERACT_EXCEPTION + prop.getProperty(ObjectKey), e);

		}

	}

	public void getTextMethod() {

		try {
			WebElement element = visiblityOfElementLocator(getDriver(), prop.getProperty(ObjectKey));

			if (element.getAttribute("value") != null) {

				copyText.set(element.getAttribute("value"));
				test.log(Status.INFO, Description + " | " + fontColorType(colorType.MAGENTA, copyText.get()));
				System.out.println("INFO: GET ATTRIBUTE FROM ELEMENT: " + copyText.get());
			} else if (getObject(ObjectKey).getText() != null) {
				copyText.set(element.getText());
				test.log(Status.INFO, Description + " | " + fontColorType(colorType.MAGENTA, copyText.get()));
				System.out.println("INFO: GET TEXT FROM ELEMENT: " + copyText.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportLog(Description + " | " + "<font color=red> Unexpected exception is thrown: </font>" + e);
		}

	}

	public void assertEqual() {

		sleepThread1Sec();
		SoftAssertions soft = new SoftAssertions();
		String getTextElement = null;
		String getData = data.get(dataKey);

		if (getData == null) {
			System.out.println("ERROR: Data sheet not map with keyword sheet! " + getData + " | " + Description);
			reportFailure(Description, "ERROR: at assertEqual where data sheet not map with keyword sheet!");
		} else {
			getData = getData.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
		}

		threadElement.set(retryingElement(prop.getProperty(ObjectKey), Description));
		try {
			getTextElement = getElement().getText();
			getTextElement = getTextElement.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
			soft.assertThat(getTextElement).as(Description).isEqualTo(getData);
			test.log(Status.PASS, Description + fontColorType(colorType.YELLOW,
					" | Actual Result: " + data.get(dataKey) + " | Expected Result: " + getTextElement));
			soft.assertAll();
		} catch (SoftAssertionError softAssertionError) {
			List<String> errors = softAssertionError.getErrors().stream().map(x -> x.replaceAll("[\\[|\\]]", ""))
					.collect(Collectors.toList());
			System.out.println("ERROR: PLEASE CHECK: " + " | " + errors);
			reportLog(Description + fontColorType(colorType.RED,
					" | ERROR: at assertEqual where expected result not matched : " + errors));
		} catch (Exception e) {
			reportLog(Description + fontColorType(colorType.RED,
					" | ERROR: at assertEqual where acctual result: expected result is null: " + e));

		}
	}

	public void verifyText() {
		SoftAssertions soft = new SoftAssertions();
		String getData = data.get(dataKey);

		try {
			String text = new WebDriverWait(getDriver(), 30)
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty(ObjectKey))))
					.getAttribute("value").toString();
			soft.assertThat(text).as(Description).isEqualTo(getData);

			test.log(Status.PASS, Description + fontColorType(colorType.YELLOW,
					" | Actual Result: " + data.get(dataKey) + " | Expected Result: " + text));

			System.out.println("INFO: ASSERT ACTUAL RESULT: " + data.get(dataKey) + " EXPECTED RESULT: " + text + " | "
					+ Description);
			soft.assertAll();

		} catch (SoftAssertionError softAssertionError) {
			List<String> errors = softAssertionError.getErrors().stream().map(x -> x.replaceAll("[\\[|\\]]", ""))
					.collect(Collectors.toList());
			System.out.println("ERROR: PLEASE ASSERT EQUAL NUM METHOD: " + errors);
			reportLog(Description + fontColorType(colorType.RED,
					" | ERROR: at verifyText where expected result is not matched: " + errors));

		}

	}

	public void assertEqualNum() {
		sleepThread1Sec();
		SoftAssertions soft = new SoftAssertions();
		int getData = Integer.parseInt(data.get(dataKey).replaceAll("[^0-9]", ""));
		String actual = String.valueOf(getData);
		threadElement.set(retryingElement(prop.getProperty(ObjectKey), Description));
		try {
			if (getElement().getAttribute("value") != null) {
				String getTextElement = getElement().getAttribute("value").replaceAll("[%\\$,]", "");
				float flotVal = Float.parseFloat(getTextElement);
				String expected = String.valueOf((int) flotVal).replaceAll("[^0-9]", "");
				soft.assertThat(expected).as(Description).isEqualTo(actual);
				test.log(Status.PASS, Description + fontColorType(colorType.YELLOW,
						" | Actual Result: " + actual + " || Expected Result: " + expected));
			} else if (getElement().getText() != null) {
				String getTextElement = getElement().getText().replaceAll("[%\\$,]", "");
				float flotVal = Float.parseFloat(getTextElement);
				String expected = String.valueOf((int) flotVal).replaceAll("[^0-9]", "");
				soft.assertThat(expected).as(Description).isEqualTo(actual);
				test.log(Status.PASS, Description + fontColorType(colorType.YELLOW,
						" | Actual Result: " + actual + " || Expected Result: " + expected));
			}
			soft.assertAll();

		} catch (SoftAssertionError softAssertionError) {
			List<String> errors = softAssertionError.getErrors().stream().map(x -> x.replaceAll("[\\[|\\]]", ""))
					.collect(Collectors.toList());
			reportLog(Description + fontColorType(colorType.RED,
					" | ERROR: at assertEqual where expected result not matched : " + errors));

		} catch (Exception e) {
			reportLog(Description
					+ fontColorType(colorType.RED, " | ERROR: at assertEqual where expected result is null: " + e));
		}
	}

	public void getAllLink() {

		Stream<String> list = getDriver().findElements(By.xpath("//a")).stream().map(WebElement::getText);
		list.forEach(System.out::println);

	}

	public void colorValidate() {
		sleepThread2Sec();
		try {
			String outputColor = visiblityOfElementLocator(getDriver(), prop.getProperty(ObjectKey))
					.getCssValue("color");
			String hex = Color.fromString(outputColor).asHex().replaceAll("\\s+", "");
			System.out.println("INFO: COLOR IS: " + hex);
			String color = (data.get(dataKey)); // "#449d44"

			if (hex.equals(color)) { // #673ab7

				System.out.println("INFO: COLOR MATCHED(GREEN): " + color + " | " + hex + " | " + Description);

				test.log(Status.PASS, Description
						+ fontColorType(colorType.YELLOW, " | Color Matched(GREEN): " + color + " | " + hex));

			} else {

				System.out.println(
						"INFO: COLOR NOT MATCHED- EXPECTED: " + color + " | ACTUAL: " + hex + " | " + Description);
				reportLog(Description + " | " + "<font color=red> " + "Color Not Matched: " + color + " | " + hex
						+ "</font>");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: PLEASE CHECK COLOR VALIDATE METHOD: " + e);
			reportFailure(Description + " | " + e);
		}

	}

	public void colorValidateRED() {

		try {
			String outputColor = visiblityOfElementLocator(getDriver(), prop.getProperty(ObjectKey))
					.getCssValue("caret-color");

			String hex = Color.fromString(outputColor).asHex().replaceAll("\\s+", "");
			System.out.println("INFO: COLOR IS: " + hex);
			String colorRED = "#f44336";

			if (hex.equals(colorRED)) { // #673ab7

				System.out.println("INFO: COLOR MATCHED(RED): " + colorRED + " | " + hex + " | " + Description);

				test.log(Status.PASS, Description + fontColorType(colorType.YELLOW,
						" | Color Matched(RED): <font color=yellow>" + colorRED + " | " + hex));

			} else {

				System.out.println(
						"INFO: COLOR NOT MATCHED- EXPECTED: " + colorRED + " | ACTUAL: " + hex + " | " + Description);
				reportLog(Description + " | " + "Color Not Matched: <font color=red>" + colorRED + " | " + hex);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: PLEASE CHECK COLOR VALIDATE METHOD: " + e);
			reportFailure(Description + " | " + e);
		}

	}

	public void numericFieldChecker() {

		WebElement element = presenceOfElementLocator(getDriver(), prop.getProperty(ObjectKey));
		String getTexts = element.getAttribute("value");
		int size = Integer.parseInt(element.getAttribute("maxlength"));
		try {
			if (getTexts.isEmpty()) {

				element.sendKeys(data.get(dataKey));

				test.log(Status.PASS,
						Description
								+ fontColorType(colorType.YELLOW, " | Max field size is: " + size + " Input size is: "
										+ data.get(dataKey).length() + " | " + "Input value is: " + data.get(dataKey)));

				System.out.println("INFO: Max field size is: " + size + " Input size is: " + data.get(dataKey).length()
						+ " | " + "Input value is: " + data.get(dataKey));

			} else {
				System.out.println("INFO: Field is not empty! : " + getTexts + " | text size: " + size);
				reportLog(Description + " | field is not empty! " + getTexts);
			}

		} catch (Exception e) {
			System.out.println("ERROR: PLEASE CHECK CHECK EMPLY METHOD: " + e);
			reportFailure(Description + " | " + "ERROR: PLEASE CHECK " + "<font color=red> checkEmpty </font>"
					+ "METHOD FOR: " + e);
			e.printStackTrace();
		}

	}

	public void messageValidator() {
		sleepThread1Sec();

		threadElement.set(retryingElement(prop.getProperty(ObjectKey), Description));
		if (getElement().isDisplayed()) {
			try {
				visiblityOfElementLocator(getDriver(), getElement());
				popUpText = getElement().getText().trim();
				List<String> msgList = readJson("message", "validationMessage.json");
				String validateMsg = msgList.stream().filter(e -> e.equals(popUpText)).collect(Collectors.joining());
				System.out.println("INFO: " + Description + " | " + validateMsg);
				test.log(Status.PASS, Description + " | " + fontColorType(colorType.YELLOW, validateMsg));

			} catch (Exception e) {
				System.out.println("ERROR: " + Description + " | " + e);
				reportLog(Description + fontColorType(colorType.RED,
						"ERROR: messageValidator xpath not working:" + prop.getProperty(ObjectKey) + " ---> " + e));
			}

		} else {
			if (prop.getProperty(ObjectKey).equals(prop.getProperty("Lendistry_sucessRostMessage_xpath"))) {
				System.out.println("ERROR: " + Description + " | Validation message not found!");

				test.log(Status.PASS,
						Description + fontColorType(colorType.ORANGE, " | Validation message not found!"));
			} else {
				System.out.println("ERROR: " + Description + " | Validation message not found!");
				test.log(Status.FAIL, Description + fontColorType(colorType.RED, " Validation message not found!"));
			}

		}
	}

	private enum Mode {
		LENGTH, EMAIL, PHONE, NUMBER;
	}

	public void checkError() {

		SoftAssertions soft = new SoftAssertions();
		String command = data.get(dataKey);

		if (command == null) {
			System.out.println(Description + " | ERROR: Please check command not found!: " + command);
			reportFailure(Description + "<font color=red> | ERROR: Please check command not found!  </font>");

		} else {

			command = command.toUpperCase();

		}

		String mes1 = "Data has been saved successfully. Please fill the below form to complete the application.";
		String mes2 = "Owner Details has been saved successfully.";

		if (popUpText != null && (popUpText.equals(mes1) || popUpText.equals(mes2))) {

			Mode mode = Mode.valueOf(command);

			try {
				String getTexts = visiblityOfElementLocator(getDriver(), prop.getProperty(ObjectKey))
						.getAttribute("value");

				int length = getTexts.length();
				int size = Integer.parseInt(getObject(ObjectKey).getAttribute("maxlength"));
				System.out.println();

				switch (mode) {
				case LENGTH:
					soft.assertThat(getTexts).hasSizeBetween(1, size).isNotNull().isNotEmpty().isNotBlank();

					test.log(Status.PASS, Description + fontColorType(colorType.ORANGE,
							" | Text size: " + length + " Max size: " + size + " Text is: " + getTexts));

					System.out.println(Description + " | " + "INFO: SIZE OF TEXT IS: " + length + " SIZE OF FIELD: "
							+ size + ": MODE " + mode + " | " + "TEXT IS: " + getTexts);
					break;
				case EMAIL:
					soft.assertThat(getTexts).hasSizeBetween(1, size).isNotNull().isNotBlank().isNotEmpty()
							.doesNotContainAnyWhitespaces().contains("@").contains(".");

					test.log(Status.PASS, Description + fontColorType(colorType.ORANGE,
							" | Text size: " + length + " Max size: " + size + " Text is: " + getTexts));
					System.out.println(Description + " | " + "INFO: EMAIL IS: " + getTexts + " | "
							+ "SIZE OF TEXT FIELD: " + size + " MODE " + mode);
					break;
				case PHONE:

					soft.assertThat(getTexts).hasSize(size).isNotNull().isNotEmpty().isNotBlank()
							.containsPattern("^\\d{3}-\\d{3}-\\d{4}$").doesNotContainAnyWhitespaces(); // hasSizeBetween(1,

					test.log(Status.PASS, Description + fontColorType(colorType.ORANGE,
							" | Text size: " + length + " Max size: " + size + " Text is: " + getTexts)); // // 10)
					System.out.println(Description + " | " + "INFO: <NUMBER> TEXT IS: " + getTexts + " | "
							+ "SIZE OF TEXT FIELD: " + size + " MODE " + mode);
					break;
				case NUMBER:
					soft.assertThat(getTexts).hasSizeBetween(1, size).isNotNull().isNotEmpty().isNotBlank()
							.doesNotContainPattern("[a-zA-Z]").doesNotContainAnyWhitespaces();

					test.log(Status.PASS, Description + fontColorType(colorType.ORANGE,
							" | Text size: " + length + " Max size: " + size + " Text is: " + getTexts));
					System.out.println(Description + " | " + "INFO: <NUMBER> TEXT IS: " + getTexts + " | "
							+ "SIZE OF TEXT FIELD: " + size + " MODE: " + mode);
					break;

				default:
					System.out.println("ERROR: Please check 'checkError' method: " + Description);
					reportFailure(
							Description + " | " + "<font color=red> ERROR: Please check 'checkError' method! </font>");
					break;
				}
				soft.assertAll();

			} catch (SoftAssertionError softAssertionError) {
				List<String> errors = softAssertionError.getErrors().stream().map(x -> x.replaceAll("[\\[|\\]]", ""))
						.collect(Collectors.toList());
				System.out.println("ERROR: PLEASE CHECK: " + Description + " | " + errors);
				reportLog(Description + " | " + "<font color=red> Unexpected exception is thrown: </font>" + errors);
			}

		} else {

			System.out.println("INFO: Pass: " + Description + " | " + true);
			test.log(Status.PASS, Description + " |  <font color=Green>" + true + "</font>");

		}
	}

	public void quit() {
		try {
			if (getDriver() != null)
				getDriver().quit();
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Unexpected exception is thrown: " + e);
		}

	}

	/******************** Utilty Funtion ******************/
//Centralized function to extract the objects
	public WebElement getObject(String objectKey) {

		WebElement e = null;
		try {

			if (objectKey.endsWith("_id"))
				e = getDriver().findElement(By.id(prop.getProperty(ObjectKey)));
			else if (objectKey.endsWith("_css"))
				e = getDriver().findElement(By.cssSelector(prop.getProperty(ObjectKey)));
			else if (objectKey.endsWith("_name"))
				e = getDriver().findElement(By.name(prop.getProperty(ObjectKey)));
			else
				e = getDriver().findElement(By.xpath(prop.getProperty(ObjectKey)));

		} catch (Exception ex) {

			reportFailure("Object Not Found " + ObjectKey);
		}
		return e;
	}

	public void loginToSalesforce() {

		try {
			presenceOfElementLocator(getDriver(), "//*[@id='username']").clear();
			presenceOfElementLocator(getDriver(), "//*[@id='username']").sendKeys(envProp.getProperty("SF_username"));
			test.log(Status.INFO,
					"Salesforce usename: " + fontColorType(colorType.MAGENTA, envProp.getProperty("SF_username")));
			presenceOfElementLocator(getDriver(), "//*[@id='password']").clear();
			presenceOfElementLocator(getDriver(), "//*[@id='password']").sendKeys(envProp.getProperty("SF_password"));
			test.log(Status.INFO,
					"Salesforce password: " + fontColorType(colorType.MAGENTA, envProp.getProperty("SF_password")));
			presenceOfElementLocator(getDriver(), "//*[@id='Login']").click();
			test.log(Status.INFO, "Click on login button:");

			switchLightMode();
			selectLendistrySalesyMode();
			test.log(Status.INFO, "Login to salesforce success");
			System.out.println("INFO: Login to salesforce success");

		} catch (Exception e) {
			reportFailure(Description, "ERROR: at loginToSalesforce: ", e);
		}

	}

	public void switchLightMode() {
		sleepThread1Sec();
		getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> dynamicElement = getDriver().findElements(By.linkText("Switch to Lightning Experience"));
		if (!dynamicElement.isEmpty()) {
			try {
				getDriver().findElement(By.linkText("Switch to Lightning Experience")).click();
				System.out.println("INFO: SWITCH TO LIGHTINING MODE!");

			} catch (Exception e) {

				throw new NoSuchElementException("EXCEPTION: LIGHTING MODE ELEMENT NOT FOUND!");

			}
		} else {
			System.out.println("INFO: ALREADY IN LIGHTINING MODE!");
		}

	}

// Returns true or False
//True- Present
//False-Not Present

	/**************************
	 * Reporting Function
	 **********************************/

	public void reportLog(String failureMsg) {
		test.log(Status.FAIL, failureMsg);
	}

	public void reportFailure(String failureMsg) {

		test.log(Status.FAIL, failureMsg);
		System.out.println(failureMsg);
		takeScreenShot();

		String expected = prop.getProperty(ObjectKey);
		drawBorder(getDriver(), expected);
		sleepThreadSec();
		takeScreenShot();

		if (proceedOnFail.equals("Y")) {// Soft Assertion
			softAssert.fail(failureMsg);
			getDriver().quit();
		} else {
			softAssert.fail(failureMsg);
			getDriver().quit();
			softAssert.assertAll();

		}

	}

	public void reportFailure(String desc, String failureMsg) {

		test.log(Status.FAIL, desc + fontColorType(colorType.RED, " | " + failureMsg));
		System.out.println(desc + failureMsg);
		takeScreenShot();
		String expected = prop.getProperty(ObjectKey);
		drawBorder(getDriver(), expected);
		sleepThreadSec();
		takeScreenShot();

		if (proceedOnFail.equals("Y")) {// Soft Assertion
			softAssert.fail(failureMsg);
			getDriver().quit();
			System.out.println("driver close");
		} else {
			softAssert.fail(failureMsg);
			getDriver().quit();
			System.out.println("driver close!");
			softAssert.assertAll();

		}

	}

	public void reportFailure(String desc, String failureMsg, Exception e) {

		test.log(Status.FAIL, desc + fontColorType(colorType.RED, " | " + failureMsg) + e);
		System.out.println(desc + failureMsg + e);
		takeScreenShot();

		String expected = prop.getProperty(ObjectKey);
		drawBorder(getDriver(), expected);
		sleepThreadSec();
		takeScreenShot();

		if (proceedOnFail.equals("Y")) {
			softAssert.fail(failureMsg);
			getDriver().quit();
		} else {
			softAssert.fail(failureMsg);
			getDriver().quit();
			softAssert.assertAll();

		}

	}

	public void highLighted() {
		try {
			String expected = prop.getProperty(ObjectKey);
			drawBorder(getDriver(), expected);
			takeScreenShot();
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Unexpected exception is thrown: " + e);
		}
	}

	public void assertAll() {
		softAssert.assertAll();
	}

	public void takeScreenShot() {
		sleepThread1Sec();
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
		// take screenshot
		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		try {
			// get the dynamic folder name
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath + screenshotFile));
			// put screenshot file in reports
			test.log(Status.INFO, "Screenshot-> "
					+ test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath + screenshotFile));
			System.out.println("INFO: Take Screenshot!");
		} catch (IOException e) {
			reportFailure("Unexpected exception is thrown: " + e);
		}
	}

	public File getTheNewestFile(String filePath, String ext) {
		File theNewestFile = null;
		File dir = new File(filePath);
		FileFilter fileFilter = new WildcardFileFilter("*." + ext);
		File[] files = dir.listFiles(fileFilter);

		if (files.length > 0) {
			/** The newest file comes first **/
			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
			theNewestFile = files[0];
		}

		return theNewestFile;
	}

	public void zipReportsFolder() {
		File dir = new File(ExtentManager.ReportFolder);
		zipDirectory(dir, ExtentManager.ReportsZipName);
	}

	private static void zipDirectory(File dir, String zipDirName) {

		try {
			populateFilesList(dir);

			try (FileOutputStream fos = new FileOutputStream(zipDirName);
					ZipOutputStream zos = new ZipOutputStream(fos)) {
				for (String filePath : filesListInDir) {
					System.out.println("Zipping: " + filePath);
					ZipEntry ze = new ZipEntry(
							filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
					zos.putNextEntry(ze);
					try (FileInputStream fis = new FileInputStream(filePath)) {
						byte[] buffer = new byte[1024];
						int len;
						while ((len = fis.read(buffer)) > 0) {
							zos.write(buffer, 0, len);
						}
						zos.closeEntry();
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void populateFilesList(File dir) throws IOException {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile())
				filesListInDir.add(file.getAbsolutePath());
			else
				populateFilesList(file);
		}
	}

	public static void drawBorder(WebDriver driver, String xpath) {
		try {
			WebElement elementNode = driver.findElement(By.xpath(xpath));
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.border='3px solid red'", elementNode);

		} catch (Exception e) {
			throw new FrameworkException("ERROR: please check locator: " + xpath);

		}

	}

	public void sleepThreadSec() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
			reportFailure("Unexpected exception is thrown: " + e);

		}
	}

	public void sleepThread1Sec() {
		try {

			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();

		}
	}

	public void sleepThread2Sec() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();

		}
	}

	public void sleepThread3Sec() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();

		}
	}

	public void sleepThread4Sec() {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();

		}
	}

	public void sleepThread5Sec() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();

		}
	}

	public void sleepThread7Sec() {
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();

		}
	}

	public void sleepThread8Sec() {
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();

		}
	}

	public void sleepThread10Sec() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();

		}
	}

	public void setDescriptionKey(String Description) {
		this.Description = Description;
	}

	public String getProceedOnFail() {
		return proceedOnFail;
	}

	public void setProceedOnFail(String proceedOnFail) {
		this.proceedOnFail = proceedOnFail;
	}

	public void setExtentTest(ExtentTest test) {
		this.test = test;
	}

	public void setEnvProp(Properties envProp) {
		this.envProp = envProp;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public void setObjectKey(String objectKey) {
		ObjectKey = objectKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public void setData(Hashtable<String, String> data) {
		this.data = data;
	}

}