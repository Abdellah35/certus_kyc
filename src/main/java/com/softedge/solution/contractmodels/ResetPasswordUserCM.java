package com.softedge.solution.contractmodels;

import lombok.Data;

@Data
public class ResetPasswordUserCM {

    private String emailId;
    private String password;
}
