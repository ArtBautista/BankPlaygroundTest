
package pages;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;

public class TransactionsPage extends BasePage {

    public enum TransactionsElement {
        TRANSACTIONS_TITLE("transactions-page-title"),
        TRANSACTIONS_TABLE("all-transactions-table"),
        TRANSACTIONS_SEARCH("all-txn-search-input"),
        TRANSACTIONS_ROW("all-txn-row"),
        NEXT_PAGE("all-txn-pagination-next"),
        LIST_DATE("all-txn-date"),
        LIST_DESCRIPTION("all-txn-description"),
        DATE_HEADER("all-txn-sort-date-header"),
        LIST_CATEGORY("all-txn-category-badge"),
        LIST_AMOUNT("all-txn-amount");

        private final String testId;

        TransactionsElement(String testId) {
            this.testId = testId;
        }

        public By locator() {
            return By.cssSelector(
                "[data-testid='" + testId + "']"
            );
        }
    }

    public enum TransactionsAccount {
        HIGH_YIELD_SAVINGS(
            "acc-savings-1", "High-Yield Savings"
        ),
        EVERYDAY_CHECKING(
            "acc-checking-1", "Everyday Checking"
        );

        private final String accountId;
        private final String displayName;

        TransactionsAccount(String accountId, String displayName) {
            this.accountId = accountId;
            this.displayName = displayName;
        }

        public By locator() {
            return By.cssSelector(
                "[data-testid='all-txn-account-option']" +
                "[data-account-id='" + accountId + "']"
            );
        }

        public String getAccountId() {
            return accountId;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Verify these locators against the actual HTML
    private final By accountFilter =
        By.cssSelector("[data-testid='all-txn-account-select']");

    private final By dateHeader =
        By.xpath("//th[normalize-space(.)='Date']");

    private final By nextButton =
        By.cssSelector("[data-testid='all-txn-next-page']");

    private final By emptyState =
        By.cssSelector("[data-testid='no-all-transactions-message']");


    // Confirmed from the supplied button HTML
    private final By debitsButton =
        By.xpath("//button[normalize-space(.)='Debits']");

    private WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isTransactionsElementVisible(
            TransactionsElement element) {
        return isDisplayed(element.locator());
    }

    public List<WebElement> getVisibleTransactionRows() {
        return driver.findElements(
            TransactionsElement.TRANSACTIONS_ROW.locator()
        ).stream()
            .filter(WebElement::isDisplayed)
            .collect(Collectors.toList());
    }

    public int getVisibleTransactionCount() {
        return getVisibleTransactionRows().size();
    }

    public List<String> getTableHeaders() {
        return driver.findElements(
            By.cssSelector(
                "[data-testid='all-transactions-table'] thead th"
            )
        ).stream()
            .map(WebElement::getText)
            .map(String::trim)
            .collect(Collectors.toList());
    }

    public By getTransactionRow(String transactionId) {
        return By.cssSelector(
            "[data-testid='all-txn-row']" +
            "[data-transaction-id='" + transactionId + "']"
        );
    }

    public String getTransactionAccountName(String transactionId) {
        return find(getTransactionRow(transactionId))
            .findElement(By.cssSelector("td:nth-child(2)"))
            .getText().trim();
    }

    public String getTransactionField(
            String transactionId,
            TransactionsElement field) {

        return find(getTransactionRow(transactionId))
            .findElement(field.locator())
            .getText().trim();
    }

    public boolean isTransactionDisplayed(String transactionId) {
        return isDisplayed(getTransactionRow(transactionId));
    }

    // Account filter
    public void selectAccount(TransactionsAccount account) {
        click(accountFilter);
        click(account.locator());
    }

    public boolean areTransactionsFromAccount(
            TransactionsAccount account) {

        List<WebElement> rows = getVisibleTransactionRows();

        return !rows.isEmpty() && rows.stream()
            .allMatch(row ->
                account.getAccountId().equals(
                    row.getDomAttribute("data-account-id")
                )
            );
    }

    // Search
    public void searchTransactions(String keyword) {
        set(
            TransactionsElement.TRANSACTIONS_SEARCH.locator(),
            keyword
        );
    }

    public List<String> getVisibleTransactionDescriptionsAndCategories() {
        return getVisibleTransactionRows().stream()
            .map(row -> {
                String description = row.findElement(
                    TransactionsElement.LIST_DESCRIPTION.locator()
                ).getText();

                String category = row.findElement(
                    TransactionsElement.LIST_CATEGORY.locator()
                ).getText();

                return description + " " + category;
            })
            .collect(Collectors.toList());
    }

    // Date sorting
    public void clickDateHeader() {
        click(TransactionsElement.DATE_HEADER.locator());
    }

    public List<LocalDate> getVisibleTransactionDates() {
        return getVisibleTransactionRows().stream()
            .map(row -> row.findElement(
                By.cssSelector(
                    "[data-testid='all-txn-date'] time"
                )
            ).getDomAttribute("datetime"))
            .map(LocalDate::parse)
            .collect(Collectors.toList());
    }

    // Pagination
    public boolean isNextPageEnabled() {
        WebElement button = find(TransactionsElement.NEXT_PAGE.locator());

        return button.isEnabled()
            && !"true".equalsIgnoreCase(
                button.getDomAttribute("aria-disabled")
            );
    }

    public void clickNextPage() {
        click(TransactionsElement.NEXT_PAGE.locator());
    }

    public List<String> getVisibleTransactionIds() {
        return getVisibleTransactionRows().stream()
            .map(row -> row.getDomAttribute(
                "data-transaction-id"
            ))
            .collect(Collectors.toList());
    }

    public boolean waitForDifferentTransactionPage(
            List<String> previousIds) {

        return getWait().until(driver -> {
            List<String> currentIds =
                getVisibleTransactionIds();

            return !currentIds.isEmpty()
                && !currentIds.equals(previousIds);
        });
    }
    public void waitForTransactionRows() {

        WebDriverWait wait = new WebDriverWait(
            driver,
            Duration.ofSeconds(10)
        );

        wait.until(webDriver ->
            webDriver.findElements(
                TransactionsElement.TRANSACTIONS_ROW.locator()
            ).stream().anyMatch(WebElement::isDisplayed)
        );
    }
    public void debugTransactionRows() {

        List<WebElement> rows = driver.findElements(
            TransactionsElement.TRANSACTIONS_ROW.locator()
        );

        System.out.println("[DEBUG] Current URL: " + driver.getCurrentUrl());
        System.out.println("[DEBUG] Total Rows Found: " + rows.size());

        for (WebElement row : rows) {
            System.out.println(
                "[DEBUG] Transaction ID: " +
                row.getDomAttribute("data-transaction-id") +
                " | Displayed: " + row.isDisplayed()
            );
        }
}
    // Empty state
    public boolean isEmptyStateDisplayed() {
        return getWait().until(
            ExpectedConditions.visibilityOfElementLocated(
                emptyState
            )
        ).isDisplayed();
    }

    // Debits filter
    public void clickDebitsFilter() {
        click(debitsButton);
    }

    public boolean isDebitsFilterActive() {
        return "true".equalsIgnoreCase(
            find(debitsButton).getDomAttribute("aria-pressed")
        );
    }

    public boolean areAllVisibleTransactionsDebits() {
        List<WebElement> rows = getVisibleTransactionRows();

        return !rows.isEmpty() && rows.stream()
            .allMatch(row ->
                "debit".equalsIgnoreCase(
                    row.getDomAttribute("data-type")
                )
            );
    }
}
