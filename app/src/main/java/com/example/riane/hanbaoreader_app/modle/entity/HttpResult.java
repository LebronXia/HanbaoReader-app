package com.example.riane.hanbaoreader_app.modle.entity;

/**
 * Created by Riane on 2016/4/15.
 */
public class HttpResult<T> {
    private int resultCode;
    private String resultMessage;
    //用来模仿Data
    private T object;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
