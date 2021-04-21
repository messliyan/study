package com.ruoyi.extra.domain;


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
import com.ruoyi.extra.domain.datail.ExamDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 考试设置对象 wxapp_exam
 *
 * @author Li Jiabin
 * @date 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "考试设置表")
//有了下面这个注解 TypeHandler才生效
@TableName(autoResultMap = true)
public class WxappExam {

  @ApiModelProperty(value = "编号")
  @TableId(type = IdType.AUTO)
  private Long examId;

  @ApiModelProperty(value = "考试名称")
  @NotNull(message = "请选择考试名称")
  @Excel(name = "考试名称")
  private String examName;

  @ApiModelProperty(value = "考试状态")
  @Excel(name = "考试状态")
  private String examStatus;

  @ApiModelProperty(value = "考试开始时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime examStart;

  @ApiModelProperty(value = "考试结束时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime examEnd;

  @ApiModelProperty(value = "考试详细")
  @TableField(typeHandler = JacksonTypeHandler.class)
  @JsonUnwrapped
  private ExamDetail examDetail;


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

  /**
   * 请求参数
   */
  @TableField(exist = false)
  private Map<String, Object> params;


}
