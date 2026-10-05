package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.ProfitMapper;
import com.ruoyi.system.service.IProfitService;
import com.ruoyi.system.service.ISysConfigService;

/** 毛利统计Service业务层处理 */
@Service
public class ProfitServiceImpl implements IProfitService
{
    /** 加工费单价参数键名 */
    public static final String PROCESS_COST_KEY = "erp.process.cost.per.kg";

    @Autowired
    private ProfitMapper profitMapper;

    @Autowired
    private ISysConfigService configService;

    /**
     * 取加工费单价：参数不存在或不是数字时按 0 处理，不影响出数
     */
    @Override
    public BigDecimal currentProcessCost()
    {
        String value = null;
        try
        {
            value = configService.selectConfigByKey(PROCESS_COST_KEY);
        }
        catch (Exception e)
        {
            value = null;
        }
        if (StringUtils.isEmpty(value))
        {
            return BigDecimal.ZERO;
        }
        try
        {
            return new BigDecimal(value.trim());
        }
        catch (NumberFormatException e)
        {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public List<Map<String, Object>> selectOrderProfit(String orderNo, String customerName)
    {
        return profitMapper.selectOrderProfit(currentProcessCost(), orderNo, customerName);
    }
}
