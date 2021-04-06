package com.ruoyi.question.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.constant.HttpStatus;
import com.ruoyi.common.core.utils.SecurityUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.page.PageDomain;
import com.ruoyi.common.core.web.page.TableSupport;
import com.ruoyi.system.api.domain.SysUser;
import java.util.List;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import javax.validation.Valid;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.PreAuthorize;
import com.ruoyi.question.domain.QueStore;
import com.ruoyi.question.service.IQueStoreService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 题库数据Controller
 * 
 * @author Li Jiabin
 * @date 2021-04-01
 */
@RestController
@RequestMapping("/store")
public class QueStoreController  extends BaseController
{
    @Resource
    private IQueStoreService queStoreService;

    /**
     * 查询题库数据列表
     */
    @PreAuthorize(hasPermi = "question:store:list")
    @GetMapping("/list")
    public TableDataInfo list(QueStore queStore)
    {

        PageDomain pageDomain=TableSupport.buildPageRequest();

        QueryWrapper<QueStore> wrapper2 = new QueryWrapper<>();
        wrapper2.lambda().like(StringUtils.isNotEmpty(queStore.getStoreContent()),QueStore::getStoreContent,queStore.getStoreContent())
            .eq(StringUtils.isNotEmpty(queStore.getStoreType()),QueStore::getStoreType,queStore.getStoreType())
            .eq(ObjectUtils.isNotEmpty(queStore.getStoreId()),QueStore::getSubject,queStore.getStoreId());

        Page<QueStore> queStorePage=queStoreService.page(new Page<>(pageDomain.getPageNum(),
            pageDomain.getPageSize()), wrapper2);

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(queStorePage.getRecords());
        rspData.setMsg("查询成功");
        rspData.setTotal(queStorePage.getTotal());
        return rspData;

    }

    /**
     * 导出题库数据列表
     */
    @PreAuthorize(hasPermi = "question:store:export")
    @Log(title = "题库数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QueStore queStore) throws IOException
    {
        QueryWrapper<QueStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotEmpty(queStore.getStoreContent()),QueStore::getStoreContent,queStore.getStoreContent())
            .eq(StringUtils.isNotEmpty(queStore.getStoreType()),QueStore::getStoreType,queStore.getStoreType())
            .eq(ObjectUtils.isNotEmpty(queStore.getStoreId()),QueStore::getSubject,queStore.getStoreId());

        List<QueStore> list = queStoreService.list(queryWrapper);
        ExcelUtil<QueStore> util = new ExcelUtil<>(QueStore.class);
        util.exportExcel(response, list, "store");
    }
    /**
     * 导入题库数据Excel
     */
    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize(hasPermi = "system:user:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<QueStore> util = new ExcelUtil<>(QueStore.class);
        List<QueStore> queStoreList = util.importExcel(file.getInputStream());

        String message = queStoreService.importUser(queStoreList, updateSupport);

        return AjaxResult.success(message);
    }
    /**
     * 导出题库数据模板Excel
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException
    {
        ExcelUtil<QueStore> util = new ExcelUtil<>(QueStore.class);
        util.importTemplateExcel(response, "题库数据");
    }


    /**
     * 获取题库数据详细信息
     */
    @PreAuthorize(hasPermi = "question:store:query")
    @GetMapping(value = "/{storeId}")
    public AjaxResult getInfo(@PathVariable("storeId") Long storeId)
    {
        return AjaxResult.success(queStoreService.getById(storeId));
    }

    /**
     * 新增题库数据
     */
    @PreAuthorize(hasPermi = "question:store:add")
    @Log(title = "题库数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody QueStore queStore)
    {
        return toAjax(queStoreService.save(queStore));
    }

    /**
     * 修改题库数据
     */
    @PreAuthorize(hasPermi = "question:store:edit")
    @Log(title = "题库数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody QueStore queStore)
    {
        QueryWrapper<QueStore> queryWrapper = new QueryWrapper<>();
        return toAjax(queStoreService.update(queStore,queryWrapper));
    }

    /**
     * 删除题库数据
     */
    @PreAuthorize(hasPermi = "question:store:remove")
    @Log(title = "题库数据", businessType = BusinessType.DELETE)
  	@DeleteMapping("/{storeIds}")
    public AjaxResult remove(@PathVariable List<Long> storeIds)
    {
        return toAjax(queStoreService.removeByIds(storeIds));
    }
}
