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
import com.ruoyi.system.domain.Tolerance;
import com.ruoyi.system.service.IToleranceService;

/**
 * 宽度允许偏差标准Controller
 * 
 * @author yuanwenbo
 */
@RestController
@RequestMapping("/basedata/tolerance")
public class ToleranceController extends BaseController
{
    @Autowired
    private IToleranceService toleranceService;

    /**
     * 查询宽度允许偏差标准列表
     */
    @PreAuthorize("@ss.hasPermi('basedata:tolerance:list')")
    @GetMapping("/list")
    public TableDataInfo list(Tolerance tolerance)
    {
        startPage();
        List<Tolerance> list = toleranceService.selectToleranceList(tolerance);
        return getDataTable(list);
    }

    /**
     * 获取宽度允许偏差标准详细信息
     */
    @PreAuthorize("@ss.hasPermi('basedata:tolerance:query')")
    @GetMapping(value = "/{toleranceId}")
    public AjaxResult getInfo(@PathVariable("toleranceId") Long toleranceId)
    {
        return success(toleranceService.selectToleranceByToleranceId(toleranceId));
    }

    /**
     * 导出宽度允许偏差标准列表
     */
    @PreAuthorize("@ss.hasPermi('basedata:tolerance:export')")
    @Log(title = "宽度公差标准", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Tolerance tolerance)
    {
        List<Tolerance> list = toleranceService.selectToleranceList(tolerance);
        ExcelUtil<Tolerance> util = new ExcelUtil<Tolerance>(Tolerance.class);
        util.exportExcel(response, list, "宽度公差标准");
    }

    /**
     * 新增宽度允许偏差标准
     */
    @PreAuthorize("@ss.hasPermi('basedata:tolerance:add')")
    @Log(title = "宽度公差标准", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Tolerance tolerance)
    {
        return toAjax(toleranceService.insertTolerance(tolerance));
    }

    /**
     * 修改宽度允许偏差标准
     */
    @PreAuthorize("@ss.hasPermi('basedata:tolerance:edit')")
    @Log(title = "宽度公差标准", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Tolerance tolerance)
    {
        return toAjax(toleranceService.updateTolerance(tolerance));
    }

    /**
     * 删除宽度允许偏差标准
     */
    @PreAuthorize("@ss.hasPermi('basedata:tolerance:remove')")
    @Log(title = "宽度公差标准", businessType = BusinessType.DELETE)
    @DeleteMapping("/{toleranceIds}")
    public AjaxResult remove(@PathVariable Long[] toleranceIds)
    {
        return toAjax(toleranceService.deleteToleranceByToleranceIds(toleranceIds));
    }
}
