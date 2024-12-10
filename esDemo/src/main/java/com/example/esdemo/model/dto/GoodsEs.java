package com.example.esdemo.model.dto;

import com.example.esdemo.model.enums.GoodsEsEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/12/5
 */
@Data
public class GoodsEs implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long goodsId;//商品ID

    private float score;// 匹配度得分

    private Integer vipRecommendType;
    private Integer vipRecommendSort;

    private String title;//商品名称
    private String titleJoin_;//商品名称(去掉特殊符号，然后拆分成：中文 英文 数字，用"_"连接)
    private String titleJoinSpace;//商品名称(去掉特殊符号，然后拆分成：中文 英文 数字，用"空格"连接)

    private String name;//别名
    private String nameJoin_;//别名(去掉特殊符号，然后拆分成：中文 英文 数字，用"_"连接)

    private Long brandId;//品牌ID
    private String brandName;//品牌名称
    private String brandNameJoin_;//品牌名称(去掉特殊符号，然后拆分成：中文 英文 数字，用"空格"连接)

    private String brandNo;//原品牌货号

    private Long storeCateIdOne;  // 店铺1级分类ID(新)
    private Long storeCateIdTwo;  // 店铺2级分类ID(新)
    private Long storeCateIdThree;// 店铺3级分类ID(新)

    private Long oneCateId;//前台1级分类ID
    private String oneCateName;//前台1级分类名称
    private Long twoCateId;//前台2级分类ID
    private String twoCateName;//前台2级分类名称
    private Long threeCateId;//前台3级分类ID
    private String threeCateName;//前台3级分类名称

    private Long oneBcateId;//店铺1级分类ID
    private Long twoBcateId;//店铺2级分类ID
    private Long threeBcateId;//店铺3级分类ID
    private Long lastCateId;//末级分类ID
    private String lastCateName;//末级分类名称
    private String cateIdPath;//前台分类

    private BigDecimal priceMax;//最高价
    private BigDecimal priceMin;//最低价

    private String imgUrl;//图片

    private String cas;//cas登记号
    private String casRemoveSpecial;//CAS号去掉特殊符号，例如：cas:7681-38-1 -> 7681381
    private String formula;//分子式

    private Long storeId;//店铺id
    private String storeName;//店铺名称
    private Integer storeType;//店铺类型
    private Integer storeTypeSort;// 店铺类型排序
    private Integer type;//商品类型(0:普通商品,1:易制爆,2:易制毒,3:易制毒+易制爆)
    private Long unitId;//单位ID
    private String unitName;//单位名称
    private Integer specType;//规格类型(0:单规格,1:多规格)
    private String keyNames;//规格
    private String keyNamesJoin_;//规格(去掉特殊符号，然后拆分成：中文 英文 数字，用"空格"连接)
    private String keyNamesRemoveSpecial;//规格 去掉空格和特殊字符，把"-"替换为"_"（不拆分中文 英文 数字）

    private Integer isSample;//是否是样品商品（0-否 1-是）
    private Integer isFreeShipping;//是否包邮(0-否 1-是)

    private Integer pointDeductionStatus;//积分抵扣(1-可用 -1-不可用)

    private Integer deleted = 0;//已删除 1
    private Integer status;//状态(10:待发布,20:待上架,30:已上架,40:已下架)
    private Date statusModifyTime;//状态修改时间
    private Integer isStoreRecommend;//店铺推荐
    private Integer storeRecommendSort;//店铺推荐排序
    private Integer isNew;//是否新品
    private Integer isRecommend;//是否推荐
    private Integer recommendSort;//推荐/新品排序

    private String supplier;//供应商
    private String deliveryTime;//货期
    private Integer discounts;//客户等级优惠(0:不享受,1:折扣优惠,2:立减优惠)

    private Integer isStoreCount = 0; //是否有库存,0:没有，1:有
    private BigDecimal storeCount = BigDecimal.ZERO;//库存量

    private BigDecimal saleCount;//总销售量
    private Long sellerCompanyId;//销售方id

    private Integer cateType;//业务类型：1-原料 2-试剂
    private Set<String> cateTypeIdSet;//业务类型：1-原料 2-试剂 3-重金属 4-橡塑
    private Integer favourite = 0;//是否喜欢 0:不喜欢，1:喜欢，默认不喜欢
    private Integer isPhenixin;//是否四氯化碳

    private Integer ctrlArea;//管控区域
    private Integer saleType;//销售方式
    private Long priceRuleId;//样品定价规则id
    private Set<String> storeCodeList;//存货编码

    private Long cityId;//城市id
    private String city;//城市

    private String priceText;//商品价格显示说明

    private Integer isDangerous;// 是否危险品：0-否 1-是
    private Integer isDisplay;  // 0显示，1隐藏，用于在查询中动态屏蔽商品，而非下架。
    private Integer searchResult;// 搜索结果
    private Integer searchResultSort;

    private Integer youliaoMall; // 有料商城
    private Integer youliaoMallSort;

    private Integer materialMall;// 原料商城
    private Integer materialMallSort;

    private Integer reagentMall; // 试剂商城
    private Integer reagentMallSort;

    private Integer sampleMall;  // 样品商城
    private Integer sampleMallSort;

    private Integer rubberMall;  // 橡塑商城
    private Integer rubberMallSort;

    public static GoodsEs getNewInstance() {
        GoodsEs goodsEs = new GoodsEs();
        // 消除副作用
        goodsEs.setStoreCount(null);
        goodsEs.setIsStoreCount(null);
        goodsEs.setFavourite(null);
        goodsEs.setDeleted(null);
        return goodsEs;
    }

    public void setCas(String cas) {
        this.cas = cas;
        this.casRemoveSpecial = cas == null ? null : cas.toLowerCase().replaceAll("[\\-_()（）<>《》\\[\\]【】{}?？/\\\\、,，.。;；:：“”'\"|·~`!！￥…@#$%^&*+=]", "");// 给cas2字段赋值
    }

    public void setTitle(String title) {
        this.title = title;
        this.titleJoin_ = GoodsEsEnum.removeSpecialJoin_(title);// 去掉特殊符号，然后拆分成：中文 英文 数字，用"_"连接
        this.titleJoinSpace = GoodsEsEnum.removeSpecialJoinSpace(title);// 去掉特殊符号，然后拆分成：中文 英文 数字，用空格分隔
    }

    public void setKeyNames(String keyNames) {
        this.keyNames = keyNames;
        this.keyNamesJoin_ = GoodsEsEnum.removeSpecialJoin_(keyNames);// 去掉特殊符号，然后拆分成：中文 英文 数字，用"_"连接
        this.keyNamesRemoveSpecial = GoodsEsEnum.removeSpecial(keyNames);// 去掉空格和特殊字符，把"-"替换为"_"（不拆分中文 英文 数字）
    }

    public void setName(String name) {
        this.name = name;
        this.nameJoin_ = GoodsEsEnum.removeSpecialJoin_(name);// 去掉特殊符号，然后拆分成：中文 英文 数字，用"_"连接
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
        this.brandNameJoin_ = GoodsEsEnum.removeSpecialJoin_(brandName);// 去掉特殊符号，然后拆分成：中文 英文 数字，用"_"连接
    }
}
