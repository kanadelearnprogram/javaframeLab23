package com.kanade.controller;

import com.kanade.entity.Score;
import com.kanade.mapper.ScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @Autowired
    private ScoreMapper scoreMapper;

    /**
     * 单条保存成绩（录入/修改）
     */
    @PostMapping("/save")
    public Map<String, Object> saveScore(@RequestBody Score score) {
        Map<String, Object> response = new HashMap<>();
        
        // 设置录入时间
        score.setInputTime(LocalDateTime.now());
        
        // 计算总评成绩
        BigDecimal dailyScore = score.getDailyScore();
        BigDecimal examScore = score.getExamScore();
        
        // 这里应该从课程表中获取权重信息，简化处理
        BigDecimal dailyRatio = new BigDecimal("0.3");
        BigDecimal examRatio = new BigDecimal("0.7");
        
        BigDecimal totalScore = dailyScore.multiply(dailyRatio).add(examScore.multiply(examRatio));
        score.setTotalScore(totalScore);
        
        // 判断是否及格（总分>=60为及格）
        score.setIsPass(totalScore.compareTo(new BigDecimal("60")) >= 0);
        
        // 如果是重修学生且总评>75，则修正为75
        // 这里简化处理，实际应用中需要查询学生状态
        
        // 保存成绩
        if (score.getScoreId() != null && score.getScoreId() > 0) {
            // 更新成绩
            scoreMapper.update(score);
        } else {
            // 添加成绩
            scoreMapper.insert(score);
        }
        
        response.put("code", 200);
        response.put("msg", "成绩保存成功");
        response.put("data", score);
        return response;
    }

    /**
     * 批量保存成绩
     */
    @PostMapping("/batch-save")
    public Map<String, Object> batchSaveScores(@RequestBody List<Score> scores) {
        Map<String, Object> response = new HashMap<>();
        
        int successCount = 0;
        int failCount = 0;
        StringBuilder failList = new StringBuilder();
        
        for (Score score : scores) {
            try {
                // 设置录入时间
                score.setInputTime(LocalDateTime.now());
                
                // 计算总评成绩
                BigDecimal dailyScore = score.getDailyScore();
                BigDecimal examScore = score.getExamScore();
                
                // 这里应该从课程表中获取权重信息，简化处理
                BigDecimal dailyRatio = new BigDecimal("0.3");
                BigDecimal examRatio = new BigDecimal("0.7");
                
                BigDecimal totalScore = dailyScore.multiply(dailyRatio).add(examScore.multiply(examRatio));
                score.setTotalScore(totalScore);
                
                // 判断是否及格（总分>=60为及格）
                score.setIsPass(totalScore.compareTo(new BigDecimal("60")) >= 0);
                
                // 如果是重修学生且总评>75，则修正为75
                // 这里简化处理，实际应用中需要查询学生状态
                
                // 保存成绩
                if (score.getScoreId() != null && score.getScoreId() > 0) {
                    // 更新成绩
                    scoreMapper.update(score);
                } else {
                    // 添加成绩
                    scoreMapper.insert(score);
                }
                
                successCount++;
            } catch (Exception e) {
                failCount++;
                failList.append("学生ID=").append(score.getStudentId()).append(": ").append(e.getMessage()).append("; ");
            }
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("successCount", successCount);
        data.put("failCount", failCount);
        data.put("failList", failList.toString());
        
        response.put("code", 200);
        response.put("msg", "批量保存成功");
        response.put("data", data);
        return response;
    }

    /**
     * 成绩多维查询（含分页排序）
     */
    @GetMapping("/list")
    public Map<String, Object> listScores(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "classId", required = false) Integer classId,
            @RequestParam(value = "courseId", required = false) Integer courseId,
            @RequestParam(value = "studentId", required = false) String studentId,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "isPass", required = false) Integer isPass,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        
        Map<String, Object> response = new HashMap<>();
        
        // 这里应该实现分页查询逻辑，目前简化处理
        List<Score> records = scoreMapper.selectAll();
        
        // 应该根据条件过滤和排序，这里简化处理
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
     * 不及格统计接口
     */
    @GetMapping("/fail-stat")
    public Map<String, Object> failStat(
            @RequestParam(value = "classId", required = false) Integer classId,
            @RequestParam(value = "courseId", required = false) Integer courseId) {
        
        Map<String, Object> response = new HashMap<>();
        
        // 这里应该实现不及格统计逻辑，目前简化处理
        // 实际应用中需要关联查询班级、课程、学生信息
        
        response.put("code", 200);
        response.put("msg", "统计成功");
        response.put("data", null);
        return response;
    }
}