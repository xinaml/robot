package com.xinaml.robot.common.custom.result;

import com.alibaba.fastjson.JSON;

/**
 * action 返回对象
 *
 * @author lgq
 * @date 2018/4/15
 **/
public class ActResult<T> implements Result {
    private int code = 0;
    private String msg;
    private T data;

    public ActResult() {
    }

    public ActResult(String msg) {
        this.msg = msg;
    }

    public ActResult(T data) {
        this.data = data;
    }

    public ActResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ActResult(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }

    public ActResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{\"code\":");
        sb.append(code);
        sb.append(",");
        sb.append("\"msg\":\"");
        sb.append(msg);
        sb.append("\"");
        if (null != data) {
            sb.append(",\"data\":\"");
            sb.append(JSON.toJSONString(data));
            sb.append("\"");
        }
        sb.append("}");
        return sb.toString();
    }


}
