import java.time.LocalDate;
import java.util.List;

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

    //Submit Without Details
    @Test
    public void testClickReviewTransferWithoutDetails(){
        transferPage.clickReviewTransfer();   
        Assert.assertTrue(transferPage.getErrorMessage().contains("Please select a From account."));
    }
    //Submit Without Details
    @Test
    public void testClickReviewTransferWithoutBalance(){
        transferPage.selectAccounts(FromAccount.HIGH_YIELD_SAVINGS,ToAccount.EVERYDAY_CHECKING);   
        transferPage.clickReviewTransfer();   
        Assert.assertTrue(transferPage.getErrorMessage().contains("Please enter a valid amount."));
    }
    //Submit Without To Account
    @Test
    public void testClickReviewTransferWithoutToAccount(){
        transferPage.selectFromAccount(FromAccount.HIGH_YIELD_SAVINGS);   
        transferPage.clickReviewTransfer();   
        Assert.assertTrue(transferPage.getErrorMessage().contains("Please select a To account."));
    }
    @Test
    public void testClickReviewTransferWithBalanceOnly(){
         transferPage.enterTransferAmount("100.00");
        transferPage.clickReviewTransfer(); 
        Assert.assertTrue(transferPage.getErrorMessage().contains("Please select a From account."));
    }
    @Test
    public void testSelectTransferDetailsToday() {
        transferPage.enterTransferDetails(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "250.00");
        transferPage.clickReviewTransfer();

        List<String> transferSummary = transferPage.getTransferSummary();
        System.out.println("Transfer summary on page:");
        transferSummary.forEach(System.out::println);
        
        String today = LocalDate.now().toString();
        System.out.println("Todays date: " + today);

        Assert.assertTrue(
            transferPage.isAccountListed(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "250.00", today),
            "Transfer summary did not match the entered details. Rows: " + transferSummary
        );
    }
    @Test
    public void testSelectTransferDetailsForLater() {
        transferPage.enterTransferDetails(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "250.00");
        transferPage.enterTransferDate("11-11-2026"); // yyyy-MM-dd, matches the native date input format
        transferPage.clickReviewTransfer();

        
        List<String> transferSummary = transferPage.getTransferSummary();
        System.out.println("Transfer summary on page:");
        transferSummary.forEach(System.out::println);

        Assert.assertTrue(
            transferPage.isAccountListed(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "250.00", "2026-11-11"),
            "Transfer summary did not match the entered details. Rows: " + transferSummary
        );
    }
}
