package com.kanade.mapper;

import com.mybatisflex.core.BaseMapper;
import com.kanade.entity.Student;

/**
 * 学生表（教务专用，关联用户表获取基础信息） 映射层。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
public interface StudentMapper extends BaseMapper<Student> {

}
