// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.converter;

import java.util.Date;

public class DateConverter extends Converter<Date> {

    @Override
    public Date convert(Object value) {
        throw new RuntimeException("We do not support date pattern yet.");
    }
}
