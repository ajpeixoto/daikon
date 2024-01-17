// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.statistic;

import org.talend.daikon.statistic.deserializer.StatisticsModule;
import org.talend.daikon.statistic.pojo.Statistic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class StatisticDeserializerUtil {

    private static final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().addModule(new JavaTimeModule())
            .addModule(new StatisticsModule()).build();

    public static Statistic read(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Statistic.class);
    }

    public static Statistic[] readValues(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Statistic[].class);
    }
}
