package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.FinishCoil;
import com.ruoyi.system.mapper.FinishCoilMapper;
import com.ruoyi.system.service.IFinishCoilService;

/**
 * 成品卷Service业务层处理
 * 
 * @author yuanwenbo
 */
@Service
public class FinishCoilServiceImpl implements IFinishCoilService
{
    @Autowired
    private FinishCoilMapper finishCoilMapper;

    /**
     * 查询成品卷列表
     */
    @Override
    public List<FinishCoil> selectFinishCoilList(FinishCoil finishCoil)
    {
        return finishCoilMapper.selectFinishCoilList(finishCoil);
    }

    /**
     * 根据主键查询成品卷
     */
    @Override
    public FinishCoil selectFinishCoilByCoilId(Long coilId)
    {
        return finishCoilMapper.selectFinishCoilByCoilId(coilId);
    }

    /**
     * 校验卷号是否唯一
     */
    @Override
    public boolean checkCoilNoUnique(FinishCoil finishCoil)
    {
        Long coilId = StringUtils.isNull(finishCoil.getCoilId()) ? -1L : finishCoil.getCoilId();
        FinishCoil info = finishCoilMapper.checkCoilNoUnique(finishCoil.getCoilNo());
        if (StringUtils.isNotNull(info) && info.getCoilId().longValue() != coilId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 新增成品卷
     */
    @Override
    public int insertFinishCoil(FinishCoil finishCoil)
    {
        if (!checkCoilNoUnique(finishCoil))
        {
            throw new ServiceException("新增失败，卷号 " + finishCoil.getCoilNo() + " 已存在");
        }
        return finishCoilMapper.insertFinishCoil(finishCoil);
    }

    /**
     * 修改成品卷
     */
    @Override
    public int updateFinishCoil(FinishCoil finishCoil)
    {
        if (!checkCoilNoUnique(finishCoil))
        {
            throw new ServiceException("修改失败，卷号 " + finishCoil.getCoilNo() + " 已存在");
        }
        return finishCoilMapper.updateFinishCoil(finishCoil);
    }

    /**
     * 批量删除成品卷
     */
    @Override
    public int deleteFinishCoilByCoilIds(Long[] coilIds)
    {
        return finishCoilMapper.deleteFinishCoilByCoilIds(coilIds);
    }
}
