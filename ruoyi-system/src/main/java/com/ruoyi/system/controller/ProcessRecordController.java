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
import com.ruoyi.system.domain.ProcessRecord;
import com.ruoyi.system.service.IProcessRecordService;

/** 工序报工Controller */
@RestController
@RequestMapping("/prod/record")
public class ProcessRecordController extends BaseController
{
    @Autowired
    private IProcessRecordService processRecordService;

    @PreAuthorize("@ss.hasPermi('prod:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProcessRecord processRecord)
    {
        startPage();
        List<ProcessRecord> list = processRecordService.selectProcessRecordList(processRecord);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('prod:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(processRecordService.selectProcessRecordByRecordId(recordId));
    }

    @PreAuthorize("@ss.hasPermi('prod:record:export')")
    @Log(title = "工序报工", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProcessRecord processRecord)
    {
        List<ProcessRecord> list = processRecordService.selectProcessRecordList(processRecord);
        ExcelUtil<ProcessRecord> util = new ExcelUtil<ProcessRecord>(ProcessRecord.class);
        util.exportExcel(response, list, "工序报工");
    }

    @PreAuthorize("@ss.hasPermi('prod:record:add')")
    @Log(title = "工序报工", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProcessRecord processRecord)
    {
        processRecord.setCreateBy(getUsername());
        processRecord.setCreateTime(new Date());
        return toAjax(processRecordService.insertProcessRecord(processRecord));
    }

    @PreAuthorize("@ss.hasPermi('prod:record:edit')")
    @Log(title = "工序报工", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProcessRecord processRecord)
    {
        processRecord.setUpdateBy(getUsername());
        return toAjax(processRecordService.updateProcessRecord(processRecord));
    }

    @PreAuthorize("@ss.hasPermi('prod:record:remove')")
    @Log(title = "工序报工", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(processRecordService.deleteProcessRecordByRecordIds(recordIds));
    }
}
