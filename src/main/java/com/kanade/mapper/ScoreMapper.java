package com.kanade.mapper;

import com.mybatisflex.core.BaseMapper;
import com.kanade.entity.Score;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 成绩表 映射层。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
public interface ScoreMapper extends BaseMapper<Score> {
    
    @Select("SELECT * FROM score")
    List<Score> selectAll();
}