package com.ruoyi.extra.domain.datail;


import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExamDetail {

  @ApiModelProperty(value = "考试范围")
  @NotNull(message = "请选择考试范围")
  private List<Long> subjectIds;

  @ApiModelProperty(value = "考题总数")
  private String total;

  @ApiModelProperty(value = "通过题数")
  private String passes;

  @ApiModelProperty(value = "考试时长")
  private String duration;

  @ApiModelProperty(value = "考试的学生ID集合")
  private List<Long> candidateIds;
}
