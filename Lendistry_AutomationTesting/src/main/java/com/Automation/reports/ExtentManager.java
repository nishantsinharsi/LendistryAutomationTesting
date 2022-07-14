package com.Automation.reports;

import java.io.File;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	public static String screenshotFolderPath;	
	public static String ReportFolder;
	public static String EmailReportName;
	public static String ReportsZipName;
	static ExtentTest test;

	public static ExtentReports getInstance(String reportPath) {
		if (extent == null) {
			String fileName = "RSI Test Automation Report.html";
			Date d = new Date();
			String folderName = d.toString().replace(":", "_").replace(" ", "_");
			// directory of the report folder
			new File(reportPath + folderName + "//screenshots").mkdirs();
			reportPath = reportPath + folderName + "//";
			ReportFolder = reportPath;
			screenshotFolderPath = reportPath + "screenshots//";
			EmailReportName = reportPath + fileName;
			ReportsZipName = reportPath + "\\report.zip";
			System.out.println(reportPath + fileName);
			createInstance(reportPath + fileName);

		}

		return extent;
	}

	public static ExtentReports createInstance(String fileName) {
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.config().setDocumentTitle("Lendistry Report");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("RSI - Lendistry Automation Testing");
		

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		return extent;
	}

	public static void generate(String status) {

		if (status.contains("Passed")) {

			test.debug("Status marked in Pass block : " + status);
			// Constants.extentTest.test(LogStatus.PASS, useCase , "<font size="+3+"
			// color='#7CFC00'>"+status +"</font>"+ " <br />" +expectedResult);

		} else if (status.contains("Fail")) {
		}
	}
}