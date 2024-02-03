package com.softedge.solution.repomodels;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "digital_ipv")
public class DigitalIPV {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserRegistration user;

    @Column(name = "user_ipv_image")
    private String userIPVImage;

    @Column(name = "ipv_code")
    private Integer ipvCode;

    @Column(name = "verification_status")
    private String verificationStatus;

    @Column(name = "user_request_raised_time")
    private Date userRequestRaisedTime;

    @Column(name = "user_request_modified_time")
    private Date userRequestModifiedTime;

    @Column(name = "audit_verified_time")
    private Date auditVerifiedTime;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_name")
    private String fileName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRegistration getUser() {
        return user;
    }

    public void setUser(UserRegistration user) {
        this.user = user;
    }

    public String getUserIPVImage() {
        return userIPVImage;
    }

    public void setUserIPVImage(String userIPVImage) {
        this.userIPVImage = userIPVImage;
    }

    public Integer getIpvCode() {
        return ipvCode;
    }

    public void setIpvCode(Integer ipvCode) {
        this.ipvCode = ipvCode;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Date getUserRequestRaisedTime() {
        return userRequestRaisedTime;
    }

    public void setUserRequestRaisedTime(Date userRequestRaisedTime) {
        this.userRequestRaisedTime = userRequestRaisedTime;
    }

    public Date getUserRequestModifiedTime() {
        return userRequestModifiedTime;
    }

    public void setUserRequestModifiedTime(Date userRequestModifiedTime) {
        this.userRequestModifiedTime = userRequestModifiedTime;
    }

    public Date getAuditVerifiedTime() {
        return auditVerifiedTime;
    }

    public void setAuditVerifiedTime(Date auditVerifiedTime) {
        this.auditVerifiedTime = auditVerifiedTime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DigitalIPV)) return false;
        DigitalIPV that = (DigitalIPV) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getUserIPVImage(), that.getUserIPVImage()) &&
                Objects.equals(getIpvCode(), that.getIpvCode()) &&
                Objects.equals(getVerificationStatus(), that.getVerificationStatus()) &&
                Objects.equals(getUserRequestRaisedTime(), that.getUserRequestRaisedTime()) &&
                Objects.equals(getUserRequestModifiedTime(), that.getUserRequestModifiedTime()) &&
                Objects.equals(getAuditVerifiedTime(), that.getAuditVerifiedTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserIPVImage(), getIpvCode(), getVerificationStatus(), getUserRequestRaisedTime(), getUserRequestModifiedTime(), getAuditVerifiedTime());
    }

    @Override
    public String toString() {
        return "DigitalIPV{" +
                "id=" + id +
                ", userIPVImage='" + userIPVImage + '\'' +
                ", ipvCode=" + ipvCode +
                ", verificationStatus='" + verificationStatus + '\'' +
                ", userRequestRaisedTime=" + userRequestRaisedTime +
                ", userRequestModifiedTime=" + userRequestModifiedTime +
                ", auditVerifiedTime=" + auditVerifiedTime +
                '}';
    }
}

