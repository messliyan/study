package com.yrot.business.api.factory;

import com.ruoyi.common.core.domain.R;
import com.yrot.business.api.QuestionsService;
import com.yrot.business.api.domain.QuestionBank;
import feign.hystrix.FallbackFactory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 *
 * @author ruoyi
 */
@Component
@Slf4j
public class QuestionsFallbackFactory implements FallbackFactory<QuestionsService> {


  @Override
  public QuestionsService create(Throwable throwable) {
    log.error("用户服务调用失败:{}", throwable.getMessage());
    return new QuestionsService() {
      @Override
      public R<QuestionBank> getRandStore(List<Long> subjectIds, Long quantity) {
        return R.fail("获取随机题目失败:" + throwable.getMessage());
      }
    };
  }
}
