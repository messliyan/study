package com.ruoyi.wxapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.wxapp.domain.WxappIdea;
import com.ruoyi.wxapp.mapper.WxappIdeaMapper;
import com.ruoyi.wxapp.service.IWxappIdeaService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 意见反馈Service业务层处理
 *
 * @author Li Jiabin
 * @date 2021-04-08
 */
@Service
public class WxappIdeaServiceImpl extends ServiceImpl<WxappIdeaMapper, WxappIdea> implements
    IWxappIdeaService {

  @Resource
  private WxappIdeaMapper wxappIdeaMapper;

}
