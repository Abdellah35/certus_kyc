package com.softedge.solution.feignbeans;

import java.util.Objects;

public class UserIPVActivation {

    private String username;
    private String emailId;
    private Integer activationCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Integer getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(Integer activationCode) {
        this.activationCode = activationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserIPVActivation)) return false;
        UserIPVActivation that = (UserIPVActivation) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getEmailId(), that.getEmailId()) &&
                Objects.equals(getActivationCode(), that.getActivationCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getEmailId(), getActivationCode());
    }

    @Override
    public String toString() {
        return "UserIPVActivation{" +
                "username='" + username + '\'' +
                ", emailId='" + emailId + '\'' +
                ", activationCode=" + activationCode +
                '}';
    }
}
