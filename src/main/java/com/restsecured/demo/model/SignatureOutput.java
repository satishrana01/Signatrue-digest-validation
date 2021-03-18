package com.restsecured.demo.model;

import java.util.Base64;

public class SignatureOutput {

    private String signature;
    private String singingString;
    private String SignatureB64;

    public SignatureOutput(String signature, String singingString) {
        this.signature = signature;
        this.singingString = singingString;
        this.SignatureB64 = Base64.getEncoder().encodeToString(this.signature.getBytes());
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignatureB64() {
        return SignatureB64;
    }

    public void setSignatureB64(String signatureB64) {
        SignatureB64 = signatureB64;
    }

    public String getSingingString() {
        return singingString;
    }

    public void setSingingString(String singingString) {
        this.singingString = singingString;
    }
}
