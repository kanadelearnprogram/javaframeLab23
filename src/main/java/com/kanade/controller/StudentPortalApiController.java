package com.kanade.controller;

import com.kanade.constant.Constant;
import com.kanade.entity.User;
import com.kanade.entity.vo.ApiResponse;
import com.kanade.entity.vo.PageResult;
import com.kanade.entity.vo.StudentCourseVO;
import com.kanade.entity.vo.StudentScoreVO;
import com.kanade.service.StudentPortalService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentPortalApiController {

    private final StudentPortalService studentPortalService;

    @GetMapping("/courses")
    public ApiResponse<PageResult<StudentCourseVO>> listSelectedCourses(HttpSession session,
                                                                       @RequestParam(value = "page", required = false) Integer page,
                                                                       @RequestParam(value = "limit", required = false) Integer limit,
                                                                       @RequestParam(value = "sortField", required = false) String sortField,
                                                                       @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        User user = getLoginUser(session);
        if (user == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        if (!"student".equals(user.getRole())) {
            return ApiResponse.forbidden("仅学生可访问");
        }
        try {
            PageResult<StudentCourseVO> result = studentPortalService.listSelectedCourses(user, page, limit, sortField, sortOrder);
            return ApiResponse.success(result);
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/scores")
    public ApiResponse<PageResult<StudentScoreVO>> listScores(HttpSession session,
                                                              @RequestParam(value = "page", required = false) Integer page,
                                                              @RequestParam(value = "limit", required = false) Integer limit,
                                                              @RequestParam(value = "sortField", required = false) String sortField,
                                                              @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        User user = getLoginUser(session);
        if (user == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        if (!"student".equals(user.getRole())) {
            return ApiResponse.forbidden("仅学生可访问");
        }
        try {
            PageResult<StudentScoreVO> result = studentPortalService.listPersonalScores(user, page, limit, sortField, sortOrder);
            return ApiResponse.success(result);
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    private User getLoginUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object attr = session.getAttribute(Constant.LOGIN_USER);
        if (attr instanceof User user) {
            return user;
        }
        return null;
    }
}

