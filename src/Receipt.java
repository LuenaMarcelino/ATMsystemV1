import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Receipt {

    public static void print(String username, String operation, double amount, double balance, ATMStatus atmStatus) {

        if (!atmStatus.hasInkAndPaper()) {
            if (atmStatus.getInkLevel() <= 0) {
                System.out.println("\n⚠️  Cannot print receipt: Out of ink!");
            } else {
                System.out.println("\n⚠️  Cannot print receipt: Out of paper!");
            }
            return;
        }

        System.out.println("\n📄 Printing receipt...");
        System.out.println("================================");
        System.out.println("       ATM RECEIPT");
        System.out.println("================================");
        System.out.println("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("User: " + username);
        System.out.println("Operation: " + operation);
        System.out.println("Amount: €" + String.format("%.2f", amount));
        System.out.println("New Balance: €" + String.format("%.2f", balance));
        System.out.println("================================");
        System.out.println("   Thank you for using our ATM!");
        System.out.println("================================\n");

        atmStatus.useInkAndPaper();
    }
}