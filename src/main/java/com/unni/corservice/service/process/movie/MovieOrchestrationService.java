package com.unni.corservice.service.process.movie;

import com.unni.corservice.service.process.ProcessChain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MovieOrchestrationService {

    ProcessChain movieProcessChain;

    public void doOrchestration() {
        movieProcessChain.run();
    }
}
