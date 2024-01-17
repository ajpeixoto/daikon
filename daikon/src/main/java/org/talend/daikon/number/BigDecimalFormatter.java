// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Format a {@link BigDecimal} instance as a {@link String}.
 */
public class BigDecimalFormatter {

    private BigDecimalFormatter() {
    }

    public static String format(BigDecimal bd, DecimalFormat format) {
        return format.format(bd).trim();
    }

}
