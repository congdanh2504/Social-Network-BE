package com.example.social_network_fpt_be.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Setting {
    public static final String BUCKET_NAME = "social-network-fpt-f670c.appspot.com";
    public static final String PATH_PREFIX = "https://storage.cloud.google.com/";
    public static final String PATH_SUFFIX = "?authuser=0";
    public static final Long MAX_SIZE = Long.valueOf(131072000);
}

