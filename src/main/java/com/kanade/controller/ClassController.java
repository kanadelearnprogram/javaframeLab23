package com.kanade.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.kanade.entity.Class;
import com.kanade.mapper.ClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/class")
public class ClassController {

    @Autowired
    private ClassMapper classMapper;

    /**
     * 单条添加班级
     */
    @PostMapping("/add")
    public Map<String, Object> addClass(@RequestBody Class clazz) {
        Map<String, Object> response = new HashMap<>();
        
        // 检查班级名称是否已存在
        Class existingClass = classMapper.selectOneByClassName(clazz.getClassName());
        if (existingClass != null) {
            response.put("code", 400);
            response.put("msg", "班级名称已存在");
            response.put("data", null);
            return response;
        }
        
        // 设置创建时间
        clazz.setCreateTime(LocalDateTime.now());
        
        // 插入班级
        classMapper.insert(clazz);
        
        response.put("code", 200);
        response.put("msg", "班级添加成功");
        response.put("data", clazz);
        return response;
    }

    /**
     * 批量添加班级
     */
    @PostMapping("/batch-add")
    public Map<String, Object> batchAddClasses(@RequestBody List<Class> classes) {
        Map<String, Object> response = new HashMap<>();
        
        int successCount = 0;
        int failCount = 0;
        StringBuilder failList = new StringBuilder();
        
        for (Class clazz : classes) {
            // 检查班级名称是否已存在
            Class existingClass = classMapper.selectOneByClassName(clazz.getClassName());
            if (existingClass != null) {
                failCount++;
                failList.append(clazz.getClassName()).append(": 班级名称已存在; ");
            } else {
                // 设置创建时间
                clazz.setCreateTime(LocalDateTime.now());
                // 插入班级
                classMapper.insert(clazz);
                successCount++;
            }
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
     * 查询班级列表
     */
    @GetMapping("/list")
    public Map<String, Object> listClasses(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "teacherId", required = false) String teacherId,
            @RequestParam(value = "className", required = false) String className) {
        
        Map<String, Object> response = new HashMap<>();
        
        // 这里应该实现分页查询逻辑，目前简化处理
        List<Class> records = classMapper.selectAll();
        
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
     * 批量删除班级
     */
    @PostMapping("/batch-delete")
    public Map<String, Object> batchDeleteClasses(@RequestBody Map<String, List<Integer>> request) {
        Map<String, Object> response = new HashMap<>();
        
        List<Integer> classIds = request.get("classIds");
        int successCount = 0;
        int failCount = 0;
        StringBuilder failList = new StringBuilder();
        
        for (Integer classId : classIds) {
            // 检查班级是否存在
            Class clazz = classMapper.selectOneById(classId);
            if (clazz == null) {
                failCount++;
                failList.append("班级ID=").append(classId).append(": 班级不存在; ");
                continue;
            }
            
            // 检查班级是否关联学生或课程，这里简化处理
            // 实际应用中需要检查关联关系
            
            // 删除班级
            classMapper.deleteById(classId);
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