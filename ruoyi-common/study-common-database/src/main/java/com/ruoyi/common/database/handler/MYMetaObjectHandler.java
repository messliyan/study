package com.ruoyi.common.database.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.database.config.DatabaseProperties;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 字段自定义填充设置
 *
 * @Description
 * @Author Li Jiabin
 * @Date 2020/11/4 10:26
 * @Version 1.0
 */
@Slf4j
public class MYMetaObjectHandler implements MetaObjectHandler {

  @Resource
  private DatabaseProperties databaseProperties;

  /**
   * 使用mp实现添加操作，这个方法执行
   *
   * @param metaObject 填充的对象类型
   */
  @Override
  public void insertFill(MetaObject metaObject) {

    if (databaseProperties.getConfig().getEnableCreateTime()) {
      log.info("start insert fill createTimeField ....");
      String createTimeField = databaseProperties.getConfig().getCreateTimeField();

      if (metaObject.hasSetter(createTimeField) && Objects
          .isNull(this.getFieldValByName(createTimeField, metaObject))) {

        this.strictInsertFill(metaObject, createTimeField, LocalDateTime.class,
            LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)

      }
    }

    if (databaseProperties.getConfig().getEnableUpdateTime()) {
      log.info("start insert fill updateTimeField ....");
      String updateTimeField = databaseProperties.getConfig().getUpdateTimeField();
      if (metaObject.hasSetter(updateTimeField) && Objects
          .isNull(this.getFieldValByName(updateTimeField, metaObject))) {
        this.strictInsertFill(metaObject, updateTimeField, LocalDateTime.class,
            LocalDateTime.now());

      }
    }

  }

  /**
   * 使用mp实现修改操作，这个方法执行
   *
   * @param metaObject 填充对象
   */
  @Override
  public void updateFill(MetaObject metaObject) {
    if (databaseProperties.getConfig().getEnableUpdateTime()) {
      String updateTimeField = databaseProperties.getConfig().getUpdateTimeField();
      if (metaObject.hasSetter(updateTimeField) && Objects
          .isNull(this.getFieldValByName(updateTimeField, metaObject))) {
        this.strictUpdateFill(metaObject, updateTimeField, LocalDateTime.class,
            LocalDateTime.now());

      }
    }
  }
}
