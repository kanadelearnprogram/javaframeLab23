package com.kanade.controller;

import com.kanade.entity.dto.LoginDTO;
import com.kanade.entity.dto.UserDTO;
import com.kanade.entity.vo.UserVO;
import com.kanade.service.TUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.kanade.constant.Constant.LOGIN_USER;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private TUserService userService ;

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO, HttpServletRequest request){
        TUser login = userService.login(loginDTO, request);
        log.info(String.valueOf(login));
        return login.toString();
    }

    @PostMapping("/add")
    public Boolean addUser(HttpServletRequest request, UserDTO user){
        TUser loginUser = (TUser) request.getSession().getAttribute(LOGIN_USER);
        if (loginUser == null || !loginUser.getRole().equals("admin")) {
            throw new RuntimeException("not authority");
        }
        boolean addUser = userService.addUser(user);
        return addUser;
    }

    @PostMapping("/update")
    public UserVO update(HttpServletRequest request, UserDTO user){
        return null;
    }
}
