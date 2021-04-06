package com.ruoyi.question.domain.datail;


import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DebugDetail {

  @ApiModelProperty(value = "纠错题目编号")
  private Long storeId;

  @ApiModelProperty(value = "纠错题目名称")
  private String storeName;

  @ApiModelProperty(value = "纠错描述")
  private String detail;

  @ApiModelProperty(value = "错误类型")
  private String type;

}
