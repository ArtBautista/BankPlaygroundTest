package base;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import pages.AccountsPage;
import pages.ApplyLoanPage;
import pages.BillPayPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.SendMoneyPage;
import pages.TransactionsPage;
import pages.TransferPage;


public class BaseTests {
    protected WebDriver driver;
    protected BasePage basePage;
    protected LoginPage loginPage;
    protected DashboardPage dashboardPage;
    protected AccountsPage accountsPage;
    protected TransferPage transferPage;
    protected SendMoneyPage sendMoneyPage;
    protected BillPayPage billPayPage;
    protected TransactionsPage transactionsPage;
    protected ApplyLoanPage applyLoanPage;
    private final String baseUrl = System.getProperty(
        "baseUrl", "https://qaplayground.com/bank/login");

    @BeforeClass 
    public void setUp(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080", "--disable-dev-shm-usage");
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        
    }

    @BeforeMethod
    public void goToLoginPage(){
        driver.get(baseUrl); // navigate first so JS execution has a matching origin
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear(); window.sessionStorage.clear();");
        driver.manage().deleteAllCookies();
        driver.get(baseUrl); // reload to actually land on login page now that session is cleared
        basePage = new BasePage();
        basePage.setDriver(driver);
        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
        accountsPage = new AccountsPage();
        transferPage = new TransferPage();
        sendMoneyPage = new SendMoneyPage();
        billPayPage = new BillPayPage();
        transactionsPage = new TransactionsPage();
        applyLoanPage = new ApplyLoanPage();
    }
    
    @AfterMethod(alwaysRun = true)
    public void captureFailure(ITestResult result) {
        if (result.isSuccess() || driver == null) {
            return;
        }
        try {
            Path directory = Path.of("target", "screenshots");
            Files.createDirectories(directory);
            String name = result.getTestClass().getRealClass().getSimpleName()
                + "-" + result.getMethod().getMethodName() + "-" + System.nanoTime();
            Files.write(directory.resolve(name + ".png"),
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
        } catch (Exception exception) {
            // Diagnostics must not replace the original test failure.
            System.err.println("Could not capture failure screenshot: " + exception.getMessage());
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown(){
        if (driver != null) {
            driver.quit();
        }
    }
}
