package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SaleOrderMapper;
import com.ruoyi.system.domain.SaleOrder;
import com.ruoyi.system.service.ISaleOrderService;

/**
 * 销售订单Service业务层处理
 * 
 * @author yuanwenbo
 */
@Service
public class SaleOrderServiceImpl implements ISaleOrderService
{
    @Autowired
    private SaleOrderMapper saleOrderMapper;

    /**
     * 查询销售订单列表
     */
    @Override
    public List<SaleOrder> selectSaleOrderList(SaleOrder saleOrder)
    {
        return saleOrderMapper.selectSaleOrderList(saleOrder);
    }

    /**
     * 根据订单ID查询销售订单
     */
    @Override
    public SaleOrder selectSaleOrderByOrderId(Long orderId)
    {
        return saleOrderMapper.selectSaleOrderByOrderId(orderId);
    }

    /**
     * 订单执行进度看板数据
     */
    @Override
    public List<Map<String, Object>> selectOrderProgress(SaleOrder saleOrder)
    {
        return saleOrderMapper.selectOrderProgress(saleOrder);
    }

    /**
     * 新增销售订单
     */
    @Override
    public int insertSaleOrder(SaleOrder saleOrder)
    {
        return saleOrderMapper.insertSaleOrder(saleOrder);
    }

    /**
     * 修改销售订单
     */
    @Override
    public int updateSaleOrder(SaleOrder saleOrder)
    {
        return saleOrderMapper.updateSaleOrder(saleOrder);
    }

    /**
     * 批量删除销售订单
     */
    @Override
    public int deleteSaleOrderByOrderIds(Long[] orderIds)
    {
        return saleOrderMapper.deleteSaleOrderByOrderIds(orderIds);
    }

    /**
     * 导入销售订单数据
     *
     * @param list 销售订单列表
     * @param updateSupport 是否更新已存在数据
     * @return 结果
     */
    @Override
    public String importSaleOrder(List<SaleOrder> list, boolean updateSupport)
    {
        if (list == null || list.size() == 0)
        {
            throw new ServiceException("导入数据不能为空");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (int i = 0; i < list.size(); i++)
        {
            SaleOrder order = list.get(i);
            int rowNo = i + 1;
            try
            {
                if (StringUtils.isEmpty(order.getOrderNo()))
                {
                    throw new ServiceException("订单号为空");
                }
                // 金额为空且数量单价齐全时自动计算
                if (order.getAmount() == null && order.getQuantity() != null && order.getUnitPrice() != null)
                {
                    order.setAmount(order.getQuantity().multiply(order.getUnitPrice()).setScale(2, RoundingMode.HALF_UP));
                }
                List<SaleOrder> existList = saleOrderMapper.selectSaleOrderByOrderNo(order.getOrderNo());
                if (existList == null || existList.size() == 0)
                {
                    order.setCreateTime(new Date());
                    saleOrderMapper.insertSaleOrder(order);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、第 ").append(rowNo).append(" 行（订单号 ")
                            .append(order.getOrderNo()).append("）导入成功");
                }
                else if (updateSupport)
                {
                    order.setOrderId(existList.get(0).getOrderId());
                    order.setUpdateTime(new Date());
                    saleOrderMapper.updateSaleOrder(order);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、第 ").append(rowNo).append(" 行（订单号 ")
                            .append(order.getOrderNo()).append("）更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、第 ").append(rowNo).append(" 行（订单号 ")
                            .append(order.getOrderNo()).append("）已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、第 ").append(rowNo).append(" 行导入失败：")
                        .append(e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }
}
