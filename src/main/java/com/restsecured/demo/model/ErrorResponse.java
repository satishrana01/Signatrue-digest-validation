package com.restsecured.demo.model;

public class ErrorResponse {

    private String errorCode;
    private String errorMessage;

    private ErrorResponse(Builder builder){
        this.errorCode = builder.errorCode;
        this.errorMessage= builder.errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static final class Builder{


        private String errorCode;
        private String errorMessage;

        public Builder withErrorCode(String errorCode){
            this.errorCode = errorCode;
            return this;
        }

        public Builder withErrorMessage(String errorMessage){
            this.errorMessage = errorMessage;
            return this;
        }

        public ErrorResponse build(){

            return new ErrorResponse(this);
        }

    }
}
