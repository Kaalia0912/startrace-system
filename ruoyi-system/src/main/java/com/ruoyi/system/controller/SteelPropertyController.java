package com.ruoyi.system.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SteelProperty;
import com.ruoyi.system.service.ISteelPropertyService;

/**
 * 钢种性能标准Controller
 * 
 * @author yuanwenbo
 */
@RestController
@RequestMapping("/basedata/property")
public class SteelPropertyController extends BaseController
{
    @Autowired
    private ISteelPropertyService steelPropertyService;

    /**
     * 查询钢种性能标准列表
     */
    @PreAuthorize("@ss.hasPermi('basedata:property:list')")
    @GetMapping("/list")
    public TableDataInfo list(SteelProperty steelProperty)
    {
        startPage();
        List<SteelProperty> list = steelPropertyService.selectSteelPropertyList(steelProperty);
        return getDataTable(list);
    }

    /**
     * 获取钢种性能标准详细信息
     */
    @PreAuthorize("@ss.hasPermi('basedata:property:query')")
    @GetMapping(value = "/{propertyId}")
    public AjaxResult getInfo(@PathVariable("propertyId") Long propertyId)
    {
        return success(steelPropertyService.selectSteelPropertyByPropertyId(propertyId));
    }

    /**
     * 导出钢种性能标准列表
     */
    @PreAuthorize("@ss.hasPermi('basedata:property:export')")
    @Log(title = "钢种性能标准", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SteelProperty steelProperty)
    {
        List<SteelProperty> list = steelPropertyService.selectSteelPropertyList(steelProperty);
        ExcelUtil<SteelProperty> util = new ExcelUtil<SteelProperty>(SteelProperty.class);
        util.exportExcel(response, list, "钢种性能标准");
    }

    /**
     * 新增钢种性能标准
     */
    @PreAuthorize("@ss.hasPermi('basedata:property:add')")
    @Log(title = "钢种性能标准", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SteelProperty steelProperty)
    {
        return toAjax(steelPropertyService.insertSteelProperty(steelProperty));
    }

    /**
     * 修改钢种性能标准
     */
    @PreAuthorize("@ss.hasPermi('basedata:property:edit')")
    @Log(title = "钢种性能标准", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SteelProperty steelProperty)
    {
        return toAjax(steelPropertyService.updateSteelProperty(steelProperty));
    }

    /**
     * 删除钢种性能标准
     */
    @PreAuthorize("@ss.hasPermi('basedata:property:remove')")
    @Log(title = "钢种性能标准", businessType = BusinessType.DELETE)
    @DeleteMapping("/{propertyIds}")
    public AjaxResult remove(@PathVariable Long[] propertyIds)
    {
        return toAjax(steelPropertyService.deleteSteelPropertyByPropertyIds(propertyIds));
    }
}
