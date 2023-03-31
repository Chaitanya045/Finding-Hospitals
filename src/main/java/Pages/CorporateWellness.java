package Pages;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import Base.Base;

public class CorporateWellness extends Base {

	WebElement Name, OrgName, Email, Contact;

	By providers = By.xpath("//span[normalize-space()='Wellness Plans']");
	By corporate = By.xpath("//a[normalize-space()='For Corporates']");
	By name = By.id("name");
	By orgName = By.id("organizationName");
	By email = By.id("officialEmailId");
	By contact = By.id("contactNumber");
	By tky = By.xpath("//div[@class='u-text--bold text-alpha']");
	By msg = By.xpath("//div[@class='u-m-t--10']");

	@SuppressWarnings("resource")
	public void formFill() throws InterruptedException, IOException {

		logger = report.createTest("Alert Capturing");

		// Initializing the Excel Sheet
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/TestData.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fs);
		XSSFSheet sheet = workbook.getSheet("Data");
		DataFormatter formatter = new DataFormatter();

		// Selecting the corporate option
		try {
			findElement(providers).click();
			//findElement(corporate).click();
			reportPass("Corporate Wellness link Clicked Successfully");
		} catch (Exception e) {
			reportFail(e.getMessage());
		}

		// Switching to the new tab
		try {
			Set<String> currentHandle = driver.getWindowHandles();
			Iterator<String> itr = currentHandle.iterator();
			itr.next();
			String corporate = itr.next();
			driver.switchTo().window(corporate);
			reportPass("Switched to new tab Successfully");
		} catch (Exception e) {
			reportFail(e.getMessage());
		}

		// Fill the form. Submit it and get the Alerts
		try {
			Name = driver.findElement(name);
			OrgName = driver.findElement(orgName);
			Email = driver.findElement(email);
			Contact = driver.findElement(contact);
			
			
			
			//System.out.println("Your Appointment is Successfully Scheduled");
			
			
			//Get data from Excel and fill out form
			Name.sendKeys(sheet.getRow(1).getCell(0).getStringCellValue());
			OrgName.sendKeys(sheet.getRow(1).getCell(1).getStringCellValue());
			Email.sendKeys(sheet.getRow(2).getCell(2).getStringCellValue());
			String data = formatter.formatCellValue(sheet.getRow(2).getCell(3));
			Contact.sendKeys(data);
			WebElement dddown=driver.findElement(By.id("organizationSize"));
			Select select= new Select( dddown);
			select.selectByValue("1001-5000");
			reportPass("Data entered successfully");
			Screenshoot("dataEntered");
			Thread.sleep(4000);
			driver.findElement(By.xpath("//header[@id='header']//button[contains(@type,'submit')][normalize-space()='Schedule a demo']")).click();
			Thread.sleep(3000);
			
			//wait.until(ExpectedConditions.alertIsPresent());
			//msg = driver.switchTo().alert().getText();
			wait.until(ExpectedConditions.urlToBe("https://www.practo.com/plus/corporate?submitted=true"));
			Screenshoot("dataSubmitted");
			//System.out.println(msg);
			//printAlert();
			reportPass("Data submitted successfully");
			System.out.println("****************************************");
			System.out.println("            The Alert is: ");
			System.out.println("              "+findElement(tky).getText());
			System.out.println(findElement(msg).getText());
			System.out.println("****************************************");
			reportPass("Captured alert successfully");
			
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

}
