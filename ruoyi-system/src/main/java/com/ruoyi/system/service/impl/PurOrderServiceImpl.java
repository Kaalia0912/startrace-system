package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.PurOrder;
import com.ruoyi.system.domain.RawCoil;
import com.ruoyi.system.mapper.PurOrderMapper;
import com.ruoyi.system.mapper.RawCoilMapper;
import com.ruoyi.system.service.IPurOrderService;

/** 采购单Service业务层处理 */
@Service
public class PurOrderServiceImpl implements IPurOrderService
{
    @Autowired
    private PurOrderMapper purOrderMapper;

    @Autowired
    private RawCoilMapper rawCoilMapper;

    @Override
    public List<PurOrder> selectPurOrderList(PurOrder purOrder)
    {
        return purOrderMapper.selectPurOrderList(purOrder);
    }

    @Override
    public PurOrder selectPurOrderByPurchaseId(Long purchaseId)
    {
        return purOrderMapper.selectPurOrderByPurchaseId(purchaseId);
    }

    @Override
    public boolean checkPurchaseNoUnique(PurOrder purOrder)
    {
        Long purchaseId = StringUtils.isNull(purOrder.getPurchaseId()) ? -1L : purOrder.getPurchaseId();
        PurOrder info = purOrderMapper.checkPurchaseNoUnique(purOrder.getPurchaseNo());
        if (StringUtils.isNotNull(info) && info.getPurchaseId().longValue() != purchaseId.longValue())
        {
            return false;
        }
        return true;
    }

    @Override
    public int insertPurOrder(PurOrder purOrder)
    {
        if (!checkPurchaseNoUnique(purOrder))
        {
            throw new ServiceException("新增失败，采购单号 " + purOrder.getPurchaseNo() + " 已存在");
        }
        return purOrderMapper.insertPurOrder(purOrder);
    }

    @Override
    public int updatePurOrder(PurOrder purOrder)
    {
        if (!checkPurchaseNoUnique(purOrder))
        {
            throw new ServiceException("修改失败，采购单号 " + purOrder.getPurchaseNo() + " 已存在");
        }
        return purOrderMapper.updatePurOrder(purOrder);
    }

    /**
     * 收货入库：按采购单生成一条原料卷台账（卷号取采购单号），再置为已收货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int receive(Long purchaseId, String updateBy)
    {
        PurOrder order = purOrderMapper.selectPurOrderByPurchaseId(purchaseId);
        if (order == null)
        {
            throw new ServiceException("采购单不存在");
        }
        if ("1".equals(order.getStatus()))
        {
            throw new ServiceException("采购单 " + order.getPurchaseNo() + " 已收货，请勿重复操作");
        }
        RawCoil coil = new RawCoil();
        coil.setCoilNo(order.getPurchaseNo());
        coil.setSupplier(order.getSupplier());
        coil.setMaterial(order.getMaterial());
        coil.setSpec(order.getSpec());
        coil.setNetWeight(order.getWeight());
        coil.setArriveDate(new Date());
        coil.setQcStatus("0");
        coil.setCoilStatus("0");
        coil.setRemark("由采购单 " + order.getPurchaseNo() + " 收货生成");
        coil.setCreateBy(updateBy);
        coil.setCreateTime(new Date());
        if (rawCoilMapper.checkCoilNoUnique(order.getPurchaseNo()) != null)
        {
            throw new ServiceException("原料卷号 " + order.getPurchaseNo() + " 已存在，请先处理该卷或改用其他采购单号");
        }
        rawCoilMapper.insertRawCoil(coil);
        return purOrderMapper.updatePurchaseStatus(purchaseId, "1", updateBy);
    }

    @Override
    public int deletePurOrderByPurchaseIds(Long[] purchaseIds)
    {
        return purOrderMapper.deletePurOrderByPurchaseIds(purchaseIds);
    }
}
