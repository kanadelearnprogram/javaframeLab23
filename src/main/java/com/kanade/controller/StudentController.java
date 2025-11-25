package com.kanade.controller;

import com.kanade.entity.Student;
import com.kanade.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 单条添加学生
     */
    @PostMapping("/add")
    public Map<String, Object> addStudent(@RequestBody Student student) {
        Map<String, Object> response = new HashMap<>();
        
        // 检查学号是否已存在
        Student existingStudent = studentMapper.selectOneByStudentId(student.getStudentId());
        if (existingStudent != null) {
            response.put("code", 400);
            response.put("msg", "学号已存在");
            response.put("data", null);
            return response;
        }
        
        // 检查userId是否已存在
        Student existingUser = studentMapper.selectOneByUserId(student.getUserId());
        if (existingUser != null) {
            response.put("code", 400);
            response.put("msg", "用户ID已存在");
            response.put("data", null);
            return response;
        }
        
        // 插入学生
        studentMapper.insert(student);
        
        response.put("code", 200);
        response.put("msg", "学生添加成功");
        response.put("data", student);
        return response;
    }

    /**
     * 批量添加学生
     */
    @PostMapping("/batch-add")
    public Map<String, Object> batchAddStudents(@RequestBody List<Student> students) {
        Map<String, Object> response = new HashMap<>();
        
        int successCount = 0;
        int failCount = 0;
        StringBuilder failList = new StringBuilder();
        
        for (Student student : students) {
            // 检查学号是否已存在
            Student existingStudent = studentMapper.selectOneByStudentId(student.getStudentId());
            if (existingStudent != null) {
                failCount++;
                failList.append(student.getStudentId()).append(": 学号已存在; ");
                continue;
            }
            
            // 检查userId是否已存在
            Student existingUser = studentMapper.selectOneByUserId(student.getUserId());
            if (existingUser != null) {
                failCount++;
                failList.append(student.getUserId()).append(": 用户ID已存在; ");
                continue;
            }
            
            // 插入学生
            studentMapper.insert(student);
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
     * 查询学生列表
     */
    @GetMapping("/list")
    public Map<String, Object> listStudents(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "classId", required = false) Integer classId,
            @RequestParam(value = "studentId", required = false) String studentId,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "status", required = false) String status) {
        
        Map<String, Object> response = new HashMap<>();
        
        // 这里应该实现分页查询逻辑，目前简化处理
        List<Student> records = studentMapper.selectAll();
        
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
     * 批量删除学生
     */
    @PostMapping("/batch-delete")
    public Map<String, Object> batchDeleteStudents(@RequestBody Map<String, List<String>> request) {
        Map<String, Object> response = new HashMap<>();
        
        List<String> studentIds = request.get("studentIds");
        int successCount = 0;
        int failCount = 0;
        StringBuilder failList = new StringBuilder();
        
        for (String studentId : studentIds) {
            // 检查学生是否存在
            Student student = studentMapper.selectOneByStudentId(studentId);
            if (student == null) {
                failCount++;
                failList.append("学号=").append(studentId).append(": 学生不存在; ");
                continue;
            }
            
            // 检查学生是否关联成绩，这里简化处理
            // 实际应用中需要检查关联关系
            
            // 删除学生
            studentMapper.deleteById(studentId);
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