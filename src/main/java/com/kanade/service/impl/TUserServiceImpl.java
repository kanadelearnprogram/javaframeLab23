package com.kanade.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.kanade.entity.TUser;
import com.kanade.mapper.TUserMapper;
import com.kanade.service.TUserService;
import org.springframework.stereotype.Service;

/**
 * 统一用户表 服务层实现。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser>  implements TUserService{

}
