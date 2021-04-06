package com.ruoyi.question.domain.datail;


import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectDetail {

  @ApiModelProperty(value = "专题描述")
  @NotNull(message = "输入专题描述")
  private String describe;

  @ApiModelProperty(value = "专题封面")
  @NotNull(message = "请上传专题封面")
  private String icon;

  @ApiModelProperty(value = "权重")
  private String weight;

}
