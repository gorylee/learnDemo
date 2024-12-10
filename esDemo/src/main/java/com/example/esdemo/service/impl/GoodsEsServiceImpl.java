package com.example.esdemo.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.esdemo.model.bo.GoodsQueryBo;
import com.example.esdemo.model.dto.GoodsEs;
import com.example.esdemo.model.dto.GoodsEsPage;
import com.example.esdemo.model.entity.Goods;
import com.example.esdemo.model.enums.GoodsEsEnum;
import com.example.esdemo.model.enums.SortRuleEnum;
import com.example.esdemo.model.enums.SortTypeEnum;
import com.example.esdemo.service.GoodsEsService;
import com.example.esdemo.service.GoodsService;
import example.common.utils.XBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Override
    public GoodsEsPage<GoodsEs> findPage(GoodsQueryBo queryBo) {
        log.info(getIndexName() + ",接收到的参数：{}", JSON.toJSONString(queryBo));
        int pageNo = 0, pageSize = 0;
        try {
            pageNo = Long.valueOf(queryBo.getPageNo()).intValue();
            pageSize = Long.valueOf(queryBo.getPageSize()).intValue();
            pageNo = pageNo <= 0 ? 1 : pageNo;
            pageSize = pageSize <= 0 ? 10 : pageSize;
        } catch (Exception e) {
            log.error("分页参数异常, pageNo={}, pageSize={}", queryBo.getPageNo(), queryBo.getPageSize(), e);
            pageNo = 1;
            pageSize = 10;
        } finally {
            queryBo.setPageNo(pageNo);
            queryBo.setPageSize(pageSize);
        }

        int from = (pageNo - 1) * pageSize;
        GoodsEsPage<GoodsEs> accuratePage = searchAccurate(queryBo); // 查询“精准匹配”的数据

        int accurateTotal = Long.valueOf(accuratePage.getTotal()).intValue();// “精准数据”的总记录数
        int currentPageAccurateSize = accuratePage.getRecords().size();  // 当前页“精准数据”的记录数

        // 如果当前页的“精准数据”足够一页，则（不需要填充非精准数据）直接返回。
        if (currentPageAccurateSize == pageSize) {
            GoodsEsPage<GoodsEs> totalPage = search(queryBo, false, from); // 总记录数
            totalPage.setRecords(accuratePage.getRecords());
            return totalPage;
        } else {
            // 当前页的“精准数据”不足一页(pageSize)，则获取“非精准数据”填充。
            queryBo.setPageSize(pageSize - currentPageAccurateSize); // 当前页需要填充多少条非精准数据 = 页大小 - 当前页“精准数据”的记录数
            GoodsEsPage<GoodsEs> page = search(queryBo, true, from < accurateTotal ? 0 : from - accurateTotal);

            if (currentPageAccurateSize != 0) {
                accuratePage.getRecords().addAll(page.getRecords());// 填充“非精准数据”
                page.setRecords(accuratePage.getRecords());
            }

            return page;
        }
    }

    private GoodsEsPage<GoodsEs> searchAccurate(GoodsQueryBo queryBo) {
        String keyword = queryBo.getTitle();
        if (StringUtils.isEmpty(keyword)) {
            keyword = queryBo.getKeyWord();
        }
        if (StrUtil.isEmpty(keyword)) {
            return new GoodsEsPage<>(queryBo.getPageNo(), queryBo.getPageSize());
        }
        keyword = GoodsEsEnum.removeSpecialJoin_(keyword);// 处理关键词
        keyword = GoodsEsEnum.subKeyword(keyword);

        // 构建查询
        BoolQueryBuilder boolQueryBuilder = builderBasicQuery(queryBo);//QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.DELETED.getValue(), 0));//删除标记：正常
        boolQueryBuilder.must(QueryBuilders.termQuery(GoodsEsEnum.TITLE_JOIN_.getValue(), keyword)); // 商品名 100%匹配 用户输入的关键词

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);

        // 分页
        searchSourceBuilder.from(Long.valueOf((queryBo.getPageNo() - 1) * queryBo.getPageSize()).intValue());
        searchSourceBuilder.size(Long.valueOf(queryBo.getPageSize()).intValue());

        // 构建排序
        builderSort(searchSourceBuilder, queryBo);

        // 执行查询并封装结果集
        return handlerResult(searchSourceBuilder, queryBo);
    }

    // 查询数据
    private GoodsEsPage<GoodsEs> search(GoodsQueryBo goodsQueryBo, boolean isExcludeAccurate, int from) {
        //构建搜索引擎查询条件
        BoolQueryBuilder boolQueryBuilder = builderBasicQuery(goodsQueryBo);//基本查询

        //关键字查询(标题(优先)，别名)
        if (!StringUtils.isEmpty(goodsQueryBo.getKeyWord())) {
            String keyword = GoodsEsEnum.removeSpecialJoinSpace(goodsQueryBo.getKeyWord());
            keyword = subKeyword(keyword);

            String key = "*" + keyword.replaceAll("\\s", "*") + "*";// 没有去掉下划线
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(GoodsEsEnum.TITLE_JOIN_.getValue(), key).boost(6f));
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(GoodsEsEnum.NAME_JOIN_.getValue(), key).boost(0.1f));
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(GoodsEsEnum.BRAND_NAME_JOIN_.getValue(), key).boost(0.1f));
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(GoodsEsEnum.CAS_REMOVE_SPECIAL.getValue(), key.replaceAll("[_\\s]", "")).boost(0.1f));// cas
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(GoodsEsEnum.KEY_NAMES_JOIN_.getValue(), key.replaceAll("[_\\s]+", "*")).boost(0.1f)); // 规格
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(GoodsEsEnum.STORE_CODE_REMOVE_SPECIAL.getValue(), key));// 存货编码
            boolQueryBuilder.minimumShouldMatch(1);// 至少满足should中的一个条件

            boolQueryBuilder.should(QueryBuilders.matchQuery(GoodsEsEnum.TITLE.getValue(), keyword).minimumShouldMatch("100%").boost(6f));
            boolQueryBuilder.should(QueryBuilders.matchQuery(GoodsEsEnum.NAME.getValue(), keyword).minimumShouldMatch("100%").boost(0.1f));
            boolQueryBuilder.should(QueryBuilders.matchQuery(GoodsEsEnum.BRAND_NAME.getValue(), keyword).minimumShouldMatch("100%").boost(0.1f));
            boolQueryBuilder.should(QueryBuilders.termsQuery(GoodsEsEnum.STORE_CODE.getValue(), keyword.replaceAll("\\s", "")).boost(0.1F));//存货编码

            // 是否排除精准查询的数据
            if (isExcludeAccurate) {
                keyword = subKeyword(GoodsEsEnum.removeSpecialJoin_(goodsQueryBo.getKeyWord()));
                boolQueryBuilder.mustNot(QueryBuilders.termQuery(GoodsEsEnum.TITLE_JOIN_.getValue(), keyword));
            }
        }

        // 模糊查询才执行，精准查询有另外的查询方式，此处不需要执行
        if (!StringUtils.isEmpty(goodsQueryBo.getTitle())) { // 商品名
            String keyword = subKeyword(GoodsEsEnum.removeSpecialJoinSpace(goodsQueryBo.getTitle()));
            String key = "*" + keyword.replaceAll("\\s", "*") + "*";// 没有去掉下划线

            boolQueryBuilder.should(QueryBuilders.wildcardQuery(GoodsEsEnum.TITLE_JOIN_.getValue(), key).boost(6f));
            boolQueryBuilder.should(QueryBuilders.matchQuery(GoodsEsEnum.TITLE.getValue(), keyword).minimumShouldMatch("100%").boost(6f));
            int min = Integer.parseInt(boolQueryBuilder.minimumShouldMatch() == null ? "0" : boolQueryBuilder.minimumShouldMatch());
            boolQueryBuilder.minimumShouldMatch(++min);
        }

        // 是否排除精准查询的数据
        if (isExcludeAccurate && !StringUtils.isEmpty(goodsQueryBo.getTitle())) {
            String keyword = subKeyword(GoodsEsEnum.removeSpecialJoin_(goodsQueryBo.getTitle()));
            boolQueryBuilder.mustNot(QueryBuilders.termQuery(GoodsEsEnum.TITLE_JOIN_.getValue(), keyword));
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);

        //构建分页
        searchSourceBuilder.from(from);//.from((pageNo - 1) * pageSize);
        searchSourceBuilder.size(Long.valueOf(goodsQueryBo.getPageSize()).intValue());

        //构建排序
        builderSort(searchSourceBuilder, goodsQueryBo);

        //执行查询并封装结果集
        return handlerFuzzyResult(searchSourceBuilder, goodsQueryBo);
    }

    private String subKeyword(String keyword) {
        if (keyword.length() > 45) {
            keyword = keyword.substring(0, 45);
        }
        return keyword;
    }
    // 处理查询结果
    private GoodsEsPage<GoodsEs> handlerFuzzyResult(SearchSourceBuilder searchSourceBuilder, GoodsQueryBo goodsEsQueryBo) {
        GoodsEsPage<GoodsEs> voPage = new GoodsEsPage<>(goodsEsQueryBo.getPageNo(), goodsEsQueryBo.getPageSize());
        try {
            //执行搜索引擎查询
            SearchRequest searchRequest = new SearchRequest(getIndexName());
            searchRequest.source(searchSourceBuilder);
            log.info("ES搜索语句:{}",searchSourceBuilder);
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            log.info("ES搜索结果:{}",search);
            SearchHits hits = search.getHits();
            SearchHit[] resultHits = hits.getHits();
            List<GoodsEs> resultList = new ArrayList<>();

            for (SearchHit hit : resultHits) {
                String json = hit.getSourceAsString();
                GoodsEs goodsEs = JSONUtil.toBean(json, GoodsEs.class);
                resultList.add(goodsEs);
            }

            voPage.setRecords(resultList);
            voPage.setTotal(hits.getTotalHits().value);
        } catch (Exception e) {
            log.error("sku搜索出现异常!", e);
        }
        return voPage;
    }

    // 处理查询结果
    private GoodsEsPage<GoodsEs> handlerResult(SearchSourceBuilder searchSourceBuilder, GoodsQueryBo goodsEsQueryBo) {
        GoodsEsPage<GoodsEs> voPage = new GoodsEsPage<>(goodsEsQueryBo.getPageNo(), goodsEsQueryBo.getPageSize());
        try {
            //执行搜索引擎查询
            SearchRequest searchRequest = new SearchRequest(getIndexName());
            searchRequest.source(searchSourceBuilder);
            log.info("执行搜索引擎查询参数:{}",JSONUtil.toJsonStr(searchRequest));
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            List<GoodsEs> resultList = handlerResult(hits);
            voPage.setRecords(resultList);
            voPage.setTotal(hits.getTotalHits().value);
        } catch (Exception e) {
            log.error("商品搜索出现异常!", e);
        }
        return voPage;
    }

    private List<GoodsEs> handlerResult(SearchHits hits) {
        SearchHit[] resultHits = hits.getHits();
        List<GoodsEs> resultList = new ArrayList<>();

        for (SearchHit hit : resultHits) {
            String json = hit.getSourceAsString();
            GoodsEs goodsEs = JSONUtil.toBean(json, GoodsEs.class);
            goodsEs.setScore(hit.getScore());
            resultList.add(goodsEs);
        }
        return resultList;
    }

    //构建排序
    private void builderSort(SearchSourceBuilder searchSourceBuilder, GoodsQueryBo goodsEsQueryBo) {
        //构建排序(店铺类型，商品排序字段，价格)
        SortOrder order = SortRuleEnum.getValue(goodsEsQueryBo.getSortRule());// 排序规则 - 1(ASC)/2(DESC)
        if (order == null) {
            order = SortRuleEnum.DESC.getValue();
        }

        searchSourceBuilder.sort(SortBuilders.fieldSort(GoodsEsEnum.IS_STORE_COUNT.getValue()).order(SortRuleEnum.DESC.getValue()));//库存排序
        searchSourceBuilder.sort(SortBuilders.fieldSort("_score").order(SortRuleEnum.DESC.getValue()));//相关性排序

        //排序类型
        if (goodsEsQueryBo.getSortType() != null) {
            if (goodsEsQueryBo.getSortType().equals(SortTypeEnum.SALECOUNT.getKey())) {//销量排序
                searchSourceBuilder.sort(SortBuilders.fieldSort(GoodsEsEnum.SALE_COUNT.getValue()).order(order));
            }
            if (goodsEsQueryBo.getSortType().equals(SortTypeEnum.PRICE.getKey())) {//价格排序
                searchSourceBuilder.sort(SortBuilders.fieldSort(GoodsEsEnum.PRICE_MIN.getValue()).order(order));
            }
        }

        //排序字段
        String sortField = goodsEsQueryBo.getSortField(); // 排序字段
        if (StrUtil.isNotEmpty(sortField)) {
            sortField = lineToHump(sortField);//转换为驼峰命名
            searchSourceBuilder.sort(SortBuilders.fieldSort(sortField).order(order));
        }

        if (goodsEsQueryBo.getStoreId() != null) {//店铺推荐排序
            searchSourceBuilder.sort(SortBuilders.fieldSort(GoodsEsEnum.STORE_RECOMMEND_SORT.getValue()).order(SortRuleEnum.ASC.getValue()));
        } else {
            searchSourceBuilder.sort(SortBuilders.fieldSort(GoodsEsEnum.IS_RECOMMEND.getValue()).order(SortRuleEnum.DESC.getValue()));//是否推荐
            searchSourceBuilder.sort(SortBuilders.fieldSort(GoodsEsEnum.RECOMMEND_SORT.getValue()).order(SortRuleEnum.ASC.getValue()));//推荐/新品排序
            searchSourceBuilder.sort(SortBuilders.fieldSort(GoodsEsEnum.STORE_TYPE.getValue()).order(SortRuleEnum.ASC.getValue()));//店铺类型
        }

    }
    //下划线转驼峰
    private String lineToHump(String str) {
        if (str == null || "".equals(str.trim())) {
            return str;
        }
        Matcher matcher = Pattern.compile("_(\\w)").matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    //构建搜索引擎基本查询条件
    private BoolQueryBuilder builderBasicQuery(GoodsQueryBo goodsEsQueryBo) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.DELETED.getValue(), 0));//删除标记：正常

        // 商品ID
        if (goodsEsQueryBo.getId() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.ID.getValue(), goodsEsQueryBo.getId()));
        }
        if (goodsEsQueryBo.getGoodsIds() != null && !goodsEsQueryBo.getGoodsIds().isEmpty()) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery(GoodsEsEnum.ID.getValue(), goodsEsQueryBo.getGoodsIds()));
        }

        // 业务类型
        if (goodsEsQueryBo.getCateType() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.CATE_TYPE.getValue(), goodsEsQueryBo.getCateType()));
        }

        //状态过虑(上架，下架)
        if (goodsEsQueryBo.getStatusAttr() != null && goodsEsQueryBo.getStatusAttr().length != 0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery(GoodsEsEnum.STATUS.getValue(), (Object[]) goodsEsQueryBo.getStatusAttr()));
        }

        //品牌过虑
        if (goodsEsQueryBo.getBrandId() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.BRAND_ID.getValue(), goodsEsQueryBo.getBrandId()));
        }
        //品牌多个
        if (goodsEsQueryBo.getBrandIds() != null && goodsEsQueryBo.getBrandIds().length != 0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery(GoodsEsEnum.BRAND_ID.getValue(), (Object[]) goodsEsQueryBo.getBrandIds()));
        }
        //一级分类
        if (goodsEsQueryBo.getOneCateId() != null) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termQuery(GoodsEsEnum.GOODS_CATE_ID_ONE.getValue(), goodsEsQueryBo.getOneCateId()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
        }
        //二级分类
        if (goodsEsQueryBo.getTwoCateId() != null) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termQuery(GoodsEsEnum.GOODS_CATE_ID_TWO.getValue(), goodsEsQueryBo.getTwoCateId()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
            //boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.GOODS_CATE_ID_TWO.getValue(), goodsEsQueryBo.getTwoCateId()));
        }
        //三级分类
        if (goodsEsQueryBo.getThreeCateId() != null) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termQuery(GoodsEsEnum.GOODS_CATE_ID_THREE.getValue(), goodsEsQueryBo.getThreeCateId()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
        }
        //末级分类
        if (goodsEsQueryBo.getLastCateId() != null) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termQuery(GoodsEsEnum.GOODS_CATE_ID_LAST.getValue(), goodsEsQueryBo.getLastCateId()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
        }

        //前台分类过虑，为了兼容接口调用方，参数名引用原来的oneCateId，但是查询的字段是新的前台分类字段goods_cate_id_one
        if (goodsEsQueryBo.getLastCateIds() != null && !goodsEsQueryBo.getLastCateIds().isEmpty()) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termsQuery(GoodsEsEnum.GOODS_CATE_ID_LAST.getValue(), goodsEsQueryBo.getLastCateIds()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
        }

        // 前台分类
        if (StrUtil.isNotEmpty((goodsEsQueryBo.getCateIdPath()))){
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termQuery(GoodsEsEnum.GOODS_CATE_ID_PATH.getValue(), goodsEsQueryBo.getCateIdPath()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
        }

        //前台分类
        //一级分类
        if (goodsEsQueryBo.getGoodsCateIdOne() != null) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termQuery(GoodsEsEnum.GOODS_CATE_ID_ONE.getValue(), goodsEsQueryBo.getGoodsCateIdOne()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
        }
        //二级分类
        if (goodsEsQueryBo.getGoodsCateIdTwo() != null) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termQuery(GoodsEsEnum.GOODS_CATE_ID_TWO.getValue(), goodsEsQueryBo.getGoodsCateIdTwo()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
        }
        //三级分类
        if (goodsEsQueryBo.getGoodsCateIdThree() != null) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termQuery(GoodsEsEnum.GOODS_CATE_ID_THREE.getValue(), goodsEsQueryBo.getGoodsCateIdThree()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
        }
        //末级分类
        if (goodsEsQueryBo.getGoodsCateIdLast() != null) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termQuery(GoodsEsEnum.GOODS_CATE_ID_LAST.getValue(), goodsEsQueryBo.getGoodsCateIdLast()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
        }
        if (CollectionUtils.isNotEmpty(goodsEsQueryBo.getGoodsCateIdLastList())) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termsQuery(GoodsEsEnum.GOODS_CATE_ID_LAST.getValue(), goodsEsQueryBo.getGoodsCateIdLastList()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
        }

        // 前台分类
        if (StrUtil.isNotEmpty(goodsEsQueryBo.getGoodsCateIdPath())) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.filter(QueryBuilders.termsQuery(GoodsEsEnum.GOODS_CATE_ID_PATH.getValue(), goodsEsQueryBo.getGoodsCateIdPath()));
            boolQueryBuilder.must(QueryBuilders.nestedQuery(GoodsEsEnum.GOODS_CATE_RELATION_LIST.getValue(), nestedBoolQuery, ScoreMode.None));
        }

        // 来源ID
        if (goodsEsQueryBo.getSourceId() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.SOURCE_ID.getValue(), goodsEsQueryBo.getSourceId()));
        }

        // 存货编码
        if (StrUtil.isNotEmpty(goodsEsQueryBo.getStoreCode())) {
            String keyword = GoodsEsEnum.removeSpecial(goodsEsQueryBo.getStoreCode());
            String key = "*" + keyword + "*";
            boolQueryBuilder.filter(QueryBuilders.wildcardQuery(GoodsEsEnum.STORE_CODE_LIST.getValue(), key));// 存货编码
        }

        //店铺分类过虑
        //一级分类
        if (goodsEsQueryBo.getOneBcateId() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.ONE_BCATE_ID.getValue(), goodsEsQueryBo.getOneBcateId()));
        }
        //二级分类
        if (goodsEsQueryBo.getTwoBcateId() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.TWO_BCATE_ID.getValue(), goodsEsQueryBo.getTwoBcateId()));
        }
        //三级分类
        if (goodsEsQueryBo.getThreeBcateId() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.THREE_BCATE_ID.getValue(), goodsEsQueryBo.getThreeBcateId()));
        }

        // 规格
        if (StrUtil.isNotEmpty(goodsEsQueryBo.getKeyName())) {
            String keyword = GoodsEsEnum.removeSpecial(goodsEsQueryBo.getKeyName());
            String key = "*" + keyword + "*";

            boolQueryBuilder.filter(QueryBuilders.wildcardQuery("keyNamesRemoveSpecial", key)); // 规格,模糊匹配，不分词
        }

        // 是否查询牌价为0的商品
        if (goodsEsQueryBo.getIsPrice() != null) {
            // 0: 表示不查询牌价为0的商品
            if (0 == goodsEsQueryBo.getIsPrice()) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(GoodsEsEnum.PRICE_MIN.getValue()).gt(0));// 过滤牌价为0的数据
            }
        }
        //店铺
        if (goodsEsQueryBo.getStoreId() != null && goodsEsQueryBo.getStoreId() != 0) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.STORE_ID.getValue(), goodsEsQueryBo.getStoreId()));
        }
        if (goodsEsQueryBo.getIsStoreRecommend() != null) {//店铺推荐
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.IS_STORE_RECOMMEND.getValue(), goodsEsQueryBo.getIsStoreRecommend()));
        }

        if (goodsEsQueryBo.getSellerCompanyId() != null) {//销售方
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.SELLER_COMPANY_ID.getValue(), goodsEsQueryBo.getSellerCompanyId()));
        }

        if (goodsEsQueryBo.getIsNew() != null) {//是否新品
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.IS_NEW.getValue(), goodsEsQueryBo.getIsNew()));
        }

        if (goodsEsQueryBo.getIsRecommend() != null) {//是否推荐
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.IS_RECOMMEND.getValue(), goodsEsQueryBo.getIsRecommend()));
        }

        if (goodsEsQueryBo.getType() != null) {//商品类型
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.TYPE.getValue(), goodsEsQueryBo.getType()));
        }

        if (StrUtil.isNotEmpty(goodsEsQueryBo.getDeliveryTime())) {//货期
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.DELIVERY_TIME.getValue(), goodsEsQueryBo.getDeliveryTime()));
        }

        if (goodsEsQueryBo.getIsFreeShipping() != null) {//是否包邮(0-否 1-是)
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.IS_FREE_SHIPPING.getValue(), goodsEsQueryBo.getIsFreeShipping()));
        }

        //价格过虑
        if (goodsEsQueryBo.getMinPrice() != null) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery(GoodsEsEnum.PRICE_MIN.getValue()).gte(goodsEsQueryBo.getMinPrice()));
        }
        if (goodsEsQueryBo.getMaxPrice() != null) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery(GoodsEsEnum.PRICE_MAX.getValue()).lte(goodsEsQueryBo.getMaxPrice()));
        }

        // 市
        if (StrUtil.isNotEmpty(goodsEsQueryBo.getCity())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(GoodsEsEnum.CITY.getValue(), goodsEsQueryBo.getCity()));
        }

        return boolQueryBuilder;
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
