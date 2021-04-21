package com.ruoyi.extra.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.extra.domain.WxappIdea;
import com.ruoyi.extra.mapper.WxappIdeaMapper;
import com.ruoyi.extra.service.IWxappIdeaService;
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
