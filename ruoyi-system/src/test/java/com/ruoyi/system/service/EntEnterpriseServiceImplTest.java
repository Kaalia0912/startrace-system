package com.ruoyi.system.service;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.EntEnterprise;
import com.ruoyi.system.mapper.EntEnterpriseMapper;
import com.ruoyi.system.service.impl.EntEnterpriseServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 企业档案 Service 单元测试（Mockito，不依赖数据库）
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EntEnterpriseServiceImpl 单元测试")
class EntEnterpriseServiceImplTest {

    @Mock
    private EntEnterpriseMapper entEnterpriseMapper;

    @InjectMocks
    private EntEnterpriseServiceImpl service;

    private EntEnterprise buildEnterprise(Long id, String code, String name) {
        EntEnterprise e = new EntEnterprise();
        e.setEntId(id);
        e.setEntCode(code);
        e.setEntName(name);
        return e;
    }

    // ---------- checkEntCodeUnique ----------

    @Test
    @DisplayName("信用代码不存在时，校验应通过")
    void checkEntCodeUnique_whenCodeNotExists_shouldReturnTrue() {
        when(entEnterpriseMapper.checkEntCodeUnique("NEW-CODE")).thenReturn(null);
        EntEnterprise ent = buildEnterprise(null, "NEW-CODE", "新企业");
        assertTrue(service.checkEntCodeUnique(ent));
        verify(entEnterpriseMapper).checkEntCodeUnique("NEW-CODE");
    }

    @Test
    @DisplayName("信用代码已被其他企业占用时，校验应失败")
    void checkEntCodeUnique_whenCodeExistsOtherEntity_shouldReturnFalse() {
        EntEnterprise existing = buildEnterprise(1L, "EXIST-CODE", "已有企业");
        when(entEnterpriseMapper.checkEntCodeUnique("EXIST-CODE")).thenReturn(existing);
        EntEnterprise ent = buildEnterprise(2L, "EXIST-CODE", "待新增企业");
        assertFalse(service.checkEntCodeUnique(ent));
    }

    @Test
    @DisplayName("信用代码属于本企业自身时，校验应通过（编辑场景）")
    void checkEntCodeUnique_whenCodeExistsSameEntity_shouldReturnTrue() {
        EntEnterprise existing = buildEnterprise(1L, "SAME-CODE", "本企业");
        when(entEnterpriseMapper.checkEntCodeUnique("SAME-CODE")).thenReturn(existing);
        EntEnterprise ent = buildEnterprise(1L, "SAME-CODE", "本企业");
        assertTrue(service.checkEntCodeUnique(ent));
    }

    // ---------- importEnterprise ----------

    @Test
    @DisplayName("导入空列表应抛出异常")
    void importEnterprise_emptyList_shouldThrow() {
        assertThrows(ServiceException.class, () -> service.importEnterprise(new ArrayList<>(), false));
        verify(entEnterpriseMapper, never()).insertEntEnterprise(any(EntEnterprise.class));
    }

    @Test
    @DisplayName("导入全新企业应全部插入")
    void importEnterprise_newCodes_shouldInsertAll() {
        when(entEnterpriseMapper.checkEntCodeUnique(anyString())).thenReturn(null);
        List<EntEnterprise> list = new ArrayList<>();
        list.add(buildEnterprise(null, "C1", "企业一"));
        list.add(buildEnterprise(null, "C2", "企业二"));
        String msg = service.importEnterprise(list, false);
        verify(entEnterpriseMapper, times(2)).insertEntEnterprise(any(EntEnterprise.class));
        assertTrue(msg.contains("导入成功"));
        assertTrue(msg.contains("企业一"));
        assertTrue(msg.contains("企业二"));
    }

    @Test
    @DisplayName("已存在企业且不支持更新时，应全部跳过并抛出异常")
    void importEnterprise_existingCodesWithoutUpdate_shouldSkip() {
        when(entEnterpriseMapper.checkEntCodeUnique(anyString())).thenReturn(buildEnterprise(1L, "C1", "已有"));
        List<EntEnterprise> list = new ArrayList<>();
        list.add(buildEnterprise(null, "C1", "重复企业"));
        // 全部失败时实现抛出 ServiceException
        assertThrows(ServiceException.class, () -> service.importEnterprise(list, false));
        verify(entEnterpriseMapper, never()).insertEntEnterprise(any(EntEnterprise.class));
        verify(entEnterpriseMapper, never()).updateEntEnterprise(any(EntEnterprise.class));
    }

    @Test
    @DisplayName("已存在企业且支持更新时，应执行更新")
    void importEnterprise_existingCodesWithUpdate_shouldUpdate() {
        EntEnterprise existing = buildEnterprise(1L, "C1", "已有");
        when(entEnterpriseMapper.checkEntCodeUnique(anyString())).thenReturn(existing);
        List<EntEnterprise> list = new ArrayList<>();
        list.add(buildEnterprise(null, "C1", "更新企业"));
        String msg = service.importEnterprise(list, true);
        verify(entEnterpriseMapper, never()).insertEntEnterprise(any(EntEnterprise.class));
        verify(entEnterpriseMapper, times(1)).updateEntEnterprise(any(EntEnterprise.class));
        assertTrue(msg.contains("更新成功"));
    }

    // ---------- CRUD 基础 ----------

    @Test
    @DisplayName("按主键查询应调用 Mapper 并返回实体")
    void selectEntEnterpriseByEntId_shouldCallMapper() {
        when(entEnterpriseMapper.selectEntEnterpriseByEntId(1L)).thenReturn(buildEnterprise(1L, "C1", "企业一"));
        EntEnterprise result = service.selectEntEnterpriseByEntId(1L);
        assertNotNull(result);
        assertEquals(1L, result.getEntId());
        assertEquals("企业一", result.getEntName());
        verify(entEnterpriseMapper).selectEntEnterpriseByEntId(1L);
    }

    @Test
    @DisplayName("新增企业应调用 Mapper 并返回影响行数")
    void insertEntEnterprise_shouldReturnRows() {
        when(entEnterpriseMapper.insertEntEnterprise(any(EntEnterprise.class))).thenReturn(1);
        int rows = service.insertEntEnterprise(buildEnterprise(null, "C1", "企业一"));
        assertEquals(1, rows);
        verify(entEnterpriseMapper).insertEntEnterprise(any(EntEnterprise.class));
    }

    @Test
    @DisplayName("修改企业应调用 Mapper 并返回影响行数")
    void updateEntEnterprise_shouldReturnRows() {
        when(entEnterpriseMapper.updateEntEnterprise(any(EntEnterprise.class))).thenReturn(1);
        int rows = service.updateEntEnterprise(buildEnterprise(1L, "C1", "企业一"));
        assertEquals(1, rows);
        verify(entEnterpriseMapper).updateEntEnterprise(any(EntEnterprise.class));
    }

    @Test
    @DisplayName("批量删除应透传主键数组并返回影响行数")
    void deleteEntEnterpriseByEntIds_shouldPassIds() {
        when(entEnterpriseMapper.deleteEntEnterpriseByEntIds(any(Long[].class))).thenReturn(2);
        Long[] ids = { 1L, 2L };
        int rows = service.deleteEntEnterpriseByEntIds(ids);
        assertEquals(2, rows);
        verify(entEnterpriseMapper).deleteEntEnterpriseByEntIds(ids);
    }
}
