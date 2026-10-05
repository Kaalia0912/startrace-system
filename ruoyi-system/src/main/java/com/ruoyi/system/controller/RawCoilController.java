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
import com.ruoyi.system.domain.RawCoil;
import com.ruoyi.system.service.IRawCoilService;

/**
 * 原料卷料Controller
 * 
 * @author yuanwenbo
 */
@RestController
@RequestMapping("/inv/rawcoil")
public class RawCoilController extends BaseController
{
    @Autowired
    private IRawCoilService rawCoilService;

    /**
     * 查询原料卷料列表
     */
    @PreAuthorize("@ss.hasPermi('inv:rawcoil:list')")
    @GetMapping("/list")
    public TableDataInfo list(RawCoil rawCoil)
    {
        startPage();
        List<RawCoil> list = rawCoilService.selectRawCoilList(rawCoil);
        return getDataTable(list);
    }

    /**
     * 获取原料卷料详细信息
     */
    @PreAuthorize("@ss.hasPermi('inv:rawcoil:query')")
    @GetMapping(value = "/{coilId}")
    public AjaxResult getInfo(@PathVariable("coilId") Long coilId)
    {
        return success(rawCoilService.selectRawCoilByCoilId(coilId));
    }

    /**
     * 导出原料卷料列表
     */
    @PreAuthorize("@ss.hasPermi('inv:rawcoil:export')")
    @Log(title = "原料卷料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RawCoil rawCoil)
    {
        List<RawCoil> list = rawCoilService.selectRawCoilList(rawCoil);
        ExcelUtil<RawCoil> util = new ExcelUtil<RawCoil>(RawCoil.class);
        util.exportExcel(response, list, "原料卷料");
    }

    /**
     * 新增原料卷料
     */
    @PreAuthorize("@ss.hasPermi('inv:rawcoil:add')")
    @Log(title = "原料卷料", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RawCoil rawCoil)
    {
        rawCoil.setCreateBy(getUsername());
        return toAjax(rawCoilService.insertRawCoil(rawCoil));
    }

    /**
     * 修改原料卷料
     */
    @PreAuthorize("@ss.hasPermi('inv:rawcoil:edit')")
    @Log(title = "原料卷料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RawCoil rawCoil)
    {
        rawCoil.setUpdateBy(getUsername());
        return toAjax(rawCoilService.updateRawCoil(rawCoil));
    }

    /**
     * 删除原料卷料
     */
    @PreAuthorize("@ss.hasPermi('inv:rawcoil:remove')")
    @Log(title = "原料卷料", businessType = BusinessType.DELETE)
    @DeleteMapping("/{coilIds}")
    public AjaxResult remove(@PathVariable Long[] coilIds)
    {
        return toAjax(rawCoilService.deleteRawCoilByCoilIds(coilIds));
    }
}
