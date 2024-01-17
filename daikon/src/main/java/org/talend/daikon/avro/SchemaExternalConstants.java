// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro;

/**
 * Constants and enums used for external avro schema custom properties.
 * <p/>
 * When an avro schema is built from external metadata or used to read/write
 * from non-avro sources, avro schema primitives may not be enough to represent
 * the external metadata semantics.
 * <p/>
 * The properties described here are intended for those systems that need to
 * manipulate such external data. All other systems can safely ignore them.
 *
 */
public final class SchemaExternalConstants {

    /**
     * This utility class is best used as a static import.
     */
    private SchemaExternalConstants() {
    }

    // -------------------------------------------------------------------------
    // General purpose properties
    // -------------------------------------------------------------------------
    /**
     * Element name in source/target system if it differs from the avro name.
     * <p/>
     * Avro schema type level property for "type": "record" and "type": "enum"
     * and Field level for all types.
     * <p/>
     * Optional. Only needed when original name contains characters that avro
     * does not support (These characters are usually replaced with underscore
     * to form a valid Avro name).
     */
    public static final String ORIGINAL_NAME = "talend.original.name";

    /**
     * For a string and other bounded simple types, indicates the maximum number
     * of characters.
     * <p/>
     * Avro schema type level property for "type": "string" and "type": "enum".
     * <p/>
     * Optional. By default, strings are unbounded.
     */
    public static final String MAX_LENGTH = "talend.max.len";

    /**
     * For an enum, provides a description for each of the symbols.
     * <p/>
     * labels are separated by the pipe character. labels are is the exact order
     * of symbols.
     * <p/>
     * Avro schema type level for "type": "enum".
     * <p/>
     * Optional. By default, the description is the symbol itself.
     */
    public static final String ENUM_LABELS = "talend.enum.labels";

    /**
     * Sequence number of a field within its parent record.
     * <p/>
     * This is useful when there is some reason the original sequence numbering
     * was not preserved in the avro schema.
     * <p/>
     * Avro schema field level.
     * <p/>
     * Optional. By default, sequence number is the order of the field within
     * its parent.
     */
    public static final String FIELD_SEQNO = "talend.field.seqno";

    /**
     * For a bounded array, indicates the upper bound if any.
     * <p/>
     * Avro schema field level. Field must be of "type": "array".
     * <p/>
     * Optional. Arrays in avro are always unbounded. if there is an upper
     * bound, we use this extra property.
     */
    public static final String MAX_OCCURS = "talend.max.occurs";

    /**
     * In the original system, invisible elements do not appear but their
     * children do. Usually needed when elements need to be grouped but the
     * group itself must not be materialized (and is therefore invisible). When
     * such an element is materialized in the Avro schema, this custom property
     * preserves the "invisible" character of the field in the original system.
     * <p/>
     * Avro schema field level.
     * <p/>
     * Optional. By default, elements are visible.
     */
    public static final String FIELD_INVISIBLE = "talend.field.invisible";

    // -------------------------------------------------------------------------
    // Storage related properties
    // -------------------------------------------------------------------------
    /**
     * An avro schema may correspond to a number of external structures. Worst
     * case is every record and every enum in the avro schema is a separate
     * structure (this is an extreme case).
     * <p§/>
     * In multi-structure systems, structures need a location to store them.
     * This property preserves such location so that the structure could be
     * created in the correct place when the avro schema is imported.
     * <p/>
     * This is some kind of path that the multi-structure system understands.
     * This is not necessarily a file system location or URI.
     * <p/>
     * Avro schema type level property for "type": "record" and "type": "enum".
     * <p/>
     * Optional.
     */
    public static final String STORE_PATH = "talend.store.path";

    // -------------------------------------------------------------------------
    // Representation properties
    // -------------------------------------------------------------------------
    /**
     * Indicates the representation that should be associated with this
     * structure by default.
     * <p/>
     * Avro schema type level property for "type": "record".
     * <p/>
     * Optional. By default, the representation will be avro. Otherwise
     * use the {@link Representation} Enum.
     */
    public static final String DEFAULT_REP = "talend.default.rep";

    /**
     * An external representation. These are the various external formats
     * structures can be serialized to or deserialized from.
     */
    public enum Representation {
        XML,
        JSON,
        AVRO,
        X12,
        EDIFACT,
        HL7V2,
        COBOL,
        TEXT_DELIMITED,
        TEXT_POSITIONAL,
        SAP_IDOC,
        JAVA,
        MAP,
        NCPDP,
        DATABASE
    };

}
