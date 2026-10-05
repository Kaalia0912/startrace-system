package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PurOrder;

/** 采购单Service接口 */
public interface IPurOrderService
{
    public List<PurOrder> selectPurOrderList(PurOrder purOrder);

    public PurOrder selectPurOrderByPurchaseId(Long purchaseId);

    public boolean checkPurchaseNoUnique(PurOrder purOrder);

    public int insertPurOrder(PurOrder purOrder);

    public int updatePurOrder(PurOrder purOrder);

    /** 收货：生成原料卷台账并置为已收货 */
    public int receive(Long purchaseId, String updateBy);

    public int deletePurOrderByPurchaseIds(Long[] purchaseIds);
}
