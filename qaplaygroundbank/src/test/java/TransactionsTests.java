
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.DashboardPage.Sidebar;
import pages.TransactionsPage.TransactionsElement;

public class TransactionsTests extends BaseTests{

    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
        dashboardPage.clickSideBarButton(Sidebar.TRANSACTIONS);
    }

    

    @Test
    public void testTransactionDetails() {

        String transactionId = "txn-1022";

        Assert.assertTrue(transactionsPage.isTransactionDisplayed(transactionId),"Transaction was not found.");

        Assert.assertEquals(transactionsPage.getTransactionAccountName(transactionId),"Everyday Checking");

        Assert.assertEquals(transactionsPage.getTransactionField(transactionId,
                TransactionsElement.LIST_DESCRIPTION
            ),
            "Bill Pay — City Electric Co."
        );

        Assert.assertEquals(
            transactionsPage.getTransactionField(transactionId,TransactionsElement.LIST_CATEGORY
            ),
            "Utilities");

        Assert.assertEquals(transactionsPage.getTransactionField(transactionId,TransactionsElement.LIST_AMOUNT),"-$100.00");
    }
}
