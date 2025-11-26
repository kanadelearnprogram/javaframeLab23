package com.kanade.service;

import com.kanade.entity.Student;
import com.kanade.entity.User;
import com.kanade.entity.vo.PageResult;
import com.kanade.entity.vo.StudentCourseVO;
import com.kanade.entity.vo.StudentScoreVO;
import com.kanade.mapper.ScoreMapper;
import com.kanade.mapper.StudentMapper;
import com.kanade.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class StudentPortalService {

    private static final Map<String, String> COURSE_ORDER_COLUMNS = new HashMap<>();
    private static final Map<String, String> SCORE_ORDER_COLUMNS = new HashMap<>();

    static {
        COURSE_ORDER_COLUMNS.put("courseName", "c.course_name");
        COURSE_ORDER_COLUMNS.put("teacherName", "teacher_name");
        COURSE_ORDER_COLUMNS.put("selectTime", "s.create_time");

        SCORE_ORDER_COLUMNS.put("totalScore", "s.total_score");
        SCORE_ORDER_COLUMNS.put("dailyScore", "s.daily_score");
        SCORE_ORDER_COLUMNS.put("examScore", "s.exam_score");
        SCORE_ORDER_COLUMNS.put("updateTime", "s.update_time");
    }

    public PageResult<StudentCourseVO> listSelectedCourses(User student,
                                                           Integer page,
                                                           Integer limit,
                                                           String sortField,
                                                           String sortOrder) {
        int currentPage = normalizePage(page);
        int pageSize = normalizeLimit(limit);
        String column = COURSE_ORDER_COLUMNS.getOrDefault(sortField, "c.course_name");
        String direction = normalizeDirection(sortOrder, "ASC");
        int offset = (currentPage - 1) * pageSize;
        String studentId = resolveStudentId(student);

        try (SqlSession session = MyBatisUtil.getSession()) {
            ScoreMapper mapper = session.getMapper(ScoreMapper.class);
            long total = mapper.countStudentCourses(studentId);
            if (total == 0) {
                return PageResult.empty(currentPage, pageSize);
            }
            List<StudentCourseVO> records = mapper.queryStudentCourses(studentId,
                    column,
                    direction,
                    offset,
                    pageSize);
            return PageResult.of(total, currentPage, pageSize, records);
        }
    }

    public PageResult<StudentScoreVO> listPersonalScores(User student,
                                                         Integer page,
                                                         Integer limit,
                                                         String sortField,
                                                         String sortOrder) {
        int currentPage = normalizePage(page);
        int pageSize = normalizeLimit(limit);
        String column = SCORE_ORDER_COLUMNS.getOrDefault(sortField, "s.total_score");
        String direction = normalizeDirection(sortOrder, "DESC");
        int offset = (currentPage - 1) * pageSize;
        String studentId = resolveStudentId(student);

        try (SqlSession session = MyBatisUtil.getSession()) {
            ScoreMapper mapper = session.getMapper(ScoreMapper.class);
            long total = mapper.countStudentScores(studentId);
            if (total == 0) {
                return PageResult.empty(currentPage, pageSize);
            }
            List<StudentScoreVO> records = mapper.queryStudentScores(studentId,
                    column,
                    direction,
                    offset,
                    pageSize);
            return PageResult.of(total, currentPage, pageSize, records);
        }
    }

    private int normalizePage(Integer page) {
        return page == null || page < 1 ? 1 : page;
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null || limit < 1) {
            return 10;
        }
        return Math.min(limit, 100);
    }

    private String normalizeDirection(String direction, String defaultDir) {
        if (direction == null) {
            return defaultDir;
        }
        String upper = direction.toUpperCase(Locale.ROOT);
        if (!upper.equals("ASC") && !upper.equals("DESC")) {
            return defaultDir;
        }
        return upper;
    }

    private String resolveStudentId(User user) {
        try (SqlSession session = MyBatisUtil.getSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            Student student = mapper.selectByUserId(user.getUserId());
            if (student == null) {
                throw new IllegalArgumentException("未找到学生档案");
            }
            return student.getStudentId();
        }
    }
}

