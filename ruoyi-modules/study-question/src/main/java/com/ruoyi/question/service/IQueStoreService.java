package com.ruoyi.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.api.domain.SysUser;
import java.util.List;
import com.ruoyi.question.domain.QueStore;

/**
 * 题库数据Service接口
 * 
 * @author Li Jiabin
 * @date 2021-04-01
 */
public interface IQueStoreService extends IService<QueStore>
{
  /**
   * 导入题库数据Excel
   *
   * @param userList 题库数据列表
   * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
   * @return 结果
   */
  public String importUser(List<QueStore> userList, Boolean isUpdateSupport);
}
