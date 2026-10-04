
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTests;
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
}
