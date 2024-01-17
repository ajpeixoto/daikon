// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.converter;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.StringUtils;

public class BooleanConverter extends Converter<Boolean> {

    @Override
    public Boolean convert(Object value) {
        if (value == null) {
            return returnDefaultValue();
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue() != 0.0;
        } else if (value instanceof CharSequence) {
            if (StringUtils.isBlank((CharSequence) value)) {
                return returnDefaultValue();
            } else {
                return Boolean.parseBoolean(value.toString());
            }
        } else if (value instanceof ByteBuffer) {
            ByteBuffer bb = ((ByteBuffer) value).slice();
            return bb.limit() != 0 && bb.get() != 0;
        } else {
            return Boolean.valueOf(value.toString());
        }
    }
}
