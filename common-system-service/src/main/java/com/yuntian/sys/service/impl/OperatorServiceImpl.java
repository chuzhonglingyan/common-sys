package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.architecture.data.BaseEntity;
import com.yuntian.architecture.data.BaseServiceImpl;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.architecture.redis.config.RedisManage;
import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.architecture.util.PasswordUtil;
import com.yuntian.architecture.util.TokenUtil;
import com.yuntian.cache.SysRedisKey;
import com.yuntian.sys.enums.EnabledEnum;
import com.yuntian.sys.mapper.OperatorMapper;
import com.yuntian.sys.model.dto.DownQueryDTO;
import com.yuntian.sys.model.dto.LoginDTO;
import com.yuntian.sys.model.dto.OperatorQueryDTO;
import com.yuntian.sys.model.dto.OperatorSaveDTO;
import com.yuntian.sys.model.dto.OperatorUpdateDTO;
import com.yuntian.sys.model.dto.RegisterDTO;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.entity.Operator;
import com.yuntian.sys.model.entity.Role;
import com.yuntian.sys.model.vo.OperatorVO;
import com.yuntian.sys.model.vo.PageVO;
import com.yuntian.sys.service.MenuService;
import com.yuntian.sys.service.OperatorRoleService;
import com.yuntian.sys.service.OperatorService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import static java.util.stream.Collectors.toList;

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
    private MenuService menuService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveByDTO(OperatorSaveDTO dto) {
        Operator operator = BeanCopyUtil.copyProperties(dto, Operator.class);
        operator.setPassWord(PasswordUtil.md5HexWithSalt("123456"));
        boolean flag = false;
        try {
            flag = save(operator);
        } catch (DuplicateKeyException e) {
            BusinessException.throwMessage("用户名已存在");
        }
        AssertUtil.isNotTrue(flag, "保存失败");
        List<Long> roleIdList = dto.getRoleIdList();
        if (CollectionUtils.isNotEmpty(roleIdList)) {
            operatorRoleService.saveRoleListByOperatorId(operator.getId(), roleIdList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByDTO(OperatorUpdateDTO dto) {
        getById(dto.getId());
        Operator operator = BeanCopyUtil.copyProperties(dto, Operator.class);
        boolean flag = updateById(operator);
        AssertUtil.isNotTrue(flag, "更新失败");
        List<Long> roleIdList = dto.getRoleIdList();
        operatorRoleService.saveRoleListByOperatorId(operator.getId(), roleIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByDTO(Operator dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        Operator operatorTemp = getById(dto.getId());
        if (operatorTemp.getStatus() == EnabledEnum.ENABLED.getValue()) {
            BusinessException.throwMessage("用户处于启用状态，无法删除.");
        }
        boolean flag = deleteByIdWithFill(dto);
        operatorRoleService.deleteByOperatorId(dto.getId());
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByDTO(Long operatorId, List<Long> idList) {
        AssertUtil.isNotEmpty(idList, "集合不能为空");
        Collection<Operator> entityList = new ArrayList<>();
        idList.forEach(id -> {
            Operator operatorTemp = getById(id);
            if (operatorTemp.getStatus() == EnabledEnum.ENABLED.getValue()) {
                BusinessException.throwMessage("用户处于启用状态，无法删除.");
            }
            operatorRoleService.deleteByOperatorId(id);

            Operator operator = new Operator();
            operator.setId(id);
            operator.setUpdateId(operatorId);
            entityList.add(operator);
        });
        boolean flag = deleteByIdsWithFill(entityList);
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");
    }


    @Override
    public void enable(Operator dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        dto.setStatus(EnabledEnum.ENABLED.getValue());
        LambdaQueryWrapper<Operator> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Operator::getId, dto.getId());
        updateWrapper.eq(Operator::getStatus, EnabledEnum.DISENABLED.getValue());
        boolean flag = update(dto, updateWrapper);
        AssertUtil.isNotTrue(flag, "启用失败,请刷新页面");
    }



    @Override
    public void disEnable(Operator dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        dto.setStatus(EnabledEnum.DISENABLED.getValue());
        LambdaQueryWrapper<Operator> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Operator::getId, dto.getId());
        updateWrapper.eq(Operator::getStatus, EnabledEnum.ENABLED.getValue());
        boolean flag = update(dto, updateWrapper);
        AssertUtil.isNotTrue(flag, "禁用失败,请刷新页面");
    }

    @Override
    public void changeState(Operator dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        AssertUtil.isNotNull(dto.getStatus(), "状态不能为空");
        if (dto.getStatus() == 0) {
            enable(dto);
        } else {
            disEnable(dto);
        }
    }


    @Override
    public Operator getById(Serializable id) {
        Operator operator = super.getById(id);
        AssertUtil.isNotNull(operator, "该用户不存在");
        return operator;
    }


    @Override
    public PageVO<OperatorVO> queryListByPage(OperatorQueryDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<Operator> pageParam = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<Operator> queryWrapper = new LambdaQueryWrapper<>();
        List<String> createTime = dto.getCreateTime();
        if (!CollectionUtils.isEmpty(createTime)) {
            String createTimeStart = createTime.get(0);
            String createTimeEnd = createTime.get(1);
            queryWrapper.between(Operator::getCreateTime, createTimeStart, createTimeEnd);
        }
        queryWrapper.eq(Objects.nonNull(dto.getStatus()), Operator::getStatus, dto.getStatus());
        queryWrapper.orderByDesc(Operator::getUpdateTime);

        PageVO<OperatorVO> operatorVoPage = new PageVO<OperatorVO>(page(pageParam, queryWrapper)) {};
        List<OperatorVO> operatorVoList = operatorVoPage.getRecords();
        if (CollectionUtils.isEmpty(operatorVoList)) {
            return operatorVoPage;
        }
        Map<Long, String> operatorNameMap = getOperatorNameMap(operatorVoList);
        operatorVoPage.getRecords().forEach(vo -> {
            List<Long> roleIdList = operatorRoleService.getRoleIdListByOperatorId(vo.getId());
            vo.setRoleIdList(roleIdList);
            vo.setCreateName(operatorNameMap.get(vo.getCreateId()));
            vo.setUpdateName(operatorNameMap.get(vo.getUpdateId()));
        });
        return operatorVoPage;
    }

    public <T extends BaseEntity> Map<Long, Operator> getOperatorMap(List<T> voList) {
        Set<Long> createIdList = voList.stream().map(T::getCreateId).collect(Collectors.toSet());
        Set<Long> updateIdList = voList.stream().map(T::getUpdateId).collect(Collectors.toSet());
        List<Long> listAll = new ArrayList<>();
        listAll.addAll(createIdList);
        listAll.addAll(updateIdList);
        List<Long> listAllDistinct = listAll.stream().distinct().collect(toList());
        if (CollectionUtils.isEmpty(listAllDistinct)) {
            return new HashMap<>();
        }
        List<Operator> operatorList = listByIds(listAllDistinct);
        return operatorList.stream().collect(Collectors.toMap(Operator::getId, Function.identity(), (key1, key2) -> key2));
    }

    public <T extends BaseEntity> Map<Long, String> getOperatorNameMap(List<T> voList) {
        Set<Long> createIdList = voList.stream().map(T::getCreateId).collect(Collectors.toSet());
        Set<Long> updateIdList = voList.stream().map(T::getUpdateId).collect(Collectors.toSet());
        List<Long> listAll = new ArrayList<>();
        listAll.addAll(createIdList);
        listAll.addAll(updateIdList);
        List<Long> listAllDistinct = listAll.stream().distinct().collect(toList());
        if (CollectionUtils.isEmpty(listAllDistinct)) {
            return new HashMap<>();
        }
        List<Operator> operatorList = listByIds(listAllDistinct);
        return operatorList.stream().collect(Collectors.toMap(Operator::getId, Operator::getUserName, (key1, key2) -> key2));
    }


    /**
     * 登录
     *
     * @param dto
     */
    @Override
    public OperatorVO login(LoginDTO dto) {
        AssertUtil.isNotBlank(dto.getUserName(), "账号名不能为空");
        AssertUtil.isNotBlank(dto.getPassWord(), "密码不能为空");
        // 查询验证码
        String code = redisManage.getValue(dto.getUuid());
        // 清除验证码
        if (StringUtils.isNotBlank(dto.getCode())) {
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
        OperatorVO operatorVO = getInfo(dto.getUserName());
        AssertUtil.isNotTrue(PasswordUtil.verify(password, operatorVO.getPassWord()), "密码错误");
        String tokenKey = SysRedisKey.getBackendTokenkey(String.valueOf(operatorVO.getId()));

        String token = TokenUtil.createToken(String.valueOf(operatorVO.getId()));
        String useInfoKey = SysRedisKey.getOperatorInfoKey(token, dto.getClientIp());
        //缓存当前用户信息
        Objects.requireNonNull(operatorVO).setToken(token);

        redisManage.set(useInfoKey, operatorVO, SysRedisKey.ONE_DAY);
        redisManage.set(tokenKey, token, SysRedisKey.ONE_DAY);
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
        Operator operator = getUserByName(dto.getUserName());
        operator.setPassWord(null);
        return operator;
    }


    @Override
    public Operator getUserByName(String userName) {
        LambdaQueryWrapper<Operator> queryWrapper = new QueryWrapper<Operator>().lambda()
                .eq(Operator::getUserName, userName);
        return getOne(queryWrapper);
    }

    @Override
    public OperatorVO getInfo(Long userId) {
        Operator operator = getById(userId);
        OperatorVO operatorVO = BeanCopyUtil.copyProperties(operator, OperatorVO.class);
        List<Role> roleVoList = operatorRoleService.getEnableListByOperatorId(userId);
        List<String> roleKeyList = roleVoList.stream().map(Role::getRoleKey).collect(toList());
        List<Menu> menuList = menuService.getEnableMenuListByOperatorId(userId);
        List<String> permissionList = menuList.stream().filter(vo -> StringUtils.isNotBlank(vo.getPermission())).map(Menu::getPermission).collect(toList());
        permissionList.addAll(roleKeyList);
        operatorVO.setPermissionList(permissionList);
        return operatorVO;
    }

    @Override
    public OperatorVO getInfo(String userName) {
        LambdaQueryWrapper<Operator> lambdaQueryWrapper = new QueryWrapper<Operator>().lambda()
                .eq(Operator::getUserName, userName);
        Operator operator = getOne(lambdaQueryWrapper);
        AssertUtil.isNotNull(operator, "该用户不存在");
        OperatorVO operatorVO = BeanCopyUtil.copyProperties(operator, OperatorVO.class);
        List<Role> roleVoList = operatorRoleService.getEnableListByOperatorId(operator.getId());
        List<String> roleKeyList = roleVoList.stream().map(Role::getRoleKey).collect(toList());
        List<Menu> menuList = menuService.getEnableMenuListByOperatorId(operator.getId());
        List<String> permissionList = menuList.stream().filter(vo -> StringUtils.isNotBlank(vo.getPermission())).map(Menu::getPermission).collect(toList());
        permissionList.addAll(roleKeyList);
        operatorVO.setPermissionList(permissionList);
        return operatorVO;
    }

    @Override
    public List<Map<String, Object>> getDownLoadData(DownQueryDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        List<Map<String, Object>> list = new ArrayList<>();
        PageVO<OperatorVO> operatorPageVo = queryListByPage(dto);
        List<OperatorVO> operatorVoList = operatorPageVo.getRecords();
        operatorVoList.forEach(vo -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", vo.getUserName());
            map.put("头像", vo.getAvatar());
            map.put("邮箱", vo.getEmail());
            map.put("状态", vo.getStatus() == 1 ? "启用" : "禁用");
            map.put("手机号码", vo.getPhone());
            map.put("创建日期", vo.getCreateTime());
            list.add(map);
        });
        return list;
    }


}
