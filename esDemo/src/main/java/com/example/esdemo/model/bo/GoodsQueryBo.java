package com.example.esdemo.model.bo;

import lombok.Data;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/12/5
 */
@Data
public class GoodsQueryBo {
    private Long id;
    private Long[] ids;
    private String keyWord;//搜索关键词

    private List<Long> goodsIds;// 商品ID 集合
    private String goodsIdsStr; // 多个商品ID，用逗号隔开

    //为了兼容接口调用方，参数名引用原来的oneCateId，但是查询的字段是新的前台分类
    private Long goodsCateIdOne;  // 新前台1分类
    private Long goodsCateIdTwo;  // 新前台2分类
    private Long goodsCateIdThree;// 新前台3分类
    private Long goodsCateIdLast; // 新前台末分类
    private String goodsCateIdPath;//新前台分类
    private List<Long> goodsCateIdLastList;//新前台末级分类 集合
    private String goodsCateIdLastListStr; //前台末级分类ID，用逗号隔开

    private Long oneCateId;//前台1分类
    private Long twoCateId;//前台2分类
    private Long threeCateId;//前台3分类
    private Long lastCateId;//前台末分类

    private String cateIdPath;//前台分类

    private List<Long> lastCateIds;//末级分类 集合

    private Long oneBcateId;//店铺1级分类ID
    private Long twoBcateId;//店铺2级分类ID
    private Long threeBcateId;//店铺3级分类ID

    private String title;//商品名称
    private String name;//别名
    private String keyNames;//sku所有规格名
    private String keyName;//规格属性值,为了兼容西红市

    private Long brandId;//品牌ID
    private Long[] brandIds;//品牌IDs
    private String brandName;//品牌名称

    private String storeCode;//库存编号
    private String deliveryTime;//货期

    private Integer sortType = 0;//排序类型(0:默认 1: 销量 2: 价格)
    private Integer sortRule = 2;//排序规则(1: 升序 2: 降序)

    private Integer[] statusAttr;//状态(0 全部,10:待发布,20:待上架,30:已上架,40:已下架)
    private BigDecimal minPrice;//最低价
    private BigDecimal maxPrice;//最高价
    private Long sellerCompanyId;//销售方id

    @Min(value = 1, message = "pageNo不能小于1")
    private long pageNo = 1L;

    @Min(value = 1, message = "pageSize不能小于1")
    private long pageSize = 10L;

    private Long storeId;//店铺id
    private long currentUserId;//当前登录用户id

    private Integer cateType; //业务类型：1-原料 2-试剂
    private Integer type;//商品类型(0:普通商品,1:易制爆,2:易制毒,3:易制毒+易制爆)

    private String sortField;//排序字段
    private Integer ediStoreRecommend;//店铺推荐：现货大厅、样品商城的商品列表首页需要包含至少5家店铺的商品
    private Integer isStoreRecommend;//店铺推荐
    private Integer storeRecommendSort;//店铺推荐排序
    private Integer isRecommend;//是否推荐
    private Integer recommendSort;//推荐/新品排序

    private Integer isNew;//是否新品
    private Integer isSample;//是否样品
    private Integer isFreeShipping;//是否包邮(0-否 1-是)

    private Integer reopen;//是否冲单 1：是 0：否

    private String attrListStr;// 商品属性：attrListStr: {"attrOrigin":{"attrId":0,"value":""},"attrNetContent":{"attrId":7,"value":"12.5%"},"attrModel":{"attrId":8,"value":"工业级,食品级"}}

    private Integer isPrice;// 是否查看牌价为0的商品。0:否，表示不查询牌价为0的商品(牌价为0的商品不显示)。 1:是，表示查看牌价牌价为0的商品，牌价为0的商品也要显示在商品列表

    private Long sourceId;//商品来源ID （入驻店铺的商品）

    private Long manageId;//产品线经理id

    private Integer isMyLimitBuy;//是否我设置限购
    private Integer isLimitBuy;//是否限购
    private Integer isLimitTime;//是否限购日期判断

    private Long cityId;//城市id
    private String city;//城市

    private Integer vipRecommendType;
}
