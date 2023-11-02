package com.starkexport.model;

import org.web3j.utils.Convert;

import java.math.BigDecimal;

public class TransactionExport {
    private String nonce;
    private String dateTime;
    private String from;
    private String trxType;
    private String method;
    private String blockNo;
    private String trxFee;
    private String status;
    private String transactionHash;

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTrxType() {
        return trxType;
    }

    public void setTrxType(String trxType) {
        this.trxType = trxType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public String getTrxFee() {
        return trxFee;
    }

    public void setTrxFee(String trxFee) {
        this.trxFee = trxFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public TransactionExport(){

    }

    public TransactionExport(String nonce, String dateTime, String from, String trxType, String method, String blockNo, String trxFee, String status, String transactionHash) {
        this.nonce = nonce;
        this.dateTime = dateTime;
        this.from = from;
        this.trxType = trxType;
        this.method = method;
        this.blockNo = blockNo;
        this.trxFee = trxFee;
        this.status = status;
        this.transactionHash = transactionHash;
    }

}
