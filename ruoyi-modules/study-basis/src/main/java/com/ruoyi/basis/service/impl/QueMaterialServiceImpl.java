package com.ruoyi.basis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.basis.domain.QueMaterial;
import com.ruoyi.basis.mapper.QueMaterialMapper;
import com.ruoyi.basis.service.IQueMaterialService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 专题资料Service业务层处理
 *
 * @author Li Jiabin
 * @date 2021-04-02
 */
@Service
public class QueMaterialServiceImpl extends ServiceImpl<QueMaterialMapper, QueMaterial> implements
    IQueMaterialService {

  @Resource
  private QueMaterialMapper queMaterialMapper;

}
