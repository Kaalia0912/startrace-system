package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.RawCoil;
import com.ruoyi.system.mapper.RawCoilMapper;
import com.ruoyi.system.service.IRawCoilService;

/**
 * 原料卷料Service业务层处理
 * 
 * @author yuanwenbo
 */
@Service
public class RawCoilServiceImpl implements IRawCoilService
{
    @Autowired
    private RawCoilMapper rawCoilMapper;

    /**
     * 查询原料卷料列表
     */
    @Override
    public List<RawCoil> selectRawCoilList(RawCoil rawCoil)
    {
        return rawCoilMapper.selectRawCoilList(rawCoil);
    }

    /**
     * 根据主键查询原料卷料
     */
    @Override
    public RawCoil selectRawCoilByCoilId(Long coilId)
    {
        return rawCoilMapper.selectRawCoilByCoilId(coilId);
    }

    /**
     * 校验卷号是否唯一
     */
    @Override
    public boolean checkCoilNoUnique(RawCoil rawCoil)
    {
        Long coilId = StringUtils.isNull(rawCoil.getCoilId()) ? -1L : rawCoil.getCoilId();
        RawCoil info = rawCoilMapper.checkCoilNoUnique(rawCoil.getCoilNo());
        if (StringUtils.isNotNull(info) && info.getCoilId().longValue() != coilId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 新增原料卷料
     */
    @Override
    public int insertRawCoil(RawCoil rawCoil)
    {
        if (!checkCoilNoUnique(rawCoil))
        {
            throw new ServiceException("新增失败，卷号 " + rawCoil.getCoilNo() + " 已存在");
        }
        return rawCoilMapper.insertRawCoil(rawCoil);
    }

    /**
     * 修改原料卷料
     */
    @Override
    public int updateRawCoil(RawCoil rawCoil)
    {
        if (!checkCoilNoUnique(rawCoil))
        {
            throw new ServiceException("修改失败，卷号 " + rawCoil.getCoilNo() + " 已存在");
        }
        return rawCoilMapper.updateRawCoil(rawCoil);
    }

    /**
     * 批量删除原料卷料
     */
    @Override
    public int deleteRawCoilByCoilIds(Long[] coilIds)
    {
        return rawCoilMapper.deleteRawCoilByCoilIds(coilIds);
    }
}
