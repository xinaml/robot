/**
 * 实体基类
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.robot.base.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类
 */
@JsonInclude(JsonInclude.Include.NON_NULL) //jackson忽略空字段
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity implements Serializable {

    /**
     * id为 int 或者 long 类型时配置
     *
     * @Id
     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, length = 36, updatable = false, insertable = false)
    private String id;

    @Column(nullable = false, columnDefinition = "DATETIME  COMMENT '创建时间'")
    private LocalDateTime createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

}
