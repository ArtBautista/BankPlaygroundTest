import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.DashboardPage;
import pages.TransferPage.FromAccount;
import pages.TransferPage.ToAccount;
import pages.TransferPage.TransferButtons;
import pages.TransferPage.TransferField;
public class TransferTests extends BaseTests{

    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
        transferPage.clickTransferButtons(TransferButtons.SIDEBAR_TRANSFER_BUTTON);
    }
    // TC-XFER-001 - Verify Internal Transfer Form Load
    @Test
    public void testTransferFeildIsVisible(){
        Assert.assertTrue(transferPage.isTransferFieldDisplayed(TransferField.AMOUNT),"Amount input is not displayed.");
    }
    // TC-XFER-002 - Verify Same Account Validation
    @Test
    public void testSameAccount() {

        transferPage.selectFromAccount(FromAccount.EVERYDAY_CHECKING);
        transferPage.clickTransferFields(TransferField.TO_ACCOUNT);

        Assert.assertTrue(
            transferPage.isToAccountUnavailable(
                ToAccount.EVERYDAY_CHECKING
            ),
            "Everyday Checking should not be available in the To Account dropdown."
        );
    }
    // TC-XFER-003 - Verify Insufficient Funds Validation
    @Test
    public void testInsufficientFunds() {
        transferPage.enterTransferDetails(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "15000.00");
        transferPage.clickTransferButtons(TransferButtons.REVIEW_TRANSFER_BUTTON);
        transferPage.clickTransferButtons(TransferButtons.CONFIRM_TRANSFER_BUTTON);
    
        Assert.assertEquals(
            transferPage.getErrorMessage(),
            "Insufficient funds. Available balance: $12,800.00.",
            "Transfer Amount Exceeds Available Balance" 
        );
        
    }
    // TC-XFER-004 - Verify Successful Internal Transfer
    @Test
    public void testSuccessfulTransfer() {
        transferPage.completeTransfer(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "250.00");
        List<String> transferSummary = transferPage.getTransferSummary();
        System.out.println("Transfer summary on page:");
        transferSummary.forEach(System.out::println);
        
        String today = LocalDate.now().toString();
        System.out.println("Todays date: " + today);
        Assert.assertTrue(
            transferPage.isAccountListed(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "250.00", today),
            "Transfer summary did not match the entered details. Rows: " + transferSummary
        );
         Assert.assertEquals(
            transferPage.getTransferSuccessMessage(),
            "Transfer Successful",
            "Transfer success confirmation message does not match."
        );
    }
    //TC-XFER-005 -  Verify Scheduled Transfer Flow
    @Test
    public void testSelectTransferDetailsPast() {
        transferPage.enterTransferDetails(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "250.00");
        transferPage.enterTransferDate("08-08-2026"); // yyyy-MM-dd, matches the native date input format
        transferPage.clickTransferButtons(TransferButtons.REVIEW_TRANSFER_BUTTON);

        Assert.assertEquals(
            transferPage.getErrorMessage(),
            "Transfer date cannot be in the past.",
            "Transfer date cannot be in the past." 
        );
    }
    // TC-XFER-006 - Verify Successful Internal Transfer
    @Test
    public void testBalancesUpdate() {
        String fromAccount = "High-Yield Savings";
        String toAccount = "Everyday Checking";
        BigDecimal transferAmount = new BigDecimal("10.00");

        dashboardPage.clickSideBarButton(DashboardPage.Sidebar.ACCOUNTS);

        BigDecimal fromBalanceBefore =
            accountsPage.getAccountBalance(fromAccount);

        BigDecimal toBalanceBefore =
                accountsPage.getAccountBalance(toAccount);

        System.out.println("From Balance Before: $" + fromBalanceBefore);
        System.out.println("To Balance Before: $" + toBalanceBefore);

        dashboardPage.clickSideBarButton(DashboardPage.Sidebar.TRANSFER);

        transferPage.completeTransfer(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "10");

        dashboardPage.clickSideBarButton(DashboardPage.Sidebar.ACCOUNTS);

        BigDecimal fromBalanceAfter =
            accountsPage.getAccountBalance(fromAccount);
        BigDecimal toBalanceAfter =
                accountsPage.getAccountBalance(toAccount);

        Assert.assertTrue(
            fromBalanceBefore.subtract(fromBalanceAfter)
                    .compareTo(transferAmount) == 0,
            "From account did not decrease by $10."
        );

        Assert.assertTrue(
            toBalanceAfter.subtract(toBalanceBefore)
                    .compareTo(transferAmount) == 0,
            "To account did not increase by $10."
        );

    }

    @Test
    public void testCancelTransferButton(){
        transferPage.clickTransferButtons(TransferButtons.CANCEL_TRANSFER_BUTTON);
    }

    //Submit Without Details
    @Test
    public void testClickReviewTransferWithoutDetails(){
        transferPage.clickTransferButtons(TransferButtons.REVIEW_TRANSFER_BUTTON);  
        Assert.assertTrue(transferPage.getErrorMessage().contains("Please select a From account."));
    }
    //Submit Without Details
    @Test
    public void testClickReviewTransferWithoutBalance(){
        transferPage.selectAccounts(FromAccount.HIGH_YIELD_SAVINGS,ToAccount.EVERYDAY_CHECKING);   
        transferPage.clickTransferButtons(TransferButtons.REVIEW_TRANSFER_BUTTON);   
        Assert.assertTrue(transferPage.getErrorMessage().contains("Please enter a valid amount."));
    }
    //Submit Without To Account
    @Test
    public void testClickReviewTransferWithoutToAccount(){
        transferPage.selectFromAccount(FromAccount.HIGH_YIELD_SAVINGS);   
        transferPage.clickTransferButtons(TransferButtons.REVIEW_TRANSFER_BUTTON);   
        Assert.assertTrue(transferPage.getErrorMessage().contains("Please select a To account."));
    }
    @Test
    public void testClickReviewTransferWithBalanceOnly(){
         transferPage.enterTransferAmount("100.00");
        transferPage.clickTransferButtons(TransferButtons.REVIEW_TRANSFER_BUTTON); 
        Assert.assertTrue(transferPage.getErrorMessage().contains("Please select a From account."));
    }
    @Test
    public void testSelectTransferDetailsToday() {
        transferPage.enterTransferDetails(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "250.00");
        transferPage.clickTransferButtons(TransferButtons.REVIEW_TRANSFER_BUTTON);

        List<String> transferSummary = transferPage.getTransferSummary();
        System.out.println("Transfer summary on page:");
        transferSummary.forEach(System.out::println);
        
        String today = LocalDate.now().toString();
        System.out.println("Todays date: " + today);

        Assert.assertTrue(
            transferPage.isAccountListed(
                FromAccount.HIGH_YIELD_SAVINGS, 
                ToAccount.EVERYDAY_CHECKING, 
                "250.00", 
                today),
            "Transfer summary did not match the entered details. Rows: " + transferSummary
        );
    }
    @Test
    public void testSelectTransferDetailsForLater() {
        transferPage.enterTransferDetails(FromAccount.HIGH_YIELD_SAVINGS, ToAccount.EVERYDAY_CHECKING, "250.00");
        transferPage.enterTransferDate("11-11-2026"); // yyyy-MM-dd, matches the native date input format
        transferPage.clickTransferButtons(TransferButtons.REVIEW_TRANSFER_BUTTON);

        
        List<String> transferSummary = transferPage.getTransferSummary();
        System.out.println("Transfer summary on page:");
        transferSummary.forEach(System.out::println);

        Assert.assertTrue(
            transferPage.isAccountListed(
                FromAccount.HIGH_YIELD_SAVINGS, 
                ToAccount.EVERYDAY_CHECKING, "250.00", 
                "2026-11-11"),
            "Transfer summary did not match the entered details. Rows: " + transferSummary
        );
    }
}
