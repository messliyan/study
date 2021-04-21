package com.yrot.business.api.domain.store;

import com.yrot.business.api.enums.OptionsMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionOption {

  // 题目内容
  private String fldOptionText;
  // 题目序号
  private int fldOptionIndex;

}