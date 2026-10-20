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
import com.ruoyi.system.domain.EquipLedger;
import com.ruoyi.system.service.IEquipLedgerService;

/** 设备台账Controller */
@RestController
@RequestMapping("/prod/equip")
public class EquipLedgerController extends BaseController
{
    @Autowired
    private IEquipLedgerService equipLedgerService;

    @PreAuthorize("@ss.hasPermi('prod:equip:list')")
    @GetMapping("/list")
    public TableDataInfo list(EquipLedger equipLedger)
    {
        startPage();
        List<EquipLedger> list = equipLedgerService.selectEquipLedgerList(equipLedger);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('prod:equip:query')")
    @GetMapping(value = "/{equipId}")
    public AjaxResult getInfo(@PathVariable("equipId") Long equipId)
    {
        return success(equipLedgerService.selectEquipLedgerByEquipId(equipId));
    }

    @PreAuthorize("@ss.hasPermi('prod:equip:export')")
    @Log(title = "设备台账", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EquipLedger equipLedger)
    {
        List<EquipLedger> list = equipLedgerService.selectEquipLedgerList(equipLedger);
        ExcelUtil<EquipLedger> util = new ExcelUtil<EquipLedger>(EquipLedger.class);
        util.exportExcel(response, list, "设备台账");
    }

    @PreAuthorize("@ss.hasPermi('prod:equip:add')")
    @Log(title = "设备台账", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EquipLedger equipLedger)
    {
        equipLedger.setCreateBy(getUsername());
        equipLedger.setCreateTime(new Date());
        return toAjax(equipLedgerService.insertEquipLedger(equipLedger));
    }

    @PreAuthorize("@ss.hasPermi('prod:equip:edit')")
    @Log(title = "设备台账", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EquipLedger equipLedger)
    {
        equipLedger.setUpdateBy(getUsername());
        return toAjax(equipLedgerService.updateEquipLedger(equipLedger));
    }

    @PreAuthorize("@ss.hasPermi('prod:equip:remove')")
    @Log(title = "设备台账", businessType = BusinessType.DELETE)
    @DeleteMapping("/{equipIds}")
    public AjaxResult remove(@PathVariable Long[] equipIds)
    {
        return toAjax(equipLedgerService.deleteEquipLedgerByEquipIds(equipIds));
    }
}
