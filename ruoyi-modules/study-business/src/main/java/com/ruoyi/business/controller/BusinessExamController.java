package com.ruoyi.business.controller;

import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.yrot.business.api.QuestionsService;
import com.yrot.business.api.domain.QuestionBank;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/exam")
public class BusinessExamController extends BaseController {

  @Resource
  private QuestionsService questionsService;


  /**
   * 抽取题目
   */
  @GetMapping("/rand")
  public AjaxResult list() {

    List<Long> ra = new ArrayList<Long>() {{
      add(1L);
    }};
    QuestionBank questionBank=questionsService.getRandStore(ra, 5L).getData();

    return AjaxResult.success(questionBank);
  }


}
