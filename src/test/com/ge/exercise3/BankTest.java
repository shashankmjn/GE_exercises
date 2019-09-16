package com.ge.exercise3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BankTest {

    Bank bank;

    @Before
    public void setUp() {
        bank = new Bank();
    }

    @Test
    public void addAccountTest() {
        Account account = new Account("001");
        bank.addAccount(account);
        assertEquals(1, bank.numAccounts());
    }

    @Test
    public void getAccountTest() {
        Account account = new Account("002", "Checking", 100.0f);
        bank.addAccount(account);
        assertEquals(account, bank.getAccount("002"));
    }

    @Test
    public void depositToAccountTest() {
        Account account = new Account("003", "Checking", 100.0f);
        bank.addAccount(account);
        bank.depositToAccount("003", 100.0f);
        assertEquals(200.0f, account.getBalance(), 0.01);
    }

    @Test
    public void withdrawFromAccountTest() {
        Account account = new Account("004", "Checking", 100.0f);
        bank.addAccount(account);
        bank.withdrawFromAccount("004", 100.0f);
        assertEquals(0.0f, account.getBalance(), 0.01);
    }

    @Test
    public void getTotalHoldingsTest() {
        Account account1 = new Account("004", "Checking", 100.0f);
        Account account2 = new Account("007", "Savings", 120.0f);
        Account account3 = new Account("010", "Savings", 500.0f);
        Account account4 = new Account("011", "Checking", 80.0f);
        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.addAccount(account3);
        bank.addAccount(account4);
        assertEquals(800.0f, bank.getCurrentHoldings(), 0.01f);
    }

    @Test
    public void projectProfitLossTest() {
        Account account1 = new Account("004", "Checking", 100.0f);
        Account account2 = new Account("007", "Savings", 120.0f);
        Account account3 = new Account("010", "Savings", 500.0f);
        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.addAccount(account3);
        assertEquals("Loss", bank.projectProfitOrLoss());
    }
}