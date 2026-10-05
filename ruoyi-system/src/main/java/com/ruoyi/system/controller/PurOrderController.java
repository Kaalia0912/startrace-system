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
import com.ruoyi.system.domain.PurOrder;
import com.ruoyi.system.service.IPurOrderService;

/** 采购单Controller */
@RestController
@RequestMapping("/pur/order")
public class PurOrderController extends BaseController
{
    @Autowired
    private IPurOrderService purOrderService;

    @PreAuthorize("@ss.hasPermi('pur:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(PurOrder purOrder)
    {
        startPage();
        List<PurOrder> list = purOrderService.selectPurOrderList(purOrder);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('pur:order:query')")
    @GetMapping(value = "/{purchaseId}")
    public AjaxResult getInfo(@PathVariable("purchaseId") Long purchaseId)
    {
        return success(purOrderService.selectPurOrderByPurchaseId(purchaseId));
    }

    @PreAuthorize("@ss.hasPermi('pur:order:export')")
    @Log(title = "采购单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PurOrder purOrder)
    {
        List<PurOrder> list = purOrderService.selectPurOrderList(purOrder);
        ExcelUtil<PurOrder> util = new ExcelUtil<PurOrder>(PurOrder.class);
        util.exportExcel(response, list, "采购单");
    }

    @PreAuthorize("@ss.hasPermi('pur:order:add')")
    @Log(title = "采购单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PurOrder purOrder)
    {
        purOrder.setCreateBy(getUsername());
        return toAjax(purOrderService.insertPurOrder(purOrder));
    }

    @PreAuthorize("@ss.hasPermi('pur:order:edit')")
    @Log(title = "采购单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PurOrder purOrder)
    {
        purOrder.setUpdateBy(getUsername());
        return toAjax(purOrderService.updatePurOrder(purOrder));
    }

    /**
     * 收货入库：生成原料卷台账
     */
    @PreAuthorize("@ss.hasPermi('pur:order:receive')")
    @Log(title = "采购收货", businessType = BusinessType.UPDATE)
    @PutMapping("/receive/{purchaseId}")
    public AjaxResult receive(@PathVariable("purchaseId") Long purchaseId)
    {
        return toAjax(purOrderService.receive(purchaseId, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('pur:order:remove')")
    @Log(title = "采购单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{purchaseIds}")
    public AjaxResult remove(@PathVariable Long[] purchaseIds)
    {
        return toAjax(purOrderService.deletePurOrderByPurchaseIds(purchaseIds));
    }
}
