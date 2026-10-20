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
import com.ruoyi.system.domain.FinishCoil;
import com.ruoyi.system.service.IFinishCoilService;

/**
 * 成品卷Controller
 * 
 * @author yuanwenbo
 */
@RestController
@RequestMapping("/inv/finishcoil")
public class FinishCoilController extends BaseController
{
    @Autowired
    private IFinishCoilService finishCoilService;

    /**
     * 查询成品卷列表
     */
    @PreAuthorize("@ss.hasPermi('inv:finishcoil:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinishCoil finishCoil)
    {
        startPage();
        List<FinishCoil> list = finishCoilService.selectFinishCoilList(finishCoil);
        return getDataTable(list);
    }

    /**
     * 获取成品卷详细信息
     */
    @PreAuthorize("@ss.hasPermi('inv:finishcoil:query')")
    @GetMapping(value = "/{coilId}")
    public AjaxResult getInfo(@PathVariable("coilId") Long coilId)
    {
        return success(finishCoilService.selectFinishCoilByCoilId(coilId));
    }

    /**
     * 导出成品卷列表
     */
    @PreAuthorize("@ss.hasPermi('inv:finishcoil:export')")
    @Log(title = "成品卷", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinishCoil finishCoil)
    {
        List<FinishCoil> list = finishCoilService.selectFinishCoilList(finishCoil);
        ExcelUtil<FinishCoil> util = new ExcelUtil<FinishCoil>(FinishCoil.class);
        util.exportExcel(response, list, "成品卷");
    }

    /**
     * 新增成品卷
     */
    @PreAuthorize("@ss.hasPermi('inv:finishcoil:add')")
    @Log(title = "成品卷", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinishCoil finishCoil)
    {
        finishCoil.setCreateBy(getUsername());
        finishCoil.setCreateTime(new Date());
        return toAjax(finishCoilService.insertFinishCoil(finishCoil));
    }

    /**
     * 修改成品卷
     */
    @PreAuthorize("@ss.hasPermi('inv:finishcoil:edit')")
    @Log(title = "成品卷", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinishCoil finishCoil)
    {
        finishCoil.setUpdateBy(getUsername());
        return toAjax(finishCoilService.updateFinishCoil(finishCoil));
    }

    /**
     * 删除成品卷
     */
    @PreAuthorize("@ss.hasPermi('inv:finishcoil:remove')")
    @Log(title = "成品卷", businessType = BusinessType.DELETE)
    @DeleteMapping("/{coilIds}")
    public AjaxResult remove(@PathVariable Long[] coilIds)
    {
        return toAjax(finishCoilService.deleteFinishCoilByCoilIds(coilIds));
    }
}
