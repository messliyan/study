package com.ruoyi.question.domain;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.question.domain.datail.DebugDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 纠错管理对象 que_debug
 *
 * @author Li Jiabin
 * @date 2021-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "纠错管理表")
//有了下面这个注解 TypeHandler才生效
@TableName(autoResultMap = true)
public class QueDebug {

  @ApiModelProperty(value = "编号")
  @TableId(type = IdType.AUTO)
  private Long debugId;

  @ApiModelProperty(value = "纠错人")
  @NotNull(message = "请选择纠错人")
  @Excel(name = "纠错人")
  private String debugMan;

  @ApiModelProperty(value = "纠错详细")
  @TableField(typeHandler = JacksonTypeHandler.class)
  @JsonUnwrapped
  private DebugDetail debugDetail;

  @ApiModelProperty(value = "纠错状态")
  @NotNull(message = "请选择纠错状态")
  @Excel(name = "纠错状态")
  private String debugStatus;


  @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
  @JsonIgnore
  private Integer deleteStatus;

  @ApiModelProperty(value = "创建时间")
  @TableField(fill = FieldFill.INSERT)
  @JsonIgnore
  private LocalDateTime createTime;

  @ApiModelProperty(value = "更新时间")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @JsonIgnore
  private LocalDateTime updateTime;

  @Version
  @JsonIgnore
  private Integer version;

}
