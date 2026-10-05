package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.EquipLedger;

/** 设备台账Service接口 */
public interface IEquipLedgerService
{
    public List<EquipLedger> selectEquipLedgerList(EquipLedger equipLedger);

    public EquipLedger selectEquipLedgerByEquipId(Long equipId);

    public boolean checkEquipCodeUnique(EquipLedger equipLedger);

    public int insertEquipLedger(EquipLedger equipLedger);

    public int updateEquipLedger(EquipLedger equipLedger);

    public int deleteEquipLedgerByEquipIds(Long[] equipIds);
}
