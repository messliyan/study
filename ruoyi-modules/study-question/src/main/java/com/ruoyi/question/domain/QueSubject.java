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
import com.ruoyi.common.core.annotation.Excels;
import com.ruoyi.question.domain.datail.SubjectDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 专题数据对象 que_subject
 *
 * @author Li Jiabin
 * @date 2021-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "专题数据表")
//有了下面这个注解 TypeHandler才生效
@TableName(autoResultMap = true)
public class QueSubject {

  @ApiModelProperty(value = "编号")
  @TableId(type = IdType.AUTO)
  private Long subjectId;

  @ApiModelProperty(value = "专题名称")
  @NotNull(message = "请选择专题名称")
  @Excel(name = "专题名称")
  private String subjectName;


  @ApiModelProperty(value = "专题详细")
  @TableField(typeHandler = JacksonTypeHandler.class)
  @JsonUnwrapped
  private SubjectDetail subjectDetail;

  @ApiModelProperty(value = "岗位")
  @NotNull(message = "请选择岗位")
  @Excel(name = "岗位")
  private String occupation;

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
