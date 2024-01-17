// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.logging.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.internals.ConsumerInterceptors;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.record.TimestampType;
import org.junit.jupiter.api.Test;
import org.talend.daikon.logging.TalendKafkaConsumerInterceptor;

public class ConsumerInterceptorsTest {

    @Test
    public void testOnConsume() {

        Charset UTF8 = Charset.forName("UTF-8");
        int filterPartition1 = 5;
        int filterPartition2 = 6;
        String topic = "test";
        int partition = 1;
        TopicPartition filterTopicPart1 = new TopicPartition("test5", filterPartition1);
        TopicPartition filterTopicPart2 = new TopicPartition("test6", filterPartition2);
        String ip = "192.168.50.130";
        String message = "kafka_message" + ip;

        GenericRecord avroRecord = getGenericRecord();
        avroRecord.put("tenantId", "0123456789");

        ConsumerRecord<Object, Object> consumerRecord = new ConsumerRecord<>(topic, partition, 0, 0L, TimestampType.CREATE_TIME,
                0L, 0, 0, avroRecord, message.getBytes(UTF8));

        List<ConsumerInterceptor<Object, Object>> interceptorList = new ArrayList<>();
        TalendKafkaConsumerInterceptor interceptor = new TalendKafkaConsumerInterceptor();
        interceptorList.add(interceptor);
        ConsumerInterceptors<Object, Object> interceptors = new ConsumerInterceptors<>(interceptorList);

        Map<TopicPartition, List<ConsumerRecord<Object, Object>>> records = new HashMap<>();
        List<ConsumerRecord<Object, Object>> list = new ArrayList<>();
        list.add(consumerRecord);
        TopicPartition tp = new TopicPartition(topic, partition);
        records.put(tp, list);

        ConsumerRecords<Object, Object> consumerRecords = new ConsumerRecords<>(records);
        ConsumerRecords<Object, Object> interceptedRecords = interceptors.onConsume(consumerRecords);

        assertTrue(interceptedRecords.partitions().contains(tp));
        assertFalse(interceptedRecords.partitions().contains(filterTopicPart1));
        assertFalse(interceptedRecords.partitions().contains(filterTopicPart2));
        ConsumerRecords<Object, Object> partInterceptedRecs = interceptors.onConsume(consumerRecords);
        assertEquals(1, partInterceptedRecs.count());
        interceptors.close();
    }

    private GenericRecord getGenericRecord() {
        String accountSchema = "{\"namespace\": \"org.talend.daikon.messages\", \"type\": \"record\", "
                + "\"name\": \"MessageHeader\"," + "\"fields\": [{\"name\": \"tenantId\", \"type\": \"string\"}]}";

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(accountSchema);
        GenericRecord avroRecord = new GenericData.Record(schema);
        return avroRecord;
    }

}
