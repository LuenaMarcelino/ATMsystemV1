import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ATMStatus atmStatus = new ATMStatus();
        Bank bank = new Bank(atmStatus);

        System.out.println("╔════════════════════════════╗");
        System.out.println("║    Welcome to the ATM!     ║");
        System.out.println("╚════════════════════════════╝\n");

        boolean systemRunning = true;

        while (systemRunning) {
            // Default: Customer authentication (like real ATMs)
            System.out.print("Enter username: ");
            String username = sc.nextLine().trim();

            System.out.print("Enter password: ");
            String password = sc.nextLine().trim();

            if (!bank.authenticate(username, password)) {
                System.out.println("❌ Authentication failed\n");
                continue; // Go back to login
            }

            System.out.println("Login successful!");
            customerMenu(sc, bank, atmStatus, username);

            // After customer logout, ask if technician wants to login
            System.out.print("\nTechnician login? (yes/no): ");
            String techResponse = sc.nextLine().trim().toLowerCase();

            if (techResponse.equals("yes") || techResponse.equals("y")) {
                technicianLogin(sc, atmStatus, bank);
            }
        }
    }

    private static void customerMenu(Scanner sc, Bank bank, ATMStatus atmStatus, String username) {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n╔════════════════════════════╗");
            System.out.println("║      CUSTOMER MENU         ║");
            System.out.println("╚════════════════════════════╝");
            System.out.println("1. Check Balance");
            System.out.println("2. Withdraw Cash");
            System.out.println("3. Deposit Funds");
            System.out.println("4. Transfer Funds");
            System.out.println("5. Transaction History");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    double balance = bank.checkBalance(username);
                    System.out.println("\n💰 Current Balance: €" + String.format("%.2f", balance));
                    askForReceipt(sc, username, "Balance Check", 0, balance, atmStatus);
                }
                case 2 -> {
                    System.out.print("Enter amount to withdraw: €");
                    double amount = Double.parseDouble(sc.nextLine());

                    if (bank.withdraw(username, amount)) {
                        System.out.println("Withdrawal successful");
                        double newBalance = bank.checkBalance(username);
                        askForReceipt(sc, username, "Withdrawal", amount, newBalance, atmStatus);
                    }
                }
                case 3 -> {
                    System.out.print("Enter amount to deposit: €");
                    double amount = Double.parseDouble(sc.nextLine());

                    if (amount <= 0) {
                        System.out.println("❌ Invalid amount");
                    } else {
                        bank.deposit(username, amount);
                        System.out.println("Deposit successful");
                        double newBalance = bank.checkBalance(username);
                        askForReceipt(sc, username, "Deposit", amount, newBalance, atmStatus);
                    }
                }
                case 4 -> {
                    System.out.print("Enter recipient username: ");
                    String toUser = sc.nextLine().trim();
                    System.out.print("Enter amount to transfer: €");
                    double amount = Double.parseDouble(sc.nextLine());

                    if (bank.transfer(username, toUser, amount)) {
                        System.out.println("Transfer successful");
                        double newBalance = bank.checkBalance(username);
                        askForReceipt(sc, username, "Transfer to " + toUser, amount, newBalance, atmStatus);
                    }
                }
                case 5 -> bank.getTransactionHistory().showHistory(username);
                case 6 -> {
                    logout = true;
                    System.out.println("\nThank you for using our ATM!");
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void askForReceipt(Scanner sc, String username, String operation,
                                      double amount, double balance, ATMStatus atmStatus) {
        System.out.print("\nDo you want a receipt? (yes/no): ");
        String response = sc.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            Receipt.print(username, operation, amount, balance, atmStatus);
        } else {
            System.out.println("\nThank you for using our ATM!");
        }
    }

    private static void technicianLogin(Scanner sc, ATMStatus atmStatus, Bank bank) {
        System.out.print("\nEnter technician username: ");
        String username = sc.nextLine().trim();

        System.out.print("Enter technician password: ");
        String password = sc.nextLine().trim();

        if (!username.equals("admin") || !password.equals("admin123")) {
            System.out.println("❌ Authentication failed");
            return;
        }

        System.out.println("Technician login successful!");
        technicianMenu(sc, atmStatus, bank);
    }

    private static void technicianMenu(Scanner sc, ATMStatus atmStatus, Bank bank) {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n╔════════════════════════════╗");
            System.out.println("║     TECHNICIAN MENU        ║");
            System.out.println("║                            ║");
            System.out.println("╚════════════════════════════╝");
            System.out.println("1. View ATM Status");
            System.out.println("2. View All Transaction History");
            System.out.println("3. Logout");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option");
                continue;
            }

            switch (choice) {
                case 1 -> atmStatus.showStatus();
                case 2 -> bank.getTransactionHistory().showAllHistory();
                case 3 -> {
                    logout = true;
                    System.out.println("\nTechnician logged out");
                }
                default -> System.out.println("Invalid option");
            }
        }
    }
}