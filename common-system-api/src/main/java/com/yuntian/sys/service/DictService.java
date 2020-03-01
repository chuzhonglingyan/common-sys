package com.yuntian.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.IBaseService;
import com.yuntian.sys.model.dto.DictQueryDTO;
import com.yuntian.sys.model.entity.Dict;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.vo.DictVO;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 后台系统-数据字典 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
public interface DictService extends IBaseService<Dict> {


    void saveByDTO(Dict dto);


    void updateByDTO(Dict dto);


    void deleteByDTO(Dict dto);


    void deleteBatchByDTO(Long operatorId, List<Long> idList);


    IPage<Dict> queryListByPage(DictQueryDTO dto);


    List<DictVO> getAll();

    Dict  getDictByName(String name);
}
