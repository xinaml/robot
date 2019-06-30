package com.xinaml.robot.ser.user;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.base.dto.RT;
import com.xinaml.robot.base.rep.RedisRep;
import com.xinaml.robot.base.ser.ServiceImpl;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.okex.threads.ThreadScan;
import com.xinaml.robot.common.utils.PassWordUtil;
import com.xinaml.robot.common.utils.TokenUtil;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.dto.user.UserConfDTO;
import com.xinaml.robot.dto.user.UserDTO;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.rep.user.UserRep;
import com.xinaml.robot.ser.okex.AutoTradeSer;
import com.xinaml.robot.to.user.LoginTO;
import com.xinaml.robot.to.user.RegisterTO;
import com.xinaml.robot.to.user.UserSecretTO;
import com.xinaml.robot.vo.user.KLine;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserSerImp extends ServiceImpl<User, UserDTO> implements UserSer {
    private static Logger LOGGER = LoggerFactory.getLogger(UserSerImp.class);
    @Autowired
    private RedisRep jRedis;
    @Autowired
    private UserRep userRep;
    @Autowired
    private UserConfSer userConfSer;
    @Autowired
    private AutoTradeSer autoTradeSer;

    @Override
    public String login(LoginTO to) throws SerException {
        UserDTO dto = new UserDTO();
        dto.addRT(RT.eq("username", to.getUsername())); //username 必须唯一
        User user = findOne(dto);
        String token = null;
        boolean isPass = false;
        if (null != user) {
            try {
                isPass = PassWordUtil.validPwd(to.getPassword(), user.getPassword());
            } catch (Exception e) {
                LOGGER.error("密码解析错误！");
            }
        } else {
            throw new SerException("账号或密码错误！");
        }
        if (isPass) {
            token = TokenUtil.create(to.getIp());
            jRedis.put(token, JSON.toJSONString(user));
            return token;
        } else {
            throw new SerException("账号或密码错误！");
        }
    }

    @Override
    public String register(RegisterTO to) throws SerException {
        if (to.getPassword().equals(to.getRePassword())) {
            if (null == userRep.findByUsername(to.getUsername())) {
                try {
                    User user = new User();
                    user.setUsername(to.getUsername());
                    user.setPassword(PassWordUtil.genSaltPwd(to.getPassword()));
                    user.setCreateDate(LocalDateTime.now());
                    super.save(user);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SerException(e.getMessage());
                }
            } else {
                throw new SerException("用户名已被占用！");
            }

        } else {
            throw new SerException("密码不一致！");
        }

        return null;
    }

    @Override
    public Boolean logout(String token) throws SerException {
        if (StringUtils.isNotBlank(token)) {
            jRedis.del(token);
        }
        return true;
    }

    @Transactional
    @Override
    public void editSecret(UserSecretTO to) {
        User user = super.findById(to.getId());
        BeanUtils.copyProperties(to, user);
        UserConfDTO dto = new UserConfDTO();
        dto.addRT(RT.eq("user.id", user.getId()));
        UserConf conf = userConfSer.findOne(dto);
        KLine line = null;
        try {
            line =autoTradeSer.getLine(conf);
            if (line != null) {
                this.update(user);
                ThreadScan.scan(user.getId(), user.getStop(), conf);//重新扫描配置
            } else {
                throw new SerException("保存错误!请检查账号、secretKey、apiKey是否正确！");
            }
        } catch (Exception e) {
            throw new SerException("保存错误!请检查账号、secretKey、apiKey是否正确！");
        }

    }

    @Override
    public Boolean stop() throws SerException {
        User user = UserUtil.getUser();
        user.setStop(!user.getStop());
        String str = null;
        try {
            str = jRedis.get(user.getId() + user.getUsername());
        } catch (Exception e) {
        }
        if (null == str) {
            throw new SerException("请先配置信息！");
        }
        this.update(user);
        ThreadScan.scan(user.getId(), user.getStop(), JSON.parseObject(str, UserConf.class));
        return user.getStop();
    }

    /**
     * 以方法名+查询条件做缓存（例子）
     *
     * @param dto
     * @return
     * @throws SerException
     */
    @Cacheable(value = "users", key = "#root.methodName.concat(#dto.serId)")
    @Override
    public Map<String, Object> findByPage(UserDTO dto) throws SerException {
        return super.findByPage(dto);
    }

    @CacheEvict(value = "users", beforeInvocation = true, allEntries = true)
    @Override
    public void save(User... entities) throws SerException {
        super.save(entities);
    }

    @CacheEvict(value = "users", beforeInvocation = true, allEntries = true)
    @Override
    public void remove(String... ids) throws SerException {
        super.remove(ids);
    }

    @CacheEvict(value = "users", beforeInvocation = true, allEntries = true)
    @Override
    public void update(User... entities) throws SerException {
        User old = UserUtil.getUser();
        for (User u : entities) {
            if (u.getId().equals(old.getId())) {
                String token = UserUtil.getToken();
                jRedis.put(token, JSON.toJSONString(u));
            }
        }
        super.update(entities);
    }


}
