package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.SaleOrder;

/**
 * 销售订单Mapper接口
 * 
 * @author yuanwenbo
 */
public interface SaleOrderMapper
{
    /**
     * 查询销售订单列表
     */
    public List<SaleOrder> selectSaleOrderList(SaleOrder saleOrder);

    /**
     * 根据订单ID查询销售订单
     */
    public SaleOrder selectSaleOrderByOrderId(Long orderId);

    /**
     * 根据订单号查询销售订单（导入去重用）
     */
    public List<SaleOrder> selectSaleOrderByOrderNo(String orderNo);

    /**
     * 订单执行进度：订单 → 工单 → 成品入库 → 发货
     */
    public List<Map<String, Object>> selectOrderProgress(SaleOrder saleOrder);

    /**
     * 经营工作台汇总数据
     */
    public Map<String, Object> selectSaleDashboardSummary();

    /**
     * 经营工作台近期订单
     */
    public List<SaleOrder> selectRecentSaleOrders();

    /**
     * 新增销售订单
     */
    public int insertSaleOrder(SaleOrder saleOrder);

    /**
     * 修改销售订单
     */
    public int updateSaleOrder(SaleOrder saleOrder);

    /**
     * 新增订单后写入自动生成的订单号
     */
    public int updateSaleOrderOrderNo(@Param("orderId") Long orderId, @Param("orderNo") String orderNo);

    /**
     * 批量删除销售订单
     */
    public int deleteSaleOrderByOrderIds(Long[] orderIds);

    /**
     * 删除销售订单
     */
    public int deleteSaleOrderByOrderId(Long orderId);
}
