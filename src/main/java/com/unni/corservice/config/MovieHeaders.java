package com.unni.corservice.config;

import feign.RequestTemplate;

public class MovieHeaders extends BaseHeaderInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("x-rapidapi-host", "moviedatabase8.p.rapidapi.com");
        requestTemplate.header("x-rapidapi-key", "d58de9a0bfmshefb4218aed889bap14a028jsna27f21df2d99");
    }
}
