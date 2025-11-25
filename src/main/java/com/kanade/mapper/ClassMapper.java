package com.kanade.mapper;

import com.mybatisflex.core.BaseMapper;
import com.kanade.entity.Class;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 班级表 映射层。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
public interface ClassMapper extends BaseMapper<Class> {
    
    @Select("SELECT * FROM class WHERE class_name = #{className}")
    Class selectOneByClassName(@Param("className") String className);
    
    @Select("SELECT * FROM class WHERE class_id = #{classId}")
    Class selectOneById(@Param("classId") Integer classId);
    
    @Select("SELECT * FROM class")
    List<Class> selectAll();
}