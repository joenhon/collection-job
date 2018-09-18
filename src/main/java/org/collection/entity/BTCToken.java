package org.collection.entity;

public class BTCToken {
    private boolean divisible;
    private String frozen;
    private String id;
    private String pendingneg;
    private String pendingpos;
    private String reserved;
    private String symbol;
    private String value;

    public boolean isDivisible() {
        return divisible;
    }

    public void setDivisible(boolean divisible) {
        this.divisible = divisible;
    }

    public String getFrozen() {
        return frozen;
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPendingneg() {
        return pendingneg;
    }

    public void setPendingneg(String pendingneg) {
        this.pendingneg = pendingneg;
    }

    public String getPendingpos() {
        return pendingpos;
    }

    public void setPendingpos(String pendingpos) {
        this.pendingpos = pendingpos;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
