package com.xinaml.robot.vo.storage;


import com.xinaml.robot.base.vo.BaseVO;

public class FileVO extends BaseVO implements Comparable<FileVO> {

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小
     */
    private String size;
    /**
     * 文件长度
     */
    private Long length;

    /**
     * 是否为目录
     */
    private Boolean dir;


    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 父目录
     */
    private String parentPath;


    /**
     * 大图
     */
    private String bigPicture;

    /**
     * 小图
     */
    private String minPicture;
    /**
     * 更新时间
     */
    private String modifyTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getDir() {
        return dir;
    }

    public void setDir(Boolean dir) {
        this.dir = dir;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getBigPicture() {
        return bigPicture;
    }

    public void setBigPicture(String bigPicture) {
        this.bigPicture = bigPicture;
    }

    public String getMinPicture() {
        return minPicture;
    }

    public void setMinPicture(String minPicture) {
        this.minPicture = minPicture;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public int compareTo(FileVO o) {
        return this.getModifyTime().compareTo(o.getModifyTime());
    }
}
