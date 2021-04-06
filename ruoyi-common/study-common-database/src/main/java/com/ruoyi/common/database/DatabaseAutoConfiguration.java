package com.ruoyi.common.database;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ruoyi.common.database.config.DatabaseProperties;
import com.ruoyi.common.database.handler.MYMetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 数据库自动配置类
 *
 * @Description
 * @Author Li Jiabin
 * @Date 2020/11/4 8:55
 * @Version 1.0
 */
@EnableTransactionManagement
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({DatabaseProperties.class})
@Import({MYMetaObjectHandler.class})
@Slf4j
public class DatabaseAutoConfiguration {

  /**
   * 分页插件
   */
  // 最新版
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
  }


}
