package com.kanade.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.kanade.entity.TCourse;
import com.kanade.mapper.TCourseMapper;
import com.kanade.service.TCourseService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Service
public class TCourseServiceImpl extends ServiceImpl<TCourseMapper, TCourse>  implements TCourseService{

}
