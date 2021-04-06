package com.ruoyi.question.controller;

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
import com.ruoyi.question.domain.QueStore;
import com.ruoyi.question.domain.QueSubject;
import com.ruoyi.question.service.IQueSubjectService;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * 专题数据Controller
 *
 * @author Li Jiabin
 * @date 2021-04-02
 */
@RestController
@RequestMapping("/subject")
public class QueSubjectController  extends BaseController
{
    @Resource
    private IQueSubjectService queSubjectService;

    /**
     * 查询专题数据列表
     */
    @PreAuthorize(hasPermi = "question:subject:list")
    @GetMapping("/list")
    public TableDataInfo list(QueSubject queSubject)
    {
        PageDomain pageDomain=TableSupport.buildPageRequest();

        QueryWrapper<QueSubject> wrapper2 = new QueryWrapper<>();
        //查询条件
        wrapper2.lambda().like(StringUtils.isNotEmpty(queSubject.getSubjectName()), QueSubject::getSubjectName,queSubject.getSubjectName());
        Page<QueSubject> queSubjectPage=queSubjectService.page(new Page<>(pageDomain.getPageNum(),
            pageDomain.getPageSize()), wrapper2);

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(queSubjectPage.getRecords());
        rspData.setMsg("查询成功");
        rspData.setTotal(queSubjectPage.getTotal());
        return rspData;

    }
    /**
     * 导出专题数据列表
     */
    @PreAuthorize(hasPermi = "question:subject:export")
    @Log(title = "专题数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QueSubject queSubject) throws IOException
    {
        QueryWrapper<QueSubject> queryWrapper = new QueryWrapper<>();
      //导出条件

        List<QueSubject> list = queSubjectService.list(queryWrapper);
        ExcelUtil<QueSubject> util = new ExcelUtil<>(QueSubject.class);
        util.exportExcel(response, list, "store");
    }

    /**
     * 导出专题数据模板Excel
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException
    {
        ExcelUtil<QueSubject> util = new ExcelUtil<>(QueSubject.class);
        util.importTemplateExcel(response, "专题数据");
    }


    /**
     * 获取专题数据详细信息
     */
    @PreAuthorize(hasPermi = "question:subject:query")
    @GetMapping(value = "/{subjectId}")
    public AjaxResult getInfo(@PathVariable("subjectId") Long subjectId)
    {
        return AjaxResult.success(queSubjectService.getById(subjectId));
    }

    /**
     * 新增专题数据
     */
    @PreAuthorize(hasPermi = "question:subject:add")
    @Log(title = "专题数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody QueSubject queSubject)
    {
        return toAjax(queSubjectService.save(queSubject));
    }

    /**
     * 修改专题数据
     */
    @PreAuthorize(hasPermi = "question:subject:edit")
    @Log(title = "专题数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody QueSubject queSubject)
    {
        QueryWrapper<QueSubject> queryWrapper = new QueryWrapper<>();
        return toAjax(queSubjectService.update(queSubject,queryWrapper));
    }

    /**
     * 删除专题数据
     */
    @PreAuthorize(hasPermi = "question:subject:remove")
    @Log(title = "专题数据", businessType = BusinessType.DELETE)
  	@DeleteMapping("/{subjectIds}")
    public AjaxResult remove(@PathVariable List<Long> subjectIds)
    {
        return toAjax(queSubjectService.removeByIds(subjectIds));
    }
}
