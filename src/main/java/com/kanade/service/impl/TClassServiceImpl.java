package com.kanade.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.kanade.entity.TClass;
import com.kanade.mapper.TClassMapper;
import com.kanade.service.TClassService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Service
public class TClassServiceImpl extends ServiceImpl<TClassMapper, TClass>  implements TClassService{

}
