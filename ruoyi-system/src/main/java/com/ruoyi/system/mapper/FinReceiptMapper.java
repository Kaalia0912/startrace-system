package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.FinReceipt;

/** 收款登记Mapper接口 */
public interface FinReceiptMapper
{
    public List<FinReceipt> selectFinReceiptList(FinReceipt finReceipt);

    public FinReceipt selectFinReceiptByReceiptId(Long receiptId);

    public FinReceipt checkReceiptNoUnique(String receiptNo);

    public int insertFinReceipt(FinReceipt finReceipt);

    public int updateFinReceipt(FinReceipt finReceipt);

    public int deleteFinReceiptByReceiptIds(Long[] receiptIds);

    /** 按客户汇总应收：订单金额 / 已收 / 未收 / 账龄 */
    public List<Map<String, Object>> selectReceivableSummary();
}
