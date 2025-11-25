package com.kanade.controller;

import com.kanade.entity.Course;
import com.kanade.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 单条添加课程
     */
    @PostMapping("/add")
    public Map<String, Object> addCourse(@RequestBody Course course) {
        Map<String, Object> response = new HashMap<>();
        
        // 检查课程名称是否已存在
        Course existingCourse = courseMapper.selectOneByCourseName(course.getCourseName());
        if (existingCourse != null) {
            response.put("code", 400);
            response.put("msg", "课程名称已存在");
            response.put("data", null);
            return response;
        }
        
        // 检查权重之和是否为1.0
        BigDecimal dailyRatio = course.getDailyRatio();
        BigDecimal examRatio = course.getExamRatio();
        BigDecimal sum = dailyRatio.add(examRatio);
        if (sum.compareTo(BigDecimal.ONE) != 0) {
            response.put("code", 400);
            response.put("msg", "权重之和必须为1.0");
            response.put("data", null);
            return response;
        }
        
        // 插入课程
        courseMapper.insert(course);
        
        response.put("code", 200);
        response.put("msg", "课程添加成功");
        response.put("data", course);
        return response;
    }

    /**
     * 批量添加课程
     */
    @PostMapping("/batch-add")
    public Map<String, Object> batchAddCourses(@RequestBody List<Course> courses) {
        Map<String, Object> response = new HashMap<>();
        
        int successCount = 0;
        int failCount = 0;
        StringBuilder failList = new StringBuilder();
        
        for (Course course : courses) {
            // 检查课程名称是否已存在
            Course existingCourse = courseMapper.selectOneByCourseName(course.getCourseName());
            if (existingCourse != null) {
                failCount++;
                failList.append(course.getCourseName()).append(": 课程名称已存在; ");
                continue;
            }
            
            // 检查权重之和是否为1.0
            BigDecimal dailyRatio = course.getDailyRatio();
            BigDecimal examRatio = course.getExamRatio();
            BigDecimal sum = dailyRatio.add(examRatio);
            if (sum.compareTo(BigDecimal.ONE) != 0) {
                failCount++;
                failList.append(course.getCourseName()).append(": 权重之和必须为1.0; ");
                continue;
            }
            
            // 插入课程
            courseMapper.insert(course);
            successCount++;
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("successCount", successCount);
        data.put("failCount", failCount);
        data.put("failList", failList.toString());
        
        response.put("code", 200);
        response.put("msg", "批量添加成功");
        response.put("data", data);
        return response;
    }

    /**
     * 查询课程列表
     */
    @GetMapping("/list")
    public Map<String, Object> listCourses(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "classId", required = false) Integer classId,
            @RequestParam(value = "teacherId", required = false) String teacherId,
            @RequestParam(value = "courseName", required = false) String courseName) {
        
        Map<String, Object> response = new HashMap<>();
        
        // 这里应该实现分页查询逻辑，目前简化处理
        List<Course> records = courseMapper.selectAll();
        
        // 应该根据条件过滤，这里简化处理
        Map<String, Object> data = new HashMap<>();
        data.put("total", records.size());
        data.put("pages", 1);
        data.put("current", page);
        data.put("size", limit);
        data.put("records", records);
        
        response.put("code", 200);
        response.put("msg", "查询成功");
        response.put("data", data);
        return response;
    }

    /**
     * 批量删除课程
     */
    @PostMapping("/batch-delete")
    public Map<String, Object> batchDeleteCourses(@RequestBody Map<String, List<Integer>> request) {
        Map<String, Object> response = new HashMap<>();
        
        List<Integer> courseIds = request.get("courseIds");
        int successCount = 0;
        int failCount = 0;
        StringBuilder failList = new StringBuilder();
        
        for (Integer courseId : courseIds) {
            // 检查课程是否存在
            Course course = courseMapper.selectOneById(courseId);
            if (course == null) {
                failCount++;
                failList.append("课程ID=").append(courseId).append(": 课程不存在; ");
                continue;
            }
            
            // 检查课程是否关联成绩，这里简化处理
            // 实际应用中需要检查关联关系
            
            // 删除课程
            courseMapper.deleteById(courseId);
            successCount++;
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("successCount", successCount);
        data.put("failCount", failCount);
        data.put("failList", failList.toString());
        
        response.put("code", 200);
        response.put("msg", "批量删除成功");
        response.put("data", data);
        return response;
    }
}