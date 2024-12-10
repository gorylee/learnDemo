package com.example.esdemo.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GoodsSkuModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;//ID
    private Long goodsId;//商品ID
    private String name; //商品别名，从es获取值设置到goodsName
    private String goodsName;//商品别名
    private String keyId;//规格属性值id
    private String keyName;//规格属性值
    private Long unitId;//单位ID
    private String cas;//cas登记号
    private String unitName;//单位名称
    private BigDecimal storeCount;//库存量
    private String storeCode;//库存编号
    private BigDecimal limitMin;//最小起订量
    private BigDecimal price;//牌价
    private BigDecimal directorPrice;//总监价
    private BigDecimal directorExclusivePrice;//总监专享价
    private BigDecimal factoryPrice;//出厂价
    private BigDecimal netWeight;//净重
    private BigDecimal grossWeight;//毛重
    private String cateIdPath;//前台分类
    private Long brandId;
    private String brandName;
    private String imgUrl;
    private String prodline; //产品线
    private String def15;//产品线属性
    /**
     * sku主图
     */
    private String skuImgUrl;
    private String title;
    private Integer type;
    private String typeName;
    private Integer storeStatus;// 0 未启用 1已启用库存
    private Long warehouseId;//仓库id
    private String warehouseCode;//仓库编号
    private String warehouseName;//仓库名称
    private Long manageId;//产品经理id
    private String manageName;//产品经理
    private Integer cateType;//
    private String cateTypeName;
    private Long companyId;//所属公司id
    private String deliveryTime;//货期

    //样板商品字段
    private BigDecimal maxBuyCount;//最大购买数量
    private Integer isLimitBuy;//是否限购
    private BigDecimal limitBuyCount;//用户限购数量
    private Date limitBuyStartTime;//限购开始日期
    private Date limitBuyEndTime;//限购结束日期
    private String areaIds;//产品负责人片区id
    private List<String> areaList;//产品负责人片区集合


    private Integer isSample;//是否是样板商品
    private Integer pointDeductionStatus;//积分抵扣(1-可用 -1-不可用)
    private Integer isFreeShipping;//是否包邮(0-否 1-是)
    private Long twoCateId;//前台2级分类ID(用于低价审批发送微信消息判断)
    private Integer ctrlArea;//管控区域
    private Integer isPhenixin;//是否四氯化碳
    private Integer saleType;//销售方式
    private String saleTypeName;//销售方式名称
    private Long priceRuleId;//样品定价规则id

    private Integer status;//状态(10:待发布,20:待上架,30:已上架,40:已下架)
    private String statusName;//状态名称(待发布,待上架,已上架,已下架)
    private Integer deleted;
    private Integer unitType;//单位类型
    private String supplier;//供应商
    //后台一级分类id
    private Long oneCateId;
    //后台三级分类id
    private Long threeCateId;
    private String pkTaxitemsName;//NC字段税目税率名称

    private String packUnit; //包装单位

    private BigDecimal virtualPrice;//虚拟单价

    private String oneCateName;


}
