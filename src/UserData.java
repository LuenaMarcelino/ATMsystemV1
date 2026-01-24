public class UserData {
    private String password;
    private double balance;

    public UserData(String password, double balance) {
        this.password = password;
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

