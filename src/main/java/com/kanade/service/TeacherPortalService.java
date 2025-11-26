package com.kanade.service;

import com.kanade.entity.Class;
import com.kanade.entity.Course;
import com.kanade.entity.Score;
import com.kanade.entity.User;
import com.kanade.entity.vo.*;
import com.kanade.mapper.ClassMapper;
import com.kanade.mapper.CourseMapper;
import com.kanade.mapper.ScoreMapper;
import com.kanade.mapper.StudentMapper;
import com.kanade.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class TeacherPortalService {

    private static final Map<String, String> SCORE_ORDER_COLUMNS = new HashMap<>();
    private static final DateTimeFormatter EXPORT_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

    static {
        SCORE_ORDER_COLUMNS.put("totalScore", "s.total_score");
        SCORE_ORDER_COLUMNS.put("dailyScore", "s.daily_score");
        SCORE_ORDER_COLUMNS.put("examScore", "s.exam_score");
        SCORE_ORDER_COLUMNS.put("studentName", "student_name");
    }

    public PageResult<TeacherScoreVO> listClassScores(User teacher,
                                                      Integer classId,
                                                      Integer courseId,
                                                      Integer page,
                                                      Integer limit,
                                                      String sortField,
                                                      String sortOrder) {
        int currentPage = normalizePage(page);
        int pageSize = normalizeLimit(limit);
        loadOwnedCourse(teacher.getUserId(), classId, courseId);
        String column = SCORE_ORDER_COLUMNS.getOrDefault(sortField, "s.total_score");
        String direction = normalizeDirection(sortOrder, "DESC");
        int offset = (currentPage - 1) * pageSize;

        try (SqlSession session = MyBatisUtil.getSession()) {
            ScoreMapper mapper = session.getMapper(ScoreMapper.class);
            long total = mapper.countTeacherScores(teacher.getUserId(), classId, courseId);
            if (total == 0) {
                return PageResult.empty(currentPage, pageSize);
            }
            List<TeacherScoreVO> records = mapper.queryTeacherScores(teacher.getUserId(),
                    classId,
                    courseId,
                    column,
                    direction,
                    offset,
                    pageSize);
            return PageResult.of(total, currentPage, pageSize, records);
        }
    }

    public List<TeacherClassVO> listTeacherClasses(User teacher) {
        try (SqlSession session = MyBatisUtil.getSession()) {
            ClassMapper mapper = session.getMapper(ClassMapper.class);
            return mapper.listClassesByTeacher(teacher.getUserId());
        }
    }

    public List<Course> listCoursesUnderClass(User teacher, Integer classId) {
        if (classId == null) {
            throw new IllegalArgumentException("classId 不能为空");
        }
        try (SqlSession session = MyBatisUtil.getSession()) {
            CourseMapper mapper = session.getMapper(CourseMapper.class);
            return mapper.listByTeacherAndClass(teacher.getUserId(), classId);
        }
    }

    public TeacherScoreVO updateSingleScore(User teacher,
                                            Long scoreId,
                                            BigDecimal dailyScore,
                                            BigDecimal examScore) {
        validateScoreRange(dailyScore, "平时成绩");
        validateScoreRange(examScore, "期末成绩");
        try (SqlSession session = MyBatisUtil.getSession()) {
            ScoreMapper scoreMapper = session.getMapper(ScoreMapper.class);
            TeacherScoreVO detail = scoreMapper.selectScoreForTeacher(teacher.getUserId(), scoreId);
            if (detail == null) {
                throw new IllegalArgumentException("成绩不存在或无权限编辑");
            }
            CourseMapper courseMapper = session.getMapper(CourseMapper.class);
            Course course = courseMapper.selectById(detail.getCourseId());
            if (course == null || !teacher.getUserId().equals(course.getTeacherId())) {
                throw new IllegalArgumentException("无权编辑该课程成绩");
            }
            BigDecimal total = calcTotal(dailyScore, examScore, course);
            Score score = new Score();
            score.setScoreId(scoreId);
            score.setDailyScore(dailyScore);
            score.setExamScore(examScore);
            score.setTotalScore(total);
            score.setIsPass(total.compareTo(BigDecimal.valueOf(60)) >= 0);
            score.setUpdateUserId(teacher.getUserId());
            scoreMapper.updateScoreValues(score);
            session.commit();
            detail.setDailyScore(dailyScore);
            detail.setExamScore(examScore);
            detail.setTotalScore(total);
            detail.setIsPass(score.getIsPass());
            detail.setUpdaterName(teacher.getRealName());
            return detail;
        }
    }

    public ImportResultVO importScores(User teacher,
                                       Integer classId,
                                       Integer courseId,
                                       MultipartFile file) throws IOException {
        Course course = loadOwnedCourse(teacher.getUserId(), classId, courseId);
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        ImportResultVO result = new ImportResultVO();
        try (SqlSession session = MyBatisUtil.getSession();
             java.io.InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            ScoreMapper scoreMapper = session.getMapper(ScoreMapper.class);
            StudentMapper studentMapper = session.getMapper(StudentMapper.class);
            List<StudentBasicVO> studentList = studentMapper.listBasicInfoByClass(classId);
            Set<String> allowedStudents = studentList == null
                    ? Collections.emptySet()
                    : studentList.stream()
                    .map(StudentBasicVO::getStudentId)
                    .collect(Collectors.toCollection(HashSet::new));
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                String studentId = getCellString(row.getCell(0));
                if (studentId == null || studentId.isBlank()) {
                    result.addFailure("第" + (i + 1) + "行学号为空");
                    continue;
                }
                String normalizedId = studentId.trim();
                if (!allowedStudents.contains(normalizedId)) {
                    result.addFailure("第" + (i + 1) + "行学号不属于当前班级");
                    continue;
                }
                try {
                    BigDecimal dailyScore = parseScore(row.getCell(2), "第" + (i + 1) + "行平时成绩");
                    BigDecimal examScore = parseScore(row.getCell(3), "第" + (i + 1) + "行情期末成绩");
                    BigDecimal total = calcTotal(dailyScore, examScore, course);
                    Score score = new Score();
                    score.setStudentId(normalizedId);
                    score.setCourseId(courseId);
                    score.setDailyScore(dailyScore);
                    score.setExamScore(examScore);
                    score.setTotalScore(total);
                    score.setIsPass(total.compareTo(BigDecimal.valueOf(60)) >= 0);
                    score.setUpdateUserId(teacher.getUserId());
                    scoreMapper.upsertScore(score);
                    result.addSuccess();
                } catch (IllegalArgumentException e) {
                    result.addFailure(e.getMessage());
                }
            }
            session.commit();
        }
        return result;
    }

    public byte[] buildImportTemplate(User teacher, Integer classId, Integer courseId) throws IOException {
        Course course = loadOwnedCourse(teacher.getUserId(), classId, courseId);
        try (SqlSession session = MyBatisUtil.getSession()) {
            StudentMapper studentMapper = session.getMapper(StudentMapper.class);
            List<StudentBasicVO> students = Optional.ofNullable(studentMapper.listBasicInfoByClass(classId))
                    .orElse(Collections.emptyList());
            Workbook workbook = new XSSFWorkbook();
            String sheetName = course.getCourseName() + "导入模板";
            Sheet sheet = workbook.createSheet(sheetName);
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("学号");
            header.createCell(1).setCellValue("学生姓名");
            header.createCell(2).setCellValue("平时成绩(0-100)");
            header.createCell(3).setCellValue("期末成绩(0-100)");
            int rowIdx = 1;
            for (StudentBasicVO student : students) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(student.getStudentId());
                row.createCell(1).setCellValue(student.getRealName());
                row.createCell(2).setCellValue("");
                row.createCell(3).setCellValue("");
            }
            for (int i = 0; i <= 3; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        }
    }

    public FilePayload exportScores(User teacher, Integer classId, Integer courseId) throws IOException {
        Course course = loadOwnedCourse(teacher.getUserId(), classId, courseId);
        try (SqlSession session = MyBatisUtil.getSession()) {
            ClassMapper classMapper = session.getMapper(ClassMapper.class);
            Class clazz = classMapper.selectById(classId);
            ScoreMapper scoreMapper = session.getMapper(ScoreMapper.class);
            List<TeacherScoreVO> scores = scoreMapper.listScoresForExport(teacher.getUserId(), classId, courseId);
            if (scores.isEmpty()) {
                throw new IllegalArgumentException("暂无可导出成绩");
            }
            if (scores.size() <= 1000) {
                byte[] excel = buildExportWorkbook(scores, course, clazz);
                String filename = buildFileName(clazz, course, null) + ".xlsx";
                return new FilePayload(excel,
                        filename,
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            } else {
                ByteArrayOutputStream zipBuffer = new ByteArrayOutputStream();
                try (ZipOutputStream zos = new ZipOutputStream(zipBuffer, StandardCharsets.UTF_8)) {
                    int part = 1;
                    for (int start = 0; start < scores.size(); start += 1000) {
                        List<TeacherScoreVO> chunk = scores.subList(start, Math.min(scores.size(), start + 1000));
                        byte[] excel = buildExportWorkbook(chunk, course, clazz);
                        String entry = buildFileName(clazz, course, part) + ".xlsx";
                        zos.putNextEntry(new ZipEntry(entry));
                        zos.write(excel);
                        zos.closeEntry();
                        part++;
                    }
                }
                String filename = buildFileName(clazz, course, null) + ".zip";
                return new FilePayload(zipBuffer.toByteArray(), filename, "application/zip");
            }
        }
    }

    public ScoreStatsVO buildStats(User teacher, Integer classId, Integer courseId) {
        loadOwnedCourse(teacher.getUserId(), classId, courseId);
        try (SqlSession session = MyBatisUtil.getSession()) {
            ScoreMapper mapper = session.getMapper(ScoreMapper.class);
            ScoreStatsVO stats = mapper.queryScoreStats(teacher.getUserId(), classId, courseId);
            if (stats == null) {
                stats = new ScoreStatsVO();
            }
            if (stats.getTotalCount() == null) {
                stats.setTotalCount(0);
            }
            if (stats.getPassCount() == null) {
                stats.setPassCount(0);
            }
            if (stats.getFailCount() == null) {
                stats.setFailCount(0);
            }
            if (stats.getPassRate() == null) {
                stats.setPassRate(BigDecimal.ZERO);
            }
            if (stats.getAverageScore() == null) {
                stats.setAverageScore(BigDecimal.ZERO);
            }
            if (stats.getMaxScore() == null) {
                stats.setMaxScore(BigDecimal.ZERO);
            }
            if (stats.getMinScore() == null) {
                stats.setMinScore(BigDecimal.ZERO);
            }
            List<FailedStudentVO> failed = Optional.ofNullable(
                    mapper.listFailedStudents(teacher.getUserId(), classId, courseId)
            ).orElse(Collections.emptyList());
            stats.setFailedDetails(failed);
            return stats;
        }
    }

    private Course loadOwnedCourse(String teacherId, Integer classId, Integer courseId) {
        if (classId == null || courseId == null) {
            throw new IllegalArgumentException("classId/courseId 不能为空");
        }
        try (SqlSession session = MyBatisUtil.getSession()) {
            CourseMapper courseMapper = session.getMapper(CourseMapper.class);
            Course course = courseMapper.selectOwnedCourse(teacherId, classId, courseId);
            if (course == null) {
                throw new IllegalArgumentException("无权访问该课程");
            }
            return course;
        }
    }

    private void validateScoreRange(BigDecimal score, String label) {
        if (score == null) {
            throw new IllegalArgumentException(label + "不能为空");
        }
        if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException(label + "需在0-100之间");
        }
    }

    private BigDecimal calcTotal(BigDecimal dailyScore, BigDecimal examScore, Course course) {
        BigDecimal total = dailyScore.multiply(course.getDailyRatio())
                .add(examScore.multiply(course.getExamRatio()));
        return total.setScale(1, RoundingMode.HALF_UP);
    }

    private BigDecimal parseScore(Cell cell, String label) {
        if (cell == null) {
            throw new IllegalArgumentException(label + "缺失");
        }
        double numericValue;
        if (cell.getCellType() == CellType.NUMERIC) {
            numericValue = cell.getNumericCellValue();
        } else {
            String str = cell.getStringCellValue();
            if (str == null || str.isBlank()) {
                throw new IllegalArgumentException(label + "为空");
            }
            try {
                numericValue = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(label + "格式错误");
            }
        }
        BigDecimal score = BigDecimal.valueOf(numericValue).setScale(1, RoundingMode.HALF_UP);
        validateScoreRange(score, label);
        return score;
    }

    private String getCellString(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
        }
        return null;
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

    private byte[] buildExportWorkbook(List<TeacherScoreVO> scores, Course course, Class clazz) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("成绩导出");
        String[] headers = new String[]{"班级名称", "年级", "课程名称", "平时占比", "期末占比",
                "学号", "学生姓名", "平时成绩", "期末成绩", "总成绩", "成绩等级", "是否及格", "录入人", "最后更新时间"};
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
        int rowIdx = 1;
        for (TeacherScoreVO vo : scores) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(Optional.ofNullable(clazz).map(Class::getClassName).orElse(""));
            row.createCell(1).setCellValue(Optional.ofNullable(clazz).map(Class::getGrade).orElse(""));
            row.createCell(2).setCellValue(course.getCourseName());
            row.createCell(3).setCellValue(formatPercent(course.getDailyRatio()));
            row.createCell(4).setCellValue(formatPercent(course.getExamRatio()));
            row.createCell(5).setCellValue(vo.getStudentId());
            row.createCell(6).setCellValue(vo.getStudentName());
            row.createCell(7).setCellValue(formatScore(vo.getDailyScore()));
            row.createCell(8).setCellValue(formatScore(vo.getExamScore()));
            row.createCell(9).setCellValue(formatScore(vo.getTotalScore()));
            row.createCell(10).setCellValue(scoreLevel(vo.getTotalScore()));
            row.createCell(11).setCellValue(Boolean.TRUE.equals(vo.getIsPass()) ? "是" : "否");
            row.createCell(12).setCellValue(Optional.ofNullable(vo.getUpdaterName()).orElse(""));
            row.createCell(13).setCellValue(vo.getUpdateTime() == null ? "" : vo.getUpdateTime().toString());
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    private String buildFileName(Class clazz, Course course, Integer partIndex) {
        String className = Optional.ofNullable(clazz).map(Class::getClassName).orElse("class");
        String courseName = course.getCourseName();
        String base = className + "_" + courseName + "_成绩表_" + EXPORT_DATE.format(LocalDate.now());
        if (partIndex == null) {
            return base;
        }
        return base + "_P" + partIndex;
    }

    private String formatPercent(BigDecimal ratio) {
        if (ratio == null) {
            return "-";
        }
        return ratio.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP) + "%";
    }

    private String formatScore(BigDecimal score) {
        if (score == null) {
            return "-";
        }
        return score.setScale(1, RoundingMode.HALF_UP).toPlainString();
    }

    private String scoreLevel(BigDecimal totalScore) {
        if (totalScore == null) {
            return "-";
        }
        double score = totalScore.doubleValue();
        if (score >= 90) return "优秀";
        if (score >= 80) return "良好";
        if (score >= 70) return "中等";
        if (score >= 60) return "及格";
        return "不及格";
    }
}

