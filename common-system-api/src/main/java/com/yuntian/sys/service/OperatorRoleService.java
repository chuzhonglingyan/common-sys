package com.yuntian.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.IBaseService;
import com.yuntian.sys.model.dto.OperatorRoleDTO;
import com.yuntian.sys.model.entity.OperatorRole;
import com.yuntian.sys.model.entity.Role;
import com.yuntian.sys.model.vo.RoleVO;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 后台系统-用户角色关系表 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
public interface OperatorRoleService extends IBaseService<OperatorRole> {

   IPage<OperatorRole> queryListByPage(OperatorRoleDTO dto);


   void updateByDTO(OperatorRole dto);


   void deleteByDTO(OperatorRole dto);

   List<Long> getRoleIdListByOperatorId(Long operatorId);

   Long getRoleIdByOperatorId(Long operatorId);

   List<Long> getOperatorIdListByRoleId(Long roleId);


   Role getEnableRoleByOperatorId(Long operatorId);

   List<Role> getEnableListByOperatorId(Long operatorId);

   List<RoleVO> getRoleListByOperatorId(Long operaterId);


   void deleteBatchByDTO(Collection<OperatorRole> entityList);

   void saveRoleListByOperatorId(OperatorRoleDTO dto);

   void saveRoleListByOperatorId(Long operatorId,List<Long> roleIdList);

   void deleteByOperatorId(Long operatorId);


}
