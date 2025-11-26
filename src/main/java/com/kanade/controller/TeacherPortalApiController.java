package com.kanade.controller;

import com.kanade.constant.Constant;
import com.kanade.entity.Course;
import com.kanade.entity.User;
import com.kanade.entity.dto.ScoreUpdateRequest;
import com.kanade.entity.vo.*;
import com.kanade.service.TeacherPortalService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherPortalApiController {

    private final TeacherPortalService teacherPortalService;

    @GetMapping("/classes")
    public ApiResponse<List<TeacherClassVO>> listClasses(HttpSession session) {
        User teacher = getTeacher(session);
        if (teacher == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        if (!"teacher".equals(teacher.getRole())) {
            return ApiResponse.forbidden("仅教师可访问");
        }
        return ApiResponse.success(teacherPortalService.listTeacherClasses(teacher));
    }

    @GetMapping("/classes/{classId}/courses")
    public ApiResponse<List<Course>> listCourses(HttpSession session,
                                                 @PathVariable("classId") Integer classId) {
        User teacher = getTeacher(session);
        if (teacher == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        if (!"teacher".equals(teacher.getRole())) {
            return ApiResponse.forbidden("仅教师可访问");
        }
        try {
            return ApiResponse.success(teacherPortalService.listCoursesUnderClass(teacher, classId));
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/classes/{classId}/scores")
    public ApiResponse<PageResult<TeacherScoreVO>> listScores(HttpSession session,
                                                              @PathVariable("classId") Integer classId,
                                                              @RequestParam("courseId") Integer courseId,
                                                              @RequestParam(value = "page", required = false) Integer page,
                                                              @RequestParam(value = "limit", required = false) Integer limit,
                                                              @RequestParam(value = "sortField", required = false) String sortField,
                                                              @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        User teacher = getTeacher(session);
        if (teacher == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        if (!"teacher".equals(teacher.getRole())) {
            return ApiResponse.forbidden("仅教师可访问");
        }
        try {
            PageResult<TeacherScoreVO> pageResult = teacherPortalService.listClassScores(teacher,
                    classId,
                    courseId,
                    page,
                    limit,
                    sortField,
                    sortOrder);
            return ApiResponse.success(pageResult);
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    @PutMapping("/scores/{scoreId}")
    public ApiResponse<TeacherScoreVO> updateScore(HttpSession session,
                                                   @PathVariable("scoreId") Long scoreId,
                                                   @RequestBody ScoreUpdateRequest request) {
        User teacher = getTeacher(session);
        if (teacher == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        if (!"teacher".equals(teacher.getRole())) {
            return ApiResponse.forbidden("仅教师可访问");
        }
        try {
            TeacherScoreVO updated = teacherPortalService.updateSingleScore(teacher,
                    scoreId,
                    request.getDailyScore(),
                    request.getExamScore());
            return ApiResponse.success("更新成功", updated);
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    @PostMapping(value = "/scores/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ImportResultVO> importScores(HttpSession session,
                                                    @RequestParam("classId") Integer classId,
                                                    @RequestParam("courseId") Integer courseId,
                                                    @RequestParam("file") MultipartFile file) {
        User teacher = getTeacher(session);
        if (teacher == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        if (!"teacher".equals(teacher.getRole())) {
            return ApiResponse.forbidden("仅教师可访问");
        }
        try {
            ImportResultVO result = teacherPortalService.importScores(teacher, classId, courseId, file);
            return ApiResponse.success(result);
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (IOException e) {
            return ApiResponse.serverError("解析文件失败");
        }
    }

    @GetMapping("/scores/template")
    public void downloadTemplate(HttpSession session,
                                 HttpServletResponse response,
                                 @RequestParam("classId") Integer classId,
                                 @RequestParam("courseId") Integer courseId) throws IOException {
        User teacher = getTeacher(session);
        if (!isTeacher(teacher, response)) {
            return;
        }
        try {
            byte[] bytes = teacherPortalService.buildImportTemplate(teacher, classId, courseId);
            String filename = "class_" + classId + "_course_" + courseId + "_template.xlsx";
            writeFile(response, bytes, filename,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        }
    }

    @GetMapping("/classes/{classId}/courses/{courseId}/stats")
    public ApiResponse<ScoreStatsVO> stats(HttpSession session,
                                           @PathVariable("classId") Integer classId,
                                           @PathVariable("courseId") Integer courseId) {
        User teacher = getTeacher(session);
        if (teacher == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        if (!"teacher".equals(teacher.getRole())) {
            return ApiResponse.forbidden("仅教师可访问");
        }
        try {
            return ApiResponse.success(teacherPortalService.buildStats(teacher, classId, courseId));
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/classes/{classId}/courses/{courseId}/scores/export")
    public void exportScores(HttpSession session,
                             HttpServletResponse response,
                             @PathVariable("classId") Integer classId,
                             @PathVariable("courseId") Integer courseId) throws IOException {
        User teacher = getTeacher(session);
        if (!isTeacher(teacher, response)) {
            return;
        }
        try {
            FilePayload payload = teacherPortalService.exportScores(teacher, classId, courseId);
            writeFile(response, payload.getContent(), payload.getFilename(), payload.getContentType());
        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        }
    }

    private void writeFile(HttpServletResponse response,
                           byte[] bytes,
                           String filename,
                           String contentType) throws IOException {
        response.setContentType(contentType);
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encoded);
        response.getOutputStream().write(bytes);
        response.flushBuffer();
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String safeMsg = message == null ? "" : message.replace("\"", "'");
        response.getWriter().write("{\"code\":400,\"msg\":\"" + safeMsg + "\"}");
    }

    private boolean isTeacher(User teacher, HttpServletResponse response) throws IOException {
        if (teacher == null) {
            sendError(response, "请先登录");
            return false;
        }
        if (!"teacher".equals(teacher.getRole())) {
            sendError(response, "仅教师可访问");
            return false;
        }
        return true;
    }

    private User getTeacher(HttpSession session) {
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

