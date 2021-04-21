package com.ruoyi.basis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basis.domain.QueMaterial;
import com.ruoyi.basis.service.IQueMaterialService;
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
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 专题资料Controller
 *
 * @author Li Jiabin
 * @date 2021-04-02
 */
@RestController
@RequestMapping("/material")
public class QueMaterialController extends BaseController {

  @Resource
  private IQueMaterialService queMaterialService;

  /**
   * 查询专题资料列表
   */
  @PreAuthorize(hasPermi = "question:material:list")
  @GetMapping("/list")
  public TableDataInfo list(QueMaterial queMaterial) {
    PageDomain pageDomain = TableSupport.buildPageRequest();

    QueryWrapper<QueMaterial> wrapper2 = new QueryWrapper<>();
    //查询条件
    wrapper2.lambda()
        .like(StringUtils.isNotEmpty(queMaterial.getMaterialName()), QueMaterial::getMaterialName,
            queMaterial.getMaterialName())
        .eq(StringUtils.isNotEmpty(queMaterial.getMaterialType()), QueMaterial::getMaterialType,
            queMaterial.getMaterialType())
        .eq(ObjectUtils.isNotEmpty(queMaterial.getSubjectId()), QueMaterial::getSubjectId,
            queMaterial.getSubjectId());

    Page<QueMaterial> queMaterialPage = queMaterialService.page(new Page<>(pageDomain.getPageNum(),
        pageDomain.getPageSize()), wrapper2);

    TableDataInfo rspData = new TableDataInfo();
    rspData.setCode(HttpStatus.SUCCESS);
    rspData.setRows(queMaterialPage.getRecords());
    rspData.setMsg("查询成功");
    rspData.setTotal(queMaterialPage.getTotal());
    return rspData;

  }

  /**
   * 导出专题资料列表
   */
  @PreAuthorize(hasPermi = "question:material:export")
  @Log(title = "专题资料", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  public void export(HttpServletResponse response, QueMaterial queMaterial) throws IOException {
    QueryWrapper<QueMaterial> queryWrapper = new QueryWrapper<>();
    //导出条件

    List<QueMaterial> list = queMaterialService.list(queryWrapper);
    ExcelUtil<QueMaterial> util = new ExcelUtil<>(QueMaterial.class);
    util.exportExcel(response, list, "store");
  }

  /**
   * 导出专题资料模板Excel
   */
  @PostMapping("/importTemplate")
  public void importTemplate(HttpServletResponse response) throws IOException {
    ExcelUtil<QueMaterial> util = new ExcelUtil<>(QueMaterial.class);
    util.importTemplateExcel(response, "专题资料");
  }


  /**
   * 获取专题资料详细信息
   */
  @PreAuthorize(hasPermi = "question:material:query")
  @GetMapping(value = "/{materialId}")
  public AjaxResult getInfo(@PathVariable("materialId") Long materialId) {
    return AjaxResult.success(queMaterialService.getById(materialId));
  }

  /**
   * 新增专题资料
   */
  @PreAuthorize(hasPermi = "question:material:add")
  @Log(title = "专题资料", businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@Valid @RequestBody QueMaterial queMaterial) {
    return toAjax(queMaterialService.save(queMaterial));
  }

  /**
   * 修改专题资料
   */
  @PreAuthorize(hasPermi = "question:material:edit")
  @Log(title = "专题资料", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@Valid @RequestBody QueMaterial queMaterial) {
    return toAjax(queMaterialService.updateById(queMaterial));
  }

  /**
   * 删除专题资料
   */
  @PreAuthorize(hasPermi = "question:material:remove")
  @Log(title = "专题资料", businessType = BusinessType.DELETE)
  @DeleteMapping("/{materialIds}")
  public AjaxResult remove(@PathVariable List<Long> materialIds) {
    return toAjax(queMaterialService.removeByIds(materialIds));
  }
}
