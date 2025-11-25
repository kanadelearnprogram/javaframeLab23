package com.kanade.mapper;

import com.mybatisflex.core.BaseMapper;
import com.kanade.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 学生表（教务专用，关联用户表获取基础信息） 映射层。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
public interface StudentMapper extends BaseMapper<Student> {
    
    @Select("SELECT * FROM student WHERE student_id = #{studentId}")
    Student selectOneByStudentId(@Param("studentId") String studentId);
    
    @Select("SELECT * FROM student WHERE user_id = #{userId}")
    Student selectOneByUserId(@Param("userId") String userId);
    
    @Select("SELECT * FROM student")
    List<Student> selectAll();
}