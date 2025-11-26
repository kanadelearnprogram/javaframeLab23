package com.kanade.mapper;

import com.kanade.entity.Student;
import com.kanade.entity.vo.StudentBasicVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {
    @Insert("INSERT INTO student(student_id, user_id, class_id, status, is_delete, create_time, update_time) " +
            "VALUES(#{studentId}, #{userId}, #{classId}, #{status}, #{isDelete}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "studentId")
    boolean addStudent(Student stu);

    @Select("SELECT * FROM student WHERE student_id = #{studentId}")
    Student selectByStudentId(String studentId);

    List<StudentBasicVO> listBasicInfoByClass(Integer classId);

    @Select("SELECT * FROM student WHERE user_id = #{userId} LIMIT 1")
    Student selectByUserId(String userId);
}