import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        System.out.println("Welcome to the ATM");
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        // Admin / Technician login
        if (username.equals("admin")) {
            System.out.print("Enter admin password: ");
            String adminPassword = sc.nextLine();

            if (adminPassword.equals("admin123")) {
                System.out.println("Admin login successful!");
                System.out.println("ATM Status: ONLINE");
                System.out.println("Number of users: " + bank.getNumberOfUsers());
                System.out.println("Total money in system: " + bank.getTotalMoney());
                return; // End Version 1 admin login
            } else {
                System.out.println("Authentication failed");
                return;
            }
        }

        // Customer login
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (!bank.authenticate(username, password)) {
            System.out.println("Authentication failed");
            return;
        }

        boolean exit = false;
        while (!exit) {
            System.out.println("\n1. Check Balance");
            System.out.println("2. Withdraw Cash");
            System.out.println("3. Deposit Funds");
            System.out.println("4. Transfer Funds");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("Balance: " + bank.checkBalance(username));
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double w = Double.parseDouble(sc.nextLine());
                    if (bank.withdraw(username, w)) {
                        System.out.println("Withdraw successful");
                    } else {
                        System.out.println("Insufficient balance");
                    }
                    break;
                case 3:
                    System.out.print("Enter amount to deposit: ");
                    double d = Double.parseDouble(sc.nextLine());
                    bank.deposit(username, d);
                    System.out.println("Deposit successful");
                    break;
                case 4:
                    System.out.print("Enter recipient username: ");
                    String toUser = sc.nextLine();
                    System.out.print("Enter amount to transfer: ");
                    double t = Double.parseDouble(sc.nextLine());
                    if (bank.transfer(username, toUser, t)) {
                        System.out.println("Transfer successful");
                    } else {
                        System.out.println("Transfer failed");
                    }
                    break;
                case 5:
                    exit = true;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}
