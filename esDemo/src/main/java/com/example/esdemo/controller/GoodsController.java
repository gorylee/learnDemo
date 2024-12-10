package com.example.esdemo.controller;

import com.example.esdemo.model.bo.GoodsQueryBo;
import com.example.esdemo.model.dto.GoodsEs;
import com.example.esdemo.model.dto.GoodsEsPage;
import com.example.esdemo.model.entity.Goods;
import com.example.esdemo.service.GoodsEsService;
import com.example.esdemo.service.GoodsService;
import example.common.model.Result;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author GoryLee
 * @Date 2022/12/5 00:51
 * @Version 1.0
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsEsService goodsEsService;
    @Autowired
    private GoodsService goodsService;

    @GetMapping(value = "/sayHello")
    public String sayHello(){
        return "hello world";
    }

    @RequestMapping("/get")
    public Result<GoodsEs> findById(GoodsQueryBo queryBo){
        GoodsEs goodsEs = goodsEsService.findById(queryBo.getId());
        return Result.createSuccess(goodsEs);
    }

    /**
     * 单个更新商品索引
     * @param queryBo
     * @return
     */
    @RequestMapping("/updateGoodsEs")
    public Result<GoodsEs> updateGoodsEs(GoodsQueryBo queryBo){
        Goods goods = goodsService.getById(queryBo.getId());
        GoodsEs goodsEs = goodsEsService.saveGoodsEs(goods);
        return Result.createSuccess(goodsEs);
    }

    /**
     * 根据ID从商品表查询，然后把查询到的数据存到搜索引擎
     *
     */
    @RequestMapping("/addAll")
    public Result<String> addAll(@RequestBody GoodsQueryBo queryBo) {
        if (ArrayUtils.isEmpty(queryBo.getIds())) {
            return Result.createError("ids不能为空");
        }
        goodsEsService.saveGoodsEsAll(queryBo.getIds());
        return Result.createSuccess();
    }

    /**
     * 根据条件查询商品信息
     */
    @RequestMapping("/list")
    public Result<GoodsEsPage<GoodsEs>> list(GoodsQueryBo queryBo) {
        GoodsEsPage<GoodsEs> pageGoodsEs = goodsEsService.findPage(queryBo);
        return Result.createSuccess(pageGoodsEs);
    }
}
