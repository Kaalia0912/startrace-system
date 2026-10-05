package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WorkOrder;

/** 生产工单Mapper接口 */
public interface WorkOrderMapper
{
    public List<WorkOrder> selectWorkOrderList(WorkOrder workOrder);

    public WorkOrder selectWorkOrderByWorkOrderId(Long workOrderId);

    public WorkOrder checkWorkOrderNoUnique(String workOrderNo);

    public int insertWorkOrder(WorkOrder workOrder);

    public int updateWorkOrder(WorkOrder workOrder);

    public int deleteWorkOrderByWorkOrderIds(Long[] workOrderIds);
}
