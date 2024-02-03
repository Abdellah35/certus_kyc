package com.softedge.solution.contractmodels;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProcessRequestCM {

    private List<Long> approvedKycIds = new ArrayList<>();
    private List<Long> rejectedKycIds = new ArrayList<>();
    private List<Long> requestKycIds = new ArrayList<>();

}
