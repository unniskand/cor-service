package com.unni.corservice.service.process;

import org.springframework.stereotype.Component;

@Component
public abstract class AbstractProcessChain implements ProcessChain {

    protected ProcessChain next;

    @Override
    public void setNext(ProcessChain processChain) {
        this.next = processChain;
    }

    @Override
    public void run() {
        doProcess();
        if (next != null) {
            next.run();
        }
    }

    protected abstract void doProcess();
}
