package com.kanade.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.kanade.entity.TScore;
import com.kanade.mapper.TScoreMapper;
import com.kanade.service.TScoreService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Service
public class TScoreServiceImpl extends ServiceImpl<TScoreMapper, TScore>  implements TScoreService{

}
