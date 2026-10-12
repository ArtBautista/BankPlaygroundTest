
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseTests;
import pages.ApplyLoanPage;
import pages.ApplyLoanPage.ApplyLoanAccount;
import pages.ApplyLoanPage.ApplyLoanElement;
import pages.ApplyLoanPage.ApplyLoanFields;
import pages.ApplyLoanPage.ApplyLoanTerm;
import pages.ApplyLoanPage.ApplyLoanType;
import pages.DashboardPage;

public class ApplyLoanTests extends BaseTests{

    


    @BeforeMethod
    public void loginUser(Method method) {

        String username;

        switch (method.getName()) {
            case "testIncorrectLoanHistoryTotal":
                username = "error_user";
                break;

            case "testFrozenAccount":
                username = "frozen_user";
                break;

            default:
                username = "standard_user";
                break;
        }

        loginPage.logIntoApplication(
            username,
            "bank_sauce"
        );

        dashboardPage.clickSideBarButton(
            DashboardPage.Sidebar.APPLY_LOAN
        );
    }



    @Test
    public void testApplyLoanForm() {

        applyLoanPage.clickApplyLoanElement( ApplyLoanElement.APPLY_LOAN_BUTTON);

        ApplyLoanFields[] requiredFields = {
            ApplyLoanFields.DIALOG,
            ApplyLoanFields.TYPE,
            ApplyLoanFields.AMOUNT,
            ApplyLoanFields.TERM,
            ApplyLoanFields.INTEREST,
            ApplyLoanFields.ACCOUNT,
            ApplyLoanFields.LOAN_TYPE
        };

        SoftAssert softAssert = new SoftAssert();

        for (ApplyLoanFields field : requiredFields) {
            softAssert.assertTrue(
                applyLoanPage.isApplyLoanFieldDisplayed(field),
                field + " should be displayed"
            );
        }

        softAssert.assertAll();
    }
    @Test 
    public void testMaximumLoanAmount(){
        String amount = "300000";
        applyLoanPage.clickApplyLoanElement( ApplyLoanElement.APPLY_LOAN_BUTTON);
    
        applyLoanPage.enterLoanAmount(amount);
        applyLoanPage.selectType(ApplyLoanPage.ApplyLoanType.HOME);
        applyLoanPage.clickApplyLoanElement(ApplyLoanElement.REVIEW);
        Assert.assertEquals(applyLoanPage.getLoanErrorDisplayed(), "Loan amount cannot exceed $250,000.");
    }
    
    @Test
    public void testSuccessfulLoan() {

        String amount = "5000";
        ApplyLoanType loanType = ApplyLoanType.PERSONAL;

        
        applyLoanPage.clickApplyLoanElement(ApplyLoanElement.APPLY_LOAN_BUTTON);

        
        applyLoanPage.selectType(loanType);
        applyLoanPage.enterLoanAmount(amount);
        applyLoanPage.selectTerm(ApplyLoanTerm.MONTHS_24);
        applyLoanPage.enterLoanPurpose("Home renovation and improvements");
        applyLoanPage.selectAccount(ApplyLoanAccount.HIGH_YIELD_SAVINGS);

       
        applyLoanPage.clickApplyLoanElement(ApplyLoanElement.REVIEW);
        applyLoanPage.clickApplyLoanElement(ApplyLoanElement.CONFIRM_BUTTON);

        Assert.assertEquals(
            applyLoanPage.getLoanSuccessDisplayed(),
            "Application Submitted",
            "Loan application was not submitted successfully."
        );

        
        dashboardPage.clickSideBarButton(DashboardPage.Sidebar.APPLY_LOAN);

        
        Assert.assertTrue(
            applyLoanPage.waitForLoanInHistory(
                loanType,
                amount,
                "pending"
            ),
            "Newly submitted Personal loan of $" + amount +
            " was not found in Applied Loans history."
        );
    }
    
    @Test
    public void testIncorrectLoanHistoryTotal() {

        BigDecimal displayedTotal =
            applyLoanPage.getDisplayedLoanTotal();

        BigDecimal calculatedTotal = applyLoanPage.getCalculatedLoanTotal();

        // Get most recent loan amount
        BigDecimal latestLoanAmount =
            applyLoanPage.getMostRecentLoanAmount();
        System.out.println("Calculated LOAN AMOUNT: " + calculatedTotal);

        System.out.println("Displayed LOAN AMOUNT: " + displayedTotal);
        System.out.println("LATEST LOAN AMOUNT: " + latestLoanAmount);
        System.out.println("SUBTRACTED AMOUNT: " + calculatedTotal.subtract(displayedTotal));
        // Step 5: Verify known bug
        Assert.assertNotEquals(
            displayedTotal,
            calculatedTotal,
            "Expected a mismatch, but the displayed total is correct."
        );   
    }
    @Test 
    public void testFrozenAccount(){
        Assert.assertEquals(applyLoanPage.getLoanFrozenDisplayed(),
         "Loan applications are disabled while your account is frozen.");
         
        Assert.assertFalse(
            applyLoanPage.isApplyLoanButtonEnabled(ApplyLoanElement.APPLY_LOAN_BUTTON),
            "Apply for Loan should be disabled "
            + "when the amount exceeds the account balance."
        );
    
    }
}
