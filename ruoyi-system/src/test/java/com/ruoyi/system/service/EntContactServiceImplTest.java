package com.ruoyi.system.service;

import com.ruoyi.system.domain.EntContact;
import com.ruoyi.system.mapper.EntContactMapper;
import com.ruoyi.system.service.impl.EntContactServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 企业联系人 Service 单元测试（Mockito，不依赖数据库）
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EntContactServiceImpl 单元测试")
class EntContactServiceImplTest {

    @Mock
    private EntContactMapper entContactMapper;

    @InjectMocks
    private EntContactServiceImpl service;

    private EntContact buildContact(Long id, Long entId, String name) {
        EntContact c = new EntContact();
        c.setContactId(id);
        c.setEntId(entId);
        c.setContactName(name);
        return c;
    }

    @Test
    @DisplayName("按企业查询联系人应调用 Mapper")
    void selectByEntId_shouldCallMapper() {
        List<EntContact> list = new ArrayList<>();
        list.add(buildContact(1L, 100L, "张三"));
        when(entContactMapper.selectEntContactByEntId(100L)).thenReturn(list);
        List<EntContact> result = service.selectEntContactByEntId(100L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("张三", result.get(0).getContactName());
        verify(entContactMapper).selectEntContactByEntId(100L);
    }

    @Test
    @DisplayName("按主键查询应返回联系人")
    void selectEntContactByContactId_shouldCallMapper() {
        when(entContactMapper.selectEntContactByContactId(5L)).thenReturn(buildContact(5L, 100L, "李四"));
        EntContact result = service.selectEntContactByContactId(5L);
        assertNotNull(result);
        assertEquals("李四", result.getContactName());
        verify(entContactMapper).selectEntContactByContactId(5L);
    }

    @Test
    @DisplayName("新增联系人应调用 Mapper")
    void insertEntContact_shouldCallMapper() {
        when(entContactMapper.insertEntContact(any(EntContact.class))).thenReturn(1);
        int rows = service.insertEntContact(buildContact(null, 100L, "王五"));
        assertEquals(1, rows);
        verify(entContactMapper).insertEntContact(any(EntContact.class));
    }

    @Test
    @DisplayName("修改联系人应调用 Mapper")
    void updateEntContact_shouldCallMapper() {
        when(entContactMapper.updateEntContact(any(EntContact.class))).thenReturn(1);
        int rows = service.updateEntContact(buildContact(1L, 100L, "赵六"));
        assertEquals(1, rows);
        verify(entContactMapper).updateEntContact(any(EntContact.class));
    }

    @Test
    @DisplayName("删除联系人应透传主键数组")
    void deleteEntContactByContactIds_shouldPassIds() {
        when(entContactMapper.deleteEntContactByContactIds(any(Long[].class))).thenReturn(1);
        Long[] ids = { 3L };
        int rows = service.deleteEntContactByContactIds(ids);
        assertEquals(1, rows);
        verify(entContactMapper).deleteEntContactByContactIds(ids);
    }
}
