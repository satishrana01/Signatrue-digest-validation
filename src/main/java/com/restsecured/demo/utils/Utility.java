package com.restsecured.demo.utils;

import org.tomitribe.auth.signatures.Join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utility {

    public static String getSigningString(Map<String, String> headers) {
        final List<String> list = new ArrayList<String>(headers.size());
        headers.entrySet().stream().map(header -> list.add(header.getKey().concat(":").concat(header.getValue()))).collect(Collectors.toList());
        ;

        return Join.join("\n", list);
    }

    public static Map<String, String> getHeaders(String created, String bankID, String requestId) {
        Map<String, String> headersParam = new HashMap<>();
        headersParam.put(Headers.X_DATE.getHeaderValue().toLowerCase(), created);
        headersParam.put(Headers.X_BANKID.getHeaderValue().toLowerCase(), bankID.toLowerCase());
        headersParam.put(Headers.X_REQUESTID.getHeaderValue().toLowerCase(), requestId.toLowerCase());
        return  headersParam;
    }
}
