package com.xinaml.robot.base.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL) //jackson忽略空字段
public abstract class BaseVO implements Serializable {
}
