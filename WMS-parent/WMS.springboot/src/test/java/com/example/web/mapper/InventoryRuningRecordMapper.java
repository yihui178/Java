package com.example.web.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.web.entity.*;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存流水记录表对应的Mapper
 */
@Mapper
public interface InventoryRuningRecordMapper  extends BaseMapper<InventoryRuningRecord> {

}
