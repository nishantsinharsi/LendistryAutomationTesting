package com.Automation.Base;

import java.util.List;

import org.testng.TestNG;
import org.testng.collections.Lists;

public class runRSIFramework {

//	@SuppressWarnings("deprecation")
	public static void main(String args[]) {
	//	TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();

		if (args.length <= 0) {
			System.out.println("RSI Framework Runnning..");
			suites.add(".\\TestCases_RSI.xml");// path to xml..
		} else {
			System.out.println("runMC Runnning.. " + args[0]);
			suites.add(".\\TestCases_RSI" + args[0] + ".xml");// path to xml..
		}
		testng.setTestSuites(suites);
		testng.run();

	}
}
