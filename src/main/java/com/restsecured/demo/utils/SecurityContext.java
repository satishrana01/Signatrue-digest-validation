package com.restsecured.demo.utils;

import com.restsecured.demo.model.AccountInformation;
import org.tomitribe.auth.signatures.Signature;
import org.tomitribe.auth.signatures.Signer;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SecurityContext {

    public static final SecurityContext secureContext = new SecurityContext();

    private SecurityContext() {

    }

    /***
     * Get Signer object
     *
     * @return Signer
     */
    public Signer getSigner() {

        List<String> headers = List.of("(request-target)", "date", Headers.X_REQUESTID.getHeaderValue().toLowerCase(),
                Headers.X_BANKID.getHeaderValue().toLowerCase());
        final Signature signature = new Signature(AuthData.keyAlias, "hmac-sha256", "HmacSHA256", null, headers);
        final Key key = new SecretKeySpec(AuthData.secret.getBytes(), "HmacSHA256");
        final Signer signer = new Signer(key, signature);

        return signer;

    }

    /***
     * Validation of signature
     * @param headers
     * @param signature
     * @param uri
     * @return Boolean
     * @throws IOException
     */
    public boolean isValidateSignature(Map<String, String> headers, String signature, String uri) throws IOException {

        Signature createNewSignature = getSigner().sign(Constants.GET_METHOD, uri, headers);
        String incomingSignature = new String(Base64.getDecoder().decode(signature));
        /*System.out.println("new Signature      : " + createNewSignature);
        System.out.println("incoming Signature : " + incomingSignature);
        */
        List<String> incomingSignatureList = new ArrayList<String>(Arrays.asList(incomingSignature.split(",")));
        String stringToCompare = incomingSignatureList.stream()
                .filter(obj -> obj.contains(Headers.X_SIGNATURE.getHeaderValue().toLowerCase()))
                .map(obj -> obj.split("=", 2)[1]).findFirst().get().replace("\"", "");

        return createNewSignature.getSignature().equals(stringToCompare);
    }

    public boolean isValidateDigest(AccountInformation accountInformation, String incomingDigest) throws IOException {

        return incomingDigest.equals(getDigest(accountInformation));
    }

    /***
     * Generating the digest
     *
     * @param accountInformation
     * @return
     */
    public String getDigest(AccountInformation accountInformation){

        String digestString = "";
        try {
            String payload = accountInformation.getAccountName().concat(accountInformation.getAccountNo());
            final byte[] digest = MessageDigest.getInstance("SHA-256").digest(payload.getBytes());
            digestString = "SHA-256=" + new String(org.tomitribe.auth.signatures.Base64.encodeBase64(digest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return digestString;
    }


    private class AuthData {
        private static final String secret = "f1ce806e-57aa-4050-83ff-e370dfa1e31f30ff089d-1e3c-4e1d-a014-0792593708eb";
        private static final String keyAlias = "demo";
        private static final String storePass = "changeit";

    }
}
