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
import com.ruoyi.extra.domain.WxappIdea;
import com.ruoyi.extra.service.IWxappIdeaService;
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
 * 意见反馈Controller
 *
 * @author Li Jiabin
 * @date 2021-04-08
 */
@RestController
@RequestMapping("/idea")
public class WxappIdeaController extends BaseController {

  @Resource
  private IWxappIdeaService wxappIdeaService;

  /**
   * 查询意见反馈列表
   */
  @PreAuthorize(hasPermi = "wxapp:idea:list")
  @GetMapping("/list")
  public TableDataInfo list(WxappIdea wxappIdea) {
    PageDomain pageDomain = TableSupport.buildPageRequest();

    QueryWrapper<WxappIdea> wrapper2 = new QueryWrapper<>();
    //查询条件

    Page<WxappIdea> wxappIdeaPage = wxappIdeaService.page(new Page<>(pageDomain.getPageNum(),
        pageDomain.getPageSize()), wrapper2);

    TableDataInfo rspData = new TableDataInfo();
    rspData.setCode(HttpStatus.SUCCESS);
    rspData.setRows(wxappIdeaPage.getRecords());
    rspData.setMsg("查询成功");
    rspData.setTotal(wxappIdeaPage.getTotal());
    return rspData;

  }

  /**
   * 导出意见反馈列表
   */
  @PreAuthorize(hasPermi = "wxapp:idea:export")
  @Log(title = "意见反馈", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  public void export(HttpServletResponse response, WxappIdea wxappIdea) throws IOException {
    QueryWrapper<WxappIdea> queryWrapper = new QueryWrapper<>();
    //导出条件

    List<WxappIdea> list = wxappIdeaService.list(queryWrapper);
    ExcelUtil<WxappIdea> util = new ExcelUtil<>(WxappIdea.class);
    util.exportExcel(response, list, "store");
  }

  /**
   * 导出意见反馈模板Excel
   */
  @PostMapping("/importTemplate")
  public void importTemplate(HttpServletResponse response) throws IOException {
    ExcelUtil<WxappIdea> util = new ExcelUtil<>(WxappIdea.class);
    util.importTemplateExcel(response, "意见反馈");
  }


  /**
   * 获取意见反馈详细信息
   */
  @PreAuthorize(hasPermi = "wxapp:idea:query")
  @GetMapping(value = "/{ideaId}")
  public AjaxResult getInfo(@PathVariable("ideaId") Long ideaId) {
    return AjaxResult.success(wxappIdeaService.getById(ideaId));
  }

  /**
   * 新增意见反馈
   */
  @PreAuthorize(hasPermi = "wxapp:idea:add")
  @Log(title = "意见反馈", businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@Valid @RequestBody WxappIdea wxappIdea) {
    return toAjax(wxappIdeaService.save(wxappIdea));
  }

  /**
   * 修改意见反馈
   */
  @PreAuthorize(hasPermi = "wxapp:idea:edit")
  @Log(title = "意见反馈", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@Valid @RequestBody WxappIdea wxappIdea) {
    return toAjax(wxappIdeaService.updateById(wxappIdea));
  }

  /**
   * 删除意见反馈
   */
  @PreAuthorize(hasPermi = "wxapp:idea:remove")
  @Log(title = "意见反馈", businessType = BusinessType.DELETE)
  @DeleteMapping("/{ideaIds}")
  public AjaxResult remove(@PathVariable List<Long> ideaIds) {
    return toAjax(wxappIdeaService.removeByIds(ideaIds));
  }
}
