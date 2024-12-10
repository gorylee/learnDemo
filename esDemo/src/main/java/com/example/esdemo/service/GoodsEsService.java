package com.example.esdemo.service;

import com.example.esdemo.model.bo.GoodsQueryBo;
import com.example.esdemo.model.dto.GoodsEs;
import com.example.esdemo.model.dto.GoodsEsPage;
import com.example.esdemo.model.entity.Goods;

import java.util.Collection;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/12/5
 */
public interface GoodsEsService {

    GoodsEs findById(Long id);

    /**
     * 添加和修改索引（重载方法）
     *
     * @param goods 商品对象
     * @return 返回商品对象
     */
    GoodsEs saveGoodsEs(Goods goods);

    void saveGoodsEsAll(Long[] ids);

    /**
     * 批量添加索引
     *
     * @param goodsList 商品对象
     */
    void saveBatch(Collection<Goods> goodsList);

    GoodsEsPage<GoodsEs> findPage(GoodsQueryBo queryBo);
}
