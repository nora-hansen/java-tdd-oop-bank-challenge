package com.booleanuk.extension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class UserTest {
    @Test
    public void testCreateUser()
    {
        LocalDate birthday = LocalDate.of(1956, 3, 19);
        User user = new User("Large Nimpson", birthday);

        Assertions.assertEquals("Large Nimpson", user.getUserName());

        LocalDate birthday2 = LocalDate.of(2019, 9, 29);
        User dog = new User("Fido", birthday2);

        Assertions.assertEquals("Fido", dog.getUserName());
    }

    @Test
    public void testCreateCurrentAccount()
    {
        Bank bank = new Bank();
        LocalDate birthday = LocalDate.of(1956, 3, 19);
        User user = new User("Large Nimpson", birthday);
        user.createCurrentAccount("Groceries", bank);

        Assertions.assertEquals(0,user.getAccount("cu-78105109112115111110-1").getBalance());
        Assertions.assertEquals("Groceries",user.getAccount("cu-78105109112115111110-1").getAccountName());
    }

    @Test
    public void testCreateSavingsAccount()
    {
        Bank bank = new Bank();
        LocalDate birthday = LocalDate.of(1956, 3, 19);
        User user = new User("Large Nimpson", birthday);
        user.createSavingsAccount("Lizzie's college fund", bank);

        Assertions.assertEquals(0,user.getAccount("sa-78105109112115111110-1").getBalance());
        Assertions.assertEquals("Lizzie's college fund",user.getAccount("sa-78105109112115111110-1").getAccountName());
    }

    @Test
    public void testGenerateTransactionStatement()
    {
        Bank bank = new Bank();
        LocalDate birthday = LocalDate.of(1956, 3, 19);
        LocalDate today = LocalDate.now();
        User user = new User("Large Nimpson", birthday);
        user.createCurrentAccount("Groceries", bank);
        user.makeDeposit("cu-78105109112115111110-1", 100000);
        user.makeWithdrawal("cu-78105109112115111110-1", 5000);

        String expectedString =
                "date       || credit || debit || balance\n"
                        + today +" || 1000.0 ||       ||  1000.0\n"
                        + today + " ||        ||  50.0 ||   950.0";
        Assertions.assertEquals(expectedString, user.generateTransactionHistory("cu-78105109112115111110-1"));
    }

    @Test
    public void testGenerateTransactionStatementDoubles()
    {
        Bank bank = new Bank();
        LocalDate birthday = LocalDate.of(1956, 3, 19);
        LocalDate today = LocalDate.now();
        User user = new User("Large Nimpson", birthday);
        user.createCurrentAccount("Groceries", bank);
        user.makeDeposit("cu-78105109112115111110-1", 2000.00);
        user.makeWithdrawal("cu-78105109112115111110-1", 0.50);

        String expectedString =
                "date       || credit || debit || balance\n"
                        + today +" || 2000.0 ||       ||  2000.0\n"
                        + today + " ||        ||   0.5 ||  1999.5";
        Assertions.assertEquals(expectedString, user.generateTransactionHistory("cu-78105109112115111110-1"));
    }

    @Test
    public void testDeposit()
    {
        Bank bank = new Bank();
        LocalDate birthday = LocalDate.of(1956, 3, 19);
        User user = new User("Large Nimpson", birthday);
        user.createCurrentAccount("Groceries", bank);

        user.makeDeposit("cu-78105109112115111110-1", 1000);
        Assertions.assertEquals(1000, user.getBalance("cu-78105109112115111110-1"));
        user.makeDeposit("cu-78105109112115111110-1", 300);
        Assertions.assertEquals(1300, user.getBalance("cu-78105109112115111110-1"));
        user.makeDeposit("cu-78105109112115111110-1", -300);
        Assertions.assertEquals(1300, user.getBalance("cu-78105109112115111110-1"));
    }

    @Test
    public void testWithdrawal()
    {
        Bank bank = new Bank();
        LocalDate birthday = LocalDate.of(1956, 3, 19);
        User user = new User("Large Nimpson", birthday);
        user.createCurrentAccount("Groceries", bank);

        user.makeDeposit("cu-78105109112115111110-1", 1000);
        Assertions.assertEquals(1000, user.getBalance("cu-78105109112115111110-1"));
        user.makeWithdrawal("cu-78105109112115111110-1", 300);
        Assertions.assertEquals(700, user.getBalance("cu-78105109112115111110-1"));
        user.makeWithdrawal("cu-78105109112115111110-1", -300);
        Assertions.assertEquals(700, user.getBalance("cu-78105109112115111110-1"));
    }

    @Test
    public void testWithdrawalNotAllowed()
    {
        Bank bank = new Bank();
        LocalDate birthday = LocalDate.of(1956, 3, 19);
        User user = new User("Large Nimpson", birthday);
        user.createCurrentAccount("Groceries", bank);

        user.makeDeposit("cu-78105109112115111110-1", 100);
        Assertions.assertFalse( user.makeWithdrawal("cu-78105109112115111110-1", 300));
    }

    @Test
    public void testRequestOverdraftSuccessful()
    {
        Bank bank = new Bank();
        LocalDate birthday = LocalDate.of(1956, 3, 19);
        User user = new User("Large Nimpson", birthday);
        user.createCurrentAccount("Groceries", bank);

        Assertions.assertTrue(user.requestOverdraft("cu-78105109112115111110-1", 200));
        Assertions.assertEquals(-200, user.getAccount("cu-78105109112115111110-1").getBalance());
    }

    @Test
    public void testRequestOverdraftRejected()
    {
        Bank bank = new Bank();
        LocalDate birthday = LocalDate.of(1956, 3, 19);
        User user = new User("Large Nimpson", birthday);
        user.createCurrentAccount("Groceries", bank);

        Assertions.assertFalse(user.requestOverdraft("cu-78105109112115111110-1", 20000000));
        Assertions.assertEquals(0, user.getAccount("cu-78105109112115111110-1").getBalance());
    }

    @Test
    public void testRequestOverdraftRejectedSavings()
    {
        Bank bank = new Bank();
        LocalDate birthday = LocalDate.of(1956, 3, 19);
        User user = new User("Large Nimpson", birthday);
        user.createSavingsAccount("Life savings", bank);

        Assertions.assertFalse(user.requestOverdraft("sa-78105109112115111110-1", 20000000));
        Assertions.assertEquals(0, user.getAccount("sa-78105109112115111110-1").getBalance());
    }
}