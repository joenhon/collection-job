package org.collection.entity;

import java.math.BigDecimal;

public class UTXO {
    /*{
        "txid" : "c23495f6e7ba24705d43583edd69ff25a354c18e69fd8514c07ec6f47cb995de",
            "vout" : 0,
            "address" : "1K6JtSvrHtyFmxdtGZyZEF7ydytTGqasNc",
            "account" : "",
            "scriptPubKey" : "76a914c6734676a08e3c6438bd95fa62c57939c988a17b88ac",
            "amount" : 0.00100000,
            "confirmations" : 0,
            "spendable" : true
    }*/
    private Long id;
    private String txid;
    private Long vout;
    private String address;
    private String account;
    private String scriptPubKey;
    private BigDecimal amount;
    private String spendable;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public Long getVout() {
        return vout;
    }

    public void setVout(Long vout) {
        this.vout = vout;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(String scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String isSpendable() {
        return spendable;
    }

    public void setSpendable(String spendable) {
        this.spendable = spendable;
    }

    public String getSpendable() {
        return spendable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
