package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Inspection;
import com.ruoyi.system.domain.SteelProperty;
import com.ruoyi.system.domain.Tolerance;
import com.ruoyi.system.mapper.InspectionMapper;
import com.ruoyi.system.mapper.SteelPropertyMapper;
import com.ruoyi.system.mapper.ToleranceMapper;
import com.ruoyi.system.service.IInspectionService;

/**
 * 质量检验单Service业务层处理
 *
 * 判定依据：md_steel_property（钢种×状态的力学性能）+ md_tolerance（标准×厚度×宽度档的偏差）
 */
@Service
public class InspectionServiceImpl implements IInspectionService
{
    @Autowired
    private InspectionMapper inspectionMapper;

    @Autowired
    private SteelPropertyMapper steelPropertyMapper;

    @Autowired
    private ToleranceMapper toleranceMapper;

    @Override
    public List<Inspection> selectInspectionList(Inspection inspection)
    {
        return inspectionMapper.selectInspectionList(inspection);
    }

    @Override
    public Inspection selectInspectionByInspectionId(Long inspectionId)
    {
        return inspectionMapper.selectInspectionByInspectionId(inspectionId);
    }

    @Override
    public boolean checkInspectionNoUnique(Inspection inspection)
    {
        Long inspectionId = StringUtils.isNull(inspection.getInspectionId()) ? -1L : inspection.getInspectionId();
        Inspection info = inspectionMapper.checkInspectionNoUnique(inspection.getInspectionNo());
        if (StringUtils.isNotNull(info) && info.getInspectionId().longValue() != inspectionId.longValue())
        {
            return false;
        }
        return true;
    }

    @Override
    public int insertInspection(Inspection inspection)
    {
        if (!checkInspectionNoUnique(inspection))
        {
            throw new ServiceException("新增失败，检验单号 " + inspection.getInspectionNo() + " 已存在");
        }
        judge(inspection);
        return inspectionMapper.insertInspection(inspection);
    }

    @Override
    public int updateInspection(Inspection inspection)
    {
        if (!checkInspectionNoUnique(inspection))
        {
            throw new ServiceException("修改失败，检验单号 " + inspection.getInspectionNo() + " 已存在");
        }
        judge(inspection);
        return inspectionMapper.updateInspection(inspection);
    }

    @Override
    public int deleteInspectionByInspectionIds(Long[] inspectionIds)
    {
        return inspectionMapper.deleteInspectionByInspectionIds(inspectionIds);
    }

    /**
     * 自动判定：把实测值与标准比对，写回判定结果与判定明细
     */
    private void judge(Inspection ins)
    {
        StringBuilder detail = new StringBuilder();
        boolean failed = false;
        boolean judged = false;
        boolean manualConcession = "2".equals(ins.getJudgeResult());

        // 1. 力学性能：按 钢种 + 状态 取标准
        if (StringUtils.isNotEmpty(ins.getSteelGrade()) && StringUtils.isNotEmpty(ins.getState()))
        {
            SteelProperty query = new SteelProperty();
            query.setSteelGrade(ins.getSteelGrade());
            query.setState(ins.getState());
            SteelProperty std = steelPropertyMapper.checkGradeStateUnique(query);
            if (std == null)
            {
                detail.append("性能：未维护 ").append(ins.getSteelGrade()).append(" / ").append(ins.getState())
                        .append(" 的性能标准，未判定；");
            }
            else
            {
                judged = true;
                failed |= !appendMin(detail, "屈服", ins.getYieldVal(), std.getYieldMin(), "MPa");
                failed |= !appendMin(detail, "抗拉", ins.getTensileVal(), std.getTensileMin(), "MPa");
                failed |= !appendMin(detail, "延伸率", ins.getElongationVal(), std.getElongationMin(), "%");
                failed |= !appendRange(detail, "硬度", ins.getHvVal(), std.getHvMin(), std.getHvMax(), "HV");
            }
        }
        else
        {
            detail.append("性能：未填钢种或状态，未判定；");
        }

        // 2. 宽度偏差：按 标准 + 厚度档 + 宽度档 取允许偏差
        if (StringUtils.isNotEmpty(ins.getStandard()) && ins.getNominalWidth() != null && ins.getWidthVal() != null)
        {
            Tolerance query = new Tolerance();
            query.setStandard(ins.getStandard());
            List<Tolerance> list = toleranceMapper.selectToleranceList(query);
            Tolerance matched = null;
            for (Tolerance t : list)
            {
                if (matchBucket(ins.getThicknessVal(), t.getThicknessMin(), t.getThicknessMax())
                        && matchBucket(ins.getNominalWidth(), t.getWidthMin(), t.getWidthMax()))
                {
                    matched = t;
                    break;
                }
            }
            if (matched == null)
            {
                detail.append("宽度：未匹配到 ").append(ins.getStandard()).append(" 对应厚度/宽度档的公差，未判定；");
            }
            else
            {
                judged = true;
                BigDecimal dev = ins.getWidthVal().subtract(ins.getNominalWidth());
                boolean ok = (matched.getDevLower() == null || dev.compareTo(matched.getDevLower()) >= 0)
                        && (matched.getDevUpper() == null || dev.compareTo(matched.getDevUpper()) <= 0);
                detail.append("宽度偏差 ").append(fmt(dev))
                        .append(" 允许 ").append(fmt(matched.getDevLower())).append("~").append(fmt(matched.getDevUpper()))
                        .append(" ").append(ok ? "✓" : "✗").append("；");
                failed |= !ok;
            }
        }
        else
        {
            detail.append("宽度：未填公差标准/名义宽度/实测宽度，未判定；");
        }

        if (manualConcession)
        {
            ins.setJudgeResult("2");
            detail.append("（人工让步接收）");
        }
        else if (!judged)
        {
            ins.setJudgeResult(null);
            detail.insert(0, "无可判定数据：");
        }
        else
        {
            ins.setJudgeResult(failed ? "1" : "0");
        }
        ins.setJudgeDetail(detail.toString());
    }

    /** 下限判定（实测值不得低于标准下限） */
    private boolean appendMin(StringBuilder sb, String name, BigDecimal actual, BigDecimal min, String unit)
    {
        if (actual == null || min == null)
        {
            return true;
        }
        boolean ok = actual.compareTo(min) >= 0;
        sb.append(name).append(" ").append(fmt(actual)).append(ok ? "≥" : "<").append(fmt(min)).append(unit)
                .append(" ").append(ok ? "✓" : "✗").append("；");
        return ok;
    }

    /** 区间判定（硬度等，上下限可为空） */
    private boolean appendRange(StringBuilder sb, String name, BigDecimal actual, BigDecimal min, BigDecimal max, String unit)
    {
        if (actual == null || (min == null && max == null))
        {
            return true;
        }
        boolean ok = (min == null || actual.compareTo(min) >= 0) && (max == null || actual.compareTo(max) <= 0);
        sb.append(name).append(" ").append(fmt(actual));
        if (min != null && max != null)
        {
            sb.append(" 允许 ").append(fmt(min)).append("~").append(fmt(max));
        }
        else if (min != null)
        {
            sb.append("≥").append(fmt(min));
        }
        else
        {
            sb.append("≤").append(fmt(max));
        }
        sb.append(unit).append(" ").append(ok ? "✓" : "✗").append("；");
        return ok;
    }

    /** 分档匹配：[min, max) */
    private boolean matchBucket(BigDecimal value, BigDecimal min, BigDecimal max)
    {
        if (value == null)
        {
            return false;
        }
        if (min != null && value.compareTo(min) < 0)
        {
            return false;
        }
        if (max != null && value.compareTo(max) >= 0)
        {
            return false;
        }
        return true;
    }

    private String fmt(BigDecimal v)
    {
        if (v == null)
        {
            return "-";
        }
        return v.stripTrailingZeros().toPlainString();
    }
}
