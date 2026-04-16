package model;

public class CashAcceptor {
    private int amount;

    public CashAcceptor(int initialAmount) {
        this.amount = initialAmount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void deposit(int cash) {
        this.amount += cash;
    }
}
