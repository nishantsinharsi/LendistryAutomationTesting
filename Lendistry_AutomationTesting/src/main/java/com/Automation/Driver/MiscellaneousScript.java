/**
 * 
 */
package com.Automation.Driver;

/**
 * @author Nishant.Sinha
 *- Jun 15, 2022
 */
public class MiscellaneousScript {
	
	/*
	 * public void validateMsg() {
	 * 
	 * List<WebElement> isPresent =
	 * getDriver().findElements(By.xpath(prop.getProperty(ObjectKey)));
	 * 
	 * if (isPresent.size() != 0) {
	 * 
	 * try {
	 * 
	 * WebElement element = getWait()
	 * .until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty(
	 * ObjectKey)))); String validationMessage = element.getText().strip();
	 * System.out.println("INFO: VALIDATE MESSAGE: " + validationMessage);
	 * 
	 * switch (validationMessage) { case "File rejected successfully!":
	 * test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break; case "File accepted successfully!":
	 * test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break; case "File validated successfully!":
	 * test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break; case "File Uploaded successfully!":
	 * test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break; case
	 * "File size should be less than 15MB": test.log(Status.PASS, Description +
	 * " | " + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "COMPLETED": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Please enter a valid input": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Please enter Mobile Number in XXX-XXX-XXXX format.": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break; case "Please Enter Mobile Number in to (XXX-XXX-XXXX) Format.":
	 * test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break;
	 * 
	 * case "Please enter valid input": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Complete this field.": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Both email does not match.": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Owner City should not be blank.": test.log(Status.PASS, Description + " | "
	 * + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Please select a state.": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Both cell number does not match.": test.log(Status.PASS, Description + " | "
	 * + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Owner Zip should not be blank. ": test.log(Status.PASS, Description + " | "
	 * + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Please select a county.": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Age should not be greater than 100 years.": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break; case "Invalid Date.": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Age should not be less than 18 years.": test.log(Status.PASS, Description +
	 * " | " + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Owner Social Security# should not be blank.": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break; case
	 * "Owner Social Security # should have 9 digits (e.g. 000-00-0001)":
	 * test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break; case
	 * "% of Ownership should not be greater than 100 and not less than 0.":
	 * test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break; case
	 * "Please accept Terms and Conditions.": test.log(Status.PASS, Description +
	 * " | " + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Data has been saved successfully. Please fill the below form to complete the application."
	 * : test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break; case "Error!": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break; case "DBA should not be blank.": test.log(Status.PASS, Description +
	 * " | " + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Business EIN should not be blank.": test.log(Status.PASS, Description +
	 * " | " + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Business Phone should not be blank.": test.log(Status.PASS, Description +
	 * " | " + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Please select a business type.": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Please select a State of Incorporation.": test.log(Status.PASS, Description
	 * + " | " + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Business Address should not be blank.": test.log(Status.PASS, Description +
	 * " | " + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "City should not be blank.": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Please select a State.": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Business Website URL should not be blank.": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break; case "Please select a purpose for the grant.": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break; case "Amount Requested should not be blank.": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break; case "": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Annual Business Revenue should not be blank.": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break; case "Please select option.": test.log(Status.PASS, Description +
	 * " | " + "<font color=yellow>" + validationMessage + "</font>"); break; case
	 * "Please select an option.": test.log(Status.PASS, Description + " | " +
	 * "<font color=yellow>" + validationMessage + "</font>"); break;
	 * 
	 * case "Please select \"What does your business do?\".": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break; case "What is your type of business?.": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break; case "NAICS Code should not be blank.": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break; case "Business Start Date cannot be a future Date.":
	 * test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break; case "Invalid Business Website URL.":
	 * test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break; case "Zip should not be blank.":
	 * test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break; case
	 * "Amount Requested should be between $5,000.00 to $25,000.00.":
	 * test.log(Status.PASS, Description + " | " + "<font color=yellow>" +
	 * validationMessage + "</font>"); break; case
	 * "NAICS Code should have 6 digits (e.g. 999999)": test.log(Status.PASS,
	 * Description + " | " + "<font color=yellow>" + validationMessage + "</font>");
	 * break;
	 * 
	 * default: System.out.println("INFO:COULD NOT FOUND VALIDATION MESSAGE FOR: " +
	 * Description); reportLog(Description + " | " +
	 * "<font color=red> Message not found </font>"); break; }
	 * 
	 * } catch (Exception e) { System.out.println(); System.out.println("ERROR: " +
	 * Description + " | " + e); reportLog(Description + " | " +
	 * "<font color=red> Message not found:  </font>" + prop.getProperty(ObjectKey)
	 * + " - " + e); e.printStackTrace(); }
	 * 
	 * } }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
//"----------------------------------------------------------------------------"	
	
//	public void disapearLoderIcon() {		
//		int size = 0;
//		int count = 0;
//		List<WebElement> spin = getDriver()
//				.findElements(By.xpath("//*[contains(@class,'la-ball-spin-clockwise la-3x')]"));
//		try {
//			if (spin.size() != 0) {
//				size = spin.size();
//				System.out.println("----------------" + size);
//
//				while (spin.size() != 0 && count < 10) {
//					Thread.sleep(500);
//					count++;
//				}
//				System.out.println("INFO: LODING ICON PRESENT");
//
//			} else {
//
//				System.out.println("INFO: LODING ICON NOT PRESENT");
//
//			}
//	}


}
