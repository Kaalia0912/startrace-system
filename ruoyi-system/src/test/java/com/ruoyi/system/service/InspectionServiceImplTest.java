package com.ruoyi.system.service;

import com.ruoyi.system.domain.Inspection;
import com.ruoyi.system.domain.SteelProperty;
import com.ruoyi.system.domain.Tolerance;
import com.ruoyi.system.mapper.InspectionMapper;
import com.ruoyi.system.mapper.SteelPropertyMapper;
import com.ruoyi.system.mapper.ToleranceMapper;
import com.ruoyi.system.service.impl.InspectionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 质量检验自动判定 单元测试（Mockito，不依赖数据库）
 *
 * 判据来自 md_steel_property（SUS304 / 1-4H：屈服≥450、抗拉≥700、延伸率≥20、HV 220~250）
 * 与 md_tolerance（无锡宝浦 / 厚 0.15~0.50 / 宽 160~260：±0.15）
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("InspectionServiceImpl 自动判定单元测试")
class InspectionServiceImplTest
{
    @Mock
    private InspectionMapper inspectionMapper;

    @Mock
    private SteelPropertyMapper steelPropertyMapper;

    @Mock
    private ToleranceMapper toleranceMapper;

    @InjectMocks
    private InspectionServiceImpl service;

    /** 标准性能：SUS304 / 1-4H */
    private SteelProperty buildStandard()
    {
        SteelProperty s = new SteelProperty();
        s.setSteelGrade("SUS304");
        s.setState("1/4H");
        s.setYieldMin(new BigDecimal("450"));
        s.setTensileMin(new BigDecimal("700"));
        s.setElongationMin(new BigDecimal("20"));
        s.setHvMin(new BigDecimal("220"));
        s.setHvMax(new BigDecimal("250"));
        return s;
    }

    /** 标准公差：无锡宝浦 厚[0.150,0.500) 宽[160,260) ±0.15 */
    private List<Tolerance> buildToleranceList()
    {
        Tolerance t = new Tolerance();
        t.setStandard("无锡宝浦");
        t.setThicknessMin(new BigDecimal("0.150"));
        t.setThicknessMax(new BigDecimal("0.500"));
        t.setWidthMin(new BigDecimal("160"));
        t.setWidthMax(new BigDecimal("260"));
        t.setDevLower(new BigDecimal("-0.15"));
        t.setDevUpper(new BigDecimal("0.15"));
        List<Tolerance> list = new ArrayList<Tolerance>();
        list.add(t);
        return list;
    }

    /** 一份基础检验单：默认全部达标 */
    private Inspection buildInspection()
    {
        Inspection ins = new Inspection();
        ins.setInspectionNo("JY-TEST-001");
        ins.setSteelGrade("SUS304");
        ins.setState("1/4H");
        ins.setStandard("无锡宝浦");
        ins.setThicknessVal(new BigDecimal("0.230"));
        ins.setNominalWidth(new BigDecimal("213.00"));
        ins.setWidthVal(new BigDecimal("213.05"));
        ins.setYieldVal(new BigDecimal("460"));
        ins.setTensileVal(new BigDecimal("710"));
        ins.setElongationVal(new BigDecimal("21"));
        ins.setHvVal(new BigDecimal("240"));
        return ins;
    }

    private void stubStandards(SteelProperty std, List<Tolerance> tolerances)
    {
        when(inspectionMapper.checkInspectionNoUnique(anyString())).thenReturn(null);
        when(inspectionMapper.insertInspection(any(Inspection.class))).thenReturn(1);
        when(steelPropertyMapper.checkGradeStateUnique(any(SteelProperty.class))).thenReturn(std);
        when(toleranceMapper.selectToleranceList(any(Tolerance.class))).thenReturn(tolerances);
    }

    @Test
    @DisplayName("实测值全部达标 → 判定合格")
    void judge_allPass_shouldBeQualified()
    {
        stubStandards(buildStandard(), buildToleranceList());

        Inspection ins = buildInspection();
        service.insertInspection(ins);

        assertEquals("0", ins.getJudgeResult());
        assertTrue(ins.getJudgeDetail().contains("屈服 460≥450MPa ✓"), ins.getJudgeDetail());
        assertTrue(ins.getJudgeDetail().contains("硬度 240 允许 220~250HV ✓"), ins.getJudgeDetail());
        assertTrue(ins.getJudgeDetail().contains("宽度偏差 0.05 允许 -0.15~0.15 ✓"), ins.getJudgeDetail());
        verify(inspectionMapper).insertInspection(ins);
    }

    @Test
    @DisplayName("屈服低于标准下限 → 判定不合格")
    void judge_yieldTooLow_shouldBeUnqualified()
    {
        stubStandards(buildStandard(), buildToleranceList());

        Inspection ins = buildInspection();
        ins.setYieldVal(new BigDecimal("440"));
        service.insertInspection(ins);

        assertEquals("1", ins.getJudgeResult());
        assertTrue(ins.getJudgeDetail().contains("屈服 440<450MPa ✗"), ins.getJudgeDetail());
    }

    @Test
    @DisplayName("硬度超出上限 → 判定不合格")
    void judge_hvTooHigh_shouldBeUnqualified()
    {
        stubStandards(buildStandard(), buildToleranceList());

        Inspection ins = buildInspection();
        ins.setHvVal(new BigDecimal("260"));
        service.insertInspection(ins);

        assertEquals("1", ins.getJudgeResult());
        assertTrue(ins.getJudgeDetail().contains("硬度 260 允许 220~250HV ✗"), ins.getJudgeDetail());
    }

    @Test
    @DisplayName("宽度超出允许偏差 → 判定不合格")
    void judge_widthOutOfTolerance_shouldBeUnqualified()
    {
        stubStandards(buildStandard(), buildToleranceList());

        Inspection ins = buildInspection();
        ins.setWidthVal(new BigDecimal("213.30"));
        service.insertInspection(ins);

        assertEquals("1", ins.getJudgeResult());
        assertTrue(ins.getJudgeDetail().contains("宽度偏差 0.3 允许 -0.15~0.15 ✗"), ins.getJudgeDetail());
    }

    @Test
    @DisplayName("人工指定让步接收 → 保留让步，不被自动判定覆盖")
    void judge_manualConcession_shouldKeepConcession()
    {
        stubStandards(buildStandard(), buildToleranceList());

        Inspection ins = buildInspection();
        ins.setYieldVal(new BigDecimal("440"));
        ins.setJudgeResult("2");
        service.insertInspection(ins);

        assertEquals("2", ins.getJudgeResult());
        assertTrue(ins.getJudgeDetail().contains("人工让步接收"), ins.getJudgeDetail());
    }

    @Test
    @DisplayName("该钢种状态未维护标准 → 不判定并给出提示")
    void judge_noStandard_shouldNotJudge()
    {
        stubStandards(null, new ArrayList<Tolerance>());

        Inspection ins = buildInspection();
        ins.setSteelGrade("SUS999");
        service.insertInspection(ins);

        assertNull(ins.getJudgeResult());
        assertTrue(ins.getJudgeDetail().contains("未维护"), ins.getJudgeDetail());
        assertTrue(ins.getJudgeDetail().contains("未匹配到"), ins.getJudgeDetail());
        assertTrue(ins.getJudgeDetail().startsWith("无可判定数据："), ins.getJudgeDetail());
    }

    @Test
    @DisplayName("检验单号重复 → 抛出业务异常")
    void insert_duplicateNo_shouldThrow()
    {
        Inspection exist = new Inspection();
        exist.setInspectionId(1L);
        exist.setInspectionNo("JY-TEST-001");
        when(inspectionMapper.checkInspectionNoUnique("JY-TEST-001")).thenReturn(exist);

        Inspection ins = buildInspection();
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.insertInspection(ins));
        assertTrue(ex.getMessage().contains("已存在"), ex.getMessage());
        verify(inspectionMapper, never()).insertInspection(any(Inspection.class));
    }
}
