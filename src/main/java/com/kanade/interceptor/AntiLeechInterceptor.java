package com.kanade.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AntiLeechInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 获取Referer头
        String referer = request.getHeader("Referer");

        // 获取上下文路径
        String contextPath = request.getContextPath();

        // 如果没有Referer头，可能是直接访问，允许访问登录相关页面
        if (referer == null) {
            String requestURI = request.getRequestURI();
            if (requestURI.contains("/login") || requestURI.contains("/index")) {
                return true;
            }
        }

        // 获取服务器信息
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String serverUrl = "http://" + serverName + ":" + serverPort + contextPath;

        // 检查Referer是否来自本站
        if (referer != null && referer.startsWith(serverUrl)) {
            return true; // 允许访问
        }

        // 检查是否是登录页面或公共资源的请求
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/login") || requestURI.equals(contextPath + "/") || requestURI.equals(contextPath)) {
            return true;
        }

        // 非法请求，重定向到登录页
        response.sendRedirect(contextPath + "/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 在处理请求之后，渲染视图之前执行
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        // 在整个请求完成之后执行
    }
}