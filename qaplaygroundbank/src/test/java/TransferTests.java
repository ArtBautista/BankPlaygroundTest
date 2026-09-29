import org.testng.Assert;
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
    public void testClickReviewTransferWithoutDetails(){
        transferPage.clickReviewTransfer();   
        Assert.assertTrue(transferPage.getErrorMessage().contains("Please select a From account."));
    }
    @Test
    public void testClickReviewTransferWithoutBalance(){
        transferPage.selectAccounts(FromAccount.HIGH_YIELD_SAVINGS,ToAccount.EVERYDAY_CHECKING);   
        transferPage.clickReviewTransfer();   
        Assert.assertTrue(transferPage.getErrorMessage().contains("Please enter a valid amount."));
    }
    @Test
    public void testClickReviewTransferWithBalanceOnly(){
         transferPage.enterTransferAmount("100.00");
        transferPage.clickReviewTransfer(); 
        Assert.assertTrue(transferPage.getErrorMessage().contains("Please select a From account."));
    }
  @Test
    public void testSelectTransferDetails(){
        transferPage.enterTransferDetails(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "250.00");
    }
}
