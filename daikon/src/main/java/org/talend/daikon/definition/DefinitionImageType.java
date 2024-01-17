// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.definition;

/**
 * Image resource types that a {@link Definition} can support.
 *
 * This is used by {@link Definition#getImagePath(DefinitionImageType)} and the component service to provide image
 * resources. Not all {@link Definition} need to support all image types.
 */
public enum DefinitionImageType {
    /** A rescalable SVG icon to represent the definition. */
    SVG_ICON,
    /** A 32x32 PNG icon to represent the definition. */
    PALETTE_ICON_32X32,
    /** A 16x16 PNG icon to represent the definition. */
    TREE_ICON_16X16,
    /** A PNG banner used for the definition. */
    WIZARD_BANNER_75X66
}
