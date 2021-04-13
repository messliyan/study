package com.ruoyi.wxapp.domain;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.wxapp.domain.datail.IdeaDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 意见反馈对象 wxapp_idea
 *
 * @author Li Jiabin
 * @date 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "意见反馈表")
//有了下面这个注解 TypeHandler才生效
@TableName(autoResultMap = true)
public class WxappIdea  {

  @ApiModelProperty(value = "编号")
  @TableId(type = IdType.AUTO)
  private Long ideaId;

  @ApiModelProperty(value = "用户编号")
  @Excel(name = "用户编号")
  private Long userId;

  @ApiModelProperty(value = "意见反馈详细")
  @TableField(typeHandler = JacksonTypeHandler.class)
  @JsonUnwrapped
  private IdeaDetail ideaDetail;

  @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
  @JsonIgnore
  private Integer deleteStatus;

  @ApiModelProperty(value = "创建时间")
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  @ApiModelProperty(value = "更新时间")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @JsonIgnore
  private LocalDateTime updateTime;

  @Version
  @JsonIgnore
  private Integer version;
}
