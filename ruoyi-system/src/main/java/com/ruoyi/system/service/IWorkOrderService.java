package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WorkOrder;

/** 生产工单Service接口 */
public interface IWorkOrderService
{
    public List<WorkOrder> selectWorkOrderList(WorkOrder workOrder);

    public WorkOrder selectWorkOrderByWorkOrderId(Long workOrderId);

    public boolean checkWorkOrderNoUnique(WorkOrder workOrder);

    public int insertWorkOrder(WorkOrder workOrder);

    public int updateWorkOrder(WorkOrder workOrder);

    public int deleteWorkOrderByWorkOrderIds(Long[] workOrderIds);
}
