package com.yuntian.sys.controller;

import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.common.BaseSysController;
import com.yuntian.sys.model.dto.RoleMenuDTO;
import com.yuntian.sys.service.RoleMenuService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2019/02/26.
 */
@RestController
@RequestMapping("/sys/role/menu")
public class RoleMenuController extends BaseSysController {

    @Resource
    private RoleMenuService roleMenuService;


    @PostMapping("/saveMenuList")
    public Result saveMenuListByRoleId(@RequestBody RoleMenuDTO roleMenuDTO) {
        roleMenuDTO.setCreateId(getUserId());
        roleMenuDTO.setUpdateId(getUserId());
        roleMenuService.saveMenuListByRoleId(roleMenuDTO);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/getMenuListByRoleId")
    public  Result getMenuListByRoleId(@RequestParam Long roleId) {
        return ResultGenerator.genSuccessResult(roleMenuService.getMenuListByRoleId(roleId));
    }


    @PostMapping("/deleteByRoleId")
    public Result deleteByRoleId(@RequestParam Long roleId) {
        roleMenuService.deleteByRoleId(roleId);
        return ResultGenerator.genSuccessResult();
    }
}
