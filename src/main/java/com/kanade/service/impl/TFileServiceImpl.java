package com.kanade.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.kanade.entity.TFile;
import com.kanade.mapper.TFileMapper;
import com.kanade.service.TFileService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Service
public class TFileServiceImpl extends ServiceImpl<TFileMapper, TFile>  implements TFileService{

}
