package com.ruoyi.system.controller;

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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.FinReceipt;
import com.ruoyi.system.service.IFinReceiptService;

/** 收款登记 / 应收汇总Controller */
@RestController
@RequestMapping("/fin/receipt")
public class FinReceiptController extends BaseController
{
    @Autowired
    private IFinReceiptService finReceiptService;

    @PreAuthorize("@ss.hasPermi('fin:receipt:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinReceipt finReceipt)
    {
        startPage();
        List<FinReceipt> list = finReceiptService.selectFinReceiptList(finReceipt);
        return getDataTable(list);
    }

    /**
     * 应收汇总（按客户）
     */
    @PreAuthorize("@ss.hasPermi('fin:receivable:list')")
    @GetMapping("/summary")
    public TableDataInfo summary()
    {
        startPage();
        List<Map<String, Object>> list = finReceiptService.selectReceivableSummary();
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('fin:receipt:query')")
    @GetMapping(value = "/{receiptId}")
    public AjaxResult getInfo(@PathVariable("receiptId") Long receiptId)
    {
        return success(finReceiptService.selectFinReceiptByReceiptId(receiptId));
    }

    @PreAuthorize("@ss.hasPermi('fin:receipt:export')")
    @Log(title = "收款登记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinReceipt finReceipt)
    {
        List<FinReceipt> list = finReceiptService.selectFinReceiptList(finReceipt);
        ExcelUtil<FinReceipt> util = new ExcelUtil<FinReceipt>(FinReceipt.class);
        util.exportExcel(response, list, "收款登记");
    }

    @PreAuthorize("@ss.hasPermi('fin:receipt:add')")
    @Log(title = "收款登记", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinReceipt finReceipt)
    {
        finReceipt.setCreateBy(getUsername());
        return toAjax(finReceiptService.insertFinReceipt(finReceipt));
    }

    @PreAuthorize("@ss.hasPermi('fin:receipt:edit')")
    @Log(title = "收款登记", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinReceipt finReceipt)
    {
        finReceipt.setUpdateBy(getUsername());
        return toAjax(finReceiptService.updateFinReceipt(finReceipt));
    }

    @PreAuthorize("@ss.hasPermi('fin:receipt:remove')")
    @Log(title = "收款登记", businessType = BusinessType.DELETE)
    @DeleteMapping("/{receiptIds}")
    public AjaxResult remove(@PathVariable Long[] receiptIds)
    {
        return toAjax(finReceiptService.deleteFinReceiptByReceiptIds(receiptIds));
    }
}
