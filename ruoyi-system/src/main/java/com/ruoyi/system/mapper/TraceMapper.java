package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.FinishCoil;
import com.ruoyi.system.domain.Inspection;
import com.ruoyi.system.domain.ProcessRecord;
import com.ruoyi.system.domain.SaleOrder;
import com.ruoyi.system.domain.Shipment;

/** 卷号追溯Mapper接口 */
public interface TraceMapper
{
    /** 成品卷（追溯起点） */
    public FinishCoil selectFinishCoilByCoilNo(String coilNo);

    /** 该卷关联的销售订单（按订单号，可能多条） */
    public List<SaleOrder> selectSaleOrderByOrderNo(String orderNo);

    /** 该订单对应的工序报工记录 */
    public List<ProcessRecord> selectProcessRecordByOrderNo(String orderNo);

    /** 该卷的检验记录 */
    public List<Inspection> selectInspectionByCoilNo(String coilNo);

    /** 该卷的发货记录（含单头） */
    public List<Shipment> selectShipmentByCoilNo(String coilNo);
}
