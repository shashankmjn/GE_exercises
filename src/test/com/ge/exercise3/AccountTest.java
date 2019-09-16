package com.ge.exercise3;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.ge.exercise3.CommonContant.CANNOT_WITHDRAW;
import static com.ge.exercise3.CommonContant.NEGATIVE_BALANCE_MESSAGE;
import static com.ge.exercise3.CommonContant.OVERDRAWN_MESSAGE;
import static org.junit.Assert.assertEquals;

public class AccountTest {

    private Account checkingAccount;
    private Account savingsAccount;

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        checkingAccount = new Account("001", "Checking");
        savingsAccount = new Account("002", "Savings");
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
    }

    @Test
    public void depositAndWithdrawTest() {
        checkingAccount.setBalance(0.0f);
        checkingAccount.deposit(100.0f);
        assertEquals(100.0f, checkingAccount.getBalance(), 0.01);
        checkingAccount.withdraw(100.0f);
        savingsAccount.deposit(200.0f);
        assertEquals(0.0f, checkingAccount.getBalance(), 0.01);
    }

    @Test
    public void valueNextMonthTest() {
        checkingAccount = new Account("003", "Checking", 100.0f);
        assertEquals(100.0f, checkingAccount.valueNextMonth(), 0.01f);

        savingsAccount = new Account("004", "Savings", 100.0f);
        assertEquals(101.0f, savingsAccount.valueNextMonth(), 0.01f);

        checkingAccount.setMonthlyFee(10.0f);
        assertEquals(90.0f, checkingAccount.valueNextMonth(), 0.01f);

        savingsAccount.setMonthlyInterestRate(5.0f);
        assertEquals(105.0f, savingsAccount.valueNextMonth(), 0.01f);
    }

    @Test
    public void toStringTest() {
        savingsAccount = new Account("005", "Savings", 0.0f);
        assertEquals("No fee savings account #005", savingsAccount.toString());

        checkingAccount = new Account("006", "Checking", 0.0f);
        assertEquals("No fee checking account #006", checkingAccount.toString());

        checkingAccount.setMonthlyFee(10.0f);
        assertEquals("Checking account #006", checkingAccount.toString());

        savingsAccount.setMonthlyInterestRate(1.02f);
        assertEquals("High interest no fee savings account #005", savingsAccount.toString());
    }

    @Test
    public void savingsAccountNegativeBalanceTest() {
        savingsAccount = new Account("007", "Savings", 100.0f);
        assertEquals(100.0f, savingsAccount.getBalance(), 0.01f);

        savingsAccount.withdraw(50.0f);
        assertEquals(50.0f, savingsAccount.getBalance(), 0.01f);

        savingsAccount.withdraw(60.0f);
        assertEquals(CANNOT_WITHDRAW.concat(" ") + NEGATIVE_BALANCE_MESSAGE.concat("\n"), baos.toString());

    }

    @Test
    public void checkingAccountOverdrawnTest() {
        checkingAccount = new Account("008", "Checking", 50.0f);
        assertEquals(50.0f, checkingAccount.getBalance(), 0.01f);

        checkingAccount.withdraw(60.0f);
        assertEquals(-10.0f, checkingAccount.getBalance(), 0.01f);

        checkingAccount.withdraw(100.0f);
        assertEquals(CANNOT_WITHDRAW.concat(" ") + OVERDRAWN_MESSAGE.concat("\n"), baos.toString());

    }
}