package com.kanade.mapper;

import com.kanade.entity.Score;
import com.kanade.entity.vo.FailedStudentVO;
import com.kanade.entity.vo.ScoreStatsVO;
import com.kanade.entity.vo.StudentCourseVO;
import com.kanade.entity.vo.StudentScoreVO;
import com.kanade.entity.vo.TeacherScoreVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreMapper {

    long countStudentCourses(@Param("studentId") String studentId);

    List<StudentCourseVO> queryStudentCourses(@Param("studentId") String studentId,
                                              @Param("orderColumn") String orderColumn,
                                              @Param("orderDir") String orderDir,
                                              @Param("offset") int offset,
                                              @Param("limit") int limit);

    long countStudentScores(@Param("studentId") String studentId);

    List<StudentScoreVO> queryStudentScores(@Param("studentId") String studentId,
                                            @Param("orderColumn") String orderColumn,
                                            @Param("orderDir") String orderDir,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    long countTeacherScores(@Param("teacherId") String teacherId,
                            @Param("classId") Integer classId,
                            @Param("courseId") Integer courseId);

    List<TeacherScoreVO> queryTeacherScores(@Param("teacherId") String teacherId,
                                            @Param("classId") Integer classId,
                                            @Param("courseId") Integer courseId,
                                            @Param("orderColumn") String orderColumn,
                                            @Param("orderDir") String orderDir,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    TeacherScoreVO selectScoreForTeacher(@Param("teacherId") String teacherId,
                                         @Param("scoreId") Long scoreId);

    int updateScoreValues(Score score);

    int upsertScore(Score score);

    List<TeacherScoreVO> listScoresForExport(@Param("teacherId") String teacherId,
                                             @Param("classId") Integer classId,
                                             @Param("courseId") Integer courseId);

    ScoreStatsVO queryScoreStats(@Param("teacherId") String teacherId,
                                 @Param("classId") Integer classId,
                                 @Param("courseId") Integer courseId);

    List<FailedStudentVO> listFailedStudents(@Param("teacherId") String teacherId,
                                            @Param("classId") Integer classId,
                                            @Param("courseId") Integer courseId);
}
