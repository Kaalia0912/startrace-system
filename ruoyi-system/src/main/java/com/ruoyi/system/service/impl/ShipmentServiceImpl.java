package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.FinishCoil;
import com.ruoyi.system.domain.Shipment;
import com.ruoyi.system.domain.ShipmentItem;
import com.ruoyi.system.mapper.FinishCoilMapper;
import com.ruoyi.system.mapper.ShipmentMapper;
import com.ruoyi.system.service.IShipmentService;

/**
 * 发货单Service业务层处理
 * 
 * @author yuanwenbo
 */
@Service
public class ShipmentServiceImpl implements IShipmentService
{
    @Autowired
    private ShipmentMapper shipmentMapper;

    @Autowired
    private FinishCoilMapper finishCoilMapper;

    /**
     * 查询发货单列表
     */
    @Override
    public List<Shipment> selectShipmentList(Shipment shipment)
    {
        return shipmentMapper.selectShipmentList(shipment);
    }

    /**
     * 根据主键查询发货单（含明细）
     */
    @Override
    public Shipment selectShipmentByShipmentId(Long shipmentId)
    {
        return shipmentMapper.selectShipmentByShipmentId(shipmentId);
    }

    /**
     * 按选中的成品卷发货：生成发货单 + 明细，并把卷置为已发货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int ship(Shipment shipment)
    {
        Long[] coilIds = shipment.getCoilIds();
        if (coilIds == null || coilIds.length == 0)
        {
            throw new ServiceException("请先勾选要发货的成品卷");
        }
        List<FinishCoil> coils = finishCoilMapper.selectFinishCoilByCoilIds(coilIds);
        if (coils == null || coils.isEmpty())
        {
            throw new ServiceException("选中的成品卷不存在");
        }
        for (FinishCoil coil : coils)
        {
            if ("1".equals(coil.getCoilStatus()))
            {
                throw new ServiceException("卷号 " + coil.getCoilNo() + " 已发货，请勿重复操作");
            }
        }
        // 汇总总重与总件数
        BigDecimal totalWeight = BigDecimal.ZERO;
        int totalPieces = 0;
        for (FinishCoil coil : coils)
        {
            if (coil.getNetWeight() != null)
            {
                totalWeight = totalWeight.add(coil.getNetWeight());
            }
            if (coil.getPieces() != null)
            {
                totalPieces += coil.getPieces();
            }
        }
        // 单头缺省值：客户/订单号取第一卷，日期取当天
        FinishCoil first = coils.get(0);
        if (StringUtils.isEmpty(shipment.getCustomerName()))
        {
            shipment.setCustomerName(first.getCustomerName());
        }
        if (StringUtils.isEmpty(shipment.getOrderNo()))
        {
            shipment.setOrderNo(first.getOrderNo());
        }
        if (shipment.getShipmentDate() == null)
        {
            shipment.setShipmentDate(new Date());
        }
        if (StringUtils.isEmpty(shipment.getShipmentNo()))
        {
            shipment.setShipmentNo("FH" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        }
        if (StringUtils.isEmpty(shipment.getStatus()))
        {
            shipment.setStatus("0");
        }
        shipment.setTotalWeight(totalWeight);
        shipment.setTotalPieces(totalPieces);
        shipment.setCreateTime(new Date());
        shipmentMapper.insertShipment(shipment);
        // 明细一行一卷
        for (FinishCoil coil : coils)
        {
            ShipmentItem item = new ShipmentItem();
            item.setShipmentId(shipment.getShipmentId());
            item.setCoilNo(coil.getCoilNo());
            item.setMaterialCode(coil.getMaterialCode());
            item.setProductName(coil.getProductName());
            item.setSpec(coil.getSpec());
            item.setNetWeight(coil.getNetWeight());
            item.setPieces(coil.getPieces());
            shipmentMapper.insertShipmentItem(item);
        }
        finishCoilMapper.updateShipStatusByCoilIds(coilIds, shipment.getCreateBy());
        return 1;
    }

    /**
     * 修改发货单
     */
    @Override
    public int updateShipment(Shipment shipment)
    {
        return shipmentMapper.updateShipment(shipment);
    }

    /**
     * 批量删除发货单（含明细）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteShipmentByShipmentIds(Long[] shipmentIds)
    {
        shipmentMapper.deleteShipmentItemByShipmentIds(shipmentIds);
        return shipmentMapper.deleteShipmentByShipmentIds(shipmentIds);
    }
}
