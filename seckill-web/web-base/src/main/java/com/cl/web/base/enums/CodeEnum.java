package com.cl.web.base.enums;

/**
 * @author CarterCL
 * @create 2020/9/29 21:21
 */
public enum CodeEnum implements IEnum {
    SUCCESS("0", "秒杀成功"),
    SELL_OUT("50001", "产品已售光"),
    PARAMS_ERROR("40001","参数有误"),
    NOT_ALLOW("40002", "无权限秒杀该商品"),
    PRODUCT_NOT_FOUND("40003","商品不存在"),
    USER_NOT_FOUND("40004","用户不存在"),
    ALREADY_INVOLVED("40005","已秒杀成功，无法再次秒杀"),
    SYSTEM_ERROR("99999","系统异常")
    ;

    private String code;
    private String message;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    CodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }


}
