package com.ruoyi.wxapp.domain.datail;


import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IdeaDetail {

  @ApiModelProperty(value = "意见内容")
  @NotNull(message = "请输入您的意见")
  private String content;

}
