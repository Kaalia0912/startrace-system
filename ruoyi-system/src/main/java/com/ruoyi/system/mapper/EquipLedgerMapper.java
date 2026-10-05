package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.EquipLedger;

/** 设备台账Mapper接口 */
public interface EquipLedgerMapper
{
    public List<EquipLedger> selectEquipLedgerList(EquipLedger equipLedger);

    public EquipLedger selectEquipLedgerByEquipId(Long equipId);

    public EquipLedger checkEquipCodeUnique(String equipCode);

    public int insertEquipLedger(EquipLedger equipLedger);

    public int updateEquipLedger(EquipLedger equipLedger);

    public int deleteEquipLedgerByEquipIds(Long[] equipIds);
}
