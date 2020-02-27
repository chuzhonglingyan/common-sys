package com.yuntian.sys.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.sys.model.dto.DictDetailDTO;
import com.yuntian.sys.model.entity.DictDetail;
import com.yuntian.architecture.data.IBaseService;
import java.util.Collection;

/**
 * <p>
 * 后台系统-数据字典详情 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
public interface DictDetailService extends IBaseService<DictDetail> {

   IPage<DictDetail> queryListByPage(DictDetailDTO dto);


   void updateByDTO(DictDetail dto);


   void deleteByDTO(DictDetail dto);


   void deleteBatchByDTO(Collection<DictDetail> entityList);

}
