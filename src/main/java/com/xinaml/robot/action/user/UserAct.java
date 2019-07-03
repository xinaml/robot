package com.xinaml.robot.action.user;

import com.xinaml.robot.base.atction.BaseAct;
import com.xinaml.robot.base.dto.RT;
import com.xinaml.robot.base.entity.ADD;
import com.xinaml.robot.base.entity.EDIT;
import com.xinaml.robot.common.custom.annotation.Login;
import com.xinaml.robot.common.custom.exception.ActException;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.custom.result.Result;
import com.xinaml.robot.common.utils.PassWordUtil;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.dto.storage.StorageDTO;
import com.xinaml.robot.dto.user.UserDTO;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.ser.storage.StorageSer;
import com.xinaml.robot.ser.user.UserSer;
import com.xinaml.robot.to.user.UserSecretTO;
import com.xinaml.robot.to.user.UserTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("user")
@Login
public class UserAct extends BaseAct {

    @Autowired
    private UserSer userSer;

    @Autowired
    private StorageSer storageSer;

    @GetMapping({"page"})
    public ModelAndView user() throws ActException {
        return new ModelAndView("user/user");
    }

    @PostMapping("stop")
    public Result stop() throws ActException {
        boolean rs = userSer.stop();
        return new ActResult(0, "操作成功！", rs);
    }

    @GetMapping("maps")
    public Map<String, Object> maps(UserDTO dto) throws ActException {
        try {
            Map<String, Object> maps = userSer.findByPage(dto);
            return maps;
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    @GetMapping("list")
    public Result<User> list(UserDTO dto) throws ActException {
        try {
            StorageDTO storageDTO = new StorageDTO();
            storageDTO.addRT(RT.like("user.username", "lgq", JoinType.LEFT));
            storageDTO.addRT(RT.like("path", "lgq1"));
            storageSer.findByRTS(storageDTO);
            dto.addRT(RT.eq("username", "lgq"));
            return new ActResult("获取列表成功！", userSer.findByRTS(dto));
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    @GetMapping("info")
    public Result info() throws ActException {
        User user = UserUtil.getUser();
        return new ActResult(0, "获取用户信息成功！", user);
    }

    @PostMapping("add")
    public Result add(@Validated(ADD.class) UserTO to, BindingResult rs) throws ActException {
        try {
            User user = new User();
            BeanUtils.copyProperties(to, user);
            try {
                user.setPassword(PassWordUtil.genSaltPwd("123456"));
            } catch (Exception e) {
                throw new ActException(e.getMessage());
            }
            user.setCreateDate(LocalDateTime.now());
            userSer.save(user);
            return new ActResult("添加用户成功！");
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }


    // DeleteMapping 这种方式接收不到数组参数，见下一方法，必须用过url来传参
    @PostMapping("del")
    public Result del(String[] ids) throws ActException {
        try {
            userSer.remove(ids);
            return new ActResult("删除用户成功！");
        } catch (Exception e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * Restful 规范
     *
     * @param id
     * @return
     * @throws ActException
     */
    @DeleteMapping("del/{id}")
    public Result del(@PathVariable String id) throws ActException {
        try {
            userSer.remove(id);
            return new ActResult("删除用户成功！");
        } catch (Exception e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 编辑secret apiKey
     *
     * @param to
     * @param rs
     * @return
     * @throws ActException
     */
    @PutMapping("edit/secret")
    public Result editSecret(@Validated(EDIT.class) UserSecretTO to, BindingResult rs) throws ActException {
        try {
            userSer.editSecret(to);
            return new ActResult("编辑成功！");
        } catch (Exception e) {
            throw new ActException(e.getMessage());
        }
    }

    @PutMapping("edit")
    public Result edit(@Validated(EDIT.class) UserTO to, BindingResult rs) throws ActException {
        try {
            to.setExpired(true);
            User user = userSer.findById(to.getId());
            BeanUtils.copyProperties(to, user);
            userSer.update(user);
            return new ActResult("编辑用户成功！");
        } catch (Exception e) {
            throw new ActException(e.getMessage());
        }
    }

}
