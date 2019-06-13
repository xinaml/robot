package com.xinaml.robot.common.thread;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CopyFileThread extends Thread{
    private File toFile;
    private File fromFile;
    private boolean exists;
    public CopyFileThread(File fromFile,File toFile,boolean exists) {
        this.fromFile=fromFile;
        this.toFile=toFile;
        this.exists =exists;
    }
    @Override
    public void run() {
        try {
            if (fromFile.isFile()) {
                if(exists){
                    FileUtils.copyFile(fromFile, toFile);
                }else {
                    FileUtils.copyFileToDirectory(fromFile, toFile, true);
                }
            } else {
                FileUtils.copyDirectoryToDirectory(fromFile, toFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
