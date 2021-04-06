package com.ruoyi.question.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.question.mapper.QueDebugMapper;
import com.ruoyi.question.domain.QueDebug;
import com.ruoyi.question.service.IQueDebugService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 纠错管理Service业务层处理
 *
 * @author Li Jiabin
 * @date 2021-04-02
 */
@Service
public class QueDebugServiceImpl extends ServiceImpl<QueDebugMapper, QueDebug> implements IQueDebugService
{
  @Resource
  private QueDebugMapper queDebugMapper;

}
