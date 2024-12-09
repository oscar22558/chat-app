package com.example.chatapp.redis.converter;

import com.example.chatapp.redis.entity.Contact;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@ReadingConverter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ByteToSqlTimestampConverter implements Converter<byte[], Timestamp> {

    public ByteToSqlTimestampConverter() {
    }

    @Override
    public Timestamp convert(byte[] bytes) {

        StringBuilder builder = new StringBuilder();
        for(byte b : bytes) {
            builder.append((char) b);
        }
        long timestamp = Long.parseLong(builder.toString());
        return new Timestamp(timestamp);
    }

}
