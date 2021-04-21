package com.yrot.business.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ruoyi.common.core.enums.IntCodeEnum;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jiabin
 * @version 1.0
 * @date 2021-04-19 10:20
 */
@Getter
@AllArgsConstructor
public enum OptionsMap implements IntCodeEnum {
  A(1, "A"),
  B(2, "B"),
  C(3, "C"),
  D(4, "D"),
  E(5, "E"),
  F(6, "F");

  private final int code;
  private final String description;

  public static OptionsMap getEnumForDescription(String description) {
    return IntCodeEnum.getEnumForDescription(OptionsMap.class, description);
  }

  public static int getAnswerForDescription(String answer) {
    return IntCodeEnum.getEnumForDescription(OptionsMap.class, answer).getCode();
  }

  public static String getFldAnswerForOptions(String answer) {
    return StringUtils.join(Arrays.stream(answer.split(",")).map(OptionsMap::getAnswerForDescription).toArray(), ",");
  }

  @JsonCreator
  public OptionsMap forValue(int code) {
    return getEnumForCode(OptionsMap.class, code);
  }

  @JsonValue
  public int toValue() {
    return this.code;
  }


}
