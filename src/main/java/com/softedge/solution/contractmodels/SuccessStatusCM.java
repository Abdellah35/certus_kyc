package com.softedge.solution.contractmodels;

public class SuccessStatusCM {

    private String successCode;
    private String successMessage;

    public SuccessStatusCM() {
    }

    public SuccessStatusCM(String successMessage, String successCode) {
        this.successCode = successCode;
        this.successMessage = successMessage;
    }

    public String getSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(String successCode) {
        this.successCode = successCode;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
