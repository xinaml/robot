package com.xinaml.robot.types;

public enum SexType {
    MAN(0),
    WOMAN(1),
    UNKNOWN(2);
    private int code;

    SexType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
