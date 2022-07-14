package com.Automation.Base;

import java.util.Properties;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.Automation.reports.EmailReport;

public class TestListener implements ITestListener, ISuiteListener {


	@Override
	public void onTestStart(ITestResult result) {

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailure(ITestResult result) {

		

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ITestContext context) {

	}

	@Override
	public void onFinish(ITestContext context) {

		// remove duplicate test from extent report

	}

	@Override
	public void onStart(ISuite suite) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish(ISuite suite) {

		EmailReport EM = new EmailReport();
		Properties envProp;
		envProp = EM.getEnvProp();
		try {
			if (envProp.getProperty("SendMail").equalsIgnoreCase("true")) {
				EM.SendEmail();
			} else {
				System.out.println("Email not sent");
			}
		} catch (Exception e) {
			System.out.println("Email not sent");
		}
	}

}