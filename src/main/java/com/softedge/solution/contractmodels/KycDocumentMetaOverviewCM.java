package com.softedge.solution.contractmodels;

public class KycDocumentMetaOverviewCM {

    private Long id;
    private String documentLogo;
    private String documentName;
    private String documentDesc;
    private String documentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentLogo() {
        return documentLogo;
    }

    public void setDocumentLogo(String documentLogo) {
        this.documentLogo = documentLogo;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentDesc() {
        return documentDesc;
    }

    public void setDocumentDesc(String documentDesc) {
        this.documentDesc = documentDesc;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

}
