// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro;

import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;

/**
 * Utility class for avro {@link LogicalType}
 */
public final class LogicalTypeUtils {

    public static final String DATE = "date";

    public static final String TIME_MILLIS = "time-millis";

    public static final String TIME_MICROS = "time-micros";

    public static final String TIMESTAMP_MILLIS = "timestamp-millis";

    public static final String TIMESTAMP_MICROS = "timestamp-micros";

    private LogicalTypeUtils() {
        // Class provides static utility methods and shouldn't be instantiated
    }

    /**
     * Checks whether specified schema has logical timestamp-millis type.
     * Its type should be LONG and
     * Its logical type should be "timestamp-millis"
     * 
     * @param schema avro schema
     * @return true, if schema has logical timestamp-millis type
     */
    public static boolean isLogicalTimestampMillis(Schema schema) {
        LogicalType logicalType = schema.getLogicalType();
        return Type.LONG == schema.getType() && LogicalTypes.timestampMillis().equals(logicalType);
    }

    /**
     * Checks whether specified schema has logical timestamp-micros type.
     * Its type should be LONG and
     * Its logical type should be "timestamp-micros"
     * 
     * @param schema avro schema
     * @return true, if schema has logical timestamp-micros type
     */
    public static boolean isLogicalTimestampMicros(Schema schema) {
        LogicalType logicalType = schema.getLogicalType();
        return Type.LONG == schema.getType() && LogicalTypes.timestampMicros().equals(logicalType);
    }

    /**
     * Checks whether specified schema has logical date type
     * Its type should be INT and
     * Its logical type should be "date"
     * 
     * @param schema avro schema
     * @return true, if schema has logical date type
     */
    public static boolean isLogicalDate(Schema schema) {
        LogicalType logicalType = schema.getLogicalType();
        return Type.INT == schema.getType() && LogicalTypes.date().equals(logicalType);
    }

    /**
     * Checks whether specified schema has logical time-millis type
     * It should have type INT and logical type "time-millis"
     * 
     * @param schema avro schema
     * @return true, if schema has logical time-millis type
     */
    public static boolean isLogicalTimeMillis(Schema schema) {
        LogicalType logicalType = schema.getLogicalType();
        return Type.INT == schema.getType() && LogicalTypes.timeMillis().equals(logicalType);
    }

    /**
     * Checks whether specified schema has logical time-micros type
     * It should have type LONG and logical type "time-micros"
     * 
     * @param schema avro schema
     * @return true, if schema has logical time-micros type
     */
    public static boolean isLogicalTimeMicros(Schema schema) {
        LogicalType logicalType = schema.getLogicalType();
        return Type.LONG == schema.getType() && LogicalTypes.timeMicros().equals(logicalType);
    }

    /**
     * Returns name of schema logical type or null, if schema has no logical type
     * 
     * @param schema avro schema
     * @return logical type name
     */
    public static String getLogicalTypeName(Schema schema) {
        LogicalType logicalType = schema.getLogicalType();
        if (logicalType == null) {
            return null;
        } else {
            return logicalType.getName();
        }
    }

    /**
     * Returns schema of specified logical type
     * Returns <code>null</code>, if <code>null</code> is logical type is passed
     * 
     * @param logicalType name of Avro logical type
     * @return schema of specified logical type
     * @throws {@link UnsupportedOperationException} if not supported logical type is passed
     */
    public static Schema getSchemaByLogicalType(String logicalType) {
        if (logicalType == null) {
            return null;
        }
        switch (logicalType) {
        case DATE: {
            return AvroUtils._logicalDate();
        }
        case TIME_MILLIS: {
            return AvroUtils._logicalTime();
        }
        case TIME_MICROS: {
            return AvroUtils._logicalTimeMicros();
        }
        case TIMESTAMP_MILLIS: {
            return AvroUtils._logicalTimestamp();
        }
        case TIMESTAMP_MICROS: {
            return AvroUtils._logicalTimestampMicros();
        }
        default: {
            throw new UnsupportedOperationException("Unrecognized type " + logicalType);
        }
        }
    }

}
