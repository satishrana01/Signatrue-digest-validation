package com.restsecured.demo.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import java.util.UUID;

public class main {
    public static void main(String[] args) {

        try {
            String secret = "f1ce806e-57aa-4050-83ff-e370dfa1e31f30ff089d-1e3c-4e1d-a014-0792593708eb";
            String message = "Message";

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
            System.out.println(hash.toString());
            System.out.println(UUID.randomUUID().toString().concat(UUID.randomUUID().toString()));

        }
        catch (Exception e){
            System.out.println("Error");
        }
    }

}
