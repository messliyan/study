package com.ruoyi.question.domain.datail;


import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreDetail {

  @ApiModelProperty(value = "题目答案")
  @NotNull(message = "输入题目正确答案")
  private String answer;

  @ApiModelProperty(value = "题目解析")
  private String parsing;

  @ApiModelProperty(value = "选项A")
  private String selectionA;

  @ApiModelProperty(value = "选项B")
  private String selectionB;

  @ApiModelProperty(value = "选项C")
  private String selectionC;

  @ApiModelProperty(value = "选项D")
  private String selectionD;

  @ApiModelProperty(value = "选项E")
  private String selectionE;

  @ApiModelProperty(value = "选项F")
  private String selectionF;

}
