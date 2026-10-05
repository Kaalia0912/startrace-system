package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.EquipLedger;
import com.ruoyi.system.mapper.EquipLedgerMapper;
import com.ruoyi.system.service.IEquipLedgerService;

/** 设备台账Service业务层处理 */
@Service
public class EquipLedgerServiceImpl implements IEquipLedgerService
{
    @Autowired
    private EquipLedgerMapper equipLedgerMapper;

    @Override
    public List<EquipLedger> selectEquipLedgerList(EquipLedger equipLedger)
    {
        return equipLedgerMapper.selectEquipLedgerList(equipLedger);
    }

    @Override
    public EquipLedger selectEquipLedgerByEquipId(Long equipId)
    {
        return equipLedgerMapper.selectEquipLedgerByEquipId(equipId);
    }

    @Override
    public boolean checkEquipCodeUnique(EquipLedger equipLedger)
    {
        Long equipId = StringUtils.isNull(equipLedger.getEquipId()) ? -1L : equipLedger.getEquipId();
        EquipLedger info = equipLedgerMapper.checkEquipCodeUnique(equipLedger.getEquipCode());
        if (StringUtils.isNotNull(info) && info.getEquipId().longValue() != equipId.longValue())
        {
            return false;
        }
        return true;
    }

    @Override
    public int insertEquipLedger(EquipLedger equipLedger)
    {
        if (!checkEquipCodeUnique(equipLedger))
        {
            throw new ServiceException("新增失败，设备编号 " + equipLedger.getEquipCode() + " 已存在");
        }
        return equipLedgerMapper.insertEquipLedger(equipLedger);
    }

    @Override
    public int updateEquipLedger(EquipLedger equipLedger)
    {
        if (!checkEquipCodeUnique(equipLedger))
        {
            throw new ServiceException("修改失败，设备编号 " + equipLedger.getEquipCode() + " 已存在");
        }
        return equipLedgerMapper.updateEquipLedger(equipLedger);
    }

    @Override
    public int deleteEquipLedgerByEquipIds(Long[] equipIds)
    {
        return equipLedgerMapper.deleteEquipLedgerByEquipIds(equipIds);
    }
}
