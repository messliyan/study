package com.ruoyi.extra.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.extra.domain.WxappExamCandidate;
import com.ruoyi.extra.mapper.WxappExamCandidateMapper;
import com.ruoyi.extra.service.IWxappExamCandidateService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 考试考生数据Service业务层处理
 *
 * @author Li Jiabin
 * @date 2021-04-13
 */
@Service
public class WxappExamCandidateServiceImpl extends
    ServiceImpl<WxappExamCandidateMapper, WxappExamCandidate> implements
    IWxappExamCandidateService {

  @Resource
  private WxappExamCandidateMapper wxappExamCandidateMapper;

}
