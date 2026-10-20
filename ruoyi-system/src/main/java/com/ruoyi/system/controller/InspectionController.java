package com.ruoyi.system.controller;

import java.util.Date;
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
import com.ruoyi.system.domain.Inspection;
import com.ruoyi.system.service.IInspectionService;

/** 质量检验单Controller */
@RestController
@RequestMapping("/qc/inspection")
public class InspectionController extends BaseController
{
    @Autowired
    private IInspectionService inspectionService;

    @PreAuthorize("@ss.hasPermi('qc:inspection:list')")
    @GetMapping("/list")
    public TableDataInfo list(Inspection inspection)
    {
        startPage();
        List<Inspection> list = inspectionService.selectInspectionList(inspection);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('qc:inspection:query')")
    @GetMapping(value = "/{inspectionId}")
    public AjaxResult getInfo(@PathVariable("inspectionId") Long inspectionId)
    {
        return success(inspectionService.selectInspectionByInspectionId(inspectionId));
    }

    @PreAuthorize("@ss.hasPermi('qc:inspection:export')")
    @Log(title = "质量检验单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Inspection inspection)
    {
        List<Inspection> list = inspectionService.selectInspectionList(inspection);
        ExcelUtil<Inspection> util = new ExcelUtil<Inspection>(Inspection.class);
        util.exportExcel(response, list, "质量检验单");
    }

    @PreAuthorize("@ss.hasPermi('qc:inspection:add')")
    @Log(title = "质量检验单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Inspection inspection)
    {
        inspection.setCreateBy(getUsername());
        inspection.setCreateTime(new Date());
        return toAjax(inspectionService.insertInspection(inspection));
    }

    @PreAuthorize("@ss.hasPermi('qc:inspection:edit')")
    @Log(title = "质量检验单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Inspection inspection)
    {
        inspection.setUpdateBy(getUsername());
        return toAjax(inspectionService.updateInspection(inspection));
    }

    @PreAuthorize("@ss.hasPermi('qc:inspection:remove')")
    @Log(title = "质量检验单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{inspectionIds}")
    public AjaxResult remove(@PathVariable Long[] inspectionIds)
    {
        return toAjax(inspectionService.deleteInspectionByInspectionIds(inspectionIds));
    }
}
