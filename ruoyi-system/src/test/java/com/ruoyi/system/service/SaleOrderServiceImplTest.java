package com.ruoyi.system.service;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.SaleOrder;
import com.ruoyi.system.mapper.SaleOrderMapper;
import com.ruoyi.system.service.impl.SaleOrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    @DisplayName("导入负数数量时应在写库前失败并指出 Excel 行号")
    void importSaleOrder_withNegativeQuantity_shouldFailBeforeInsert()
    {
        SaleOrder order = orderWithoutNumber();
        order.setOrderNo("SO-NEGATIVE");
        order.setQuantity(new BigDecimal("-1"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.importSaleOrder(Collections.singletonList(order), false));

        assertTrue(exception.getMessage().contains("第 2 行"));
        assertTrue(exception.getMessage().contains("数量不能为负数"));
        verify(saleOrderMapper, never()).insertSaleOrder(any(SaleOrder.class));
    }

    @Test
    @DisplayName("导入负数单价时应在写库前失败")
    void importSaleOrder_withNegativeUnitPrice_shouldFailBeforeInsert()
    {
        SaleOrder order = orderWithoutNumber();
        order.setOrderNo("SO-NEGATIVE-PRICE");
        order.setUnitPrice(new BigDecimal("-0.01"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.importSaleOrder(Collections.singletonList(order), false));

        assertTrue(exception.getMessage().contains("单价不能为负数"));
        verify(saleOrderMapper, never()).insertSaleOrder(any(SaleOrder.class));
    }

    @Test
    @DisplayName("导入负数金额时应在写库前失败")
    void importSaleOrder_withNegativeAmount_shouldFailBeforeInsert()
    {
        SaleOrder order = orderWithoutNumber();
        order.setOrderNo("SO-NEGATIVE-AMOUNT");
        order.setAmount(new BigDecimal("-0.01"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.importSaleOrder(Collections.singletonList(order), false));

        assertTrue(exception.getMessage().contains("金额不能为负数"));
        verify(saleOrderMapper, never()).insertSaleOrder(any(SaleOrder.class));
    }

    @Test
    @DisplayName("同一文件出现重复订单号时不应写入任何订单")
    void importSaleOrder_withDuplicateNumberInFile_shouldFailBeforeInsert()
    {
        SaleOrder first = orderWithoutNumber();
        first.setOrderNo("SO-DUPLICATE");
        SaleOrder second = orderWithoutNumber();
        second.setOrderNo("SO-DUPLICATE");

        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.importSaleOrder(Arrays.asList(first, second), false));

        assertTrue(exception.getMessage().contains("第 3 行"));
        assertTrue(exception.getMessage().contains("订单号在文件中重复"));
        verify(saleOrderMapper, never()).insertSaleOrder(any(SaleOrder.class));
    }
}
