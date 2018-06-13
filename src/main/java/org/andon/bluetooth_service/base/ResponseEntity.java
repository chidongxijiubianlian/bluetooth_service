package org.andon.bluetooth_service.base;

//返回值
//2
public class ResponseEntity<T> {
    public double code;
    public String message;
    public long ts;
    public T result;

    public ResponseEntity(T entity)
    {
        this.result =entity;
        this.setCode(200);
        this.setMessage("ok");
        this.setTs(System.currentTimeMillis());
    }
    public ResponseEntity(double code,String message)
    {
        this.result =null;
        this.setCode(code);
        this.setMessage(message);
        this.setTs(System.currentTimeMillis());
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public double getCode() {
        return code;
    }
    public void setCode(double code) {
        this.code = code;
    }
}
