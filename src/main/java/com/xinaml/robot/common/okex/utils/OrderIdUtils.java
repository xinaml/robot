package com.xinaml.robot.common.okex.utils;


import com.xinaml.robot.common.okex.constant.APIConstants;

import java.util.UUID;

/**
 * OrderVO Id Utils
 *
 * @author Tony Tian
 * @version 1.0.0
 * @date 2018/3/14 10:02
 */
public class OrderIdUtils {

    /**
     * The order ids, use uuid and remove the line dividing line. <br/>
     * id length = 32
     *
     * @return String eg: 39360db0a45e41309511f4fba658b01c
     */
    public static String generator() {
        return UUID.randomUUID().toString().toLowerCase().replace(APIConstants.HLINE, APIConstants.EMPTY);
    }

    public static void main(String[] args) {
        System.out.println(generator());
    }
}
