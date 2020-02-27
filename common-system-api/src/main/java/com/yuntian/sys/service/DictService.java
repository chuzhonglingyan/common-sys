package com.yuntian.sys.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.sys.model.dto.DictDTO;
import com.yuntian.sys.model.entity.Dict;
import com.yuntian.architecture.data.IBaseService;
import java.util.Collection;

/**
 * <p>
 * 后台系统-数据字典 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
public interface DictService extends IBaseService<Dict> {

   IPage<Dict> queryListByPage(DictDTO dto);


   void updateByDTO(Dict dto);


   void deleteByDTO(Dict dto);


   void deleteBatchByDTO(Collection<Dict> entityList);

}
