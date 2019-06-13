package com.xinaml.robot.types;

public enum FieldType {
    STRING(String.class.getName()),
    INTEGER(Integer.class.getName()),
    DOUBLE(Double.class.getName());
    private String code;

    FieldType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
