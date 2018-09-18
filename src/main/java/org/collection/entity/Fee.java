package org.collection.entity;

public class Fee {
    private Long fastestFee;
    private Long halfHourFee;
    private Long hourFee;

    public Long getFastestFee() {
        return fastestFee;
    }

    public void setFastestFee(Long fastestFee) {
        this.fastestFee = fastestFee;
    }

    public Long getHalfHourFee() {
        return halfHourFee;
    }

    public void setHalfHourFee(Long halfHourFee) {
        this.halfHourFee = halfHourFee;
    }

    public Long getHourFee() {
        return hourFee;
    }

    public void setHourFee(Long hourFee) {
        this.hourFee = hourFee;
    }
}
