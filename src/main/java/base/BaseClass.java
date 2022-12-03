package base;

import static utils.IConstant.IMPLICIT_WAIT;
import static utils.IConstant.PAGELOAD_WAIT;
import static utils.IConstant.URL;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import com.google.common.io.Files;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.boat.AddresePage;
import pages.common.HomePage;
import pages.home.AboutYou;
import reporting.Logs;
import utils.Configuration;
import static utils.IConstant.*;

public class BaseClass {
Configuration config=  new Configuration();
	WebDriver driver;
	public static HomePage homePage;
	public static AboutYou aboutYou;
	public static AddresePage addresePage;
	
	@Parameters(BROWSER)
	@BeforeMethod
	public void setUpDriver() {
		//system.setProperty("webdriver.driver.chrome","/location/to/the/chrome/driver.exe");
		initDriver(config.getProperty(BROWSER));
		driver.manage().window().maximize();
		driver.get(config.getProperty((URL)));
		long pageLoadTime =Long.parseLong(config.getProperty(PAGELOAD_WAIT)) ;
		long implicitWait = Long.parseLong(config.getProperty(IMPLICIT_WAIT));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTime));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		initClasses();
		}
	
	private void initDriver (String browser) {
		String browserName = config.getProperty(BROWSER);
		switch (browserName) {
		case CHROME:
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(); 
			break;
		case FIREFOX:
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        break; 
		case EDGE:
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;
		case SAFARI:
			WebDriverManager.safaridriver().setup();
			driver = new SafariDriver();
			break;
		default:
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		}
		
	}
	
	public void initClasses() {
		homePage = new HomePage(driver);
		aboutYou = new AboutYou(driver);
		addresePage = new AddresePage(driver);
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	@AfterMethod
public void closingDriverSession() {
	getDriver().quit();
}

public String takeScreenShot(String testName) {
	Date date = new Date(0);
	SimpleDateFormat format = new SimpleDateFormat("_MMddyyyy_hhmmss");
	File localFile = new File("test-output/screenShots/" + testName + format.format(date) +".png");
	TakesScreenshot ss = (TakesScreenshot) driver;
	File driverSS = ss.getScreenshotAs(OutputType.FILE);
	try {
		Files.copy(driverSS, localFile);
		Logs.log("Screen Shot captured at \n" + localFile.getAbsolutePath());
	}catch (IOException e) {
		Logs.log("Error occurs during taking ScreenShot..!");
	}
	return localFile.getAbsolutePath();
}


}
