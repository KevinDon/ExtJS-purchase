package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Mark on 2017/12/12.
 */
public class OmsApiDto<T> implements Serializable{

    private String status;

    private String msg;

    @JsonProperty("has_next")
    private String hasNext;

    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getHasNext() {
        return hasNext;
    }

    public void setHasNext(String hasNext) {
        this.hasNext = hasNext;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
