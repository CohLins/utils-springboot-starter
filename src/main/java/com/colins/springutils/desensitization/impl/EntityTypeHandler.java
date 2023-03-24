package com.colins.springutils.desensitization.impl;


import com.colins.springutils.desensitization.DesensitizationTypeHandler;
import com.colins.springutils.utils.DesensitizationUtils;

public class EntityTypeHandler implements DesensitizationTypeHandler {
    @Override
    public Object resultDateHandler(Object data) {
        return data==null ? null: DesensitizationUtils.desensitize(data);
    }
}
