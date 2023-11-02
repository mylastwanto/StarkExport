package com.starkexport.model.pojo;

import com.starkexport.model.pojo.AccountCall;

import java.util.ArrayList;

public class Data {
    private String transaction_hash;
    private String block_hash;
    private int block_number;
    private int transaction_index;
    private String transaction_status;
    private String transaction_finality_status;
    private String transaction_execution_status;
    private String transaction_type;
    private int version;
    private ArrayList<String> signature;
    private String max_fee;
    private String actual_fee;
    private String nonce;
    private String contract_address;
    private String entry_point_selector;
    private String entry_point_type;
    private ArrayList<String> calldata;
    private String class_hash;
    private String sender_address;
    private ArrayList<String> constructor_calldata;
    private String contract_address_salt;
    private int timestamp;
    private String entry_point_selector_name;
    private int number_of_events;
    private String revert_error;
    private ArrayList<AccountCall> account_calls;

    public String getTransaction_hash() {
        return transaction_hash;
    }

    public void setTransaction_hash(String transaction_hash) {
        this.transaction_hash = transaction_hash;
    }

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

    public int getTransaction_index() {
        return transaction_index;
    }

    public void setTransaction_index(int transaction_index) {
        this.transaction_index = transaction_index;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public String getTransaction_finality_status() {
        return transaction_finality_status;
    }

    public void setTransaction_finality_status(String transaction_finality_status) {
        this.transaction_finality_status = transaction_finality_status;
    }

    public String getTransaction_execution_status() {
        return transaction_execution_status;
    }

    public void setTransaction_execution_status(String transaction_execution_status) {
        this.transaction_execution_status = transaction_execution_status;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ArrayList<String> getSignature() {
        return signature;
    }

    public void setSignature(ArrayList<String> signature) {
        this.signature = signature;
    }

    public String getMax_fee() {
        return max_fee;
    }

    public void setMax_fee(String max_fee) {
        this.max_fee = max_fee;
    }

    public String getActual_fee() {
        return actual_fee;
    }

    public void setActual_fee(String actual_fee) {
        this.actual_fee = actual_fee;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getContract_address() {
        return contract_address;
    }

    public void setContract_address(String contract_address) {
        this.contract_address = contract_address;
    }

    public String getEntry_point_selector() {
        return entry_point_selector;
    }

    public void setEntry_point_selector(String entry_point_selector) {
        this.entry_point_selector = entry_point_selector;
    }

    public String getEntry_point_type() {
        return entry_point_type;
    }

    public void setEntry_point_type(String entry_point_type) {
        this.entry_point_type = entry_point_type;
    }

    public ArrayList<String> getCalldata() {
        return calldata;
    }

    public void setCalldata(ArrayList<String> calldata) {
        this.calldata = calldata;
    }

    public String getClass_hash() {
        return class_hash;
    }

    public void setClass_hash(String class_hash) {
        this.class_hash = class_hash;
    }

    public String getSender_address() {
        return sender_address;
    }

    public void setSender_address(String sender_address) {
        this.sender_address = sender_address;
    }

    public ArrayList<String> getConstructor_calldata() {
        return constructor_calldata;
    }

    public void setConstructor_calldata(ArrayList<String> constructor_calldata) {
        this.constructor_calldata = constructor_calldata;
    }

    public String getContract_address_salt() {
        return contract_address_salt;
    }

    public void setContract_address_salt(String contract_address_salt) {
        this.contract_address_salt = contract_address_salt;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getEntry_point_selector_name() {
        return entry_point_selector_name;
    }

    public void setEntry_point_selector_name(String entry_point_selector_name) {
        this.entry_point_selector_name = entry_point_selector_name;
    }

    public int getNumber_of_events() {
        return number_of_events;
    }

    public void setNumber_of_events(int number_of_events) {
        this.number_of_events = number_of_events;
    }

    public String getRevert_error() {
        return revert_error;
    }

    public void setRevert_error(String revert_error) {
        this.revert_error = revert_error;
    }

    public ArrayList<AccountCall> getAccount_calls() {
        return account_calls;
    }

    public void setAccount_calls(ArrayList<AccountCall> account_calls) {
        this.account_calls = account_calls;
    }
}
