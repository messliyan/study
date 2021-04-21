package com.ruoyi.common.database.init;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 数据库初始化设置
 *
 * @Description
 * @Author Li Jiabin
 * @Date 2020/11/4 11:01
 * @Version 1.0
 */
public class ApplicationDatabaseInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
    ConfigurableEnvironment environment = configurableApplicationContext.getEnvironment();

    //设置填充逻辑删除字段
    String property = environment.getProperty("yuan.database.config.enable-logic-delete");
    if (property == null) {
      property = "true";
    }

    if (Boolean.parseBoolean(property)) {
      String logicDeleteField = environment.getProperty("yuan.database.config.logic-delete-field");
      System.setProperty("mybatis-plus.global-config.db-config.logic-delete-field",
          StrUtil.isBlank(logicDeleteField) ? "deleteStatus" : logicDeleteField);

      //设置删除的值
      String deleteValue = environment.getProperty("yuan.database.config.logic-delete-value");
      System.setProperty("mybatis-plus.global-config.db-config.logic-delete-value",
          StrUtil.isBlank(deleteValue) ? "1" : deleteValue);

      //设置未删除的默认值
      String notDeleteValue = environment
          .getProperty("yuan.database.config.logic-not-delete-value");
      System.setProperty("mybatis-plus.global-config.db-config.logic-not-delete-value",
          StrUtil.isBlank(notDeleteValue) ? "0" : notDeleteValue);
    }

    //填充druid默认值
    setSystemProperty(environment, "spring.datasource.dynamic.druid.initial-size", "1");
    setSystemProperty(environment, "spring.datasource.dynamic.druid.max-active", "20");
    setSystemProperty(environment, "spring.datasource.dynamic.druid.min-idle", "5");
    setSystemProperty(environment, "spring.datasource.dynamic.druid.max-wait", "60000");
    setSystemProperty(environment, "spring.datasource.dynamic.druid.pool-prepared-statements",
        "true");
    setSystemProperty(environment,
        "spring.datasource.dynamic.druid.max-pool-prepared-statement-per-connection-size", "20");
    setSystemProperty(environment, "spring.datasource.dynamic.druid.validation-query",
        "select 'x'");
    setSystemProperty(environment, "spring.datasource.dynamic.druid.test-on-borrow", "false");
    setSystemProperty(environment, "spring.datasource.dynamic.druid.test-on-return", "false");
    setSystemProperty(environment, "spring.datasource.dynamic.druid.test-while-idle", "true");
    setSystemProperty(environment,
        "spring.datasource.dynamic.druid.time-between-eviction-runs-millis",
        "60000");
    setSystemProperty(environment, "spring.datasource.dynamic.druid.min-evictable-idle-time-millis",
        "300000");
    setSystemProperty(environment,
        "spring.datasource.dynamic.druid.filters.commons-log.connection-logger-name",
        "stat,wall,log4j");
    setSystemProperty(environment, "spring.datasource.dynamic.druid.use-global-data-source-stat",
        "true");
    setSystemProperty(environment, "spring.datasource.dynamic.druid.connection-properties",
        "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");

  }

  /**
   * 设置环境变量，并设置默认值
   *
   * @param environment 环境
   * @param key         键
   * @param defaultVal  默认值
   */
  private void setSystemProperty(ConfigurableEnvironment environment, String key,
      String defaultVal) {
    String property = environment.getProperty(key);
    System.setProperty(key, property == null ? defaultVal : property);
  }
}
