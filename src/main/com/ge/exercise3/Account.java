package com.ge.exercise3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.ge.exercise3.CommonContant.CANNOT_WITHDRAW;
import static com.ge.exercise3.CommonContant.NEGATIVE_BALANCE_MESSAGE;
import static com.ge.exercise3.CommonContant.OVERDRAWN_MESSAGE;

public class Account {

    private static final Logger logger = LogManager.getLogger(Account.class);

    private float monthlyInterestRate = 0.0f;
    private float monthlyFee = 0.0f;

    private String accountNumber;
    private String accountType;
    private float balance;

    public Account(String accountNumber, String accountType, float balance) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        if (accountType.equalsIgnoreCase("Savings")) {
            monthlyInterestRate = 1.0f;
        }
    }

    public Account(String accountNumber, String accountType) {
        this(accountNumber, accountType, 0.0f);
    }

    public Account(String accountNumber) {
        this(accountNumber, "Savings", 0.0f);
    }

    public float valueNextMonth() {
        return balance + ((balance * (monthlyInterestRate/100)) - monthlyFee);
    }

    @Override
    public String toString() {
        if (accountType.equalsIgnoreCase("Checking")) {
            if (monthlyFee == 0.0f) {
                return "No fee checking account #" + accountNumber;
            } else {
                return "Checking account #" + accountNumber;
            }
        } else {
            if (monthlyInterestRate > 1.01) {
                if (monthlyFee == 0.0f) {
                    return "High interest no fee savings account #" + accountNumber;
                } else {
                    return "High interest savings account #" + accountNumber;
                }
            } else {
                if (monthlyFee == 0.0f) {
                    return "No fee savings account #" + accountNumber;
                } else {
                    return "Savings account #" + accountNumber;
                }
            }
        }
    }

    public void deposit(float amount) {
        balance += amount;
    }

    public void withdraw(float amount) {
        if("Savings".equalsIgnoreCase(accountType))
        {
            if(balance - amount < 0) {
                System.out.println(CANNOT_WITHDRAW.concat(" ")  + NEGATIVE_BALANCE_MESSAGE);
            }
            else
                balance -= amount;
        }
        else {
            if(balance - amount < -100.0f)
                System.out.println(CANNOT_WITHDRAW.concat(" ") + OVERDRAWN_MESSAGE);
            else
                balance -=amount;
        }
    }

    public float getMonthlyInterestRate() {
        return monthlyInterestRate;
    }

    public void setMonthlyInterestRate(float monthlyInterestRate) {
        this.monthlyInterestRate = monthlyInterestRate;
    }

    public float getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(float monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public float getBalance() {
        return balance;
    }

    void setBalance(float balance) {
        this.balance = balance;
    }
}
