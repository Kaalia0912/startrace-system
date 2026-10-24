package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.SaleOrder;

/**
 * 销售订单Service接口
 * 
 * @author yuanwenbo
 */
public interface ISaleOrderService
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
     * 订单执行进度看板数据
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
     * 批量删除销售订单
     */
    public int deleteSaleOrderByOrderIds(Long[] orderIds);

    /**
     * 导入销售订单数据
     *
     * @param list 销售订单列表
     * @param updateSupport 是否更新已存在数据
     * @return 结果
     */
    public String importSaleOrder(List<SaleOrder> list, boolean updateSupport);
}
