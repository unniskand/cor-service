package com.unni.corservice.service.process.movie;

import com.unni.corservice.service.process.AbstractProcessChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MovieProcessA extends AbstractProcessChain {

    @Override
    protected void doProcess() {
        log.info("Movie Process A Completed!");
    }
}
