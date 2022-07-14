package com.Automation.Base;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import com.Automation.Driver.DriverScript;
import com.Automation.Keywords.GenericKeywords;
import com.Automation.Util.DataUtil;
import com.Automation.Util.Xls_Reader;
import com.Automation.reports.EmailReport;
import com.Automation.reports.ExtentManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

@Listeners(TestListener.class)
public class BaseTest extends GenericKeywords {	

	public Properties envProp;
	public Properties prop;// env.properties
	public Xls_Reader xls;
	public String testName;
	public DriverScript ds;
	public GenericKeywords gk;
	public ExtentReports rep;
	// =ExtentManager.getInstance(prop.getProperty("reportPath"));
	public ExtentTest test;
	// = rep.createTest("LoginTest");

	public List<String> getPropertyList(Properties properties, String name) {
		List<String> result = new ArrayList<String>();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			if (((String) entry.getKey()).matches("^" + Pattern.quote(name) + "\\d+$"))
			// if ( (entry.getKey()+"").equals(name) )
			{
				result.add((String) entry.getValue());
			}
		}
		return result;
	}

	@BeforeClass(alwaysRun = true)
	public void intit() {
		// init testName
		System.out.println("Intializing...");
		System.out.println("Loading Package: " + this.getClass().getPackage().getName());
		System.out.println("Loading Test Class: " + this.getClass().getSimpleName());
		testName = this.getClass().getSimpleName();
		String arr[] = this.getClass().getPackage().getName().split("\\.");

		// System.out.println("INFO: " + arr[arr.length - 1]);
		String suiteName = arr[arr.length - 1];
		// Properties file
		prop = new Properties();
		envProp = new Properties();

		// init prop file
		try {
			FileInputStream fs = new FileInputStream(
					System.getProperty("user.dir") + "//src//test/resources//env.properties");
			this.prop.load(fs);// init env.properties
			System.out.println("INFO: Loading Property File: " + this.prop.getProperty("env"));
			// System.out.println(prop.getProperty("signinLink"));

			String env = this.prop.getProperty("env");
			fs = new FileInputStream(System.getProperty("user.dir") + "//src//test/resources//" + env + ".properties");
			envProp.load(fs);
			System.out.println("INFO: In Base Test .... Loading " + env + " Property");

			//
			List<String> getList = getPropertyList(this.prop, "env");
			for (String item : getList) {
				fs = new FileInputStream(
						System.getProperty("user.dir") + "//src//test/resources//" + item + ".properties");
				Properties prodTmp = new Properties();
				prodTmp.load(fs);
				for (Map.Entry<Object, Object> entry : prodTmp.entrySet()) {
					this.prop.put(entry.getKey(), entry.getValue());

				}
				System.out.println("INFO: In Base Test .... Loading " + item + " Property");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// init the xls file

		System.out.println("INFO: In Base Test .... " + envProp.getProperty(suiteName + "_xls"));

		xls = new Xls_Reader(System.getProperty("user.dir") + envProp.getProperty(suiteName + "_xls"));
		ds = new DriverScript();
		ds.setEnvProp(envProp);
		ds.setProp(prop);

		EmailReport er = new EmailReport();
		er.setEnvProp(envProp);

	}

	//@BeforeSuite
	public void setup(ITestContext context) {
		/*
		 * ITestNGMethod[] method = context.getAllTestMethods(); for (ITestNGMethod
		 * iTestNGMethod : method) { iTestNGMethod.setRetryAnalyzer(new
		 * RetryAnalyzer());
		 * System.out.println("Setup---------------------> RETRY FAIL TESTS");
		 * 
		 * }
		 */

	}

	@BeforeMethod(alwaysRun = true)
	public void initTest() {
		rep = ExtentManager.getInstance(System.getProperty("user.dir") + "/" + prop.getProperty("reportPath"));
		test = rep.createTest(testName);
		ds.setExtentTest(test);

	}

	@AfterMethod(alwaysRun = true)
	public void quit() {
		if (rep != null)
			rep.flush();
	}

	@AfterMethod(alwaysRun = true)
	public void reportCleanUp(Method method, ITestResult testResult) {
		if (testResult.getStatus() == 3) {
			rep.removeTest(test);
		}
	}

	@DataProvider
	public Object[][] getData() {
		// Can use xls file object to read data

		System.out.println("INFO: Inside Data Provider");
		Object[][] obj = null;
		try {
			// Object[][] data =new Object[1][1];
			// data[0][0]="a";

			// String testName="LoginTest";
			// Xls_Reader xls=new Xls_Reader("D:\\Users\\mangi.s\\Desktop\\SuiteA.xlsx");
			/*
			 * testName=this.getClass().getSimpleName();
			 * System.out.println("In Base Test ................" +
			 * "********"+this.getClass().getPackage().getName());
			 * System.out.println("In Base Test ................" + testName ); String arr[]
			 * = this.getClass().getPackage().getName().split("\\.");
			 * System.out.println("In Base Test ................" + arr[arr.length-1]);
			 * String suiteName= arr[arr.length-1];
			 * System.out.println("In Base Test ................Suite Name" +
			 * envProp.getProperty(suiteName+"_xls")); xls= new
			 * Xls_Reader(ds.getEnvProp().getProperty(suiteName+"_xls"));
			 * System.out.println("In Base Test ................" + xls.path);
			 */
			obj = DataUtil.getTestData(testName, xls);
		} catch (Exception ex) {
			System.out.println("In Base Test ................" + ex.getMessage());
			return null;
		}
		return obj;

	}

}