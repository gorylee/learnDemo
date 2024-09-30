package com.example.springbatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbatch.entity.Sale;
import com.example.springbatch.entity.SaleDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SaleMapper extends BaseMapper<Sale> {


    void saveSale(Sale sale);

    void saveSaleDetail(SaleDetail saleDetail);

}
