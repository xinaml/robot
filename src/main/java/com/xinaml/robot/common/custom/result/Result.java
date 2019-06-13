package com.xinaml.robot.common.custom.result;

/**
 * action 返回接口
 *
 * @author lgq
 * @date 2018/4/15
 **/
public interface Result<T> {
    /**
     * 消息码 1 自定义异常，-1,系统异常，其他
     */
    int getCode();

    /**
     * 错误消息
     */
    String getMsg();

    /**
     * 返回数据
     */
    T getData();
}
