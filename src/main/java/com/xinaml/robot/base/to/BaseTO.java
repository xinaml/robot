package com.xinaml.robot.base.to;

import com.xinaml.robot.base.entity.DEL;
import com.xinaml.robot.base.entity.EDIT;
import com.xinaml.robot.base.entity.GET;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public abstract class BaseTO implements Serializable {

    @NotBlank(groups = {GET.class, DEL.class, EDIT.class}, message = "id不能为空！")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
