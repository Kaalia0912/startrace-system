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
import com.ruoyi.system.domain.Shipment;
import com.ruoyi.system.service.IShipmentService;

/**
 * 发货单Controller
 * 
 * @author yuanwenbo
 */
@RestController
@RequestMapping("/inv/shipment")
public class ShipmentController extends BaseController
{
    @Autowired
    private IShipmentService shipmentService;

    /**
     * 查询发货单列表
     */
    @PreAuthorize("@ss.hasPermi('inv:shipment:list')")
    @GetMapping("/list")
    public TableDataInfo list(Shipment shipment)
    {
        startPage();
        List<Shipment> list = shipmentService.selectShipmentList(shipment);
        return getDataTable(list);
    }

    /**
     * 获取发货单详细信息（含明细）
     */
    @PreAuthorize("@ss.hasPermi('inv:shipment:query')")
    @GetMapping(value = "/{shipmentId}")
    public AjaxResult getInfo(@PathVariable("shipmentId") Long shipmentId)
    {
        return success(shipmentService.selectShipmentByShipmentId(shipmentId));
    }

    /**
     * 导出发货单列表
     */
    @PreAuthorize("@ss.hasPermi('inv:shipment:export')")
    @Log(title = "发货单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Shipment shipment)
    {
        List<Shipment> list = shipmentService.selectShipmentList(shipment);
        ExcelUtil<Shipment> util = new ExcelUtil<Shipment>(Shipment.class);
        util.exportExcel(response, list, "发货单");
    }

    /**
     * 按选中的成品卷发货
     */
    @PreAuthorize("@ss.hasPermi('inv:finishcoil:ship')")
    @Log(title = "成品发货", businessType = BusinessType.INSERT)
    @PostMapping("/ship")
    public AjaxResult ship(@RequestBody Shipment shipment)
    {
        shipment.setCreateBy(getUsername());
        shipment.setCreateTime(new Date());
        return toAjax(shipmentService.ship(shipment));
    }

    /**
     * 修改发货单（状态：已发货 / 已签收）
     */
    @PreAuthorize("@ss.hasPermi('inv:shipment:edit')")
    @Log(title = "发货单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Shipment shipment)
    {
        shipment.setUpdateBy(getUsername());
        return toAjax(shipmentService.updateShipment(shipment));
    }

    /**
     * 删除发货单
     */
    @PreAuthorize("@ss.hasPermi('inv:shipment:remove')")
    @Log(title = "发货单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{shipmentIds}")
    public AjaxResult remove(@PathVariable Long[] shipmentIds)
    {
        return toAjax(shipmentService.deleteShipmentByShipmentIds(shipmentIds));
    }
}
