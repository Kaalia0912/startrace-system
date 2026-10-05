package com.ruoyi.system.service;

import java.util.Map;

/** 卷号追溯Service接口 */
public interface ITraceService
{
    /**
     * 按卷号追溯全链路
     *
     * @param coilNo 卷号
     * @return 卷、订单、工序、检验、发货
     */
    public Map<String, Object> traceByCoilNo(String coilNo);
}
