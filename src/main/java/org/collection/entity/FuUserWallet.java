package org.collection.entity;

import javax.xml.crypto.Data;

public class FuUserWallet {
    private Long id;
    private Long userId;
    private String address;
    private String type;
    private char assetCode;
    private char isDefault;
    private String pwd;
    private String remarkes;
    private String fName;
    private char deleted;
    private Data createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public char getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(char assetCode) {
        this.assetCode = assetCode;
    }

    public char getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(char isDefault) {
        this.isDefault = isDefault;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRemarkes() {
        return remarkes;
    }

    public void setRemarkes(String remarkes) {
        this.remarkes = remarkes;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public char getDeleted() {
        return deleted;
    }

    public void setDeleted(char deleted) {
        this.deleted = deleted;
    }

    public Data getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Data createTime) {
        this.createTime = createTime;
    }
}
