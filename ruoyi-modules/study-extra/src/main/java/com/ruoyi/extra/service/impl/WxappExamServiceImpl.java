package com.ruoyi.extra.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.extra.domain.WxappExam;
import com.ruoyi.extra.mapper.WxappExamMapper;
import com.ruoyi.extra.service.IWxappExamService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 考试设置Service业务层处理
 *
 * @author Li Jiabin
 * @date 2021-04-08
 */
@Service
public class WxappExamServiceImpl extends ServiceImpl<WxappExamMapper, WxappExam> implements
    IWxappExamService {

  @Resource
  private WxappExamMapper wxappExamMapper;

}
