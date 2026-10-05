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
import com.ruoyi.system.domain.Process;
import com.ruoyi.system.service.IProcessService;

/**
 * 工序库Controller
 * 
 * @author yuanwenbo
 */
@RestController
@RequestMapping("/basedata/process")
public class ProcessController extends BaseController
{
    @Autowired
    private IProcessService processService;

    /**
     * 查询工序库列表
     */
    @PreAuthorize("@ss.hasPermi('basedata:process:list')")
    @GetMapping("/list")
    public TableDataInfo list(Process process)
    {
        startPage();
        List<Process> list = processService.selectProcessList(process);
        return getDataTable(list);
    }

    /**
     * 获取工序详细信息
     */
    @PreAuthorize("@ss.hasPermi('basedata:process:query')")
    @GetMapping(value = "/{processId}")
    public AjaxResult getInfo(@PathVariable("processId") Long processId)
    {
        return success(processService.selectProcessByProcessId(processId));
    }

    /**
     * 导出工序库列表
     */
    @PreAuthorize("@ss.hasPermi('basedata:process:export')")
    @Log(title = "工序库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Process process)
    {
        List<Process> list = processService.selectProcessList(process);
        ExcelUtil<Process> util = new ExcelUtil<Process>(Process.class);
        util.exportExcel(response, list, "工序库");
    }

    /**
     * 新增工序
     */
    @PreAuthorize("@ss.hasPermi('basedata:process:add')")
    @Log(title = "工序库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Process process)
    {
        return toAjax(processService.insertProcess(process));
    }

    /**
     * 修改工序
     */
    @PreAuthorize("@ss.hasPermi('basedata:process:edit')")
    @Log(title = "工序库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Process process)
    {
        return toAjax(processService.updateProcess(process));
    }

    /**
     * 删除工序
     */
    @PreAuthorize("@ss.hasPermi('basedata:process:remove')")
    @Log(title = "工序库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{processIds}")
    public AjaxResult remove(@PathVariable Long[] processIds)
    {
        return toAjax(processService.deleteProcessByProcessIds(processIds));
    }
}
