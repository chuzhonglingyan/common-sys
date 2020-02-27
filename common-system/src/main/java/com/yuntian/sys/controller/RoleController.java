package com.yuntian.sys.controller;


import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.sys.annotation.Permission;
import com.yuntian.sys.common.BaseBackendController;
import com.yuntian.sys.common.constant.PermissionCodes;
import com.yuntian.sys.model.dto.RoleQueryDTO;
import com.yuntian.sys.model.dto.RoleSaveDTO;
import com.yuntian.sys.model.dto.RoleUpdateDTO;
import com.yuntian.sys.model.entity.Role;
import com.yuntian.sys.model.vo.RoleVO;
import com.yuntian.sys.service.RoleService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

/**
 * <p>
 * 后台系统-角色表 前端控制器
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
@RestController
@RequestMapping("/sys/role")
public class RoleController extends BaseBackendController {


    @Resource
    private RoleService roleService;

    @Permission(value = PermissionCodes.ROLE_ADD)
    @PostMapping("/save")
    public Result save(@RequestBody @Validated RoleSaveDTO dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        roleService.saveByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @Permission(value = PermissionCodes.ROLE_EDIT)
    @PostMapping("/update")
    public Result update(@RequestBody @Validated RoleUpdateDTO dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        roleService.updateByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @Permission(value = PermissionCodes.ROLE_DEL)
    @PostMapping("/delete")
    public Result delete(Role dto) {
        dto.setUpdateId(getUserId());
        roleService.deleteByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @Permission(value = PermissionCodes.ROLE_DEL)
    @PostMapping("/deleteList")
    public Result deleteList(@RequestBody List<Long> idList) {
        roleService.deleteBatchByDTO(getUserId(), idList);
        return ResultGenerator.genSuccessResult();
    }

    @Permission(value = PermissionCodes.ROLE_LIST)
    @PostMapping("/list")
    public Result list(@RequestBody RoleQueryDTO dto) {
        return ResultGenerator.genSuccessResult(roleService.queryListByPage(dto));
    }

    @Permission(value = PermissionCodes.ROLE_STATE)
    @PostMapping("/changeState")
    public Result changeState(@RequestBody Role dto) {
        dto.setUpdateId(getUserId());
        roleService.changeState(dto);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/getInfo")
    public Result getInfo(@RequestBody Role dto) {
        RoleVO entity = roleService.getInfo(dto.getId());
        return ResultGenerator.genSuccessResult(entity);
    }


    @PostMapping("/getEnableList")
    public Result getEnableList() {
        return ResultGenerator.genSuccessResult(roleService.getEnableList());
    }


}
