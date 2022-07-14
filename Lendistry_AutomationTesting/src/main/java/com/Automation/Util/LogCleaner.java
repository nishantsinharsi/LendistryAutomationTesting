package com.Automation.Util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;


  //@author Nishant.Sinha - Mar 22, 2022
 
public final class LogCleaner {

	public static void main(String[] args) {
		deleteAllemailLogs();
		deleteAllReport();
	}

	private static final void deleteAllemailLogs() {

		try {
			String root = System.getProperty("user.dir") + "/LendistryLog/";
			String emailLog = root + "emaillog.txt";
			String dirLog = root + "dirLogs.txt";
			String yopLog = root + "yopMailLog.txt";
			emailLog(emailLog);
			emailLog(dirLog);
			emailLog(yopLog);
			Files.newBufferedWriter(Paths.get(dirLog), StandardOpenOption.TRUNCATE_EXISTING);
			Files.newBufferedWriter(Paths.get(emailLog), StandardOpenOption.TRUNCATE_EXISTING);
			Files.newBufferedWriter(Paths.get(yopLog), StandardOpenOption.TRUNCATE_EXISTING);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final void emailLog(String filename) {
		long count = 0;

		try {
			File file = new File(filename);
			LineIterator lineIterator = FileUtils.lineIterator(file, "UTF-8");
			while (lineIterator.hasNext()) {
				String readLine = lineIterator.nextLine();
				count += readLine.lines().count();
				System.out.println(count + " " + readLine);
			}
			String dirName = filename.substring(45);

			System.out.println(count + " email log delete sucessfully from " + dirName);
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	private static final void deleteAllReport() {
		int count = 0;
		try {
			String root = System.getProperty("user.dir") + "/Report/";
			File file = new File(root);
			if (file.list().length != 0) {
				count += file.list().length;
				deleteOldFiles(file.listFiles());
				System.out.println(count + " Test report delete sucessfully . . . ");

			} else {
				System.out.println("Test Report not found...!!!");
			}

	

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final void deleteOldFiles(File[] filesToDelete) {
		try {
			for (File report : filesToDelete) {
				if (report.lastModified() > 0) {
					FileUtils.deleteDirectory(report);
					System.out.println("Report: " + report);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
