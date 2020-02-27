package com.yuntian.sys.service;


import com.yuntian.architecture.data.IBaseService;
import com.yuntian.sys.model.dto.RoleQueryDTO;
import com.yuntian.sys.model.dto.RoleSaveDTO;
import com.yuntian.sys.model.dto.RoleUpdateDTO;
import com.yuntian.sys.model.entity.Role;
import com.yuntian.sys.model.vo.PageVO;
import com.yuntian.sys.model.vo.RoleVO;

import java.util.List;

/**
 * <p>
 * 后台系统-角色表 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
public interface RoleService extends IBaseService<Role> {


    void saveByDTO(RoleSaveDTO dto);

    void updateByDTO(RoleUpdateDTO dto);

    void deleteByDTO(Role dto);

    void enable(Role dto);

    void disEnable(Role dto);

    void   changeState(Role dto);

    PageVO<RoleVO> queryListByPage(RoleQueryDTO dto);

    void deleteBatchByDTO(Long operatorId,List<Long> idList);

    List<Role> getEnableList();

    List<Role> getRoleByKey(String roleKey);


    RoleVO  getInfo(Long id);


}
