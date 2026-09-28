import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.TransferPage.FromAccount;
import pages.TransferPage.ToAccount;
import pages.TransferPage.TransferButtons;

public class TransferTests extends BaseTests{

    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
        transferPage.clickTransferButtons(TransferButtons.SIDEBAR_TRANSFER_BUTTON);
    }
    
    @Test
    public void testCancelTransferButton(){
        transferPage.clickTransferButtons(TransferButtons.CANCEL_TRANSFER_BUTTON);
    }
    @Test
    public void testSelectTransfer(){
        transferPage.enterTransferDetails(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "250.00");
        
    }

   
}
