package com.ruoyi.basis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basis.domain.QueStore;
import java.util.List;

/**
 * 题库数据Service接口
 *
 * @author Li Jiabin
 * @date 2021-04-01
 */
public interface IQueStoreService extends IService<QueStore> {

  /**
   * 导入题库数据Excel
   *
   * @param userList        题库数据列表
   * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
   * @return 结果
   */
  public String importUser(List<QueStore> userList, Boolean isUpdateSupport);

  /**
   * 获取随机数据
   *
   * @param subjects 随机的专题名称
   * @param quantity 随机的题目数量
   * @return 结果
   */
  public List<QueStore> randData(List<String> subjects, Long quantity);
}
