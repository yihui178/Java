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
import com.example.web.dto.WarehouseBinDto;
import com.example.web.dto.query.WarehouseBinPagedInput;
import com.example.web.entity.Warehouse;
import com.example.web.entity.WarehouseBin;
import com.example.web.mapper.AppUserMapper;
import com.example.web.mapper.WarehouseBinMapper;
import com.example.web.mapper.WarehouseMapper;
import com.example.web.service.WarehouseBinService;
import com.example.web.tools.Extension;
import com.example.web.tools.dto.IdInput;
import com.example.web.tools.dto.IdsInput;
import com.example.web.tools.dto.PagedResult;
import com.example.web.tools.exception.CustomException;

import lombok.SneakyThrows;

/**
 * 库位功能实现类
 */
@Service
public class WarehouseBinServiceImpl extends ServiceImpl<WarehouseBinMapper, WarehouseBin>
        implements WarehouseBinService {

    /**
     * 操作数据库AppUser表mapper对象
     */
    @Autowired
    private AppUserMapper AppUserMapper;
    /**
     * 操作数据库的WarehouseBin表mapper对象
     */
    @Autowired
    private WarehouseBinMapper WarehouseBinMapper;
    @Autowired
    private WarehouseMapper WarehouseMapper;

    /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<WarehouseBin> BuilderQuery(WarehouseBinPagedInput input) {
        // 声明一个支持库位查询的(拉姆达)表达式
        LambdaQueryWrapper<WarehouseBin> queryWrapper = Wrappers.<WarehouseBin>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, WarehouseBin::getId, input.getId())
                .eq(input.getCreatorId() != null, WarehouseBin::getCreatorId, input.getCreatorId());
        // 如果前端搜索传入查询条件则拼接查询条件
        if (Extension.isNotNullOrEmpty(input.getBinCode())) {
            queryWrapper = queryWrapper.like(WarehouseBin::getBinCode, input.getBinCode());
        }
        if (Extension.isNotNullOrEmpty(input.getType())) {
            queryWrapper = queryWrapper.like(WarehouseBin::getType, input.getType());
        }
        if (Extension.isNotNullOrEmpty(input.getLevel())) {
            queryWrapper = queryWrapper.like(WarehouseBin::getLevel, input.getLevel());
        }

        if (input.getWarehouseId() != null) {
            queryWrapper = queryWrapper.eq(WarehouseBin::getWarehouseId, input.getWarehouseId());
        }
        return queryWrapper;
    }

    /**
     * 处理库位对于的外键数据
     */
    private WarehouseBinDto DispatchItem(WarehouseBinDto item)
            throws InvocationTargetException, IllegalAccessException {

        // 查询出关联的Warehouse表信息
        Warehouse WarehouseEntity = WarehouseMapper.selectById(item.getWarehouseId());
        if (WarehouseEntity != null) {
            item.setWarehouseDto(WarehouseEntity.MapToDto());
        }

        return item;
    }

    /**
     * 库位分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<WarehouseBinDto> List(WarehouseBinPagedInput input) {
        // 构建where条件+排序
        LambdaQueryWrapper<WarehouseBin> queryWrapper = BuilderQuery(input);

        // 按创建时间从大到小排序 最新的显示在最前面
        queryWrapper = queryWrapper.orderByDesc(WarehouseBin::getCreationTime);
        // 构建一个分页查询的model
        Page<WarehouseBin> page = new Page<>(input.getPage(), input.getLimit());
        // 从数据库进行分页查询获取库位数据
        IPage<WarehouseBin> pageRecords = WarehouseBinMapper.selectPage(page, queryWrapper);
        // 获取所有满足条件的数据行数
        Long totalCount = WarehouseBinMapper.selectCount(queryWrapper);
        // 把WarehouseBin实体转换成WarehouseBin传输模型
        List<WarehouseBinDto> items = Extension.copyBeanList(pageRecords.getRecords(), WarehouseBinDto.class);

        for (WarehouseBinDto item : items) {
            DispatchItem(item);
        }
        // 返回一个分页结构给前端
        return PagedResult.GetInstance(items, totalCount);

    }

    /**
     * 单个库位查询
     */
    @SneakyThrows
    @Override
    public WarehouseBinDto Get(WarehouseBinPagedInput input) {
        if (input.getId() == null) {
            return new WarehouseBinDto();
        }
        WarehouseBin entity = WarehouseBinMapper.selectById(input.getId());
        WarehouseBinDto dto = entity.MapToDto();
        DispatchItem(dto);
        return dto;
    }

    /**
     * 库位创建或者修改
     */
    @SneakyThrows
    @Override
    public WarehouseBinDto CreateOrEdit(WarehouseBinDto input) {

        Long sameBinCount = WarehouseBinMapper.selectCount(
                Wrappers.<WarehouseBin>lambdaQuery().ne(input.getId() != null, WarehouseBin::getId, input.getId())
                        .eq(WarehouseBin::getBinCode, input.getBinCode()));
        if (sameBinCount > 0) {
            throw new CustomException("库位编码已存在");
        }
        // 声明一个库位实体
        WarehouseBin WarehouseBin = input.MapToEntity();
        // 调用数据库的增加或者修改方法
        saveOrUpdate(WarehouseBin);
        // 把传输模型返回给前端
        return WarehouseBin.MapToDto();
    }

    /**
     * 库位删除
     */
    @Override
    public void Delete(IdInput input) {
        WarehouseBin entity = WarehouseBinMapper.selectById(input.getId());
        WarehouseBinMapper.deleteById(entity);
    }

    /**
     * 库位批量删除
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
