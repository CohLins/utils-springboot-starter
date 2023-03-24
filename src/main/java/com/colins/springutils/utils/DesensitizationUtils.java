package com.colins.springutils.utils;


import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.colins.springutils.annotation.DataDesensitizationMode;
import com.colins.springutils.desensitization.DesensitizationRule;
import com.colins.springutils.desensitization.impl.CharReplaceRule;
import com.colins.springutils.desensitization.impl.RegularReplaceRule;
import com.colins.springutils.enums.DesensitizationTypeEnum;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class DesensitizationUtils {
    private static final Logger log = LoggerFactory.getLogger(DesensitizationUtils.class);
    private static final char DEFAULT_REPLACE_CHAR_SYMBOL = '*';
    private static final int VOID_START_INDEX = -1;

    private DesensitizationUtils() {
    }

    public static String desensitize(DesensitizationTypeEnum desensitizationType, String source) {
        if (desensitizationType == null) {
            return source;
        } else {
            DesensitizationRule ruleInfo = desensitizationType.getRuleInfo();
            if (ruleInfo instanceof CharReplaceRule) {
                return desensitize((CharReplaceRule) ruleInfo, source);
            } else {
                return ruleInfo instanceof RegularReplaceRule ? desensitize((RegularReplaceRule) ruleInfo, source) : source;
            }
        }
    }

    public static String desensitize(CharReplaceRule charReplaceRule, String source) {
        if (StringUtil.isBlank(source)) {
            return "";
        } else {
            int startInclude = charReplaceRule.getStartInclude();
            int endExclude = charReplaceRule.getEndExclude() == 0 ? source.length() : charReplaceRule.getEndExclude();
            char replacedChar = charReplaceRule.getReplacedChar() == 0 ? 42 : charReplaceRule.getReplacedChar();
            return CharSequenceUtil.replace(source, startInclude, endExclude, replacedChar);
        }
    }

    public static String desensitize(RegularReplaceRule regularReplaceRule, String source) {
        if (StringUtil.isBlank(source)) {
            return "";
        } else {
            return CharSequenceUtil.replace(source, regularReplaceRule.getRegex(), regularReplaceRule.getReplaceFunction()::apply);
        }
    }

    public static <T> T desensitize(T source) {
        process(source);
        return source;
    }

    private static void process(Object source) {
        if (source != null) {
            Class clazz = source.getClass();
            if (!ClassUtil.isJdkClass(clazz)) {
                Field[] fields = ReflectUtil.getFields(clazz);
                Field[] var3 = fields;
                int var4 = fields.length;

                for (int var5 = 0; var5 < var4; ++var5) {
                    Field field = var3[var5];
                    Class<?> fieldType = field.getType();
                    Object value = null;
                    if (!ClassUtil.isAssignable(CharSequence.class, fieldType)) {
                        value = ReflectUtil.getFieldValue(source, field);
                        if (value != null) {
                            fieldType = value.getClass();
                        }
                    }

                    if (!ClassUtil.isAssignable(Map.class, fieldType)) {
                        if (ClassUtil.isAssignable(Collection.class, fieldType)) {
                            Collection<Object> fieldValue = (Collection) value;
                            if (fieldValue != null) {
                                Iterator var15 = fieldValue.iterator();

                                while (var15.hasNext()) {
                                    Object element = var15.next();
                                    process(element);
                                }
                            }
                        } else if (fieldType.isArray()) {
                            Object[] fieldValue = (Object[]) ((Object[]) value);
                            if (fieldValue != null) {
                                Object[] var10 = fieldValue;
                                int var11 = fieldValue.length;

                                for (int var12 = 0; var12 < var11; ++var12) {
                                    Object element = var10[var12];
                                    process(element);
                                }
                            }
                        } else if (ClassUtil.isAssignable(CharSequence.class, fieldType)) {
                            setFieldValue(source, field);
                        } else {
                            process(value);
                        }
                    }
                }

            }
        }
    }

    private static void setFieldValue(Object source, Field field) {
        DataDesensitizationMode fieldAnnotation = field.getAnnotation(DataDesensitizationMode.class);

        if (fieldAnnotation != null) {
            if (fieldAnnotation.typeEnum() == DesensitizationTypeEnum.ORDER) {
                // 优先走正则
                if (fieldAnnotation.regex()!=null){
                    RegularReplaceRule regularReplaceRule = new RegularReplaceRule(fieldAnnotation.regex(), matcher -> {
                        return fieldAnnotation.replacedChar();
                    });
                    ReflectUtil.setFieldValue(source, field, desensitize(regularReplaceRule, (String) ReflectUtil.getFieldValue(source, field)));
                }
                if (fieldAnnotation.startInclude() > VOID_START_INDEX) {
                    CharReplaceRule charReplaceRule = new CharReplaceRule(fieldAnnotation.startInclude(), fieldAnnotation.endExclude(), DEFAULT_REPLACE_CHAR_SYMBOL);
                    ReflectUtil.setFieldValue(source, field, desensitize(charReplaceRule, (String) ReflectUtil.getFieldValue(source, field)));
                }
            } else {
                ReflectUtil.setFieldValue(source, field, desensitize(fieldAnnotation.typeEnum(), (String) ReflectUtil.getFieldValue(source, field)));
            }
        }
    }
}
