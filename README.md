Banking UI Automation with Selenium

A hands-on practice project for UI / end-to-end test automation using Selenium WebDriver, Java, and TestNG, built with the Page Object Model (POM).

The system under test is QA Playground Bank, a realistic banking journey with forms, validations, and account workflows. It is a stable target, so it's ideal for practicing automation without flaky third-party sites.

What's covered
Area	Scenarios
Login	Valid login, session reset between tests
Dashboard	Welcome message, quick actions (transfer, send money, bill pay, apply loan, transactions), notifications, logout
Accounts	Header display, list vs. detail value comparison, add account, delete account (cancel / confirm)
Transfer	Sidebar navigation, cancel transfer

Coverage grows as new pages and flows are added.

Tech stack
Java 21
Selenium WebDriver 4.x
TestNG (annotations, @DataProvider, assertions)
Maven (dependency management and test runs)
Google Chrome with ChromeDriver (Selenium Manager resolves it automatically)
Project structure
src
├── main/java
│   ├── base
│   │   └── BasePage.java        # Shared helpers: click, set, find, isDisplayed, waits
│   └── pages
│       ├── LoginPage.java
│       ├── DashboardPage.java
│       ├── AccountsPage.java
│       └── TransferPage.java
└── test/java
    ├── base
    │   └── BaseTests.java       # Driver lifecycle + page object setup
    ├── DashboardTests.java
    ├── AccountsTests.java
    └── TransferTests.java

Adjust the tree above if your folders differ.

Design approach
Page Object Model: each page class exposes user actions and checks; tests contain only the flow and the assertions.
Stable locators: elements are located through data-testid attributes (By.cssSelector("[data-testid='...']")) rather than brittle XPath or auto-generated IDs.
Enums for locators: related elements are grouped in enums (for example AccountButtons, QuickAction) so tests read clearly and locators live in one place.
Explicit waits: BasePage wraps WebDriverWait so tests don't rely on Thread.sleep.
Test isolation: before every test, BaseTests clears cookies, localStorage, and sessionStorage and reloads the login page, so each test starts logged out regardless of run order.
Data-driven tests: @DataProvider is used where the same check repeats across inputs (quick actions, account fields).
Getting started
Prerequisites
JDK 21 (or a version matching your pom.xml)
Maven 3.9+
Google Chrome (latest)
Clone and run
bash
git clone https://github.com/<your-username>/<your-repo>.git
cd <your-repo>
mvn clean test
Run a single class or method
bash
mvn test -Dtest=DashboardTests
mvn test -Dtest=AccountsTests#testAddNewAccount
Test credentials

The demo site provides a standard user for practice:

Username	Password
standard_user	bank_sauce
Writing a new test
Create a page class in pages/ that extends BasePage.
Add locators (or an enum of data-testid values) and action methods.
Declare and initialize the page in BaseTests (goToLoginPage()).
Create a test class extending BaseTests and log in inside @BeforeMethod.
java
public class TransferTests extends BaseTests {

    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
        transferPage.clickTransferButtons(TransferButtons.SIDEBAR_TRANSFER_BUTTON);
    }

    @Test
    public void testCancelTransferButton() {
        transferPage.clickTransferButtons(TransferButtons.CANCEL_TRANSFER_BUTTON);
        // assert the expected outcome here
    }
}
Ideas to practice next
Login validation: wrong password, empty fields, locked-out user
Form validation messages on transfer, bill pay, and loan application
Full transfer flow: fill, review, confirm, and verify the transaction history
Cross-page checks: balance changes after a transfer
Screenshots on failure via a TestNG listener
HTML reports (Allure or ExtentReports)
Parallel execution with a ThreadLocal<WebDriver>
CI with GitHub Actions running headless Chrome
Notes
Tests hit a live public site. If the site is slow or down, failures may not be caused by your code.
Some tests change data (for example deleting an account). Prefer creating the data a test needs rather than depending on seeded records.
This project is for learning and practice purposes only.
Acknowledgements

Thanks to QA Playground for providing a free, realistic practice environment.
