import org.junit.Test;
import static org.junit.Assert.*;

public class BankTest {

    @Test
    public void testAuthenticateValidUser() {
        // Setup - Create objects we need
        ATMStatus atmStatus = new ATMStatus();
        Bank bank = new Bank(atmStatus);

        // Execute - Test the method
        boolean result = bank.authenticate("user1", "1234");

        // Verify - Check if result is correct
        assertTrue("Valid credentials should authenticate", result);
    }
}