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
import com.ruoyi.question.domain.datail.MaterialDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 专题资料对象 que_material
 *
 * @author Li Jiabin
 * @date 2021-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "专题资料表")
//有了下面这个注解 TypeHandler才生效
@TableName(autoResultMap = true)
public class QueMaterial {

  @ApiModelProperty(value = "编号")
  @TableId(type = IdType.AUTO)
  private Long materialId;

  @ApiModelProperty(value = "专题资料名称")
  @NotNull(message = "请输入专题资料名称")
  @Excel(name = "专题资料名称")
  private String materialName;

  @ApiModelProperty(value = "专题资料类型")
  @NotNull(message = "请选择专题资料类型")
  @Excel(name = "专题资料类型")
  private String materialType;

  @ApiModelProperty(value = "专题资料详细")
  @TableField(typeHandler = JacksonTypeHandler.class)
  @JsonUnwrapped
  private MaterialDetail materialDetail;

  @ApiModelProperty(value = "所属专题")
  @Excel(name = "所属专题")
  @NotNull(message = "请选择所属专题")
  private Long subjectId;

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
