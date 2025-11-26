package com.kanade.mapper;

import com.kanade.entity.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    Course selectById(Integer courseId);

    Course selectOwnedCourse(String teacherId, Integer classId, Integer courseId);

    List<Course> listByTeacherAndClass(String teacherId, Integer classId);
}
