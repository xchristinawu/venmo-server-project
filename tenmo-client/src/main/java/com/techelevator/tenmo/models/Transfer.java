package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {

    private int userId;
    private int recipientId;
    private int transferId;
    private int transferType;
    private int transferStatus;
    private BigDecimal amount;
    private String accountFrom;
    private String accountTo;

    public int getUserId()
    { return userId; }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getRecipientId()
    {
        return recipientId;
    }

    public void setRecipientId(int recipientId)
    {
        this.recipientId = recipientId;
    }

    public int getTransferId()
    {
        return transferId;
    }

    public void setTransferId(int transferId)
    {
        this.transferId = transferId;
    }

    public int getTransferType()
    {
        return transferType;
    }

    public void setTransferType(int transferType)
    {
        this.transferType = transferType;
    }

    public int getTransferStatus()
    {
        return transferStatus;
    }

    public void setTransferStatus(int transferStatus)
    {
        this.transferStatus = transferStatus;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public String getAccountFrom()
    {
        return accountFrom;
    }

    public void setAccountFrom(String accountFrom)
    {
        this.accountFrom = accountFrom;
    }

    public String getAccountTo()
    {
        return accountTo;
    }

    public void setAccountTo(String accountTo)
    {
        this.accountTo = accountTo;
    }

    public Transfer(int transferId, int transferType, int transferStatus, BigDecimal amount, String accountFrom, String accountTo)
    {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.amount = amount;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Transfer()
    {
    }

    public Transfer(int userId, int recipientId, BigDecimal amount) {
        this.userId = userId;
        this.recipientId = recipientId;
        this.amount = amount;
    }
}
