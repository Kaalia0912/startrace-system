package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Shipment;

/**
 * 发货单Service接口
 * 
 * @author yuanwenbo
 */
public interface IShipmentService
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
     * 按选中的成品卷发货：生成发货单 + 明细，并把卷置为已发货
     *
     * @param shipment 发货单（含 coilIds）
     * @return 结果
     */
    public int ship(Shipment shipment);

    /**
     * 修改发货单（状态等）
     */
    public int updateShipment(Shipment shipment);

    /**
     * 批量删除发货单（含明细）
     */
    public int deleteShipmentByShipmentIds(Long[] shipmentIds);
}
