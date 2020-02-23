package com.yuntian.sys.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.sys.common.BaseBackendController;
import com.yuntian.sys.model.dto.RoleSaveDTO;
import com.yuntian.sys.model.dto.RoleUpdateDTO;
import com.yuntian.sys.model.entity.Role;
import com.yuntian.sys.model.dto.RoleQueryDTO;

import org.springframework.web.bind.annotation.RestController;
import com.yuntian.sys.service.RoleService;

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


    @PostMapping("/save")
    public Result save(RoleSaveDTO dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        roleService.saveByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(RoleUpdateDTO dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        roleService.updateByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(Role dto) {
        dto.setUpdateId(getUserId());
        roleService.deleteByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Long id) {
        Role entity = roleService.getById(id);
        return ResultGenerator.genSuccessResult(entity);
    }


    @PostMapping("/list")
    public Result list(RoleQueryDTO dto) {
        return ResultGenerator.genSuccessResult(roleService.queryListByPage(dto));
    }

}
