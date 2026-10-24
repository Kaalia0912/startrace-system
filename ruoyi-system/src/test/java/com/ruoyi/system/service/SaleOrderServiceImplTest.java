package com.ruoyi.system.service;

import com.ruoyi.system.domain.SaleOrder;
import com.ruoyi.system.mapper.SaleOrderMapper;
import com.ruoyi.system.service.impl.SaleOrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SaleOrderServiceImpl 订单号测试")
class SaleOrderServiceImplTest
{
    @Mock
    private SaleOrderMapper saleOrderMapper;

    @InjectMocks
    private SaleOrderServiceImpl service;

    private SaleOrder orderWithoutNumber()
    {
        SaleOrder order = new SaleOrder();
        order.setOrderDate(Date.valueOf("2026-10-24"));
        order.setCustomerName("示例客户");
        return order;
    }

    @Test
    @DisplayName("新订单保存后按业务日期和主键生成订单号")
    void insertSaleOrder_withoutNumber_shouldGenerateNumber()
    {
        SaleOrder order = orderWithoutNumber();
        when(saleOrderMapper.insertSaleOrder(any(SaleOrder.class))).thenAnswer(invocation -> {
            order.setOrderId(42L);
            return 1;
        });
        when(saleOrderMapper.updateSaleOrderOrderNo(42L, "SO20261024000042")).thenReturn(1);

        int rows = service.insertSaleOrder(order);

        assertEquals(1, rows);
        assertEquals("SO20261024000042", order.getOrderNo());
        verify(saleOrderMapper).updateSaleOrderOrderNo(42L, "SO20261024000042");
    }

    @Test
    @DisplayName("已有订单号保存时不应被覆盖")
    void insertSaleOrder_withNumber_shouldKeepExistingNumber()
    {
        SaleOrder order = orderWithoutNumber();
        order.setOrderNo("CUSTOM-001");
        when(saleOrderMapper.insertSaleOrder(any(SaleOrder.class))).thenAnswer(invocation -> {
            order.setOrderId(7L);
            return 1;
        });

        service.insertSaleOrder(order);

        assertEquals("CUSTOM-001", order.getOrderNo());
        verify(saleOrderMapper, never()).updateSaleOrderOrderNo(anyLong(), anyString());
    }

    @Test
    @DisplayName("Excel 导入订单号为空时应自动生成")
    void importSaleOrder_withoutNumber_shouldGenerateNumber()
    {
        SaleOrder order = orderWithoutNumber();
        when(saleOrderMapper.insertSaleOrder(any(SaleOrder.class))).thenAnswer(invocation -> {
            order.setOrderId(8L);
            return 1;
        });
        when(saleOrderMapper.updateSaleOrderOrderNo(8L, "SO20261024000008")).thenReturn(1);

        String message = service.importSaleOrder(Collections.singletonList(order), false);

        assertTrue(message.contains("导入成功"));
        assertEquals("SO20261024000008", order.getOrderNo());
        verify(saleOrderMapper, never()).selectSaleOrderByOrderNo(anyString());
    }
}
