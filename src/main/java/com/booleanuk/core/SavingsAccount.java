package com.booleanuk.core;

import java.util.ArrayList;

public class SavingsAccount extends Account{
    public SavingsAccount(String name, String owner, int num)
    {
        super(name);
        this.accountName = name;
        this.accountID = "sa-" + generateID(owner) + "-" + num;
        System.out.println(accountID);
        this.transactions = new ArrayList<>();
        this.balance = 0;
    }
}
