package com.yrot.business.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.yrot.business.api.domain.QuestionBank;
import com.yrot.business.api.factory.QuestionsFallbackFactory;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 题库基础服务
 *
 * @author ruoyi
 */
@FeignClient(contextId = "questionsService", value = ServiceNameConstants.STUDY_BUSINESS, fallbackFactory = QuestionsFallbackFactory.class)
@RequestMapping("/store")
public interface QuestionsService {

  /**
   * 知道专题抽取对应数量的题目
   *
   * @param subjectIds 专题id
   * @param quantity   指定数量
   * @return 结果
   */
  @GetMapping(value = "/rand/{subjectIds}/{quantity}")
  public R<QuestionBank> getRandStore(@PathVariable(value = "subjectIds") List<Long> subjectIds,
      @PathVariable(value = "quantity") Long quantity);
}
