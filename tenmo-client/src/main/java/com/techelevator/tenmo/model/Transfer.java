package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private long id;
    private String fromUsername;
    private String toUsername;
    private String type;
    private String status;
    private BigDecimal amount;

    public Transfer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    //    Id: 23
//    From: Bernice
//    To: Me Myselfandi
//    Type: Send
//    Status: Approved
//    Amount: $903.14
}
