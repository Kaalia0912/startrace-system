package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.FinishCoil;

/**
 * 成品卷Mapper接口
 * 
 * @author yuanwenbo
 */
public interface FinishCoilMapper
{
    /**
     * 查询成品卷列表
     */
    public List<FinishCoil> selectFinishCoilList(FinishCoil finishCoil);

    /**
     * 根据主键查询成品卷
     */
    public FinishCoil selectFinishCoilByCoilId(Long coilId);

    /**
     * 按卷号查询（唯一性校验用）
     */
    public FinishCoil checkCoilNoUnique(String coilNo);

    /**
     * 新增成品卷
     */
    public int insertFinishCoil(FinishCoil finishCoil);

    /**
     * 修改成品卷
     */
    public int updateFinishCoil(FinishCoil finishCoil);

    /**
     * 批量删除成品卷
     */
    public int deleteFinishCoilByCoilIds(Long[] coilIds);

    /**
     * 按主键批量查询成品卷（发货时取卷信息）
     */
    public List<FinishCoil> selectFinishCoilByCoilIds(Long[] coilIds);

    /**
     * 按主键批量置为已发货
     */
    public int updateShipStatusByCoilIds(@Param("coilIds") Long[] coilIds, @Param("updateBy") String updateBy);
}
