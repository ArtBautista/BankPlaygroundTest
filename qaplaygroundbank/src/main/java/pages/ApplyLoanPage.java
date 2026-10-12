package pages;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;
import pages.ApplyLoanPage.ApplyLoanElement;


public class ApplyLoanPage extends BasePage{

    private By transactionsRow = By.cssSelector("[data-testid='loan-history-row']");
    
    public enum ApplyLoanElement {
        APPLY_LOAN_BUTTON("open-apply-loan-btn"),
        RESET_DATA_BUTTON("reset-data-btn"),
        SEARCH_BUTTON("loan-search-input"),

        CONFIRM_BUTTON("confirm-loan-btn"),
        BACK_BUTTON("cancel-confirm-loan-btn"),
        SUCCESSFUL("loan-success-heading"),
        FROZEN_HEADER("apply-loan-frozen-banner"),
        DATE("loan-history-date"),
        LOAN_TYPE("loan-history-type-badge"),
        AMOUNT("loan-history-amount"),
        REVIEW("review-loan-btn"),
        REVIEW_CANCEL("cancel-loan-btn"),
        ERROR_MESSAGE("apply-loan-error-message"),
        TOTAL_AMOUNT("loan-history-total-value"),
        STATUS("loan-history-status-badge");

        private final String testId;

        ApplyLoanElement(String testId) {
            this.testId = testId;
        }

        public By locator() {
            return By.cssSelector(
                "[data-testid='" + testId + "']"
            );
        }
    }
    
    public enum ApplyLoanFields {
        DIALOG("apply-loan-dialog-title"),
        TYPE("loan-type-select"),
        AMOUNT("loan-amount-input"),
        TERM("loan-term-select"),
        INTEREST("loan-interest-rate-input"),
        ACCOUNT("loan-account-select"),
        LOAN_TYPE("loan-account-select"),
        PURPOSE("loan_purpose_field", true);

        private final String locatorValue;
        private final boolean useName;

        ApplyLoanFields(String locatorValue) {
            this(locatorValue, false);
        }

        ApplyLoanFields(String locatorValue, boolean useName) {
            this.locatorValue = locatorValue;
            this.useName = useName;
        }

        public By locator() {
            return useName
                ? By.name(locatorValue)
                : By.cssSelector(
                    "[data-testid='" + locatorValue + "']"
                );
        }
    }

    public enum ApplyLoanType {
        PERSONAL("personal", "Personal"),
        AUTO("auto", "Auto"),
        HOME("home", "Home"),
        STUDENT("student", "Student");

        private final String type;
        private final String displayName;

        ApplyLoanType(String type, String displayName) {
            this.type = type;
            this.displayName = displayName;
        }

        By locator() {
            return By.cssSelector(
                "[data-testid='loan-type-option']" +
                "[data-loan-type='" + type + "']"
            );
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    public enum ApplyLoanTerm {
        MONTHS_12("12"),
        MONTHS_24("24"),
        MONTHS_36("36"),
        MONTHS_48("48"),
        MONTHS_60("60");

        private final String months;

        ApplyLoanTerm(String months) {
            this.months = months;
        }

        public String getMonths() {
            return months;
        }

        By locator() {
           return By.cssSelector(
                "[data-testid='loan-term-option']" +
                "[data-term-months='" + months + "']"
            );
        }
    }
    public enum ApplyLoanAccount {
        HIGH_YIELD_SAVINGS("acc-savings-1", "High-Yield Savings"),
        EVERYDAY_CHECKING("acc-checking-1", "Everyday Checking");

        private final String accountId;
        private final String displayName;

        ApplyLoanAccount(String accountId,String displayName) {
            this.accountId = accountId;
            this.displayName = displayName;
        }

        By locator() {
            return By.cssSelector(
                "[data-testid='loan-account-option'][data-account-id='" + accountId + "']");
        }
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public boolean isLoanInHistory(
            ApplyLoanType type,
            String amount,
            String status) {

        for (WebElement row : getAllLoanHistoryRows()) {

            String rowType = row.getDomAttribute("data-loan-type");
            String rowAmount = row.getDomAttribute("data-amount");
            String rowStatus = row.getDomAttribute("data-status");

            if (rowType.equalsIgnoreCase(type.name())
                    && rowAmount.equals(amount)
                    && rowStatus.equalsIgnoreCase(status)) {
                return true;
            }
        }

        return false;
    }
    public boolean waitForLoanInHistory(
            ApplyLoanType type,
            String amount,
            String status) {

        WebDriverWait wait = new WebDriverWait(
            driver, Duration.ofSeconds(10)
        );

        return wait.until(driver ->
            isLoanInHistory(type, amount, status)
        );
    }
    public void clickApplyLoanField(ApplyLoanFields field){
        click(field.locator());
    }
    public void clickApplyLoanElement(ApplyLoanElement element){
        click(element.locator());
    }
    public By getLoanHistoryRow(String loanId) {
        return By.cssSelector(
            "[data-testid='loan-history-row']" +
            "[data-loan-id='" + loanId + "']"
        );
    }
    public List<WebElement> getAllLoanHistoryRows() {
        return driver.findElements(
            By.cssSelector("[data-testid='loan-history-row']")
        );
    }
    public boolean isApplyLoanFieldDisplayed(ApplyLoanFields element){
        return isDisplayed(element.locator());
    }

    // Get a row by its position (zero-based)
    public WebElement getLoanHistoryRowByIndex(int index) {
        return getAllLoanHistoryRows().get(index);
    }

    // Retrieve a specific loan field
    public String getLoanHistoryField(
            String loanId,
            ApplyLoanElement field) {

        WebElement row = find(getLoanHistoryRow(loanId));

        return row.findElement(field.locator())
                .getText()
                .trim();
    }

    // Retrieve a column without data-testid
    public String getLoanHistoryColumn(
            String loanId,
            int columnIndex) {

        WebElement row = find(getLoanHistoryRow(loanId));

        return row.findElement(
            By.cssSelector("td:nth-child(" + columnIndex + ")")
        ).getText().trim();
    }

    // Check if loan exists
    public boolean isLoanHistoryDisplayed(String loanId) {
        return isDisplayed(getLoanHistoryRow(loanId));
    }
    public boolean isApplyLoanButtonEnabled(ApplyLoanElement button) {
        return find(button.locator()).isEnabled();
    }

    // Click Edit Loan within a specific row
    public void clickEditLoan(String loanId) {
        WebElement row = find(getLoanHistoryRow(loanId));

        row.findElement(
            By.cssSelector("button[title='Edit Loan']")
        ).click();
    }
    
    public String getLoanErrorDisplayed() {
        return find(ApplyLoanElement.ERROR_MESSAGE.locator()).getText().trim();
    }
    public String getLoanFrozenDisplayed() {
        return find(ApplyLoanElement.FROZEN_HEADER.locator()).getText().trim();
    }
    public String getLoanSuccessDisplayed() {
        return find(ApplyLoanElement.SUCCESSFUL.locator()).getText().trim();
    }
    public void selectType(ApplyLoanType type){
        clickApplyLoanField(ApplyLoanFields.TYPE);
        click(type.locator());
    }
    public void selectTerm(ApplyLoanTerm term){
        clickApplyLoanField(ApplyLoanFields.TERM);
        click(term.locator());
    }
    public void enterLoanAmount(String amount){
        set(ApplyLoanFields.AMOUNT.locator(), amount);
    }
    //
    public void selectAccount(ApplyLoanAccount accountId){
        clickApplyLoanField(ApplyLoanFields.ACCOUNT);
        click(accountId.locator());
    }
    // Enter loan purpose
    public void enterLoanPurpose(String purpose) {
        set(ApplyLoanFields.PURPOSE.locator(), purpose);
    }

    // Retrieve entered loan purpose
    public String getLoanPurpose() {
        return find(
            ApplyLoanFields.PURPOSE.locator()
        ).getDomProperty("value").trim();
    }
    
private BigDecimal parseAmount(String amount) {
    return new BigDecimal(
        amount.replaceAll("[^0-9.-]", "")
    );
}

// Get displayed total amount
public BigDecimal getDisplayedLoanTotal() {
    String total = find(
        ApplyLoanElement.TOTAL_AMOUNT.locator()
    ).getText().trim();

    return parseAmount(total);
}

// Calculate total of all visible loans
public BigDecimal getCalculatedLoanTotal() {
    BigDecimal total =  getAllLoanHistoryRows().stream()
        .filter(WebElement::isDisplayed)
        .map(row -> row.findElement(
            ApplyLoanElement.AMOUNT.locator()
        ).getText())
        .map(this::parseAmount)
        .peek(amount ->
            System.out.println("Loan Amount: $" + amount)
        )
        .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("---------------------");
        System.out.println("Calculated Total: $" + total);
        return total;
}

// Get the most recent visible loan amount
public BigDecimal getMostRecentLoanAmount() {
    return getAllLoanHistoryRows().stream()
        .filter(WebElement::isDisplayed)
        .max(Comparator.comparing(row -> {
            WebElement date = row.findElement(
                By.cssSelector(
                    "[data-testid='loan-history-date'] time"
                )
            );

            return date.getDomAttribute("datetime");
        }))
        .map(row -> parseAmount(
            row.findElement(
                ApplyLoanElement.AMOUNT.locator()
            ).getText()
        ))
        .orElseThrow(() ->
            new AssertionError("No visible loans found")
        );
}

    
}
