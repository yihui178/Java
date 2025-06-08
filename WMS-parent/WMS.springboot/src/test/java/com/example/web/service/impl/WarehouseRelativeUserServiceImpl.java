package com.example.web.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.web.dto.WarehouseRelativeUserDto;
import com.example.web.dto.query.WarehouseRelativeUserPagedInput;
import com.example.web.entity.AppUser;
import com.example.web.entity.Warehouse;
import com.example.web.entity.WarehouseRelativeUser;
import com.example.web.mapper.AppUserMapper;
import com.example.web.mapper.WarehouseMapper;
import com.example.web.mapper.WarehouseRelativeUserMapper;
import com.example.web.service.WarehouseRelativeUserService;
import com.example.web.tools.Extension;
import com.example.web.tools.dto.IdInput;
import com.example.web.tools.dto.IdsInput;
import com.example.web.tools.dto.PagedResult;
import com.example.web.tools.exception.CustomException;

import lombok.SneakyThrows;

/**
 * 仓库用户功能实现类
 */
@Service
public class WarehouseRelativeUserServiceImpl extends ServiceImpl<WarehouseRelativeUserMapper, WarehouseRelativeUser>
        implements WarehouseRelativeUserService {

    /**
     * 操作数据库AppUser表mapper对象
     */
    @Autowired
    private AppUserMapper AppUserMapper;
    /**
     * 操作数据库的WarehouseRelativeUser表mapper对象
     */
    @Autowired
    private WarehouseRelativeUserMapper WarehouseRelativeUserMapper;
    @Autowired
    private WarehouseMapper WarehouseMapper;

    /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<WarehouseRelativeUser> BuilderQuery(WarehouseRelativeUserPagedInput input) {
        // 声明一个支持仓库用户查询的(拉姆达)表达式
        LambdaQueryWrapper<WarehouseRelativeUser> queryWrapper = Wrappers.<WarehouseRelativeUser>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, WarehouseRelativeUser::getId, input.getId())
                .eq(input.getCreatorId() != null, WarehouseRelativeUser::getCreatorId, input.getCreatorId());
        // 如果前端搜索传入查询条件则拼接查询条件

        if (input.getUserId() != null) {
            queryWrapper = queryWrapper.eq(WarehouseRelativeUser::getUserId, input.getUserId());
        }

        if (input.getWarehouseId() != null) {
            queryWrapper = queryWrapper.eq(WarehouseRelativeUser::getWarehouseId, input.getWarehouseId());
        }
        return queryWrapper;
    }

    /**
     * 处理仓库用户对于的外键数据
     */
    private WarehouseRelativeUserDto DispatchItem(WarehouseRelativeUserDto item)
            throws InvocationTargetException, IllegalAccessException {

        // 查询出关联的AppUser表信息
        AppUser UserEntity = AppUserMapper.selectById(item.getUserId());
        if (UserEntity != null) {
            item.setUserDto(UserEntity.MapToDto());
        }

        // 查询出关联的Warehouse表信息
        Warehouse WarehouseEntity = WarehouseMapper.selectById(item.getWarehouseId());
        if (WarehouseEntity != null) {
            item.setWarehouseDto(WarehouseEntity.MapToDto());
        }

        return item;
    }

    /**
     * 仓库用户分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<WarehouseRelativeUserDto> List(WarehouseRelativeUserPagedInput input) {
        // 构建where条件+排序
        LambdaQueryWrapper<WarehouseRelativeUser> queryWrapper = BuilderQuery(input);

        // 按创建时间从大到小排序 最新的显示在最前面
        queryWrapper = queryWrapper.orderByDesc(WarehouseRelativeUser::getCreationTime);
        // 构建一个分页查询的model
        Page<WarehouseRelativeUser> page = new Page<>(input.getPage(), input.getLimit());
        // 从数据库进行分页查询获取仓库用户数据
        IPage<WarehouseRelativeUser> pageRecords = WarehouseRelativeUserMapper.selectPage(page, queryWrapper);
        // 获取所有满足条件的数据行数
        Long totalCount = WarehouseRelativeUserMapper.selectCount(queryWrapper);
        // 把WarehouseRelativeUser实体转换成WarehouseRelativeUser传输模型
        List<WarehouseRelativeUserDto> items = Extension.copyBeanList(pageRecords.getRecords(),
                WarehouseRelativeUserDto.class);

        for (WarehouseRelativeUserDto item : items) {
            DispatchItem(item);
        }
        // 返回一个分页结构给前端
        return PagedResult.GetInstance(items, totalCount);

    }

    /**
     * 单个仓库用户查询
     */
    @SneakyThrows
    @Override
    public WarehouseRelativeUserDto Get(WarehouseRelativeUserPagedInput input) {
        if (input.getId() == null) {
            return new WarehouseRelativeUserDto();
        }
        WarehouseRelativeUser entity = WarehouseRelativeUserMapper.selectById(input.getId());
        WarehouseRelativeUserDto dto = entity.MapToDto();
        DispatchItem(dto);
        return dto;
    }

    /**
     * 仓库用户创建或者修改
     */
    @SneakyThrows
    @Override
    public WarehouseRelativeUserDto CreateOrEdit(WarehouseRelativeUserDto input) {

        Long existSameCount = WarehouseRelativeUserMapper.selectCount(Wrappers.<WarehouseRelativeUser>lambdaQuery()
                .ne(input.getId() != null, WarehouseRelativeUser::getId, input.getId())
                .eq(WarehouseRelativeUser::getUserId, input.getUserId())
                .eq(WarehouseRelativeUser::getWarehouseId, input.getWarehouseId()));
        if (existSameCount > 0) {
            throw new CustomException("仓库用户已存在");
        }
        // 声明一个仓库用户实体
        WarehouseRelativeUser WarehouseRelativeUser = input.MapToEntity();
        // 调用数据库的增加或者修改方法
        saveOrUpdate(WarehouseRelativeUser);
        // 把传输模型返回给前端
        return WarehouseRelativeUser.MapToDto();
    }

    /**
     * 仓库用户删除
     */
    @Override
    public void Delete(IdInput input) {
        WarehouseRelativeUser entity = WarehouseRelativeUserMapper.selectById(input.getId());
        WarehouseRelativeUserMapper.deleteById(entity);
    }

    /**
     * 仓库用户批量删除
     */
    @Override
    public void BatchDelete(IdsInput input) {
        for (Integer id : input.getIds()) {
            IdInput idInput = new IdInput();
            idInput.setId(id);
            Delete(idInput);
        }
    }
}
