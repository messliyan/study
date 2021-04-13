package com.ruoyi.wxapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.wxapp.domain.WxappExam;
import com.ruoyi.wxapp.mapper.WxappExamMapper;
import com.ruoyi.wxapp.service.IWxappExamService;
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
