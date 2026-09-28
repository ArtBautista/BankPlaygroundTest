package pages;

import org.openqa.selenium.By;

import base.BasePage;

public class TransferPage extends BasePage {

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

    public void clickTransferButtons(TransferButtons action){
        click(action.locator());
    }
}
