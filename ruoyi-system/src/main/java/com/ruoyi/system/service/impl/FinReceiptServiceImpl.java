package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.FinReceipt;
import com.ruoyi.system.mapper.FinReceiptMapper;
import com.ruoyi.system.service.IFinReceiptService;

/** 收款登记Service业务层处理 */
@Service
public class FinReceiptServiceImpl implements IFinReceiptService
{
    @Autowired
    private FinReceiptMapper finReceiptMapper;

    @Override
    public List<FinReceipt> selectFinReceiptList(FinReceipt finReceipt)
    {
        return finReceiptMapper.selectFinReceiptList(finReceipt);
    }

    @Override
    public FinReceipt selectFinReceiptByReceiptId(Long receiptId)
    {
        return finReceiptMapper.selectFinReceiptByReceiptId(receiptId);
    }

    @Override
    public boolean checkReceiptNoUnique(FinReceipt finReceipt)
    {
        Long receiptId = StringUtils.isNull(finReceipt.getReceiptId()) ? -1L : finReceipt.getReceiptId();
        FinReceipt info = finReceiptMapper.checkReceiptNoUnique(finReceipt.getReceiptNo());
        if (StringUtils.isNotNull(info) && info.getReceiptId().longValue() != receiptId.longValue())
        {
            return false;
        }
        return true;
    }

    @Override
    public int insertFinReceipt(FinReceipt finReceipt)
    {
        if (!checkReceiptNoUnique(finReceipt))
        {
            throw new ServiceException("新增失败，收款单号 " + finReceipt.getReceiptNo() + " 已存在");
        }
        return finReceiptMapper.insertFinReceipt(finReceipt);
    }

    @Override
    public int updateFinReceipt(FinReceipt finReceipt)
    {
        if (!checkReceiptNoUnique(finReceipt))
        {
            throw new ServiceException("修改失败，收款单号 " + finReceipt.getReceiptNo() + " 已存在");
        }
        return finReceiptMapper.updateFinReceipt(finReceipt);
    }

    @Override
    public int deleteFinReceiptByReceiptIds(Long[] receiptIds)
    {
        return finReceiptMapper.deleteFinReceiptByReceiptIds(receiptIds);
    }

    @Override
    public List<Map<String, Object>> selectReceivableSummary()
    {
        return finReceiptMapper.selectReceivableSummary();
    }
}
