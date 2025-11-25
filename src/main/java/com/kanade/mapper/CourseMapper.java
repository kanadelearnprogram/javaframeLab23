package com.kanade.mapper;

import com.mybatisflex.core.BaseMapper;
import com.kanade.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 课程表（权重之和=1.0需通过代码校验） 映射层。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
public interface CourseMapper extends BaseMapper<Course> {
    
    @Select("SELECT * FROM course WHERE course_name = #{courseName}")
    Course selectOneByCourseName(@Param("courseName") String courseName);
    
    @Select("SELECT * FROM course WHERE course_id = #{courseId}")
    Course selectOneById(@Param("courseId") Integer courseId);
    
    @Select("SELECT * FROM course")
    List<Course> selectAll();
}