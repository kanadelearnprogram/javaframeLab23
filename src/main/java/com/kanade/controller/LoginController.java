package com.kanade.controller;

import com.kanade.constant.Constant;
import com.kanade.entity.User;
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
            if (user == null || !user.getPassword().equals(password)) {
                return "redirect:/login?error=invalid";
            }

            request.getSession().setAttribute(Constant.LOGIN_USER, user);
            String role = user.getRole();
            return switch (role) {
                case "admin" -> "redirect:/admin/index.jsp";
                case "student" -> "redirect:/student/index.jsp";
                case "teacher" -> "redirect:/teacher/index.jsp";
                default -> "redirect:/index.jsp";
            };
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
    }
}