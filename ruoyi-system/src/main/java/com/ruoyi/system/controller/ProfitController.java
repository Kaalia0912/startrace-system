package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.IProfitService;

/** 毛利统计Controller */
@RestController
@RequestMapping("/fin/profit")
public class ProfitController extends BaseController
{
    @Autowired
    private IProfitService profitService;

    /**
     * 订单毛利列表
     */
    @PreAuthorize("@ss.hasPermi('fin:profit:list')")
    @GetMapping("/list")
    public TableDataInfo list(String orderNo, String customerName)
    {
        startPage();
        List<Map<String, Object>> list = profitService.selectOrderProfit(orderNo, customerName);
        return getDataTable(list);
    }

    /**
     * 当前加工费单价(元/kg)，供页面显示计算口径
     */
    @PreAuthorize("@ss.hasPermi('fin:profit:list')")
    @GetMapping("/rate")
    public AjaxResult rate()
    {
        return success(profitService.currentProcessCost());
    }
}
