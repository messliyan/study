package com.ruoyi.basis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.basis.domain.QueSubject;
import com.ruoyi.basis.mapper.QueSubjectMapper;
import com.ruoyi.basis.service.IQueSubjectService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 专题数据Service业务层处理
 *
 * @author Li Jiabin
 * @date 2021-04-02
 */
@Service
public class QueSubjectServiceImpl extends ServiceImpl<QueSubjectMapper, QueSubject> implements
    IQueSubjectService {

  @Resource
  private QueSubjectMapper queSubjectMapper;

}
