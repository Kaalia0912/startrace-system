package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
     * 经营工作台汇总数据
     */
    @Override
    public Map<String, Object> selectSaleDashboardSummary()
    {
        return saleOrderMapper.selectSaleDashboardSummary();
    }

    /**
     * 经营工作台近期订单
     */
    @Override
    public List<SaleOrder> selectRecentSaleOrders()
    {
        return saleOrderMapper.selectRecentSaleOrders();
    }

    /**
     * 新增销售订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSaleOrder(SaleOrder saleOrder)
    {
        int rows = saleOrderMapper.insertSaleOrder(saleOrder);
        if (StringUtils.isBlank(saleOrder.getOrderNo()))
        {
            Date orderDate = saleOrder.getOrderDate() == null ? new Date() : saleOrder.getOrderDate();
            String orderNo = "SO" + new SimpleDateFormat("yyyyMMdd").format(orderDate)
                    + String.format("%06d", saleOrder.getOrderId());
            saleOrder.setOrderNo(orderNo);
            if (saleOrderMapper.updateSaleOrderOrderNo(saleOrder.getOrderId(), orderNo) != 1)
            {
                throw new ServiceException("订单号生成失败");
            }
        }
        return rows;
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
    @Transactional(rollbackFor = Exception.class)
    public String importSaleOrder(List<SaleOrder> list, boolean updateSupport)
    {
        if (list == null || list.size() == 0)
        {
            throw new ServiceException("导入数据不能为空");
        }
        Set<String> orderNosInFile = new HashSet<String>();
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (int i = 0; i < list.size(); i++)
        {
            SaleOrder order = list.get(i);
            int rowNo = i + 2;
            try
            {
                if (StringUtils.isBlank(order.getOrderNo()))
                {
                    // 导入时不填写订单号，保存后自动生成
                    order.setOrderNo(null);
                }
                else
                {
                    order.setOrderNo(order.getOrderNo().trim());
                }
                if (order.getQuantity() != null && order.getQuantity().compareTo(BigDecimal.ZERO) < 0)
                {
                    throw new ServiceException("数量不能为负数");
                }
                if (order.getUnitPrice() != null && order.getUnitPrice().compareTo(BigDecimal.ZERO) < 0)
                {
                    throw new ServiceException("单价不能为负数");
                }
                if (order.getAmount() != null && order.getAmount().compareTo(BigDecimal.ZERO) < 0)
                {
                    throw new ServiceException("金额不能为负数");
                }
                // 金额为空且数量单价齐全时自动计算
                if (order.getAmount() == null && order.getQuantity() != null && order.getUnitPrice() != null)
                {
                    order.setAmount(order.getQuantity().multiply(order.getUnitPrice()).setScale(2, RoundingMode.HALF_UP));
                }
                if (order.getOrderNo() != null)
                {
                    if (!orderNosInFile.add(order.getOrderNo()))
                    {
                        throw new ServiceException("订单号在文件中重复");
                    }
                    List<SaleOrder> existList = saleOrderMapper.selectSaleOrderByOrderNo(order.getOrderNo());
                    if (existList != null && existList.size() > 1)
                    {
                        throw new ServiceException("订单号存在多条历史记录，请先清理重复数据");
                    }
                    if (existList != null && existList.size() == 1)
                    {
                        if (!updateSupport)
                        {
                            throw new ServiceException("订单号已存在");
                        }
                        order.setOrderId(existList.get(0).getOrderId());
                    }
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
        for (int i = 0; i < list.size(); i++)
        {
            SaleOrder order = list.get(i);
            int rowNo = i + 2;
            try
            {
                if (order.getOrderId() == null)
                {
                    order.setCreateTime(new Date());
                    insertSaleOrder(order);
                    successMsg.append("<br/>").append(successNum + 1).append("、第 ").append(rowNo)
                            .append(" 行（订单号 ").append(order.getOrderNo()).append("）导入成功");
                }
                else
                {
                    order.setUpdateTime(new Date());
                    saleOrderMapper.updateSaleOrder(order);
                    successMsg.append("<br/>").append(successNum + 1).append("、第 ").append(rowNo)
                            .append(" 行（订单号 ").append(order.getOrderNo()).append("）更新成功");
                }
                successNum++;
            }
            catch (Exception e)
            {
                throw new ServiceException("第 " + rowNo + " 行写入失败：" + e.getMessage()).setDetailMessage(e.getMessage());
            }
        }
        successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }
}
