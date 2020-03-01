package com.yuntian.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.IBaseService;
import com.yuntian.sys.model.dto.DictDetailSaveDTO;
import com.yuntian.sys.model.dto.DictDetailUpdateDTO;
import com.yuntian.sys.model.dto.DictQueyDetailDTO;
import com.yuntian.sys.model.entity.DictDetail;

import java.util.List;

/**
 * <p>
 * 后台系统-数据字典详情 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
public interface DictDetailService extends IBaseService<DictDetail> {

    void saveByDTO(DictDetailSaveDTO dto);

    void deleteByDTO(DictDetail dto);

    void deleteByDictId(Long dictId);

    void deleteByDictIdList(List<Long> dictIdList);

    void updateByDTO(DictDetailUpdateDTO dto);

    void deleteBatchByDTO(Long operatorId, List<Long> idList);


    IPage<DictDetail> queryListByPage(DictQueyDetailDTO dto);



}
