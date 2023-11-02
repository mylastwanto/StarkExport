package com.starkexport.model.pojo;

import java.util.ArrayList;

public class AccountCall {
    private String block_hash;
    private int block_number;
    private String transaction_hash;
    private String caller_address;
    private String contract_address;
    private ArrayList<String> calldata;
    private ArrayList<String> result;
    private int timestamp;
    private String call_type;
    private String class_hash;
    private String selector;
    private String entry_point_type;
    private String selector_name;

    public String getBlock_hash() {
        return block_hash;
    }

    public void setBlock_hash(String block_hash) {
        this.block_hash = block_hash;
    }

    public int getBlock_number() {
        return block_number;
    }

    public void setBlock_number(int block_number) {
        this.block_number = block_number;
    }

    public String getTransaction_hash() {
        return transaction_hash;
    }

    public void setTransaction_hash(String transaction_hash) {
        this.transaction_hash = transaction_hash;
    }

    public String getCaller_address() {
        return caller_address;
    }

    public void setCaller_address(String caller_address) {
        this.caller_address = caller_address;
    }

    public String getContract_address() {
        return contract_address;
    }

    public void setContract_address(String contract_address) {
        this.contract_address = contract_address;
    }

    public ArrayList<String> getCalldata() {
        return calldata;
    }

    public void setCalldata(ArrayList<String> calldata) {
        this.calldata = calldata;
    }

    public ArrayList<String> getResult() {
        return result;
    }

    public void setResult(ArrayList<String> result) {
        this.result = result;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getCall_type() {
        return call_type;
    }

    public void setCall_type(String call_type) {
        this.call_type = call_type;
    }

    public String getClass_hash() {
        return class_hash;
    }

    public void setClass_hash(String class_hash) {
        this.class_hash = class_hash;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getEntry_point_type() {
        return entry_point_type;
    }

    public void setEntry_point_type(String entry_point_type) {
        this.entry_point_type = entry_point_type;
    }

    public String getSelector_name() {
        return selector_name;
    }

    public void setSelector_name(String selector_name) {
        this.selector_name = selector_name;
    }
}
