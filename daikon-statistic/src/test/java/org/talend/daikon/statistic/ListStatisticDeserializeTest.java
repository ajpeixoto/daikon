// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.statistic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.talend.daikon.statistic.pojo.HistogramStatistic;
import org.talend.daikon.statistic.pojo.KeyValueStatistic;
import org.talend.daikon.statistic.pojo.SimpleStatistic;
import org.talend.daikon.statistic.pojo.Statistic;

public class ListStatisticDeserializeTest {

    @ParameterizedTest
    @ValueSource(strings = { "listStatistics.json" })
    public void testListDeserializeHistogramStat(String filename) throws IOException {
        final InputStream json = StatisticDeserializerUtil.class.getResourceAsStream(filename);
        String jsonString = IOUtils.toString(json, StandardCharsets.UTF_8.name());

        Statistic[] stats = StatisticDeserializerUtil.readValues(jsonString);
        assertEquals(3, stats.length);
        assertEquals(HistogramStatistic.class, stats[0].getClass());
        assertEquals(KeyValueStatistic.class, stats[1].getClass());
        assertEquals(SimpleStatistic.class, stats[2].getClass());
    }

}
