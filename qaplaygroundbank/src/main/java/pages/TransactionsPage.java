
package pages;

import org.openqa.selenium.By;

import base.BasePage;

public class TransactionsPage extends BasePage {

    public enum TransactionsElement {
        TRANSACTIONS_TABLE("all-transactions-table"),
        TRANSACTIONS_ROW("all-txn-row"),
        LIST_DATE("all-txn-date"),
        LIST_DESCRIPTION("all-txn-description"),
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

    // Verify transaction element visibility
    public boolean isTransactionsElementVisible(
            TransactionsElement element) {
        return isDisplayed(element.locator());
    }

    // Locate a specific transaction row
    public By getTransactionRow(String transactionId) {
        return By.cssSelector(
            "[data-testid='all-txn-row']" +
            "[data-transaction-id='" + transactionId + "']"
        );
    }

    // Retrieve account name from a transaction
    public String getTransactionAccountName(String transactionId) {
        By accountLocator = By.cssSelector(
            "[data-testid='all-txn-row']" +
            "[data-transaction-id='" + transactionId + "']" +
            " td:nth-child(2)"
        );

        return find(accountLocator).getText().trim();
    }

    // Retrieve a specific field from a transaction
    public String getTransactionField(
            String transactionId,
            TransactionsElement field) {

        By fieldLocator = By.cssSelector(
            "[data-testid='all-txn-row']" +
            "[data-transaction-id='" + transactionId + "'] " +
            "[data-testid='" + field.testId + "']"
        );

        return find(fieldLocator).getText().trim();
    }

    // Verify a specific transaction exists
    public boolean isTransactionDisplayed(String transactionId) {
        return isDisplayed(getTransactionRow(transactionId));
    }
}
