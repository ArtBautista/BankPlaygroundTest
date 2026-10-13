
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.DashboardPage;
import pages.TransactionsPage.TransactionsAccount;
import pages.TransactionsPage.TransactionsElement;

public class TransactionsTests extends BaseTests {

    @BeforeMethod
    public void loginUser() {
        loginPage.logIntoApplication(
            "standard_user",
            "bank_sauce"
        );

        dashboardPage.clickSideBarButton(
            DashboardPage.Sidebar.TRANSACTIONS
        );
    }

    
    @Test
    public void testTransactionsTableColumns() {

        System.out.println("\n========== TRANSACTIONS TABLE TEST ==========");

        boolean isTableVisible =
            transactionsPage.isTransactionsElementVisible(
                TransactionsElement.TRANSACTIONS_TABLE
            );

        System.out.println(
            "[DEBUG] Transactions Table Visible: " + isTableVisible
        );

        Assert.assertTrue(
            isTableVisible,
            "Transactions table is not visible."
        );

        List<String> headers =
            transactionsPage.getTableHeaders();

        System.out.println("\n[DEBUG] Actual Table Headers:");
        headers.forEach(header ->
            System.out.println("  - " + header)
        );

        String[] expectedHeaders = {
            "Date",
            "Description",
            "Category",
            "Amount"
        };

        System.out.println("\n[DEBUG] Validating Expected Headers:");

        for (String expected : expectedHeaders) {

            boolean isPresent = headers.stream()
                .map(String::trim)
                .anyMatch(header ->
                    header.equalsIgnoreCase(expected)
                    || header.startsWith(expected + " ")
                );

            System.out.println(
                "[DEBUG] Expected: " + expected +
                " | Found: " + isPresent
            );

            Assert.assertTrue(
                isPresent,
                "Missing transaction column: " + expected +
                " | Actual headers: " + headers
            );
        }

        System.out.println(
            "\n[PASS] All expected transaction columns are present."
        );
    }

    @Test
    public void testAccountFilter() {

        TransactionsAccount account =
            TransactionsAccount.EVERYDAY_CHECKING;

        transactionsPage.selectAccount(account);

        Assert.assertTrue(
            transactionsPage.areTransactionsFromAccount(account),
            "Transactions do not all belong to " +
            account.getDisplayName()
        );
    }

    @Test
    public void testTransactionSearch() {

        String keyword = "Transfer";

        transactionsPage.searchTransactions(keyword);

        List<String> results =
            transactionsPage
                .getVisibleTransactionDescriptionsAndCategories();

        Assert.assertFalse(
            results.isEmpty(),
            "No transactions found for: " + keyword
        );

        for (String result : results) {
            Assert.assertTrue(
                result.toLowerCase().contains(
                    keyword.toLowerCase()
                ),
                "Unexpected search result: " + result
            );
        }
    }

    @Test
    public void testTransactionDateSorting() {

        // Ascending
        transactionsPage.clickDateHeader();

        List<LocalDate> ascending =
            transactionsPage.getVisibleTransactionDates();

        Assert.assertFalse(
            ascending.isEmpty(),
            "No transactions available for sorting."
        );

        List<LocalDate> expectedAscending =
            new ArrayList<>(ascending);

        Collections.sort(expectedAscending);

        Assert.assertEquals(
            ascending,
            expectedAscending,
            "Transactions are not sorted by ascending date."
        );

        // Descending
        transactionsPage.clickDateHeader();

        List<LocalDate> descending =
            transactionsPage.getVisibleTransactionDates();

        List<LocalDate> expectedDescending =
            new ArrayList<>(descending);

        expectedDescending.sort(Collections.reverseOrder());

        Assert.assertEquals(
            descending,
            expectedDescending,
            "Transactions are not sorted by descending date."
        );
    }

    
    
@Test
public void testTransactionPagination() {

    System.out.println(
        "\n========== TRANSACTION PAGINATION TEST =========="
    );

    // Step 1: Debug initial state
    transactionsPage.debugTransactionRows();

    // Step 2: Wait for transaction rows to load
    transactionsPage.waitForTransactionRows();

    // Step 3: Retrieve first page IDs
    List<String> firstPageIds =
        transactionsPage.getVisibleTransactionIds();

    System.out.println("\n[DEBUG] PAGE 1");
    System.out.println("[DEBUG] Total Transactions: " +
        firstPageIds.size());

    firstPageIds.forEach(id ->
        System.out.println("[DEBUG] Transaction ID: " + id)
    );

    Assert.assertFalse(
        firstPageIds.isEmpty(),
        "No transactions displayed on page 1."
    );

    // Step 4: Verify Next button
    boolean nextEnabled =
        transactionsPage.isNextPageEnabled();

    System.out.println(
        "[DEBUG] Next Button Enabled: " + nextEnabled
    );

    Assert.assertTrue(
        nextEnabled,
        "Next pagination button is disabled."
    );

    // Step 5: Navigate to page 2
    System.out.println("[ACTION] Clicking Next...");
    transactionsPage.clickNextPage();

    // Step 6: Wait for different transactions
    Assert.assertTrue(
        transactionsPage.waitForDifferentTransactionPage(
            firstPageIds
        ),
        "Page 2 did not display different transactions."
    );

    // Step 7: Validate second page
    List<String> secondPageIds =
        transactionsPage.getVisibleTransactionIds();

    System.out.println("\n[DEBUG] PAGE 2");
    System.out.println("[DEBUG] Total Transactions: " +
        secondPageIds.size());

    secondPageIds.forEach(id ->
        System.out.println("[DEBUG] Transaction ID: " + id)
    );

    List<String> duplicates =
        new ArrayList<>(firstPageIds);

    duplicates.retainAll(secondPageIds);

    System.out.println("[DEBUG] Duplicate IDs: " + duplicates);

    Assert.assertTrue(
        duplicates.isEmpty(),
        "Duplicate transactions found: " + duplicates
    );

    System.out.println(
        "\n[PASS] Transaction pagination works correctly."
    );
}



    @Test
    public void testTransactionsEmptyState() {

        transactionsPage.searchTransactions(
            "XYZ123NonExistent"
        );

        Assert.assertTrue(
            transactionsPage.isEmptyStateDisplayed(),
            "No transactions found message is missing."
        );

        Assert.assertEquals(
            transactionsPage.getVisibleTransactionCount(),
            0,
            "Transactions remain visible after an unmatched search."
        );
    }

    @Test
    public void testDebitsFilter() {

        transactionsPage.clickDebitsFilter();

        Assert.assertTrue(
            transactionsPage.isDebitsFilterActive(),
            "Debits filter is not active."
        );

        Assert.assertTrue(
            transactionsPage.areAllVisibleTransactionsDebits(),
            "Non-debit transactions are displayed."
        );
    }
}
