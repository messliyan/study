package com.yrot.business.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ruoyi.common.core.enums.IntCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jiabin
 * @version 1.0
 * @date 2021-04-19 10:20
 */
@Getter
@AllArgsConstructor
public enum QuestionType implements IntCodeEnum {

  MC(0, "多选题"),
  SC(1, "单选题"),
  JU(1, "判断题");

  private final int code;
  private final String description;

  public static QuestionType getEnumForDescription(String description) {
    return IntCodeEnum.getEnumForDescription(QuestionType.class, description);
  }

  @JsonCreator
  public QuestionType forValue(int v) {
    return getEnumForCode(QuestionType.class, v);
  }

  @JsonValue
  public int toValue() {
    return this.code;
  }


}
