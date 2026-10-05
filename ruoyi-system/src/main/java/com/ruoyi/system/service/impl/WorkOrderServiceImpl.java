package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.WorkOrder;
import com.ruoyi.system.mapper.WorkOrderMapper;
import com.ruoyi.system.service.IWorkOrderService;

/** 生产工单Service业务层处理 */
@Service
public class WorkOrderServiceImpl implements IWorkOrderService
{
    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Override
    public List<WorkOrder> selectWorkOrderList(WorkOrder workOrder)
    {
        return workOrderMapper.selectWorkOrderList(workOrder);
    }

    @Override
    public WorkOrder selectWorkOrderByWorkOrderId(Long workOrderId)
    {
        return workOrderMapper.selectWorkOrderByWorkOrderId(workOrderId);
    }

    @Override
    public boolean checkWorkOrderNoUnique(WorkOrder workOrder)
    {
        Long workOrderId = StringUtils.isNull(workOrder.getWorkOrderId()) ? -1L : workOrder.getWorkOrderId();
        WorkOrder info = workOrderMapper.checkWorkOrderNoUnique(workOrder.getWorkOrderNo());
        if (StringUtils.isNotNull(info) && info.getWorkOrderId().longValue() != workOrderId.longValue())
        {
            return false;
        }
        return true;
    }

    @Override
    public int insertWorkOrder(WorkOrder workOrder)
    {
        if (!checkWorkOrderNoUnique(workOrder))
        {
            throw new ServiceException("新增失败，工单号 " + workOrder.getWorkOrderNo() + " 已存在");
        }
        return workOrderMapper.insertWorkOrder(workOrder);
    }

    @Override
    public int updateWorkOrder(WorkOrder workOrder)
    {
        if (!checkWorkOrderNoUnique(workOrder))
        {
            throw new ServiceException("修改失败，工单号 " + workOrder.getWorkOrderNo() + " 已存在");
        }
        return workOrderMapper.updateWorkOrder(workOrder);
    }

    @Override
    public int deleteWorkOrderByWorkOrderIds(Long[] workOrderIds)
    {
        return workOrderMapper.deleteWorkOrderByWorkOrderIds(workOrderIds);
    }
}
