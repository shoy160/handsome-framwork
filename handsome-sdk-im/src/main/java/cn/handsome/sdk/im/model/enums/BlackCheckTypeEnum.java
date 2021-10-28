package cn.handsome.sdk.im.model.enums;

/**
 * @author shoy
 * @date 2021/6/19
 */
public enum BlackCheckTypeEnum {
    /**
     * 只会检查 From_Account 的黑名单中是否有 To_Account
     */
    BlackCheckResult_Type_Single,
    /**
     * 双向检测
     */
    BlackCheckResult_Type_Both
}
