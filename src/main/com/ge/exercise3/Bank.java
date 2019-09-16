package com.ge.exercise3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Bank {

    private static final Logger logger = LogManager.getLogger(Bank.class);
    private Map<String, Account> accountMap;

    public Bank() {
        accountMap = new HashMap<>();
    }

    public Account getAccount(String accountNumber) {
        return accountMap.get(accountNumber);
    }

    public void addAccount(Account account) {
        accountMap.put(account.getAccountNumber(), account);
    }

    public void depositToAccount(String accountNumber, float amount) {
        getAccount(accountNumber).deposit(amount);
    }

    public void withdrawFromAccount(String accountNumber, float amount) {
        getAccount(accountNumber).withdraw(amount);
    }

    public int numAccounts() {
        return accountMap.size();
    }

    public float getCurrentHoldings() {
        if(accountMap.size() != 0)
            return accountMap.entrySet().stream().map(entry -> entry.getValue().getBalance()).reduce(0.0f, (a, b) -> a+b);
        else
            return 0.0f;
    }

    public String projectProfitOrLoss() {
        if(accountMap.size() == 0)
            return "Profit";
        else {
            AtomicReference<Float> totalInterest = new AtomicReference<>((float) 0);
            AtomicReference<Float> totalMonthlyFee = new AtomicReference<>((float) 0);
            accountMap.forEach((key, value) -> {
                totalInterest.updateAndGet(v -> v + value.getBalance() * value.getMonthlyInterestRate());
                totalMonthlyFee.updateAndGet(v -> v + value.getMonthlyFee());
            });
            if(totalInterest.get() > totalMonthlyFee.get())
                return "Loss";
            else
                return "Profit";
        }
    }
}
