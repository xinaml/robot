package com.xinaml.frame;

import com.alibaba.fastjson.JSON;
import com.xinaml.frame.base.dto.RT;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.dto.user.UserDTO;
import com.xinaml.frame.entity.user.User;
import com.xinaml.frame.ser.user.UserSer;
import com.xinaml.frame.to.user.UserTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppRoot.class)
@WebAppConfiguration
public class UserTest {
    @Autowired
    private UserSer userSer;
    private String userId;

    @Before
    public void init()throws SerException{
        UserDTO dto = new UserDTO();
        dto.addRT(RT.eq("username","lgq"));
       User u = userSer.findOne(dto);
       if(null!=u){
           userId = u.getId();
       }else {
           u = new User();
           u.setEmail("xinaml@163.com");
           u.setPhone("199999999");
           u.setUsername("lgq");
           u.setCreateDate(LocalDateTime.now());
           userSer.save(u);
       }
    }

    @Test
    public void query() throws SerException {
        System.out.println(JSON.toJSONString(userSer.findAll()));
    }

    @Test
    public void queryByRT() throws SerException {
        UserDTO dto=new UserDTO();
        dto.addRT(RT.eq("username", "lgq"));
        dto.addRT(RT.like("email", "163"));
        dto.addRT(RT.isNotNull("username"));
        System.out.println(JSON.toJSONString(userSer.findByRTS(dto)));
    }

    @Transactional
    @Rollback
    @Test
    public void add() throws SerException {
        UserTO to = new UserTO();
        to.setEmail("xxxd@163.com");
        to.setPhone("13457555555");
        to.setUsername("mmd");
        User user = new User();
        BeanUtils.copyProperties(to, user);
        user.setCreateDate(LocalDateTime.now());
        userSer.save(user);
        System.out.println(JSON.toJSON(user));
    }

    @Transactional
    @Rollback
    @Test
    public void edit() throws SerException {
        User user = userSer.findById(userId);
        user.setEmail("xxxyy@163.com");
        user.setPhone("18888888888");
        userSer.update(user);
    }

    @Transactional
    @Rollback
    @Test
    public void del() throws SerException {
        userSer.remove(userId);
    }

}
