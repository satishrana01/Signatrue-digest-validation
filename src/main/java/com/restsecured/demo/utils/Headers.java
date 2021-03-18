package com.restsecured.demo.utils;

public enum Headers {

    X_SIGNATURE("Signature"),
    X_REQUESTID("RequestId"),
    X_BANKID("BankId"),
    X_DIGEST("Digest"),
    X_DATE("date");

    private String getHeader;

    private Headers(String code){

        this.getHeader = code;
    }

    public String getHeaderValue(){
        return this.getHeader;
    }
}
