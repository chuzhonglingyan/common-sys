package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.architecture.data.BaseServiceImpl;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.architecture.redis.config.RedisManage;
import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.architecture.util.PasswordUtil;
import com.yuntian.architecture.util.TokenUtil;
import com.yuntian.sys.common.constant.RedisKey;
import com.yuntian.sys.common.dict.EnabledEnum;
import com.yuntian.sys.mapper.OperatorMapper;
import com.yuntian.sys.model.dto.LoginDTO;
import com.yuntian.sys.model.dto.OperatorQueryDTO;
import com.yuntian.sys.model.dto.OperatorSaveDTO;
import com.yuntian.sys.model.dto.OperatorUpdateDTO;
import com.yuntian.sys.model.dto.RegisterDTO;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.entity.Operator;
import com.yuntian.sys.model.entity.Role;
import com.yuntian.sys.model.vo.MenuComponentVo;
import com.yuntian.sys.model.vo.MenuTreeVO;
import com.yuntian.sys.model.vo.OperatorVO;
import com.yuntian.sys.service.OperatorRoleService;
import com.yuntian.sys.service.OperatorService;
import com.yuntian.sys.service.RoleMenuService;
import com.yuntian.sys.util.TreeUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * <p>
 * 后台系统-用户表 服务实现类
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
@Service
public class OperatorServiceImpl extends BaseServiceImpl<OperatorMapper, Operator> implements OperatorService {


    @Value("${rsa.private_key}")
    private String privateKey;

    @Resource
    private RedisManage redisManage;


    @Resource
    private OperatorRoleService operatorRoleService;

    @Resource
    private RoleMenuService roleMenuService;


    @Override
    public void saveByDTO(OperatorSaveDTO dto) {
        Operator operator = BeanCopyUtil.copyProperties(dto, Operator.class);
        boolean flag = save(operator);
        AssertUtil.isNotTrue(flag, "保存失败");
    }

    @Override
    public void updateByDTO(OperatorUpdateDTO dto) {
        getById(dto.getId());
        Operator operator = BeanCopyUtil.copyProperties(dto, Operator.class);
        boolean flag = updateById(operator);
        AssertUtil.isNotTrue(flag, "更新失败");
    }

    @Override
    public void deleteByDTO(Operator dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = deleteByIdWithFill(dto);
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");
    }

    @Override
    public void isEnable(Operator dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        dto.setIsEnable(EnabledEnum.DISENABLED.getType());
        UpdateWrapper<Operator> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("id", dto.getId());
        updateWrapper.set("is_enable", EnabledEnum.ENABLED.getType());
        update(dto, updateWrapper);
    }

    @Override
    public void isDisEnable(Operator dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        dto.setIsEnable(EnabledEnum.ENABLED.getType());
        UpdateWrapper<Operator> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("id", dto.getId());
        updateWrapper.set("is_enable", EnabledEnum.DISENABLED.getType());
        update(dto, updateWrapper);
    }


    @Override
    public Operator getById(Serializable id) {
        Operator operator = super.getById(id);
        AssertUtil.isNotNull(operator, "该用户不存在");
        return operator;
    }


    @Override
    public void deleteBatchByDTO(Collection<Operator> entityList) {

    }


    @Override
    public IPage<Operator> queryListByPage(OperatorQueryDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<Operator> page = new Page<>(dto.getCurrent(), dto.getSize());
        return page(page);
    }


    /**
     * 登录
     *
     * @param dto
     */
    @Override
    public OperatorVO login(LoginDTO dto) {
        AssertUtil.isNotBlank(dto.getAccount(), "账号名不能为空");
        AssertUtil.isNotBlank(dto.getPassWord(), "密码不能为空");
        // 查询验证码
        String code = redisManage.getValue(dto.getUuid());
        // 清除验证码
        if (StringUtils.isNotBlank(dto.getCode())){
            redisManage.del(dto.getUuid());
        }
        if (StringUtils.isBlank(code)) {
            throw new BusinessException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(dto.getCode()) || !dto.getCode().equalsIgnoreCase(code)) {
            throw new BusinessException("验证码错误");
        }
        // 密码解密
        RSA rsa = new RSA(privateKey, null);
        String password = null;
        try {
            password = new String(rsa.decrypt(dto.getPassWord(), KeyType.PrivateKey));
        } catch (Exception e) {
            BusinessException.throwMessage("密码解密错误");
        }
        OperatorVO operatorVO = getInfo(dto.getAccount());
        AssertUtil.isNotTrue(PasswordUtil.verify(password, operatorVO.getPassWord()), "密码错误");
        String tokenKey = RedisKey.getBackendTokenkey(String.valueOf(operatorVO.getId()));

        String token = TokenUtil.createToken(String.valueOf(operatorVO.getId()));
        String useInfoKey = RedisKey.getOperatorInfoKey(token, dto.getClientIp());
        //缓存当前用户信息
        Objects.requireNonNull(operatorVO).setToken(token);

        redisManage.set(useInfoKey, operatorVO, RedisKey.ONE_DAY);
        redisManage.set(tokenKey, token, RedisKey.ONE_DAY);
        return operatorVO;
    }


    /**
     * 注册
     *
     * @param dto
     */
    @Override
    public Operator register(RegisterDTO dto) {
        Operator operatorDto = new Operator();
        BeanUtils.copyProperties(dto, operatorDto);
        operatorDto.setPassWord(PasswordUtil.md5HexWithSalt(dto.getPassWord()));
        boolean flag = false;
        try {
            operatorDto.setCreateId(1L);
            operatorDto.setUpdateId(1L);
            flag = save(operatorDto);
        } catch (DuplicateKeyException e) {
            BusinessException.throwMessage("注册失败，账号名重复");
        }
        if (!flag) {
            BusinessException.throwMessage("注册失败，请重新注册");
        }
        Operator operator = getUserByAccount(dto.getAccount());
        operator.setPassWord(null);
        return operator;
    }


    @Override
    public Operator getUserByAccount(String account) {
        LambdaQueryWrapper<Operator> queryWrapper = new QueryWrapper<Operator>().lambda()
                .eq(Operator::getAccount, account);
        return getOne(queryWrapper);
    }

    @Override
    public OperatorVO getInfo(Long userId) {
        Operator operator = getById(userId);
        OperatorVO operatorVO = BeanCopyUtil.copyProperties(operator, OperatorVO.class);
        List<Role> roleVoList = operatorRoleService.getEnableListByOperatorId(userId);
        List<String> roleKeyList = roleVoList.stream().map(Role::getRoleKey).collect(Collectors.toList());
        Objects.requireNonNull(operatorVO).setRoles(roleKeyList);
        return operatorVO;
    }

    @Override
    public OperatorVO getInfo(String account) {
        LambdaQueryWrapper<Operator> lambdaQueryWrapper = new QueryWrapper<Operator>().lambda()
                .eq(Operator::getAccount, account);
        Operator operator = getOne(lambdaQueryWrapper);
        AssertUtil.isNotNull(operator, "该用户不存在");


        OperatorVO operatorVO = BeanCopyUtil.copyProperties(operator, OperatorVO.class);
        List<Role> roleVoList = operatorRoleService.getEnableListByOperatorId(operator.getId());
        List<String> roleKeyList = roleVoList.stream().map(Role::getRoleKey).collect(Collectors.toList());
        Objects.requireNonNull(operatorVO).setRoles(roleKeyList);
        return operatorVO;
    }

    @Override
    public List<MenuTreeVO> getMenuTreeVoListByOperator(Long operatorId) {
        List<Menu> menuList = getEnableMenuListByOperatorId(operatorId);
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        return TreeUtil.getMenuTreeVolist(menuList);
    }

    @Override
    public List<MenuComponentVo> getMenuComponentTreeVoListByOperator(Long operatorId) {
        List<Menu> menuList = getEnableMenuListByOperatorId(operatorId);
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        List<MenuTreeVO> menuTreeVOList = TreeUtil.buildMenuTree(menuList);
        return TreeUtil.buildMenuComponents(menuTreeVOList);
    }


    @Override
    public List<Menu> getEnableMenuListByOperatorId(Long operatorId) {
        List<Role> roleList = operatorRoleService.getEnableListByOperatorId(operatorId);
        if (CollectionUtils.isEmpty(roleList)) {
            return new ArrayList<>();
        }
        List<Long> roleIdList = roleList.stream().map(Role::getId).collect(Collectors.toList());
        return roleMenuService.getEnableMenuListByRoleIds(roleIdList);
    }


}
