package com.xinaml.robot.entity.storage;

import com.xinaml.robot.base.entity.BaseEntity;
import com.xinaml.robot.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name = "tb_storage")
public class Storage extends BaseEntity {
    @Column(nullable = false, columnDefinition = "VARCHAR(50) COMMENT 'md5' ")
    private String md5;

    @Column(nullable = false, columnDefinition = "VARCHAR(56) COMMENT '路径' ")
    private String path;

    @Column(nullable = false, columnDefinition = "VARCHAR(56) COMMENT '文件名' ")
    private String fileName;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "VARCHAR(36) COMMENT '所属用户' ")
    private User user;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
