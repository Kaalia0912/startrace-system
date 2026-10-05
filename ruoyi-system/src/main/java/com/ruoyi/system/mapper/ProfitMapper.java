package com.ruoyi.system.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/** 毛利统计Mapper接口 */
public interface ProfitMapper
{
    /**
     * 订单毛利：订单金额 − 原料成本（成品净重×物料原料单价）− 加工成本（成品净重×加工费单价）
     *
     * @param processCost 加工费单价(元/kg)
     * @param orderNo 订单号（模糊，可空）
     * @param customerName 客户（模糊，可空）
     */
    public List<Map<String, Object>> selectOrderProfit(@Param("processCost") BigDecimal processCost,
                                                       @Param("orderNo") String orderNo,
                                                       @Param("customerName") String customerName);
}
