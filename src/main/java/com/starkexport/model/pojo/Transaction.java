package com.starkexport.model.pojo;

import java.util.ArrayList;

public class Transaction {
    private String next_url;
    private ArrayList<Data> data;
    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
