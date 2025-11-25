package com.kanade.service;

import com.kanade.entity.dto.LoginDTO;
import com.kanade.entity.dto.UserDTO;
import com.kanade.mapper.TUserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.kanade.constant.Constant.LOGIN_USER;

/**
 * 统一用户表 服务层。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Slf4j
@Service
public class TUserService  {

    @Autowired
    TUserMapper userMapper;
    public TUser login(LoginDTO loginDTO, HttpServletRequest request){
        if (loginDTO == null){
            throw new RuntimeException("loginDTO is null");
        }

        String username = loginDTO.getId();
        String password = loginDTO.getPassword();
        if (username == null){
            throw new RuntimeException("id is null");
        }
        if (password == null){
            throw new RuntimeException("Password is null");
        }
        TUser loginUser = userMapper.getLoginUser(username, password);

        request.getSession().setAttribute(LOGIN_USER,loginUser);

        return loginUser;
    };

    public boolean addUser(UserDTO user) {
        TUser user1 = new TUser();

        if (user.getRole().equals("admin")) {
            user.setId("A"+ user.getId());
        }
        if (user.getRole().equals("student")) {
            user.setId("S"+ user.getId());
        }
        if (user.getRole().equals("teacher")) {
            user.setId("T"+ user.getId());
        }

        BeanUtils.copyProperties(user,user1);

        log.info(user1.toString());
        return true;
    }
}
