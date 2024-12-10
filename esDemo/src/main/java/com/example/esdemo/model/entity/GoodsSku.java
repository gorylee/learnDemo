package com.example.esdemo.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品sku实体
 */
@Data
@TableName(value = "gd_goods_sku")
public class GoodsSku implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;//ID
	private Long goodsId;//商品ID
	private String keyId;//规格属性值id
	private String keyName;//规格属性值
	private Long unitId;//单位ID
	private String unitName;//单位名称
    private String deliveryTime;//货期
	private Integer unitType;//销售（单位）类型
	private BigDecimal storeCount;//库存量
	private String storeCode;//库存编号
	private BigDecimal limitMin;//最小起订量
	private BigDecimal price;//牌价
	private BigDecimal directorPrice;//总监价
    private BigDecimal factoryPrice;//出厂价
    private BigDecimal virtualPrice;//虚拟牌价
	private Long creatorId;//创建人id
	private String creatorName;//创建人名称
	private Date createTime;//创建时间
	private Long modifierId;//修改人id
	private String modifierName;//修改人名称
	private Date modifyTime;//修改时间

    private Integer storeStatus;// 0 未启用 1已启用库存
    private Long warehouseId;//仓库id
    private String warehouseCode;//仓库编号
    private String warehouseName;//仓库名称


    private BigDecimal netWeight;//净重
    private BigDecimal grossWeight;//毛重
    private Date adjustPriceTime;//最后调价时间
    private Integer deleted;//是否删除：正常 0 已删除 1  已回收 2
    private BigDecimal maxBuyCount;//最大购买数量
    private Integer isOrderNotify;//是否推送订单到商品中心：0-否  1-是
    private Long oneSpecId;//规格属性id1
    private String oneSpecName;//规格属性值1
    private Long twoSpecId;//规格属性id2
    private String twoSpecName;//规格属性值2
    private Long threeSpecId;//规格属性id3
    private String threeSpecName;//规格属性值3
    private BigDecimal quantity;//单位数量
    private Integer saleType;//销售方式
    /**
     * sku主图
     */
    private String skuImgUrl;
    private Date recoverTime;//回收时间

    private BigDecimal priceMax;//最高价
    private BigDecimal priceMin;//最低价
    private Integer isDefault;// 是否默认显示，0:否，1:是

    private String packUnit;//包装单位
    private Integer isAutoUpdateVirtualPrice;// 是否自动更新虚拟牌价，0:否，1:是
    private Long companyId;//公司id，调价影响公司的时候用
}
