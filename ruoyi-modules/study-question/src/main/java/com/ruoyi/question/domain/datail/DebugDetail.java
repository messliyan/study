package com.ruoyi.question.domain.datail;


import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DebugDetail {

  @ApiModelProperty(value = "纠错题目")
  private String store;

  @ApiModelProperty(value = "纠错描述")
  private String detail;

  @ApiModelProperty(value = "错误类型")
  private String type;

}
