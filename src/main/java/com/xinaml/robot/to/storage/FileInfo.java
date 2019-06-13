package com.xinaml.robot.to.storage;

import org.springframework.web.multipart.MultipartFile;

public class FileInfo {
    private String guid;//文件名
    private String md5value;//客户端生成md5值
    private String chunks;//分块数
    private String chunk;//分块序号
    private String id;//文件id便于区分
    private String name;//传文件名带后缀
    private String type;// 文件类型
    private String lastModifiedDate;//上次修改
    private int size;//文件大小
    private String partName;//分片文件名
    private String ext;//后缀名
    private String path;//上传文件夹路径
    private Long relevanceId; //文件关联id
    private MultipartFile file;//文件本身

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getMd5value() {
        return md5value;
    }

    public void setMd5value(String md5value) {
        this.md5value = md5value;
    }

    public String getChunks() {
        return chunks;
    }

    public void setChunks(String chunks) {
        this.chunks = chunks;
    }

    public String getChunk() {
        return chunk;
    }

    public void setChunk(String chunk) {
        this.chunk = chunk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getRelevanceId() {
        return relevanceId;
    }

    public void setRelevanceId(Long relevanceId) {
        this.relevanceId = relevanceId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
