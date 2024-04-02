package com.example.chatapp.redis.entity;

public class ContactKeyBuilder {
    public static String build(String Id){
        return "contact:" + Id;
    }
}
