package com.unni.corservice.service.process.movie;

import com.unni.corservice.service.process.ProcessChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MovieProcessConfig {

    @Autowired
    private MovieProcessA movieProcessA;

    @Autowired
    private MovieProcessB movieProcessB;

    @Bean
    public ProcessChain movieProcessChain() {
        movieProcessA.setNext(movieProcessB);
        return movieProcessA;
    }

}

