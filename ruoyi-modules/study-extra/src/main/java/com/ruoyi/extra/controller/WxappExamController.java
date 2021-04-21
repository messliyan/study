package com.ruoyi.extra.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.constant.HttpStatus;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.PageDomain;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.core.web.page.TableSupport;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.PreAuthorize;
import com.ruoyi.extra.domain.WxappExam;
import com.ruoyi.extra.service.IWxappExamCandidateService;
import com.ruoyi.extra.service.IWxappExamService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考试设置Controller
 *
 * @author Li Jiabin
 * @date 2021-04-08
 */
@RestController
@RequestMapping("/exam")
public class WxappExamController extends BaseController {

  @Resource
  private IWxappExamService wxappExamService;

  @Resource
  private IWxappExamCandidateService wxappExamCandidateService;

  /**
   * 查询考试设置列表
   */
  @PreAuthorize(hasPermi = "wxapp:exam:list")
  @GetMapping("/list")
  public TableDataInfo list(WxappExam wxappExam) {
    PageDomain pageDomain = TableSupport.buildPageRequest();
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    QueryWrapper<WxappExam> wrapper2 = new QueryWrapper<>();
    //查询条件
    wrapper2.lambda().like(StringUtils.isNotEmpty(wxappExam.getExamName()), WxappExam::getExamName,
        wxappExam.getExamName())
        .eq(StringUtils.isNotEmpty(wxappExam.getExamStatus()), WxappExam::getExamStatus,
            wxappExam.getExamStatus());

    if (StringUtils.isNotEmpty(wxappExam.getParams())) {
      wrapper2.lambda()
          .apply(StringUtils.isNotEmpty(wxappExam.getParams().get("beginTime").toString()),
              "date_format (exam_start,'%Y-%m-%d') >= date_format('" + wxappExam.getParams()
                  .get("beginTime").toString() + "','%Y-%m-%d')")
          .apply(StringUtils.isNotBlank(wxappExam.getParams().get("endTime").toString()),
              "date_format (exam_end,'%Y-%m-%d') <= date_format('" + wxappExam.getParams()
                  .get("endTime").toString() + "','%Y-%m-%d')");
    }

    Page<WxappExam> wxappExamPage = wxappExamService.page(new Page<>(pageDomain.getPageNum(),
        pageDomain.getPageSize()), wrapper2);

    TableDataInfo rspData = new TableDataInfo();
    rspData.setCode(HttpStatus.SUCCESS);
    rspData.setRows(wxappExamPage.getRecords());
    rspData.setMsg("查询成功");
    rspData.setTotal(wxappExamPage.getTotal());
    return rspData;

  }

  /**
   * 导出考试设置列表
   */
  @PreAuthorize(hasPermi = "wxapp:exam:export")
  @Log(title = "考试设置", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  public void export(HttpServletResponse response, WxappExam wxappExam) throws IOException {
    QueryWrapper<WxappExam> queryWrapper = new QueryWrapper<>();
    //导出条件

    List<WxappExam> list = wxappExamService.list(queryWrapper);
    ExcelUtil<WxappExam> util = new ExcelUtil<>(WxappExam.class);
    util.exportExcel(response, list, "store");
  }

  /**
   * 导出考试设置模板Excel
   */
  @PostMapping("/importTemplate")
  public void importTemplate(HttpServletResponse response) throws IOException {
    ExcelUtil<WxappExam> util = new ExcelUtil<>(WxappExam.class);
    util.importTemplateExcel(response, "考试设置");
  }


  /**
   * 获取考试设置详细信息
   */
  @PreAuthorize(hasPermi = "wxapp:exam:query")
  @GetMapping(value = "/{examId}")
  public AjaxResult getInfo(@PathVariable("examId") Long examId) {
    WxappExam wxappExam = wxappExamService.getById(examId);
    List<Long> candidateIds = wxappExam.getExamDetail().getCandidateIds();
    wxappExam.getExamDetail()
        .setCandidateIds(candidateIds.stream().map(s -> s + 10000).collect(Collectors.toList()));

    return AjaxResult.success(wxappExam);
  }

  /**
   * 新增考试设置
   */
  @PreAuthorize(hasPermi = "wxapp:exam:add")
  @Log(title = "考试设置", businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@Valid @RequestBody WxappExam wxappExam) {
    if (StringUtils.isNotEmpty(wxappExam.getParams())) {
      DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      wxappExam
          .setExamStart(LocalDateTime.parse(wxappExam.getParams().get("beginTime").toString(), df));
      wxappExam
          .setExamEnd(LocalDateTime.parse(wxappExam.getParams().get("endTime").toString(), df));
    }
    wxappExam.setExamStatus("未开始");
    //过滤非用户id和还原用户真实id
    List<Long> candidateIds = wxappExam.getExamDetail().getCandidateIds();
    wxappExam.getExamDetail().setCandidateIds(
        candidateIds.stream().filter(e -> e > 10000).map(s -> s - 10000)
            .collect(Collectors.toList()));
    return toAjax(wxappExamService.save(wxappExam));
  }

  /**
   * 修改考试设置
   */
  @PreAuthorize(hasPermi = "wxapp:exam:edit")
  @Log(title = "考试设置", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@Valid @RequestBody WxappExam wxappExam) {
    if (StringUtils.isNotEmpty(wxappExam.getParams())) {
      DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      wxappExam
          .setExamStart(LocalDateTime.parse(wxappExam.getParams().get("beginTime").toString(), df));
      wxappExam
          .setExamEnd(LocalDateTime.parse(wxappExam.getParams().get("endTime").toString(), df));
    }
    //过滤非用户id和还原用户真实id
    List<Long> candidateIds = wxappExam.getExamDetail().getCandidateIds();
    wxappExam.getExamDetail().setCandidateIds(
        candidateIds.stream().filter(e -> e > 10000).map(s -> s - 10000)
            .collect(Collectors.toList()));

    return toAjax(wxappExamService.updateById(wxappExam));
  }

  /**
   * 删除考试设置
   */
  @PreAuthorize(hasPermi = "wxapp:exam:remove")
  @Log(title = "考试设置", businessType = BusinessType.DELETE)
  @DeleteMapping("/{examIds}")
  public AjaxResult remove(@PathVariable List<Long> examIds) {
    return toAjax(wxappExamService.removeByIds(examIds));
  }
}
