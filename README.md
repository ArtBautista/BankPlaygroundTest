# BankPlaygroundTest

A banking UI test automation project built with **Java, Selenium WebDriver, TestNG, and Maven**. The framework uses the **Page Object Model (POM)** to separate browser interactions from test scenarios and assertions.

Tests exercise [QA Playground Bank](https://qaplayground.com/bank/login), including account management, transfers, payments, loan applications, and transaction history. This repository demonstrates positive and negative testing, financial value assertions, data-driven navigation checks, and reusable page objects.

## Test coverage

The repository contains eight test classes. The scenarios below describe the checks in source code, not a claim that the current suite passes.

| Module | Scenarios in the test suite |
| --- | --- |
| Login | Valid credentials, invalid username/password, and empty fields |
| Dashboard | Welcome message, net worth versus total account balances, quick-action navigation, notifications navigation, and logout |
| Accounts | Account list and details, overdraft and frozen-account alerts, account creation, and delete dialog/cancel/confirm behavior |
| Transfer | Form visibility, same-account restrictions, insufficient funds, required fields, transfer confirmation, balance updates, date selection, and cancellation |
| Send Money | Form visibility, entering payment details, payee account-number validation, successful sending, insufficient funds, and frozen-account restrictions |
| Bill Pay | Form fields, biller search and selection, adding a biller, successful payment, and insufficient funds |
| Apply Loan | Form fields, maximum amount validation, submission and history verification, frozen-account restrictions, history pagination, and a known total mismatch for the error-user scenario |
| Transactions | Table headers, account filtering, description search, date sorting, pagination, empty results, and debit filtering |

## Technology

| Component | Implementation |
| --- | --- |
| Language | Java; JDK 21 is the suggested local setup |
| Browser automation | Selenium WebDriver `4.48.0` |
| Test framework | TestNG `7.12.0` |
| Build tool | Maven |
| Browser | Google Chrome through `ChromeDriver` |
| Design pattern | Page Object Model with shared base classes |
| Assertions | TestNG assertions, `SoftAssert`, and `BigDecimal` comparisons |

Dependency versions come from [`qaplaygroundbank/pom.xml`](qaplaygroundbank/pom.xml). The POM currently does not pin the Java compiler level or Maven plugin versions.

## Framework design

- **Page objects:** each banking module exposes actions and checks through a dedicated page class.
- **Shared browser helpers:** `BasePage` provides element lookup, text entry, clicks, visibility checks, and URL checks, using a 20-second explicit wait.
- **Locators and enums:** page classes use `data-testid` attributes, IDs, and other selectors; enums group related controls and account choices.
- **Session reset:** before each test method, `BaseTests` visits the login page, clears cookies and local/session storage, reloads the page, and initializes page objects.
- **Browser lifecycle:** a Chrome instance is created per test class and closed after that class finishes.
- **Data-driven checks:** dashboard quick actions use a TestNG `@DataProvider`.
- **Debugging:** selected tests print balances, table headers, transaction IDs, and other diagnostic values.

The driver and wait are static in `BasePage`, so the current implementation should run sequentially. Explicit waits are used alongside a fixed-delay helper, including a five-second teardown delay.

## Repository structure

```text
BankPlaygroundTest/
├── README.md
├── LICENSE
└── qaplaygroundbank/
    ├── pom.xml
    └── src/
        ├── main/java/
        │   ├── base/BasePage.java
        │   └── pages/
        │       ├── LoginPage.java
        │       ├── DashboardPage.java
        │       ├── AccountsPage.java
        │       ├── TransferPage.java
        │       ├── SendMoneyPage.java
        │       ├── BillPayPage.java
        │       ├── ApplyLoanPage.java
        │       └── TransactionsPage.java
        └── test/java/
            ├── base/BaseTests.java
            ├── LoginTests.java
            ├── DashboardTests.java
            ├── AccountsTests.java
            ├── TransferTests.java
            ├── SendMoneyTests.java
            ├── BillPayTests.java
            ├── ApplyLoanTests.java
            └── TransactionsTests.java
```

## Local setup

Install JDK 21, Maven, and Google Chrome. Ensure Java and Maven are on your `PATH` and that the machine can access the demo application and download Maven/browser-driver dependencies.

```bash
java -version
mvn -version

git clone https://github.com/ArtBautista/BankPlaygroundTest.git
cd BankPlaygroundTest/qaplaygroundbank
```

### Current build prerequisite

`BaseTests.java` imports `utilities.Utility.setUtilityDriver` and calls `setUtilityDriver()`, but the corresponding `utilities.Utility` source is absent from this checkout. Restore that utility or remove the obsolete import and call before attempting a clean build. Existing files under `target/` do not establish that a fresh checkout compiles.

### Run tests

After resolving the missing utility, run these commands from `qaplaygroundbank/`. The compiler properties explicitly select Java 21 because the POM does not set a compiler level. The test selector explicitly includes the repository's `*Tests` class names.

```bash
# Run all test classes
mvn clean test -Dmaven.compiler.source=21 -Dmaven.compiler.target=21 -Dtest="*Tests"

# Run one module
mvn test -Dmaven.compiler.source=21 -Dmaven.compiler.target=21 -Dtest=TransactionsTests

# Run one test method
mvn test -Dmaven.compiler.source=21 -Dmaven.compiler.target=21 -Dtest=AccountsTests#testAddNewAccount
```

These are execution instructions, not verified passing results. The current browser setup opens a visible Chrome window; there is no headless command-line option wired into `BaseTests`.

### Demo users

The tests use the following practice accounts with password `bank_sauce`:

| Username | Usage in this suite |
| --- | --- |
| `standard_user` | Standard banking workflows |
| `overdraft_user` | Overdraft alert checks |
| `frozen_user` | Frozen-account restrictions |
| `error_user` | Known loan-history total mismatch |

The error-user loan test deliberately asserts that the displayed and calculated totals differ. A passing result for that test confirms the demo defect is reproduced.

## Results and troubleshooting

After a Maven test run, inspect `qaplaygroundbank/target/surefire-reports/` for generated test results and the console for assertion messages and debug output. Previously committed reports may be stale; use the output of your own run.

| Symptom | What to check |
| --- | --- |
| `package utilities does not exist` | Resolve the missing `Utility` dependency described above. |
| Unsupported source/target level | Confirm `mvn -version` uses JDK 21 and include the compiler properties shown above. |
| Chrome fails to start | Check Chrome installation, driver compatibility, network access, and availability of a graphical session. |
| Element timeout or failed assertion | Check demo-site availability, the active user, locator changes, and the expected test data. |
| Pagination returns no IDs | Check that rows are loaded and that the page object's row/ID selectors match the current DOM. |

## Adding coverage

1. Add or extend a page object under `src/main/java/pages/` and reuse `BasePage` helpers.
2. Initialize any new page object in `BaseTests.goToLoginPage()`.
3. Add a test class extending `BaseTests`, or extend an existing module's test class.
4. Set up the appropriate user and navigate to the module in `@BeforeMethod`.
5. Assert an observable outcome, such as a validation message, updated balance, or history entry.
6. Run the focused test and update the coverage table when adding a new scenario category.

## Planned improvements

- Restore a reproducible clean build and pin compiler and test-runner plugin versions.
- Add configurable base URL, browser options, and headless execution.
- Add a CI workflow to execute tests and publish reports; no CI/CD workflow is currently committed.
- Capture screenshots and browser diagnostics on failure.
- Replace fixed delays with condition-based waits.
- Isolate driver state before enabling parallel execution.
- Keep generated `target/` output out of version control.

## License

See [LICENSE](LICENSE) for the GNU General Public License, version 3.

## Acknowledgements

[QA Playground](https://qaplayground.com) provides the banking application used by this practice project.
