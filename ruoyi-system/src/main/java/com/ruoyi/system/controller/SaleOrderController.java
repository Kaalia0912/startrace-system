package com.ruoyi.system.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SaleOrder;
import com.ruoyi.system.service.ISaleOrderService;

/**
 * 销售订单Controller
 * 
 * @author yuanwenbo
 */
@RestController
@RequestMapping("/order/sale")
public class SaleOrderController extends BaseController
{
    @Autowired
    private ISaleOrderService saleOrderService;

    /**
     * 查询销售订单列表
     */
    @PreAuthorize("@ss.hasPermi('order:sale:list')")
    @GetMapping("/list")
    public TableDataInfo list(SaleOrder saleOrder)
    {
        startPage();
        List<SaleOrder> list = saleOrderService.selectSaleOrderList(saleOrder);
        return getDataTable(list);
    }

    /**
     * 获取销售订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:sale:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(saleOrderService.selectSaleOrderByOrderId(orderId));
    }

    /**
     * 订单执行进度看板（订单 → 工单 → 成品入库 → 发货）
     */
    @PreAuthorize("@ss.hasPermi('order:progress:list')")
    @GetMapping("/progress")
    public TableDataInfo progress(SaleOrder saleOrder)
    {
        startPage();
        List<Map<String, Object>> list = saleOrderService.selectOrderProgress(saleOrder);
        return getDataTable(list);
    }

    /**
     * 导出销售订单列表
     */
    @PreAuthorize("@ss.hasPermi('order:sale:export')")
    @Log(title = "销售订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SaleOrder saleOrder)
    {
        List<SaleOrder> list = saleOrderService.selectSaleOrderList(saleOrder);
        ExcelUtil<SaleOrder> util = new ExcelUtil<SaleOrder>(SaleOrder.class);
        util.exportExcel(response, list, "销售订单数据");
    }

    /**
     * 导入销售订单数据
     */
    @PreAuthorize("@ss.hasPermi('order:sale:import')")
    @Log(title = "销售订单", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SaleOrder> util = new ExcelUtil<SaleOrder>(SaleOrder.class);
        List<SaleOrder> list = util.importExcel(file.getInputStream());
        String message = saleOrderService.importSaleOrder(list, updateSupport);
        return success(message);
    }

    /**
     * 下载销售订单导入模板
     */
    @PreAuthorize("@ss.hasPermi('order:sale:import')")
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<SaleOrder> util = new ExcelUtil<SaleOrder>(SaleOrder.class);
        // 模板自带一行自编示例数据（勿取自真实订单），便于参照格式填写
        List<SaleOrder> demoList = new ArrayList<SaleOrder>();
        SaleOrder demo = new SaleOrder();
        demo.setOrderNo("EX-20260907-001");
        demo.setOrderDate(java.sql.Date.valueOf("2026-09-07"));
        demo.setCustomerName("示例不锈钢贸易有限公司");
        demo.setContactPerson("王示例");
        demo.setContactPhone("13800000000");
        demo.setContactAddress("广东省佛山市示例路88号");
        demo.setMaterialCode("WL-DEMO-001");
        demo.setProductName("304不锈钢冷轧卷");
        demo.setMaterial("304");
        demo.setSpec("0.8×1219×C");
        demo.setHardness("180~220");
        demo.setTensileStrength("520~620");
        demo.setUnit("吨");
        demo.setQuantity(new BigDecimal("10"));
        demo.setUnitPrice(new BigDecimal("13200"));
        demo.setAmount(new BigDecimal("132000"));
        demo.setDeliveryDate(java.sql.Date.valueOf("2026-09-30"));
        demo.setRemark("示例数据行，正式导入前请删除本行");
        demoList.add(demo);
        AjaxResult result = util.exportExcel(demoList, "销售订单数据");
        // ExcelUtil 把模板文件名放在 msg 中，转为可访问的相对路径返回给前端
        return AjaxResult.success("操作成功", Constants.RESOURCE_PREFIX + "/download/" + result.get(AjaxResult.MSG_TAG));
    }

    /**
     * 新增销售订单
     */
    @PreAuthorize("@ss.hasPermi('order:sale:add')")
    @Log(title = "销售订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaleOrder saleOrder)
    {
        saleOrder.setCreateBy(getUsername());
        saleOrder.setCreateTime(new Date());
        return toAjax(saleOrderService.insertSaleOrder(saleOrder));
    }

    /**
     * 修改销售订单
     */
    @PreAuthorize("@ss.hasPermi('order:sale:edit')")
    @Log(title = "销售订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaleOrder saleOrder)
    {
        return toAjax(saleOrderService.updateSaleOrder(saleOrder));
    }

    /**
     * 删除销售订单
     */
    @PreAuthorize("@ss.hasPermi('order:sale:remove')")
    @Log(title = "销售订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(saleOrderService.deleteSaleOrderByOrderIds(orderIds));
    }
}
