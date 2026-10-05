package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.PurOrder;

/** 采购单Mapper接口 */
public interface PurOrderMapper
{
    public List<PurOrder> selectPurOrderList(PurOrder purOrder);

    public PurOrder selectPurOrderByPurchaseId(Long purchaseId);

    public PurOrder checkPurchaseNoUnique(String purchaseNo);

    public int insertPurOrder(PurOrder purOrder);

    public int updatePurOrder(PurOrder purOrder);

    public int updatePurchaseStatus(@Param("purchaseId") Long purchaseId, @Param("status") String status, @Param("updateBy") String updateBy);

    public int deletePurOrderByPurchaseIds(Long[] purchaseIds);
}
