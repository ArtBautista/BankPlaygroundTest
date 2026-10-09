
import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.SendMoneyPage;
import pages.SendMoneyPage.FromAccount;
import pages.SendMoneyPage.PayeeAccount;
import pages.SendMoneyPage.SendMoneyButtons;

public class SendMoneyTests extends BaseTests{
    
    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
        sendMoneyPage.clickSendMoneyButtons(SendMoneyButtons.SIDEBAR_SENDMONEY_BUTTON);
    }

    @Test
    public void testEnterSendMoneyDetails(){
        sendMoneyPage.enterSendMoneyDetails(FromAccount.EVERYDAY_CHECKING, PayeeAccount.BANK_OF_AMERICA,"250.00");
    }
    // TC-SEND-001 - Verify Send Money Form Load
    @Test
    public void testSendMoneyForm(){
        
        Assert.assertTrue(sendMoneyPage.isSendMoneyFieldDisplayed(SendMoneyPage.SendMoneyFields.FROM_SENDMONEY_BUTTON),"From Account is not displayed.");
        Assert.assertTrue(sendMoneyPage.isSendMoneyButtonsDisplayed(SendMoneyButtons.ADD_SENDMONEY_BUTTON),"Add a new payee option is not displayed.");
    }
    // TC-SEND-002 - Verify Add New Payee Validation
    @Test
    public void testAddNewPayee(){
        sendMoneyPage.enterAddNewDetails("Eliee Newman", "Magnus Pride", "674923486", "297109791149288651");
        sendMoneyPage.clickSendMoneyButtons(SendMoneyButtons.SAVE_SENDMONEY_BUTTON);
        Assert.assertEquals(
            sendMoneyPage.getSAddNewPayeeMessage(),
            "Account number must be 8–17 digits.",
            "Account Number Is more than 17 digits."
        );
    }

    // TC-SEND-003 - Verify Successful Send Money
    @Test
    public void testSuccessfulSendMoney(){
        sendMoneyPage.compeleteSendMoney(FromAccount.EVERYDAY_CHECKING, PayeeAccount.BANK_OF_AMERICA,"25.00");
        Assert.assertEquals(
            sendMoneyPage.getSendMoneySuccessMessage(),
            "Money Sent Successfully",
            "Unable to Send Money."
        );
    }
    // TC-SEND-004 - Verify Insufficient Funds
    @Test
    public void testInsufficientFundsValidation() {

        // Step 1: Select From Account and retrieve balance
        BigDecimal balance = sendMoneyPage
            .selectFromAccountAndGetBalance(
                FromAccount.EVERYDAY_CHECKING
            );

        // Step 2: Select existing payee
        sendMoneyPage.selectPayeeAccount(
            PayeeAccount.CHASE_BANK
        );

        // Step 3: Generate amount greater than balance
        BigDecimal insufficientAmount = balance.add(
            new BigDecimal("100.00")
        );

        sendMoneyPage.enterSendMoneyAmount(
            insufficientAmount.toPlainString()
        );

        // Step 4: Click Review & Send
        sendMoneyPage.clickSendMoneyButtons(
            SendMoneyButtons.REVIEW_SENDMONEY_BUTTON
        );
        sendMoneyPage.clickSendMoneyButtons(
            SendMoneyButtons.CONFIRM_SENDMONEY_BUTTON
        );

        // Step 5: Assert insufficient funds error
        Assert.assertTrue(
            sendMoneyPage.isInsufficientFundsDisplayed(),
            "Insufficient funds error was not displayed."
        );

    }
    // TC-SEND-005 - 
    @Test 
    public void TestFrozenAccount(){
        dashboardPage.clickLogoutButton();
        loginPage.logIntoApplication("frozen_user", "bank_sauce");
        sendMoneyPage.clickSendMoneyButtons(SendMoneyButtons.SIDEBAR_SENDMONEY_BUTTON);

        Assert.assertTrue(
            sendMoneyPage.isFrozenAccountDisplayed(),
            "Sending money is disabled banner was not displayed."
        );
        Assert.assertFalse(
            sendMoneyPage.isSendMoneyButtonEnabled(SendMoneyButtons.REVIEW_SENDMONEY_BUTTON),
            "Review & Send button should be disabled "
            + "when the amount exceeds the account balance."
        );
    }
}
