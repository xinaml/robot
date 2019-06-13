package com.xinaml.robot.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PathConst {
    public static String ROOT_PATH; //存储根路径
    public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String TMP_PATH = System.getProperty("file.separator") + "tmp";//存储临时目录
    public static final String PREV_URL="prevUrl"; // 上次请求页面url

    @Value("${storage.win.path}")
    private String winPath;
    @Value("${storage.linux.path}")
    private String linuxPath;


    @PostConstruct
    public void initPath() {
        if (System.getProperty("file.separator").equals("/")) { // linux 系统路径
            PathConst.ROOT_PATH = linuxPath;
        } else {
            PathConst.ROOT_PATH = winPath; // win 系统路径
        }
    }




}
