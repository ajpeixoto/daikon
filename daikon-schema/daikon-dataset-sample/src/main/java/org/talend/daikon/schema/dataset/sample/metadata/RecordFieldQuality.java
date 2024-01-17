// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.schema.dataset.sample.metadata;

import java.util.List;

/**
 * Pojo representation of {@link sample/recordFieldQuality.json}
 */
public record RecordFieldQuality(QualityStatus aggregated, QualityStatus dqType, List<DqRuleQuality> dqRules) {
}
