package com.booleanuk.extension;

import java.util.ArrayList;

public class Account {
    String accountName;
    String accountID;
    ArrayList<Transaction> transactions;
    Branch branch;

    public Account(String accountName, Bank bank)
    {
        this.accountName  = accountName;
        this.accountID    = "" ;    // Will get to that
        this.transactions = new ArrayList<>();
        this.branch       = bank.getBranch("Oslo_East");
        this.branch.addAccount(this.accountID);// Default branch in Oslo
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountID() {
        return accountID;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public int getBalance() {
        int balance = 0;
        for(Transaction t : this.transactions)
        {
            balance = t.isDeposit() ? balance + t.getAmount() : balance - t.getAmount();
        }
        return balance;
    }

    public boolean deposit(int amount)  {
        if(amount >= 0) {
            this.transactions.add(new Transaction(amount,true));
            return true;
        }
        System.out.println("Cannot deposit negative value!");
        return false;
    }

    public String generateID(String owner)
    {
        String[] names = owner.split(" ");
        StringBuilder ID = new StringBuilder();
        for(int i = 0; i < names[names.length-1].length(); i++)
        {
            char c = names[names.length-1].charAt(i);
            ID.append((int) c);
        }
        return ID.toString();
    }

    public String generateTransactionStatement()
    {
        StringBuilder statement = new StringBuilder();
        int balance = 0;
        int creditWidth = getCreditWidth(), debitWidth = getDebitWidth();
        statement = new StringBuilder(String.format(
                "%-11s%s%-" + (creditWidth - 1) + "s%s%-" + (debitWidth - 1) + "s%s%-7s",
                "date ", "|| ", "credit", "|| ", "debit", "|| ", "balance"));


        for(Transaction t: this.transactions)
        {
            statement.append("\n");
            balance = t.isDeposit() ? balance + t.getAmount() : balance - t.getAmount();
            statement.append(String.format("%-11s%s%" + (creditWidth - 1) + "s%s%" + (debitWidth - 1) + "s%s%8s",
                    t.getDate(), "||", t.isDeposit() ? (double) t.getAmount() / 100.0 : "",
                    " ||", t.isDeposit() ? "" : (double) t.getAmount() / 100.0,
                    " ||", (double) balance / 100.0));

        }
        return statement.toString();
    }

    public int getCreditWidth()
    {
        int max = 8;
        for(Transaction t : this.transactions)
        {
            if(t.isDeposit()) {
                String number = "" + t.getAmount();
                if (number.length() > max) max = number.length();
            }
        }
        return max;
    }

    public int getDebitWidth()
    {
        int max = 7;
        for(Transaction t : this.transactions)
        {
            if(!t.isDeposit()) {
                String number = "" + t.getAmount();
                if (number.length() > max) max = number.length();
            }
        }
        return max;
    }

    public boolean withdraw(int amount)  {
        if(amount >= 0) {
            this.transactions.add(new Transaction(amount,false));
            return true;
        }
        System.out.println("Cannot withdraw negative value!");
        return false;
    }

    public void assignBranch(Branch branch)
    {
        this.branch = branch;
        branch.addAccount(this.accountID);
    }

    public Branch getBranch()
    {
        return this.branch;
    }
}
