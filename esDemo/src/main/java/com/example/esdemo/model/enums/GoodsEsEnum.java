package com.example.esdemo.model.enums;

/**
 * 搜索引擎goods索引下的字段名
 */
public enum GoodsEsEnum {

    ID("id"),
    GOODS_ID("goodsId"),

    TITLE("title"),//商品标题
    TITLE_JOIN_("titleJoin_"),//商品标题(去掉特殊符号，然后拆分成：中文 英文 数字，用"_"连接)
    TITLE_JOIN_SPACE("titleJoinSpace"),//商品标题(去掉特殊符号，然后拆分成：中文 英文 数字，用空格连接)

    NAME("name"),  //别名
    NAME_JOIN_("nameJoin_"),//商品别名(去掉特殊符号，然后拆分成：中文 英文 数字，用"_"连接)

    CAS("cas"),    //CAS号
    CAS_REMOVE_SPECIAL("casRemoveSpecial"),

    KEY_NAMES("keyNames"), //规格
    KEY_NAMES_JOIN_("keyNamesJoin_"), //规格(去掉特殊符号，然后拆分成：中文 英文 数字，用"_"连接)

    TYPE("type"),  //商品类型(0:普通商品,1:易制爆,2:易制毒,3:易制毒+易制爆,4:普通危险品)
    CATE_TYPE("cateType"),
    CATE_TYPE_ID_SET("cateTypeIdSet"),
    SOURCE_ID("sourceId"),// 商品来源ID （入驻店铺的商品）

    ONE_BCATE_ID("oneBcateId"), //店铺1级分类ID
    TWO_BCATE_ID("twoBcateId"), //店铺2级分类ID
    THREE_BCATE_ID("threeBcateId"),//店铺3级分类ID

    GOODS_CATE_RELATION_LIST("goodsCateRelationList"),
    GOODS_CATE_ID_ONE("goodsCateRelationList.goodsCateIdOne"),    // 新前台一级分类
    GOODS_CATE_ID_TWO("goodsCateRelationList.goodsCateIdTwo"),    // 新前台二级分类
    GOODS_CATE_ID_THREE("goodsCateRelationList.goodsCateIdThree"),// 新前台三级分类
    GOODS_CATE_ID_LAST("goodsCateRelationList.goodsCateIdLast"),  // 新前台末级分类
    GOODS_CATE_ID_PATH("goodsCateRelationList.goodsCateIdPath"),  // 新前分类路径

    ONE_CATE_ID("oneCateId"),//一级分类
    TWO_CATE_ID("twoCateId"),//二级分类
    THREE_CATE_ID("threeCateId"),//三级分类
    LAST_CATE_ID("lastCateId"),//末级分类

    STORE_ID("storeId"),//店铺id
    STORE_NAME("storeName"),//店铺名称
    BRAND_ID("brandId"),//品牌
    PROD_LINE("prodline"),//产品线
    BRAND_NAME("brandName"),//品牌名称
    BRAND_NAME_JOIN_("brandNameJoin_"),//品牌名称 (去掉特殊符号，然后拆分成：中文 英文 数字，用"_"连接)

    DELIVERY_TIME("deliveryTime"),//货期
    PRICE_MIN("priceMin"),//价格
    PRICE_MAX("priceMax"),//价格

    PRICE("price"),// 牌价
    FACTORY_PRICE("factoryPrice"), //出厂价

    IS_STORE_COUNT("isStoreCount"),//是否有库存, 0:无，1:有
    STORE_COUNT("storeCount"),//库存量
    SALE_COUNT("saleCount"),  //总销售量
    STORE_TYPE("storeType"),  //店铺类型
    STORE_TYPE_SORT("storeTypeSort"),  //店铺类型排序
    STORE_RECOMMEND_SORT("storeRecommendSort"),//店铺推荐排序
    IS_STORE_RECOMMEND("isStoreRecommend"),    //店铺推荐

    EDI_STORE_RECOMMEND("ediStoreRecommend"),

    STATUS_MODIFY_TIME("statusModifyTime"),
    STATUS("status"),  //状态
    DELETED("deleted"),//删除

    SELLER_COMPANY_ID("sellerCompanyId"),//销售方id
    IS_NEW("isNew"),//是否新品

    VIP_RECOMMEND_TYPE("vipRecommendType"),

    /**
     * 会员收费排序，满足搜索条件时必须保证排最前面。
     *  1：会员收费排序商品
     *  0：普通商品
     */
    VIP_RECOMMEND_SORT("vipRecommendSort"),

    IS_RECOMMEND("isRecommend"),//是否推荐
    RECOMMEND_SORT("recommendSort"),//推荐/新品排序
    IS_SAMPLE("isSample"),//是否是样板商品

    ATTR_LIST_ID("attrList.attrId"), //商品属性: 产地
    ATTR_LIST_VALUE("attrList.value"), //商品属性: 含量
    ATTR_LIST_VALUE_REMOVE_SPECIAL("attrList.valueRemoveSpecial"), //属性值 去掉空格和特殊字符，把"-"替换为"_"（不拆分中文 英文 数字）

    STORE_CODE_LIST("storeCodeList"), //库存编码
    STORE_CODE("storeCode"), //库存编码
    STORE_CODE_REMOVE_SPECIAL("storeCodeRemoveSpecial"), //库存编码 去掉空格和特殊字符，把"-"替换为"_"（不拆分中文 英文 数字）

    STOCK_COUNT("stockCount"),//gd_goods_stock表的库存量

    CITY_ID("cityId"),// 城市id
    CITY("city"),     // 城市名称

    IS_FREE_SHIPPING("isFreeShipping"),//是否包邮(0-否 1-是)

    INVCLASSCODE("invclasscode"), //NC商品分类编码
    DEF15("def15"), //产品线属性
    PACK_UNIT("packUnit"), //包装单位

    SALESMAN_NUM_SORT("salesmanNumSort"),  //业务员购买数排序
    CUSTOMER_NUM_SORT("customerNumSort"),  //客户购买数排序
    ON_HAND_NUM("onHandNum"),  // NC现存量
    ;

    private final String value;

    public static final Long GOODS_ATTR_PLACE = 6L; // 商品属性：产地
    public static final Long GOODS_ATTR_MODEL = 8L; // 商品属性：型号
    public static final String CATE_TYPE_ID_SPLIT = "、"; // 分类类型的分隔符

    GoodsEsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String subKeyword(String keyword) {
        if (keyword.length() > 45) {
            keyword = keyword.substring(0, 45);
        }
        return keyword;
    }

    /**
     * 去掉空格和特殊字符，转换成小写，把"-"替换为"_"（不拆分中文 英文 数字）
     */
    public static String removeSpecial(String keyword) {
        if (keyword == null || keyword.length() == 0) {
            return keyword;
        }
        return keyword.replaceAll("(\\\\t|\\\\r|\\\\n)", " ") // 制表符、换行符 替换为空格
                .replaceAll("[()（）<>《》\\[\\]【】{}?？/\\\\、,，.。;；:：“”'\"|·~`!！￥…@#$%^&*+=]+", "")
                .replaceAll("[\\-]+", "_")
                .trim().replaceAll("\\s", "").toLowerCase();
    }

    /**
     * 去掉特殊符号，转换成小写，再拆分成：中文 英文 数字，用英文"_"连接起来
     */
    public static String removeSpecialJoin_(String keyword) {
        if (keyword == null || keyword.length() == 0) {
            return keyword;
        }
        return keyword.replaceAll("(\\\\t|\\\\r|\\\\n)", " ") // 制表符、换行符 替换为空格
                .replaceAll("[()（）<>《》\\[\\]【】{}?？/\\\\、,，.。;；:：“”'\"|·~`!！￥…@#$%^&*+=]+", "")
                .replaceAll("(.*?)(\\d+)", "$1 $2")
                .replaceAll("(\\d+)(.*?)", "$1 $2")
                .replaceAll("([\\u4e00-\\u9fa5]+)([a-zA-Z]+)", "$1 $2")
                .replaceAll("([a-zA-Z]+)([\\u4e00-\\u9fa5]*)", "$1 $2")
                .trim().replaceAll("[\\-\\s]+", "_").toLowerCase();
    }

    /**
     * 去掉特殊符号，转换成小写，再拆分成：中文 英文 数字，用空格连接起来
     */
    public static String removeSpecialJoinSpace(String keyword) {
        if (keyword == null || keyword.length() == 0) {
            return keyword;
        }
        return keyword.replaceAll("(\\\\t|\\\\r|\\\\n)", " ") // 制表符、换行符 替换为空格
                .replaceAll("[()（）<>《》\\[\\]【】{}?？/\\\\、,，.。;；:：“”'\"|·~`!！￥…@#$%^&*+=]+", "")
                .replaceAll("(.*?)(\\d+)", "$1 $2")
                .replaceAll("(\\d+)(.*?)", "$1 $2")
                .replaceAll("([\\u4e00-\\u9fa5]+)([a-zA-Z]+)", "$1 $2")
                .replaceAll("([a-zA-Z]+)([\\u4e00-\\u9fa5]*)", "$1 $2")
                .replaceAll("\\s?\\-\\s?", "_")  // "xl - 70" -> "xl_70"
                .trim().replaceAll("\\s+", " ").toLowerCase();// 去掉多余的空格
    }

    public static void main(String[] args) {
        String str = "3[()（）<>《》\\[\\]【】{}?？/\\\\、,，.。;；:：“”'\"|·~`!！￥…@#$%^&*+=]+中文aa12|12aa中/\\aa中_112   a";
        System.out.println(removeSpecialJoinSpace("105763-500g"));
        System.out.println(removeSpecialJoin_("105763-500g"));
    }
}
