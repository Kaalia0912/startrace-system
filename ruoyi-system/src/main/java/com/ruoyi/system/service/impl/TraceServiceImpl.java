package com.ruoyi.system.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.FinishCoil;
import com.ruoyi.system.mapper.TraceMapper;
import com.ruoyi.system.service.ITraceService;

/** 卷号追溯Service业务层处理 */
@Service
public class TraceServiceImpl implements ITraceService
{
    @Autowired
    private TraceMapper traceMapper;

    @Override
    public Map<String, Object> traceByCoilNo(String coilNo)
    {
        if (StringUtils.isEmpty(coilNo))
        {
            throw new ServiceException("请输入卷号");
        }
        FinishCoil coil = traceMapper.selectFinishCoilByCoilNo(coilNo);
        if (coil == null)
        {
            throw new ServiceException("未找到卷号 " + coilNo + " 的成品卷记录（追溯以成品卷为起点）");
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("coil", coil);
        result.put("orders", traceMapper.selectSaleOrderByOrderNo(coil.getOrderNo()));
        result.put("processRecords", traceMapper.selectProcessRecordByOrderNo(coil.getOrderNo()));
        result.put("inspections", traceMapper.selectInspectionByCoilNo(coilNo));
        result.put("shipments", traceMapper.selectShipmentByCoilNo(coilNo));
        return result;
    }
}
