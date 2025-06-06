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
import com.example.web.dto.StockOutDetDto;
import com.example.web.dto.query.StockOutDetPagedInput;
import com.example.web.entity.Cargo;
import com.example.web.entity.StockOut;
import com.example.web.entity.StockOutDet;
import com.example.web.entity.Warehouse;
import com.example.web.entity.WarehouseBin;
import com.example.web.enums.StockOutStatusEnum;
import com.example.web.mapper.AppUserMapper;
import com.example.web.mapper.CargoMapper;
import com.example.web.mapper.StockOutDetMapper;
import com.example.web.mapper.StockOutMapper;
import com.example.web.mapper.WarehouseBinMapper;
import com.example.web.mapper.WarehouseMapper;
import com.example.web.service.StockOutDetService;
import com.example.web.tools.Extension;
import com.example.web.tools.dto.IdInput;
import com.example.web.tools.dto.IdsInput;
import com.example.web.tools.dto.PagedResult;
import com.example.web.tools.exception.CustomException;

import lombok.SneakyThrows;

/**
 * 出库明细功能实现类
 */
@Service
public class StockOutDetServiceImpl extends ServiceImpl<StockOutDetMapper, StockOutDet> implements StockOutDetService {

    /**
     * 操作数据库AppUser表mapper对象
     */
    @Autowired
    private AppUserMapper AppUserMapper;
    /**
     * 操作数据库的StockOutDet表mapper对象
     */
    @Autowired
    private StockOutDetMapper StockOutDetMapper;
    @Autowired
    private CargoMapper CargoMapper;
    @Autowired
    private WarehouseMapper WarehouseMapper;
    @Autowired
    private WarehouseBinMapper WarehouseBinMapper;
    @Autowired
    private StockOutMapper StockOutMapper;

    /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<StockOutDet> BuilderQuery(StockOutDetPagedInput input) {
        // 声明一个支持出库明细查询的(拉姆达)表达式
        LambdaQueryWrapper<StockOutDet> queryWrapper = Wrappers.<StockOutDet>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, StockOutDet::getId, input.getId())
                .eq(input.getCreatorId() != null, StockOutDet::getCreatorId, input.getCreatorId());
        // 如果前端搜索传入查询条件则拼接查询条件

        if (input.getStockOutId() != null) {
            queryWrapper = queryWrapper.eq(StockOutDet::getStockOutId, input.getStockOutId());
        }

        if (input.getCargoId() != null) {
            queryWrapper = queryWrapper.eq(StockOutDet::getCargoId, input.getCargoId());
        }

        if (input.getWarehouseId() != null) {
            queryWrapper = queryWrapper.eq(StockOutDet::getWarehouseId, input.getWarehouseId());
        }

        if (input.getWarehouseBinId() != null) {
            queryWrapper = queryWrapper.eq(StockOutDet::getWarehouseBinId, input.getWarehouseBinId());
        }
        return queryWrapper;
    }

    /**
     * 处理出库明细对于的外键数据
     */
    private StockOutDetDto DispatchItem(StockOutDetDto item) throws InvocationTargetException, IllegalAccessException {

        // 查询出关联的Cargo表信息
        Cargo CargoEntity = CargoMapper.selectById(item.getCargoId());
        if (CargoEntity != null) {
            item.setCargoDto(CargoEntity.MapToDto());
        }

        // 查询出关联的Warehouse表信息
        Warehouse WarehouseEntity = WarehouseMapper.selectById(item.getWarehouseId());
        if (WarehouseEntity != null) {
            item.setWarehouseDto(WarehouseEntity.MapToDto());
        }

        // 查询出关联的WarehouseBin表信息
        WarehouseBin WarehouseBinEntity = WarehouseBinMapper.selectById(item.getWarehouseBinId());
        if (WarehouseBinEntity != null) {
            item.setWarehouseBinDto(WarehouseBinEntity.MapToDto());
        }

        // 查询出关联的StockOut表信息
        StockOut StockOutEntity = StockOutMapper.selectById(item.getStockOutId());
        if (StockOutEntity != null) {
            item.setStockOutDto(StockOutEntity.MapToDto());
        }

        return item;
    }

    /**
     * 出库明细分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<StockOutDetDto> List(StockOutDetPagedInput input) {
        // 构建where条件+排序
        LambdaQueryWrapper<StockOutDet> queryWrapper = BuilderQuery(input);

        // 按创建时间从大到小排序 最新的显示在最前面
        queryWrapper = queryWrapper.orderByDesc(StockOutDet::getCreationTime);
        // 构建一个分页查询的model
        Page<StockOutDet> page = new Page<>(input.getPage(), input.getLimit());
        // 从数据库进行分页查询获取出库明细数据
        IPage<StockOutDet> pageRecords = StockOutDetMapper.selectPage(page, queryWrapper);
        // 获取所有满足条件的数据行数
        Long totalCount = StockOutDetMapper.selectCount(queryWrapper);
        // 把StockOutDet实体转换成StockOutDet传输模型
        List<StockOutDetDto> items = Extension.copyBeanList(pageRecords.getRecords(), StockOutDetDto.class);

        for (StockOutDetDto item : items) {
            DispatchItem(item);
        }
        // 返回一个分页结构给前端
        return PagedResult.GetInstance(items, totalCount);

    }

    /**
     * 单个出库明细查询
     */
    @SneakyThrows
    @Override
    public StockOutDetDto Get(StockOutDetPagedInput input) {
        if (input.getId() == null) {
            return new StockOutDetDto();
        }
        StockOutDet entity = StockOutDetMapper.selectById(input.getId());
        StockOutDetDto dto = entity.MapToDto();
        DispatchItem(dto);
        return dto;
    }

    /**
     * 出库明细创建或者修改
     */
    @SneakyThrows
    @Override
    public StockOutDetDto CreateOrEdit(StockOutDetDto input) {

        Long sameCount = StockOutDetMapper.selectCount(
                Wrappers.<StockOutDet>lambdaQuery().ne(input.getId() != null, StockOutDet::getId, input.getId())
                        .eq(StockOutDet::getWarehouseBinId, input.getWarehouseBinId())
                        .eq(StockOutDet::getCargoId, input.getCargoId())
                        .eq(StockOutDet::getStockOutId, input.getStockOutId()));
        if (sameCount > 0) {
            throw new CustomException("当前出库单存在相同商品相同库位的明细数据了,请检查数据是否正确");
        }

        // 声明一个出库明细实体
        StockOutDet StockOutDet = input.MapToEntity();
        // 调用数据库的增加或者修改方法
        saveOrUpdate(StockOutDet);

        BeginStockOut(StockOutDet.getStockOutId());
        // 把传输模型返回给前端
        return StockOutDet.MapToDto();
    }

    /**
     * 修改订单的状态变成开始
     *
     * @param StockOutId
     */
    private void BeginStockOut(Integer StockOutId) {
        StockOut stockOut = StockOutMapper.selectById(StockOutId);
        if (stockOut.getStockOutStatus() == StockOutStatusEnum.待出库.index()) {
            stockOut.setStockOutStatus(StockOutStatusEnum.拣货中.index());
            StockOutMapper.updateById(stockOut);
        }

    }

    /**
     * 出库明细删除
     */
    @Override
    public void Delete(IdInput input) {
        StockOutDet entity = StockOutDetMapper.selectById(input.getId());
        StockOutDetMapper.deleteById(entity);
    }

    /**
     * 出库明细批量删除
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
