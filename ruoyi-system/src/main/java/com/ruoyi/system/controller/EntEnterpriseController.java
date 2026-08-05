package com.ruoyi.system.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.EntEnterprise;
import com.ruoyi.system.service.IEntEnterpriseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 企业档案Controller
 * 
 * @author ruoyi
 * @date 2026-08-03
 */
@RestController
@RequestMapping("/enterprise/enterprise")
public class EntEnterpriseController extends BaseController
{
    @Autowired
    private IEntEnterpriseService entEnterpriseService;

    /**
     * 查询企业档案列表
     */
    @PreAuthorize("@ss.hasPermi('enterprise:enterprise:list')")
    @GetMapping("/list")
    public TableDataInfo list(EntEnterprise entEnterprise)
    {
        startPage();
        List<EntEnterprise> list = entEnterpriseService.selectEntEnterpriseList(entEnterprise);
        return getDataTable(list);
    }

    /**
     * 导出企业档案列表
     */
    @PreAuthorize("@ss.hasPermi('enterprise:enterprise:export')")
    @Log(title = "企业档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EntEnterprise entEnterprise)
    {
        List<EntEnterprise> list = entEnterpriseService.selectEntEnterpriseList(entEnterprise);
        ExcelUtil<EntEnterprise> util = new ExcelUtil<EntEnterprise>(EntEnterprise.class);
        util.exportExcel(response, list, "企业档案数据");
    }

    /**
     * 获取企业档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('enterprise:enterprise:query')")
    @GetMapping(value = "/{entId}")
    public AjaxResult getInfo(@PathVariable("entId") Long entId)
    {
        return success(entEnterpriseService.selectEntEnterpriseByEntId(entId));
    }

    /**
     * 新增企业档案
     */
    @PreAuthorize("@ss.hasPermi('enterprise:enterprise:add')")
    @Log(title = "企业档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EntEnterprise entEnterprise)
    {
        return toAjax(entEnterpriseService.insertEntEnterprise(entEnterprise));
    }

    /**
     * 修改企业档案
     */
    @PreAuthorize("@ss.hasPermi('enterprise:enterprise:edit')")
    @Log(title = "企业档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EntEnterprise entEnterprise)
    {
        return toAjax(entEnterpriseService.updateEntEnterprise(entEnterprise));
    }

    /**
     * 删除企业档案
     */
    @PreAuthorize("@ss.hasPermi('enterprise:enterprise:remove')")
    @Log(title = "企业档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{entIds}")
    public AjaxResult remove(@PathVariable Long[] entIds)
    {
        return toAjax(entEnterpriseService.deleteEntEnterpriseByEntIds(entIds));
    }

    /**
     * 导入企业档案数据
     */
    @PreAuthorize("@ss.hasPermi('enterprise:enterprise:import')")
    @Log(title = "企业档案", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<EntEnterprise> util = new ExcelUtil<EntEnterprise>(EntEnterprise.class);
        List<EntEnterprise> list = util.importExcel(file.getInputStream());
        String message = entEnterpriseService.importEnterprise(list, updateSupport);
        return success(message);
    }

    /**
     * 下载企业档案导入模板
     */
    @PreAuthorize("@ss.hasPermi('enterprise:enterprise:import')")
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<EntEnterprise> util = new ExcelUtil<EntEnterprise>(EntEnterprise.class);
        return util.importTemplateExcel("企业数据");
    }
}
