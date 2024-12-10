package com.example.esdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.esdemo.model.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}
