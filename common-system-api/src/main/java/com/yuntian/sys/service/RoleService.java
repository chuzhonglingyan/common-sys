package com.yuntian.sys.service;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.IBaseService;

import java.util.Collection;
import java.util.List;

import com.yuntian.sys.model.dto.RoleQueryDTO;
import com.yuntian.sys.model.dto.RoleSaveDTO;
import com.yuntian.sys.model.dto.RoleUpdateDTO;
import com.yuntian.sys.model.entity.Role;

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

    void isEnable(Role dto);

    void isDisEnable(Role dto);


    IPage<Role> queryListByPage(RoleQueryDTO dto);

    void deleteBatchByDTO(Collection<Role> entityList);

    List<Role> getEnableList();

    List<Role> getRoleByKey(String roleKey);

}
