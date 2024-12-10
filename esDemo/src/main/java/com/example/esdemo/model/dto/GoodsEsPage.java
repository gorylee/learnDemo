package com.example.esdemo.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 商品分页对象，对原Page对象的增强
 *
 * @param <E>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsEsPage<E> extends Page<E> {

    // 这里为什么不统一用集合或者数组？ 历史原因，为了兼容以前的查询对象BrandQueryBo、CategoryQueryBo

    private Set<Long> brandIdSets;    // 品牌ID
    private Set<Long> lastCateIdSets;// 末级分类ID
    private Set<Object> placeIdSets; // 商品属性：产地
    private Set<Object> modelIdSets; // 商品属性：模型

    private Set<Object> citySets; // 城市名称

    public GoodsEsPage() {
    }

    public GoodsEsPage(long pageNo, long pageSize) {
        super(pageNo, pageSize);
    }

    @Override
    public List<E> getRecords() {
        if (CollectionUtils.isEmpty(super.getRecords())) {
            super.setRecords(new ArrayList<>());
        }
        return super.getRecords();//Collections.emptyList();
    }

    public Set<Long> getBrandIdSets() {
        return CollectionUtils.isEmpty(this.brandIdSets) ? this.brandIdSets = new HashSet<>() : this.brandIdSets;//Collections.emptySet();
    }

    public Set<Long> getLastCateIdSets() {
        return CollectionUtils.isEmpty(this.lastCateIdSets) ? this.lastCateIdSets = new HashSet<>() : this.lastCateIdSets;//Collections.emptySet();
    }

    public Set<Object> getPlaceIdSets() {
        return CollectionUtils.isEmpty(this.placeIdSets) ? this.placeIdSets = new HashSet<>() : this.placeIdSets;//Collections.emptySet();
    }

    public Set<Object> getModelIdSets() {
        return CollectionUtils.isEmpty(this.modelIdSets) ? this.modelIdSets = new HashSet<>() : this.modelIdSets;//Collections.emptySet();
    }

    public Set<Object> getCitySets() {
        return CollectionUtils.isEmpty(this.citySets) ? this.citySets = new HashSet<>() : this.citySets;//Collections.emptySet();
    }
}