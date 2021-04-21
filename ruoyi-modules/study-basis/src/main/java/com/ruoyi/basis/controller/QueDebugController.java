package com.ruoyi.basis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basis.domain.QueDebug;
import com.ruoyi.basis.service.IQueDebugService;
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
 * 纠错管理Controller
 *
 * @author Li Jiabin
 * @date 2021-04-02
 */
@RestController
@RequestMapping("/debug")
public class QueDebugController extends BaseController {

  @Resource
  private IQueDebugService queDebugService;

  /**
   * 查询纠错管理列表
   */
  @PreAuthorize(hasPermi = "question:debug:list")
  @GetMapping("/list")
  public TableDataInfo list(QueDebug queDebug) {
    PageDomain pageDomain = TableSupport.buildPageRequest();

    QueryWrapper<QueDebug> wrapper2 = new QueryWrapper<>();
    //查询条件
    wrapper2.lambda()
        .eq(StringUtils.isNotEmpty(queDebug.getDebugStatus()), QueDebug::getDebugStatus,
            queDebug.getDebugStatus());

    Page<QueDebug> queDebugPage = queDebugService.page(new Page<>(pageDomain.getPageNum(),
        pageDomain.getPageSize()), wrapper2);

    TableDataInfo rspData = new TableDataInfo();
    rspData.setCode(HttpStatus.SUCCESS);
    rspData.setRows(queDebugPage.getRecords());
    rspData.setMsg("查询成功");
    rspData.setTotal(queDebugPage.getTotal());
    return rspData;

  }

  /**
   * 导出纠错管理列表
   */
  @PreAuthorize(hasPermi = "question:debug:export")
  @Log(title = "纠错管理", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  public void export(HttpServletResponse response, QueDebug queDebug) throws IOException {
    QueryWrapper<QueDebug> queryWrapper = new QueryWrapper<>();
    //导出条件

    List<QueDebug> list = queDebugService.list(queryWrapper);
    ExcelUtil<QueDebug> util = new ExcelUtil<>(QueDebug.class);
    util.exportExcel(response, list, "store");
  }

  /**
   * 导出纠错管理模板Excel
   */
  @PostMapping("/importTemplate")
  public void importTemplate(HttpServletResponse response) throws IOException {
    ExcelUtil<QueDebug> util = new ExcelUtil<>(QueDebug.class);
    util.importTemplateExcel(response, "纠错管理");
  }


  /**
   * 获取纠错管理详细信息
   */
  @PreAuthorize(hasPermi = "question:debug:query")
  @GetMapping(value = "/{debugId}")
  public AjaxResult getInfo(@PathVariable("debugId") Long debugId) {
    return AjaxResult.success(queDebugService.getById(debugId));
  }

  /**
   * 新增纠错管理
   */
  @PreAuthorize(hasPermi = "question:debug:add")
  @Log(title = "纠错管理", businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@Valid @RequestBody QueDebug queDebug) {
    return toAjax(queDebugService.save(queDebug));
  }

  /**
   * 修改纠错管理
   */
  @PreAuthorize(hasPermi = "question:debug:edit")
  @Log(title = "纠错管理", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@Valid @RequestBody QueDebug queDebug) {
    return toAjax(queDebugService.updateById(queDebug));
  }

  /**
   * 删除纠错管理
   */
  @PreAuthorize(hasPermi = "question:debug:remove")
  @Log(title = "纠错管理", businessType = BusinessType.DELETE)
  @DeleteMapping("/{debugIds}")
  public AjaxResult remove(@PathVariable List<Long> debugIds) {
    return toAjax(queDebugService.removeByIds(debugIds));
  }

  /**
   * 纠错管理进行纠错
   */
  @PreAuthorize(hasPermi = "question:debug:remove")
  @Log(title = "纠错管理", businessType = BusinessType.UPDATE)
  @PutMapping("/{debugId}")
  public AjaxResult debugById(@PathVariable("debugId") Long debugId) {
    QueDebug queDebug = new QueDebug();
    queDebug.setDebugId(debugId);
    queDebug.setDebugStatus("已纠错");
    return toAjax(queDebugService.updateById(queDebug));
  }
}
