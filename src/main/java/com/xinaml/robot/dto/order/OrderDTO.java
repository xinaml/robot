package com.xinaml.robot.dto.order;

import com.xinaml.robot.base.dto.BaseDTO;

public class OrderDTO extends BaseDTO {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
