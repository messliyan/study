package com.yrot.business.api.domain.store;

import com.yrot.business.api.enums.QuestionType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

  // 题目id
  private String questionID;
  // 题目问题内容
  private String fldName;
  // 正确答案选项
  private String fldAnswer;
  // 题目解析
  private String analysis;
  // 我的题目答案选项
  private String answer;
  // 题目类型 [0=多选 1=单选]
  private QuestionType questionType;
  // 题目选项
  private List<QuestionOption> questionOption;

}