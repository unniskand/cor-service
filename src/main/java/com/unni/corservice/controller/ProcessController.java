package com.unni.corservice.controller;

import com.unni.corservice.dto.Movie;
import com.unni.corservice.service.process.movie.MovieOrchestrationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @PostMapping("/process")
    public ResponseEntity doPostProcessOrchestration(@RequestBody Movie request) {
        return ResponseEntity.ok("Movie: "+ request.getName());
    }

}
