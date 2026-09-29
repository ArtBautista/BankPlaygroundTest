package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.BasePage;

public class TransferPage extends BasePage {

    private By transferFromButton = By.cssSelector("[data-testid='transfer-from-select']");
    private By transfertoButton = By.cssSelector("[data-testid='transfer-to-select']");
    private By transferAmountField = By.id("transfer-amount");
    private By transferReviewButton = By.cssSelector("[data-testid='review-transfer-btn']");
    private By errorMessage = By.xpath("//*[@data-testid='transfer-error-message']");


    public enum TransferButtons {
        SIDEBAR_TRANSFER_BUTTON("sidebar-link-transfer"),
        CANCEL_TRANSFER_BUTTON("cancel-transfer-btn"),
        REVIEW_TRANSFER_BUTTON("review-transfer-btn");
        

        private final String testId;

        TransferButtons(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }

    public enum FromAccount {
        HIGH_YIELD_SAVINGS("acc-savings-1"),
        EVERYDAY_CHECKING("acc-checking-1");   // use your real ids

        // add the other accounts' ids here

        private final String accountId;

        FromAccount(String accountId) {
            this.accountId = accountId;
        }

        By locator() {
            return By.cssSelector("[data-testid='transfer-from-option'][data-account-id='" + accountId + "']");
        }
    }
    public enum ToAccount {
        HIGH_YIELD_SAVINGS("acc-savings-1"),
        EVERYDAY_CHECKING("acc-checking-1");   // use your real ids

        // add the other accounts' ids here

        private final String accountId;

        ToAccount(String accountId) {
            this.accountId = accountId;
        }

        By locator() {
            return By.cssSelector("[data-testid='transfer-to-option'][data-account-id='" + accountId + "']");
        }
    }
    public void clickTransferButtons(TransferButtons action){
        click(action.locator());
    }
    public String getErrorMessage() {
        WebElement error = wait.until(
            ExpectedConditions.visibilityOfElementLocated(errorMessage)
        );
        return error.getText();
    }

    //Transfering Account
    public void selectFromAccount(FromAccount accountId){
        click(transferFromButton);
        click(accountId.locator());
    }
    public void selectToAccount(ToAccount accountId){
        click(transfertoButton);
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
        set(transferAmountField, amount);
    }
    
    public void clickReviewTransfer(){
        click(transferReviewButton);
    }
    
}
