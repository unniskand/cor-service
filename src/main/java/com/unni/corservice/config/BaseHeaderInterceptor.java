package com.unni.corservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class BaseHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("X-Client-Id", "wolverine");
    }
}
