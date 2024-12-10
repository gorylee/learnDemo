package com.example.esdemo.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品实体
 */
@TableName(value = "gd_goods")
@Data
public class Goods implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;//商品ID
	private String title;//商品名称
	private String name;//别名
	private String description;//描述简介
	private String cas;//cas登记号
	private String formula;//分子式
	private Integer type;//商品类型(0:普通商品,1:易制爆,2:易制毒,3:易制毒+易制爆)

    @Deprecated
    private Long goodsCateIdOne;      // 前台1级分类ID
    @Deprecated
    private String goodsCateNameOne;  // 前台1级分类名称
    @Deprecated
    private Long goodsCateIdTwo;      // 前台2级分类ID
    @Deprecated
    private String goodsCateNameTwo;  // 前台2级分类名称
    @Deprecated
    private Long goodsCateIdThree;    // 前台3级分类ID
    @Deprecated
    private String goodsCateNameThree;// 前台3级分类名称
    @Deprecated
    private Long goodsCateIdLast;//前台末级分类ID
    @Deprecated
    private String goodsCateNameLast;//前台末级分类名称
    @Deprecated
    private String goodsCateIdPath;//前台分类

    private Long storeCateIdOne;  // 店铺1级分类ID(新)
    private Long storeCateIdTwo;  // 店铺2级分类ID(新)
    private Long storeCateIdThree;// 店铺3级分类ID(新)

	private Long oneBcateId;//店铺1级分类ID
	private Long twoBcateId;//店铺2级分类ID
	private Long threeBcateId;//店铺3级分类ID
	private Long oneCateId;//后台1级分类ID
	private String oneCateName;//后台1级分类名称
	private Long twoCateId;//后台2级分类ID
	private String twoCateName;//后台2级分类名称
	private Long threeCateId;//后台3级分类ID
	private String threeCateName;//后台3级分类名称
    private Long lastCateId;//后台末级分类ID
    private String lastCateName;//后台末级分类名称
    private String cateIdPath;//后台分类
    private Long unitId;//单位ID
    private String unitName;//单位名称
    private BigDecimal discountRate;//红包使用比率
    private BigDecimal couponRate;//优惠券最高使用比率
	private Long brandId;//品牌ID
	private String brandName;//品牌名称
	private String supplier;//供应商
//	private String deliveryTime;//货期
	private Integer discounts;//客户等级优惠(0:不享受,1:折扣优惠,2:立减优惠)
	private Integer specType;//规格类型(0:单规格,1:多规格)
	private String attrIds;//规格(属性)id
	private BigDecimal priceMax;//最高价
	private BigDecimal priceMin;//最低价
	private BigDecimal storeCount;//库存量
	private BigDecimal saleCount;//总销售量
	private Integer status;//状态(10:待发布,20:待上架,30:已上架,40:已下架)
	private Long creatorId;//创建人id
	private String creatorName;//创建人名称
	private Date createTime;//创建时间
	private Long modifierId;//修改人id
	private String modifierName;//修改人名称
	private Date modifyTime;//修改时间
    private Date statusModifyTime;//状态修改时间
    private String keyNames;//sku所有规格名
    private Integer deleted;//是否删除：正常 0 已删除 1  已回收 2
    private String imgUrl;//图片
    private Long sellerCompanyId;//销售方id
    private Long storeId;//店铺id
    private String storeName;//店铺名称
    private Integer storeType;//店铺类型
    private Integer storeTypeSort;// 店铺类型排序
    private String xlsCode;//商品编号，xls批量导入使用
    private Long sourceId;//商品来源ID （入驻店铺的商品）

    private Integer isStoreRecommend;//店铺推荐
    private Integer storeRecommendSort;//店铺推荐排序
    private Integer isNew;//是否新品
    private Integer isRecommend;//是否推荐
    private Integer recommendSort;//推荐/新品排序
    @Deprecated
    private Integer cateType;
    private String cateTypeId;//前台分类类型
    private Integer ediStoreRecommend;//edi店铺推荐排序

    //样板商品字段
    private Integer isSample;//是否是样板商品
    private Integer pointDeductionStatus;//积分抵扣(1-可用 -1-不可用)
    private Integer isFreeShipping;//是否包邮(0-否 1-是)
    private Integer saleType;//销售方式
    private String brandNo;//原品牌货号
    private String memo;//备注
    private Integer isViolate;//下架类型：1-违规下架 2-审核下架 3-商家下架
    private BigDecimal goodsRate;//商品税率
    private Date newApplyTime;//最新提交审核时间
    private Date checkPassTime;//审核通过时间
    private Date recoverTime;//回收时间
    private String forinvname;//外名名称
    private Date syncTime;
    private String pkInvcl;//存货分类主键
    private String invclasscode;//分类编码
    private String invclassname;//分类名称
    private BigDecimal virtualPriceMin;//最低虚拟牌价


    private Integer ctrlArea;//管控区域
    private Integer isPhenixin;
    private Long priceRuleId;//样品定价规则id
    private Long cityId;//城市id
    private String city;//城市名称

    private Integer isDangerous;// 是否危险品：0-否 1-是
    private Integer isDisplay;  // 0显示，1隐藏，用于在查询中动态屏蔽商品，而非下架。

    private String prodline; //产品线
    private String def15;//产品线属性

    private String pkTaxitemsName;

    private Long relateGoodsId;



}
