package com.ruoyi.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.exception.CustomException;
import com.ruoyi.common.core.utils.StringUtils;
import java.util.List;

import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.ruoyi.question.mapper.QueStoreMapper;
import com.ruoyi.question.domain.QueStore;
import com.ruoyi.question.service.IQueStoreService;

/**
 * 题库数据Service业务层处理
 * 
 * @author Li Jiabin
 * @date 2021-04-01
 */
@Service
public class QueStoreServiceImpl extends ServiceImpl<QueStoreMapper, QueStore> implements IQueStoreService
{
  @Resource
  private QueStoreMapper queStoreMapper;
  /**
   * 导入题库数据
   *
   * @param queStoreList 题库数据列表
   * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
   * @return 结果
   */
  @Override
  public String importUser(List<QueStore> queStoreList, Boolean isUpdateSupport)
  {
    if (StringUtils.isNull(queStoreList) || queStoreList.size() == 0)
    {
      throw new CustomException("导入题库数据不能为空！");
    }

    StringBuilder successMsg = new StringBuilder();
    StringBuilder failureMsg = new StringBuilder();

    AtomicInteger successNum = new AtomicInteger();
    AtomicInteger failureNum = new AtomicInteger();

    queStoreList.forEach(qu->{

      try
      {
        // 验证题目是否已经存在
        QueryWrapper<QueStore> wrapper2 = new QueryWrapper<>();

        wrapper2.lambda()
            .eq(QueStore::getStoreContent,qu.getStoreContent());

        QueStore u = queStoreMapper.selectOne(wrapper2);
        if (StringUtils.isNull(u))
        {
          queStoreMapper.insert(qu);
          successNum.getAndIncrement();
          successMsg.append("<br/>" + successNum + "、题库 " + qu.getStoreContent() + " 导入成功");
        }
        else if (isUpdateSupport)
        {
          qu.setStoreId(u.getStoreId());
          queStoreMapper.updateById(qu);
          successNum.getAndIncrement();
          successMsg.append("<br/>" + successNum + "、题库 " + qu.getStoreContent() + " 更新成功");
        }
        else
        {
          failureNum.getAndIncrement();
          failureMsg.append("<br/>" + failureNum + "、题库 " + qu.getStoreContent() + " 已存在");
        }
      }
      catch (Exception e)
      {
        failureNum.getAndIncrement();
        String msg = "<br/>" + failureNum + "、题库 " + qu.getStoreContent() + " 导入失败：";
        failureMsg.append(msg + e.getMessage());
        log.error(msg, e);
      }
    });

    if (failureNum.get() > 0)
    {
      failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
      throw new CustomException(failureMsg.toString());
    }
    else
    {
      successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
    }
    return successMsg.toString();
  }
}
