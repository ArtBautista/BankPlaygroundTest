package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static base.BasePage.delay;
import pages.DashboardPage;
import pages.LoginPage;


public class BaseTests {
    protected WebDriver driver;
    protected BasePage basePage;
    protected LoginPage loginPage;
    protected DashboardPage dashboardPage;
    private String BASE_URL = "https://qaplayground.com/bank/login";

    @BeforeClass 
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
    }

     @BeforeMethod
    public void goToLoginPage(){
        driver.get(BASE_URL);
        basePage = new BasePage();
        basePage.setDriver(driver);
        loginPage = new LoginPage();
    }
    
    @AfterClass 
    public void tearDown(){
        delay(1000);
        driver.quit();
    }
}
