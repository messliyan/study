package com.ruoyi.extra.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.constant.HttpStatus;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.PageDomain;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.core.web.page.TableSupport;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.PreAuthorize;
import com.ruoyi.extra.domain.WxappExamCandidate;
import com.ruoyi.extra.service.IWxappExamCandidateService;
import java.io.IOException;
import java.util.List;
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
 * 考试考生数据Controller
 *
 * @author Li Jiabin
 * @date 2021-04-13
 */
@RestController
@RequestMapping("/candidate")
public class WxappExamCandidateController extends BaseController {

  @Resource
  private IWxappExamCandidateService wxappExamCandidateService;

  /**
   * 查询考试考生数据列表
   */
  @PreAuthorize(hasPermi = "wxapp:candidate:list")
  @GetMapping("/list")
  public TableDataInfo list(WxappExamCandidate wxappExamCandidate) {
    PageDomain pageDomain = TableSupport.buildPageRequest();

    QueryWrapper<WxappExamCandidate> wrapper2 = new QueryWrapper<>();
    //查询条件

    Page<WxappExamCandidate> wxappExamCandidatePage = wxappExamCandidateService
        .page(new Page<>(pageDomain.getPageNum(),
            pageDomain.getPageSize()), wrapper2);

    TableDataInfo rspData = new TableDataInfo();
    rspData.setCode(HttpStatus.SUCCESS);
    rspData.setRows(wxappExamCandidatePage.getRecords());
    rspData.setMsg("查询成功");
    rspData.setTotal(wxappExamCandidatePage.getTotal());
    return rspData;

  }

  /**
   * 导出考试考生数据列表
   */
  @PreAuthorize(hasPermi = "wxapp:candidate:export")
  @Log(title = "考试考生数据", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  public void export(HttpServletResponse response, WxappExamCandidate wxappExamCandidate)
      throws IOException {
    QueryWrapper<WxappExamCandidate> queryWrapper = new QueryWrapper<>();
    //导出条件

    List<WxappExamCandidate> list = wxappExamCandidateService.list(queryWrapper);
    ExcelUtil<WxappExamCandidate> util = new ExcelUtil<>(WxappExamCandidate.class);
    util.exportExcel(response, list, "store");
  }

  /**
   * 导出考试考生数据模板Excel
   */
  @PostMapping("/importTemplate")
  public void importTemplate(HttpServletResponse response) throws IOException {
    ExcelUtil<WxappExamCandidate> util = new ExcelUtil<>(WxappExamCandidate.class);
    util.importTemplateExcel(response, "考试考生数据");
  }


  /**
   * 获取考试考生数据详细信息
   */
  @PreAuthorize(hasPermi = "wxapp:candidate:query")
  @GetMapping(value = "/{candidateId}")
  public AjaxResult getInfo(@PathVariable("candidateId") Long candidateId) {
    return AjaxResult.success(wxappExamCandidateService.getById(candidateId));
  }

  /**
   * 新增考试考生数据
   */
  @PreAuthorize(hasPermi = "wxapp:candidate:add")
  @Log(title = "考试考生数据", businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@Valid @RequestBody WxappExamCandidate wxappExamCandidate) {
    return toAjax(wxappExamCandidateService.save(wxappExamCandidate));
  }

  /**
   * 修改考试考生数据
   */
  @PreAuthorize(hasPermi = "wxapp:candidate:edit")
  @Log(title = "考试考生数据", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@Valid @RequestBody WxappExamCandidate wxappExamCandidate) {
    return toAjax(wxappExamCandidateService.updateById(wxappExamCandidate));
  }

  /**
   * 删除考试考生数据
   */
  @PreAuthorize(hasPermi = "wxapp:candidate:remove")
  @Log(title = "考试考生数据", businessType = BusinessType.DELETE)
  @DeleteMapping("/{candidateIds}")
  public AjaxResult remove(@PathVariable List<Long> candidateIds) {
    return toAjax(wxappExamCandidateService.removeByIds(candidateIds));
  }
}
