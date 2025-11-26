package com.kanade.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClassMapper {
    @Select("select class_id from class where class_name = #{className}")
    Integer searchIdByName(String className);
}
