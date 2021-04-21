package com.ruoyi.extra.domain.datail;


import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CandidateDetail {

  @ApiModelProperty(value = "考试成绩")
  private String result;

  @ApiModelProperty(value = "考试用时(秒)")
  private Long timeCost;

  @ApiModelProperty(value = "考试是否通过")
  private Boolean pass;

  @ApiModelProperty(value = "交卷时间")
  private LocalDateTime handoverTime;
}
