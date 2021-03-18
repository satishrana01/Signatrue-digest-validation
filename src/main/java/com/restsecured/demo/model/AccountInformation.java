package com.restsecured.demo.model;

public class AccountInformation {

    private String accountNo;
    private String accountName;

    public AccountInformation(String accountNo, String accountName) {
        this.accountNo = accountNo;
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
