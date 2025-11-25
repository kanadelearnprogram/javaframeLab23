package com.kanade.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.kanade.entity.TFileCategory;
import com.kanade.mapper.TFileCategoryMapper;
import com.kanade.service.TFileCategoryService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Service
public class TFileCategoryServiceImpl extends ServiceImpl<TFileCategoryMapper, TFileCategory>  implements TFileCategoryService{

}
