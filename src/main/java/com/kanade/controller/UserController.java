package com.kanade.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.kanade.entity.User;
import com.kanade.entity.UserMyBatis;
import com.kanade.mapper.UserMapper;
import com.kanade.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    // 游客
    @PostMapping("/register")
    public String register(@RequestParam("password")String password,@RequestParam("username") String username){
        SqlSession session = null;
        try{
            session = MyBatisUtil.getSession();
            UserMapper userMapper = session.getMapper(UserMapper.class);
            User user = new User();
            Snowflake snowflake = IdUtil.createSnowflake(1, System.currentTimeMillis());
            user.setUserId(snowflake.nextIdStr());
            user.setPassword(password);
            user.setUsername(username);
            user.setRole("visitor");
            User user1 = userMapper.selectUserName(username);
            if(user1 != null){
                throw new RuntimeException("username is chongfu");
            }
            userMapper.addUser(user);
            return user.getUserId();
        }catch (Exception e){
             e.printStackTrace();
        }
        return null;
    }
}
