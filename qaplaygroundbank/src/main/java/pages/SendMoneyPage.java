package pages;

import org.openqa.selenium.By;

import base.BasePage;


public class SendMoneyPage extends BasePage {

    public enum SendMoneyButtons {
        SIDEBAR_SENDMONEY_BUTTON("sidebar-link-send-money"),
        CANCEL_SENDMONEY_BUTTON("cancel-send-btn"),
        ADD_SENDMONEY_BUTTON("add-payee-btn"),
        REVIEW_SENDMONEY_BUTTON("review-send-btn");
        

        private final String testId;

        SendMoneyButtons(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }

    public void clickSendMoneyButtons(SendMoneyButtons action) {
        click(action.locator());
    }

}
