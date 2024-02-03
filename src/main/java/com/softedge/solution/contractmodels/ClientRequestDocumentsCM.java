package com.softedge.solution.contractmodels;

import lombok.Data;

import java.util.List;

@Data
public class ClientRequestDocumentsCM {

    private Long userId;
    private List<Long> documentIds;


}
