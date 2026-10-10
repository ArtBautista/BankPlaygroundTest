
import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.BillPayPage;
import pages.BillPayPage.BillPayButtons;
import pages.BillPayPage.BillPayFields;
import pages.BillPayPage.BillerAccount;
import pages.DashboardPage.Sidebar;

public class BillPayTests extends BaseTests {
    
    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");

        dashboardPage.clickSideBarButton(Sidebar.BILL_PAY);
    }

    //TC-BILL-001 - Verify Bill Pay Form Load
    @Test 
    public void testBillPayFields() {
        Assert.assertTrue(billPayPage.isBillPayFieldDisplayed(BillPayFields.FROM_SENDMONEY_BUTTON),"From Account is not displayed");
        Assert.assertTrue(billPayPage.isBillPayFieldDisplayed(BillPayFields.BILLER_SENDMONEY_BUTTON),"Biller combobox/dropdown is not displayed");
    }
    //TC-BILL-002 - Verify Biller Selection
    @Test
    public void testSearchAndSelectBiller() {

        // Step 1-3: Search and select City Electric
        billPayPage.selectBillerAccount(
            BillerAccount.CITY_ELECTRIC
        );
        System.out.println("Selected Biller: " + billPayPage.getSelectedBiller());
        
        
        Assert.assertEquals(
            billPayPage.getSelectedBiller(),
            BillerAccount.CITY_ELECTRIC.getDisplayName(),
            "Selected biller does not match the expected biller."
        );
    }
    //TC-BILL-003 - Verify Add New Biller
    @Test
    public void testAddNewBiller() {

        billPayPage.completeAddNewBiller("Lila", "LLA-0767");
        
        Assert.assertEquals(
            billPayPage.getSelectedBiller(),
            "Lila",
            "Selected biller does not match the expected biller."
        );

        
    }
    //TC-BILL-004 - Verify Successful Bill Payment
    @Test
    public void testSuccessfulBillPayment() {
        
        billPayPage.completeReview(BillPayPage.FromAccount.HIGH_YIELD_SAVINGS, BillerAccount.CITY_ELECTRIC, "100.00");
        billPayPage.clickBillPayButtons(BillPayButtons.REVIEW_PAYMENT_BUTTON);
        billPayPage.clickBillPayButtons(BillPayButtons.CONFIRM_BILLER_BUTTON);
        
        Assert.assertTrue(
            billPayPage.isButtonDisplayed(BillPayButtons.PAYMENT_SUCCESS),
            "Bill payment was not successfully submitted"
        );
    }
    
    //TC-BILL-006 - Verify Insufficient Funds
    @Test
    public void testInsufficientFundsValidation() {
        
        BigDecimal balance = billPayPage.selectFromAccountAndGetBalance(BillPayPage.FromAccount.HIGH_YIELD_SAVINGS);
        BigDecimal insufficientAmount = balance.add(new BigDecimal("100.00"));

        billPayPage.completeReview(BillPayPage.FromAccount.HIGH_YIELD_SAVINGS, BillerAccount.CITY_ELECTRIC, insufficientAmount.toPlainString());

        billPayPage.clickBillPayButtons(BillPayButtons.REVIEW_PAYMENT_BUTTON);
        billPayPage.clickBillPayButtons(BillPayButtons.CONFIRM_BILLER_BUTTON);

        
        Assert.assertTrue(
            billPayPage.isButtonDisplayed(BillPayButtons.PAYMENT_ERROR),
            "Insufficient funds error was not displayed."
        );
    }

}
