package com.yuntian.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.sys.common.BaseBackendController;
import com.yuntian.sys.model.dto.MenuQueryDTO;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.vo.MenuComponentVo;
import com.yuntian.sys.service.MenuService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

/**
 * <p>
 * 后台系统-菜单表 前端控制器
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
@RestController
@RequestMapping("/sys/menu")
public class MenuController extends BaseBackendController {


    @Resource
    private MenuService menuService;


    @PostMapping("/save")
    public Result save(Menu dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        menuService.save(dto);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/update")
    public Result update(Menu dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        menuService.updateByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/delete")
    public Result delete(Menu dto) {
        dto.setUpdateId(getUserId());
        menuService.deleteByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/detail")
    public Result detail(@RequestParam Long id) {
        Menu entity = menuService.getById(id);
        return ResultGenerator.genSuccessResult(entity);
    }


    @PostMapping("/list")
    public IPage<Menu> list(MenuQueryDTO dto) {
        return menuService.queryListByPage(dto);
    }


    @PostMapping("/build")
    public Result buildMenuTree() {
        List<MenuComponentVo> componentVoList = menuService.getMenuComponentTreeVoListByOperator(getUserId());
        return ResultGenerator.genSuccessResult(componentVoList);
    }


    @PostMapping("/getMenuTree")
    public Result getMenuTree(MenuQueryDTO dto) {
        return ResultGenerator.genSuccessResult(menuService.getMenuTreeVoList(dto));
    }


    @PostMapping("/getEnabledMenuTree")
    public Result getEnabledMenuTree() {
        return ResultGenerator.genSuccessResult(menuService.getEnabledMenuTreeList());
    }

}
