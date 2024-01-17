// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.statistic.pojo;

import java.io.Serializable;

public class SimpleStatistic<T extends Serializable> extends Statistic<T> {

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
