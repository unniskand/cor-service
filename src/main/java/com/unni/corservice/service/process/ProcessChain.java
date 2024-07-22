package com.unni.corservice.service.process;

import org.springframework.stereotype.Component;

@Component
public interface ProcessChain {

    void setNext(ProcessChain processChain);

    void run();
}
