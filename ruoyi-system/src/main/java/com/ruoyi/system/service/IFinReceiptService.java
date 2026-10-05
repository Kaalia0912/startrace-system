package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.FinReceipt;

/** 收款登记Service接口 */
public interface IFinReceiptService
{
    public List<FinReceipt> selectFinReceiptList(FinReceipt finReceipt);

    public FinReceipt selectFinReceiptByReceiptId(Long receiptId);

    public boolean checkReceiptNoUnique(FinReceipt finReceipt);

    public int insertFinReceipt(FinReceipt finReceipt);

    public int updateFinReceipt(FinReceipt finReceipt);

    public int deleteFinReceiptByReceiptIds(Long[] receiptIds);

    /** 应收汇总 */
    public List<Map<String, Object>> selectReceivableSummary();
}
