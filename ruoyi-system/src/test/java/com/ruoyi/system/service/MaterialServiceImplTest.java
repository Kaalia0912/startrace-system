package com.ruoyi.system.service;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.Material;
import com.ruoyi.system.mapper.MaterialMapper;
import com.ruoyi.system.service.impl.MaterialServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("MaterialServiceImpl 导入测试")
class MaterialServiceImplTest
{
    @Mock
    private MaterialMapper materialMapper;

    @InjectMocks
    private MaterialServiceImpl service;

    private Material material(String code)
    {
        Material material = new Material();
        material.setMaterialCode(code);
        material.setProductName("示例不锈钢");
        return material;
    }

    @Test
    @DisplayName("物料编码为空时应拒绝导入")
    void importMaterial_withoutCode_shouldFail()
    {
        Material row = material("  ");

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.importMaterial(Collections.singletonList(row), false));

        assertTrue(error.getMessage().contains("物料编码为空"));
        verify(materialMapper, never()).insertMaterial(any(Material.class));
    }

    @Test
    @DisplayName("同一文件内物料编码重复时应拒绝导入")
    void importMaterial_withDuplicateCode_shouldFail()
    {
        when(materialMapper.checkMaterialCodeUnique("WL-001")).thenReturn(null);

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.importMaterial(Arrays.asList(material("WL-001"), material("WL-001")), false));

        assertTrue(error.getMessage().contains("文件内物料编码重复"));
        verify(materialMapper).insertMaterial(any(Material.class));
    }

    @Test
    @DisplayName("新物料编码应执行新增")
    void importMaterial_newCode_shouldInsert()
    {
        Material row = material("WL-001");
        when(materialMapper.checkMaterialCodeUnique("WL-001")).thenReturn(null);

        String message = service.importMaterial(Collections.singletonList(row), false);

        assertTrue(message.contains("新增 1 条"));
        verify(materialMapper).insertMaterial(row);
    }

    @Test
    @DisplayName("已有编码支持更新时应保留 Excel 空白的原有字段")
    void importMaterial_existingCode_shouldUpdateAndPreserveBlankFields()
    {
        Material existing = material("WL-001");
        existing.setMaterialId(10L);
        existing.setHardness("180~220");
        Material row = material("WL-001");
        row.setProductName(null);
        when(materialMapper.checkMaterialCodeUnique("WL-001")).thenReturn(existing);

        String message = service.importMaterial(Collections.singletonList(row), true);

        assertTrue(message.contains("更新 1 条"));
        assertTrue(row.getMaterialId().equals(10L));
        assertTrue("示例不锈钢".equals(row.getProductName()));
        assertTrue("180~220".equals(row.getHardness()));
        verify(materialMapper).updateMaterial(row);
    }
}
