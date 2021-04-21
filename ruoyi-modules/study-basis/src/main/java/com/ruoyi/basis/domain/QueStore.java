package com.ruoyi.basis.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ruoyi.basis.domain.datail.StoreDetail;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excels;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 题库数据对象 que_store
 *
 * @author Li Jiabin
 * @date 2021-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "题库数据表")
//有了下面这个注解 TypeHandler才生效
@TableName(autoResultMap = true)
public class QueStore {

  @ApiModelProperty(value = "编号")
  @TableId(type = IdType.AUTO)
  private Long storeId;

  @NotNull(message = "请选择专题")
  @ApiModelProperty(value = "所属专题")
  @Excel(name = "所属专题")
  private String subject;

  @ApiModelProperty(value = "题目类型（que_Judgment 判断 1单选 2多选）")
  @Excel(name = "题目类型")
  @NotNull(message = "请选择题目类型")
  private String storeType;

  @ApiModelProperty(value = "題目内容")
  @Excel(name = "題目内容", width = 100)
  @NotNull(message = "请填写題目内容")
  private String storeContent;


  @ApiModelProperty(value = "題目详细")
  @TableField(typeHandler = JacksonTypeHandler.class)
  @JsonUnwrapped
  /** 导出部门多个对象 */
  @Excels({
      @Excel(name = "题目答案", targetAttr = "answer"),
      @Excel(name = "题目解析", targetAttr = "parsing"),
      @Excel(name = "选项A", targetAttr = "selectionA"),
      @Excel(name = "选项B", targetAttr = "selectionB"),
      @Excel(name = "选项C", targetAttr = "selectionC"),
      @Excel(name = "选项D", targetAttr = "selectionD"),
      @Excel(name = "选项E", targetAttr = "selectionE"),
      @Excel(name = "选项F", targetAttr = "selectionF")
  })
  private StoreDetail storeDetail = new StoreDetail();


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
