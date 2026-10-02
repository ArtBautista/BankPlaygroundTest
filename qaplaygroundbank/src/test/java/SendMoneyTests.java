
import org.testng.annotations.BeforeMethod;

import base.BaseTests;
import pages.SendMoneyPage.SendMoneyButtons;

public class SendMoneyTests extends BaseTests{
    
    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
        sendMoneyPage.clickSendMoneyButtons(SendMoneyButtons.SIDEBAR_SENDMONEY_BUTTON);
    }

}
