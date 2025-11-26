package com.kanade.controller;

import com.kanade.entity.User;
import com.kanade.entity.UserMyBatis;
import com.kanade.mapper.UserMapper;
import com.kanade.util.MyBatisUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(HttpServletRequest request,
                       @RequestParam("password") String password, 
                       @RequestParam("username") String username){
        SqlSession sess = null;
        try {
            sess = MyBatisUtil.getSession();
            UserMapper userMapper = sess.getMapper(UserMapper.class);
            User user = userMapper.selectUserName(username);
            if (user == null){
                throw new RuntimeException("username");
            }
            if (!user.getPassword().equals(password)) {
                throw new RuntimeException("password is wrong");
            }

            request.getSession().setAttribute("userLogin",user);
            String role = user.getRole();
            switch (role){
                case "admin" ->{
                    break;
                }
                case "student"->{
                    break;
                }
                case "teacher" ->{

                }
            }
            return "redirect:/index.jsp";
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
    }
}