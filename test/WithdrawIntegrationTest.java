import org.junit.Test;
import static org.junit.Assert.*;

public class WithdrawIntegrationTest {

    @Test
    public void testSuccessfulWithdrawal() {
        // Setup
        ATMStatus atmStatus = new ATMStatus();
        Bank bank = new Bank(atmStatus);

        String username = "user1";
        double initialBalance = bank.checkBalance(username);
        double withdrawAmount = 50.0;

        // Execute
        boolean result = bank.withdraw(username, withdrawAmount);

        // Verify+




































































        assertTrue("Withdrawal should succeed", result);
        assertEquals("Balance should decrease by 50",
                initialBalance - 50.0,
                bank.checkBalance(username),
                0.01);
    }

    @Test
    public void testWithdrawalInsufficientBalance() {
        ATMStatus atmStatus = new ATMStatus();
        Bank bank = new Bank(atmStatus);

        String username = "user1";
        double initialBalance = bank.checkBalance(username);

        // Try to withdraw more than balance
        boolean result = bank.withdraw(username, 99999.0);

        assertFalse("Withdrawal should fail", result);
        assertEquals("Balance should not change",
                initialBalance,
                bank.checkBalance(username),
                0.01);
    }
}
