package com.smartcontactmanager.entities;

public class JWTResponse {
    String toekn;

    public String getToekn() {
        return toekn;
    }

    public void setToekn(String toekn) {
        this.toekn = toekn;
    }

    public JWTResponse() {
    }

    public JWTResponse(String toekn) {
        this.toekn = toekn;
    }
}
