package com.ruoyi.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** 毛利统计Service接口 */
public interface IProfitService
{
    /** 当前加工费单价(元/kg) */
    public BigDecimal currentProcessCost();

    /** 订单毛利列表 */
    public List<Map<String, Object>> selectOrderProfit(String orderNo, String customerName);
}
