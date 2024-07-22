package com.unni.corservice.controller;

import com.unni.corservice.service.process.movie.MovieOrchestrationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
@Getter
public class ProcessController {

    MovieOrchestrationService movieOrchestrationService;

    @GetMapping("/process")
    public ResponseEntity doProcessOrchestration() {
        getMovieOrchestrationService().doOrchestration();
        return ResponseEntity.ok(HttpEntity.EMPTY);
    }

}
