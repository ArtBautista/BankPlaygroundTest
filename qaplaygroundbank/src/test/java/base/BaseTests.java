package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static base.BasePage.delay;
import pages.AccountsPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.SendMoneyPage;
import pages.TransferPage;
import static utilities.Utility.setUtilityDriver;


public class BaseTests {
    protected WebDriver driver;
    protected BasePage basePage;
    protected LoginPage loginPage;
    protected DashboardPage dashboardPage;
    protected AccountsPage accountsPage;
    protected TransferPage transferPage;
    protected SendMoneyPage sendMoneyPage;
    private String BASE_URL = "https://qaplayground.com/bank/login";

    @BeforeClass 
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
    }

    @BeforeMethod
    public void goToLoginPage(){
        driver.get(BASE_URL); // navigate first so JS execution has a matching origin
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear(); window.sessionStorage.clear();");
        driver.manage().deleteAllCookies();
        driver.get(BASE_URL); // reload to actually land on login page now that session is cleared
        basePage = new BasePage();
        basePage.setDriver(driver);
        setUtilityDriver();
        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
        accountsPage = new AccountsPage();
        transferPage = new TransferPage();
        sendMoneyPage = new SendMoneyPage();
    }
    
    @AfterClass 
    public void tearDown(){
        delay(5000);
        driver.quit();
    }
}
