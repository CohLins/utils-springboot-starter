package com.colins.springutils.enums;


import com.colins.springutils.desensitization.DesensitizationRule;
import com.colins.springutils.desensitization.impl.CharReplaceRule;
import com.colins.springutils.desensitization.impl.RegularReplaceRule;

public enum DesensitizationTypeEnum {
    USER_ID(new RegularReplaceRule(".*(....$)", (matcher) -> {
        return "********$1";
    }), "账号"),
    CHINESE_NAME(new CharReplaceRule(1), "中文名"),
    ID_CARD(new RegularReplaceRule("^([1-9][0-9]{5})([0-9])*([0-9]{4}$)", (matcher) -> {
        return "$1********$3";
    }), "身份证号"),
    FIXED_PHONE(new RegularReplaceRule("^(0[0-9]{2,3}-?)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$", (matcher) -> {
        return "$1******$3";
    }), "座机号"),
    MOBILE_PHONE(new CharReplaceRule(3, 8), "手机号"),
    ADDRESS(new RegularReplaceRule("([^省]+省|.+自治区|[^澳门]+澳门|[^香港]+香港|[^市]+市)?([^自治州]+自治州|[^特别行政区]+特别行政区|[^市]+市|.*?地区|.*?行政单位|.+盟|市辖区|[^县]+县)([^县]+县|[^市]+市|[^镇]+镇|[^区]+区|[^乡]+乡|.+场|.+旗|.+海域|.+岛)?(.*)", (matcher) -> {
        return "$1$2$3*************";
    }), "地址"),
    EMAIL(new RegularReplaceRule("(\\w{1,3})?[^@]*(@).*(\\..*$)", (matcher) -> {
        return "$1***$2***$3";
    }), "电子邮件"),
    PASSWORD(new CharReplaceRule(0), "密码"),
    CAR_LICENSE(new CharReplaceRule(1), "中国大陆车牌，包含普通车辆、新能源车辆"),
    BANK_CARD(new RegularReplaceRule("^([1-9][0-9]{5})([0-9])*([0-9]{4}$)", (matcher) -> {
        return "$1**********$3";
    }), "银行卡"),
    ORDER(null,"其他");

    private DesensitizationRule ruleInfo;
    private String remark;

    private DesensitizationTypeEnum(DesensitizationRule ruleInfo, String remark) {
        this.ruleInfo = ruleInfo;
        this.remark = remark;
    }

    public DesensitizationRule getRuleInfo() {
        return this.ruleInfo;
    }

    public String getRemark() {
        return this.remark;
    }
}
