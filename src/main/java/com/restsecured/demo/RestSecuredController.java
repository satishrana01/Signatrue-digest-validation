package com.restsecured.demo;

import com.restsecured.demo.model.AccountInformation;
import com.restsecured.demo.model.DigestResult;
import com.restsecured.demo.model.ErrorResponse;
import com.restsecured.demo.utils.Constants;
import com.restsecured.demo.utils.SecurityContext;
import com.restsecured.demo.utils.Utility;
import com.restsecured.demo.model.SignatureOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tomitribe.auth.signatures.Signer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/***
 * Implementation of Rest secured service
 *
 * @author Satish.kumar
 *
 */
@RestController
public class RestSecuredController {

    private SecurityContext context = SecurityContext.secureContext;

    @GetMapping(value = "/getAccountinfo/{accountNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAccountInfo(@PathVariable("accountNo") String accountNo,
                                         @RequestHeader("Signature") String signature,
                                         @RequestHeader("RequestId") @NotNull String requestId,
                                         @RequestHeader("BankID") @NotNull String bankID,
                                         @RequestHeader("Created") @NotNull String created,
                                         HttpServletRequest request) {


        Map<String, String> headersParam = Utility.getHeaders(created, bankID, requestId);

        try {
            if (!context.isValidateSignature(headersParam, signature, request.getRequestURI().toLowerCase())) {
                return new ResponseEntity<ErrorResponse>(new ErrorResponse.Builder().withErrorCode("401").withErrorMessage("Invalid Signature").build(), HttpStatus.UNAUTHORIZED);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<AccountInformation>(new AccountInformation(accountNo, "Satish Kumar"), HttpStatus.OK);
    }

    @GetMapping(value = "/getSignature", produces = MediaType.APPLICATION_JSON_VALUE)
    public SignatureOutput getSignature(
            @RequestParam String uri,
            @RequestHeader("RequestId") @NotNull String requestId,
            @RequestHeader("BankID") @NotNull String bankID) {

        final Date today = new Date();
        final String created = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).format(today);
        Signer signer = context.getSigner();
        Map<String, String> signedValues = Utility.getHeaders(created, bankID, requestId);

        String signature = "";
        try {
            signature = signer.sign(Constants.GET_METHOD, uri.toLowerCase(), signedValues).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SignatureOutput(signature, Utility.getSigningString(signedValues));
    }

    @PostMapping(value = "/getDigest", produces = MediaType.APPLICATION_JSON_VALUE)
    public DigestResult getDigest(
            @RequestHeader("RequestId") String requestId,
            @RequestHeader("BankID") String bankID,
            @RequestBody AccountInformation accountInformation) {

        String digestString = context.getDigest(accountInformation);

        return new DigestResult(digestString);
    }

    @PostMapping(value = "/getAccountinfo/{accountNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAccountDetails(
            @RequestHeader("Signature") String signature,
            @RequestHeader("RequestId") @NotNull String requestId,
            @RequestHeader("BankID") @NotNull String bankID,
            @RequestHeader("Created") @NotNull String created,
            @RequestHeader("Digest") @NotNull String digest,
            @RequestBody AccountInformation accountInformation,
            HttpServletRequest request) {


        Map<String, String> headersParam = Utility.getHeaders(created, bankID, requestId);

        try {
            if (!context.isValidateSignature(headersParam, signature, request.getRequestURI().toLowerCase())) {
                return new ResponseEntity<ErrorResponse>(new ErrorResponse.Builder().withErrorCode("401").withErrorMessage("Invalid Signature").build(), HttpStatus.UNAUTHORIZED);

            }
            if (!context.isValidateDigest(accountInformation, digest)) {
                return new ResponseEntity<ErrorResponse>(new ErrorResponse.Builder().withErrorCode("401").withErrorMessage("Invalid Digest").build(), HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<AccountInformation>(new AccountInformation(accountInformation.getAccountNo(), "Satish Kumar"), HttpStatus.OK);
    }
}
