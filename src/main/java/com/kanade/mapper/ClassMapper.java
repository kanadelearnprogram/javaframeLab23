package com.kanade.mapper;

import com.kanade.entity.Class;
import com.kanade.entity.vo.TeacherClassVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassMapper {
    @Select("select class_id from class where class_name = #{className}")
    Integer searchIdByName(String className);

    Class selectById(Integer classId);

    List<TeacherClassVO> listClassesByTeacher(String teacherId);
}
