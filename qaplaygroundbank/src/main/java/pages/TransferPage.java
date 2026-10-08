package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;
import pages.TransferPage.TransferButtons;
import pages.TransferPage.TransferField;

public class TransferPage extends BasePage {

    private By errorMessage = By.xpath("//*[@data-testid='transfer-error-message']");
    private By billPaymentDateInput = By.id("transfer-scheduled-date");
    private By transferDateButton = By.cssSelector("[data-testid='date-type-scheduled']");
    private By transferResult = By.cssSelector("[data-testid='transfer-success-heading']");
    private By transferSummary = By.cssSelector("[data-testid='transfer-confirm-summary']");

    public enum TransferButtons {
        SIDEBAR_TRANSFER_BUTTON("sidebar-link-transfer"),
        CANCEL_TRANSFER_BUTTON("cancel-transfer-btn"),
        REVIEW_TRANSFER_BUTTON("review-transfer-btn"),
        CANCEL_REVIEWED_TRANSFER_BUTTON("cancel-confirm-transfer-btn"),
        CONFIRM_TRANSFER_BUTTON("confirm-transfer-btn");

        private final String testId;

        TransferButtons(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }
    public enum TransferField{
        FROM_ACCOUNT("transfer-from-select"),
        TO_ACCOUNT("transfer-to-select"),
        AMOUNT("transfer-amount-input");

        private final String testId;

        TransferField(String testId){
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }
    
    public enum FromAccount {
        HIGH_YIELD_SAVINGS("acc-savings-1", "High-Yield Savings"),
        EVERYDAY_CHECKING("acc-checking-1", "Everyday Checking");   // use your real ids

        private final String accountId;
        private final String displayName;

        FromAccount(String accountId, String displayName) {
            this.accountId = accountId;
            this.displayName = displayName;
        }
        
        By locator() {
            return By.cssSelector("[data-testid='transfer-from-option'][data-account-id='" + accountId + "']");
        }

        public String getDisplayName() {
            return displayName;
        }
        
    }
    public enum ToAccount {
        HIGH_YIELD_SAVINGS("acc-savings-1", "High-Yield Savings"),
        EVERYDAY_CHECKING("acc-checking-1", "Everyday Checking");   

        private final String accountId;
        private final String displayName;

        ToAccount(String accountId, String displayName) {
            this.accountId = accountId;
            this.displayName = displayName;
        }

        By locator() {
            return By.cssSelector("[data-testid='transfer-to-option'][data-account-id='" + accountId + "']");
        }
        public String getDisplayName() {
            return displayName;
        }
    }
    public void clickTransferButtons(TransferButtons action){
        click(action.locator());
    }
    public void clickTransferFields(TransferField action){
        click(action.locator());
    }
    public List<String> getTransferSummary() {
        return getElements(transferSummary);
    }
    public String getTransferSuccessMessage() {
        return find(transferResult).getText().trim();
    }
    public boolean isAccountListed(FromAccount fromAccount, ToAccount toAccount, String amount, String date) {
        return getTransferSummary().stream()
            .anyMatch(row -> row.contains(fromAccount.getDisplayName())
                    && row.contains(toAccount.getDisplayName())
                    && row.contains(amount)
                    && row.contains(date));
    }
    public String getErrorMessage() {
        WebElement error = wait.until(
            ExpectedConditions.visibilityOfElementLocated(errorMessage)
        );
        return error.getText();
    }
    public boolean isTransferFieldDisplayed(TransferField field) {
        return isDisplayed(field.locator());
    }
    //Transfering Account
    public void selectFromAccount(FromAccount accountId){
        clickTransferFields(TransferField.FROM_ACCOUNT);
        click(accountId.locator());
    }
    public void selectToAccount(ToAccount accountId){
        clickTransferFields(TransferField.TO_ACCOUNT);
        click(accountId.locator());
    }
    public void selectAccounts(FromAccount fromAccount,ToAccount toAccount){
        selectFromAccount(fromAccount);
        selectToAccount(toAccount);
    }
    public void enterTransferDetails(FromAccount fromAccount,ToAccount toAccount,String amount){
        selectAccounts(fromAccount,toAccount);
        enterTransferAmount(amount);
    }
    public void enterTransferAmount(String amount){
        set(TransferField.AMOUNT.locator(), amount);
    }
    public void completeTransfer(FromAccount fromAccount,ToAccount toAccount,String amount){
        enterTransferDetails(fromAccount,toAccount,amount);
        clickTransferButtons(TransferButtons.REVIEW_TRANSFER_BUTTON);
        clickTransferButtons(TransferButtons.CONFIRM_TRANSFER_BUTTON);
    }

    //Transfer Date
    public void enterTransferDate(String date){
        click(transferDateButton);
        set(billPaymentDateInput, date);
    }

    public boolean isToAccountUnavailable(ToAccount account) {

    By toAccountOptions =
            By.cssSelector("[data-testid='transfer-to-option']");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(toAccountOptions));

        return driver.findElements(account.locator()).isEmpty();
    }

}
