package com.ruoyi.common.database.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据库配置类
 *
 * @Description
 * @Author Li Jiabin
 * @Date 2020/11/4 10:26
 * @Version 1.0
 */
@Data
@ConfigurationProperties("yuan.database")
public class DatabaseProperties {

  private DatabaseConfig config = new DatabaseConfig();

  @Data
  public static class DatabaseConfig {

    /**
     * 是否开启逻辑删除
     */
    private Boolean enableLogicDelete = true;

    /**
     * 逻辑已删除值(默认为 1)
     */
    private String logicDeleteValue = "1";
    /**
     * 逻辑未删除值(默认为 0)
     */
    private String logicNotDeleteValue = "0";


    /**
     * 是否开启创建时间字段
     */
    private Boolean enableCreateTime = true;

    /**
     * 创建时间字段
     */
    private String createTimeField = "createTime";

    /**
     * 是否开启更新时间字段
     */
    private Boolean enableUpdateTime = true;

    /**
     * 更新时间
     */
    private String updateTimeField = "updateTime";


  }
}
