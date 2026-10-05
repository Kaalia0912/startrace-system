package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Shipment;
import com.ruoyi.system.domain.ShipmentItem;

/**
 * 发货单Mapper接口
 * 
 * @author yuanwenbo
 */
public interface ShipmentMapper
{
    /**
     * 查询发货单列表
     */
    public List<Shipment> selectShipmentList(Shipment shipment);

    /**
     * 根据主键查询发货单（含明细）
     */
    public Shipment selectShipmentByShipmentId(Long shipmentId);

    /**
     * 新增发货单
     */
    public int insertShipment(Shipment shipment);

    /**
     * 新增发货单明细
     */
    public int insertShipmentItem(ShipmentItem item);

    /**
     * 修改发货单
     */
    public int updateShipment(Shipment shipment);

    /**
     * 批量删除发货单
     */
    public int deleteShipmentByShipmentIds(Long[] shipmentIds);

    /**
     * 批量删除发货单明细
     */
    public int deleteShipmentItemByShipmentIds(Long[] shipmentIds);
}
