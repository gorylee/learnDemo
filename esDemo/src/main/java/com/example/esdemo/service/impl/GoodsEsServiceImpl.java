package com.example.esdemo.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.esdemo.mapper.GoodsMapper;
import com.example.esdemo.model.dto.GoodsEs;
import com.example.esdemo.model.entity.Goods;
import com.example.esdemo.model.entity.GoodsSku;
import com.example.esdemo.model.enums.GoodsEsEnum;
import com.example.esdemo.service.GoodsEsService;
import com.example.esdemo.service.GoodsService;
import example.common.utils.XBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/11/26
 */
@Service
@Slf4j
public class GoodsEsServiceImpl implements GoodsEsService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private GoodsService goodsService;

    @Value("${es.index-alias.goods}")// 商品索引别名
    private String indexAlias;

    public String getIndexName() {
        return indexAlias;
    }

    @Override
    public GoodsEs findById(Long id) {
        try {
            if (id == null) {
                return null;
            }
            GetRequest request = new GetRequest(getIndexName(), String.valueOf(id));
            GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            if (response.isExists()) {
                return JSONUtil.toBean(response.getSourceAsString(), GoodsEs.class);
            }
        } catch (Exception e) {
            log.error("根据ID搜索商品出现异常！商品id={}", id, e);
        }
        return null;
    }

    //保存索引
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoodsEs saveGoodsEs(Goods goods) {
        if (goods == null || goods.getId() == null) return null;
        GoodsEs goodsEs = convertGoodsEs(goods);
        saveToEs(goodsEs);
        return goodsEs;
    }

    private GoodsEs convertGoodsEs(Goods goods) {
        GoodsEs goodsEs = new GoodsEs();
        BeanUtils.copyProperties(goods, goodsEs);
        goodsEs.setGoodsId(goods.getId());
        return goodsEs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGoodsEsAll(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        List<Goods> goodsList = goodsService.listByIds(Arrays.asList(ids));

        // 批量添加索引
        saveBatch(goodsList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(Collection<Goods> goodsList) {
        if (StrUtil.isEmpty(getIndexName())) {
            log.warn("索引不能为空！");
            return;
        }
        if (goodsList == null || goodsList.size() == 0) {
            return;
        }
        try {
            BulkRequest bulkRequest = new BulkRequest();
            Map<String, Object> jsonMap;
            int size = 0;
            for (Goods goods : goodsList) {
                if (goods == null || goods.getId() == null) {
                    continue;
                }

                jsonMap = beanToMap(goods);
                String json = JSON.toJSONString(jsonMap);// 注意：更新时，null值字段要去掉，不参与更新。
                if (jsonMap.size() == 0) {
                    continue;
                }

                bulkRequest.add(
                        new UpdateRequest(getIndexName(), String.valueOf(goods.getId())).doc(json, XContentType.JSON).upsert(json, XContentType.JSON)
                );

                if (++size >= 200) {
                    restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);// 执行批量更新
                    bulkRequest = new BulkRequest();
                    size = 0;
                }
            }

            if (size > 0) {
                restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);// 执行批量更新
            }
        } catch (Exception e) {
            log.error("批量导入索引异常", e);
        }
    }
    private Map<String, Object> beanToMap(Goods goods) {
        GoodsEs goodsEs = convertGoodsEs(goods);
        return XBeanUtils.beanToMap(goodsEs); // 去掉值为null和""的字段
    }

    private void saveToEs(GoodsEs goodsEs) {
        String json = JSON.toJSONString(goodsEs);//null值字段要去掉，不参与更新。 JsonUtils.toString(goodsEs);

        if (goodsEs.getDeleted() == 1) {//删除
//            deleteEs(goodsEs.getGoodsId());
            log.info("索引删除成功：id={}", goodsEs.getId());
            return;
        }

        try {
            UpdateRequest request = new UpdateRequest(getIndexName(), String.valueOf(goodsEs.getId())).doc(json, XContentType.JSON).upsert(json, XContentType.JSON);
            restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("索引保存失败，索引id={}, [{}]", goodsEs.getId(), json, e);
        }
    }
}
