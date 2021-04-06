package com.ruoyi.question.domain.datail;


import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MaterialDetail {

  @ApiModelProperty(value = "专题资料文件")
  @NotNull(message = "请上传专题资料文件")
  private String url;

}
