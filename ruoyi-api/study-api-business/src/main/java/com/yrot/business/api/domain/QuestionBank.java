package com.yrot.business.api.domain;

import com.yrot.business.api.domain.store.Question;
import java.util.List;
import lombok.Data;

@Data
public class QuestionBank {

  //专题名称
  private String paperName;
  //题库数据
  private List<Question> question;

}