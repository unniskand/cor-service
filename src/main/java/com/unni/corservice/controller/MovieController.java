package com.unni.corservice.controller;

import com.unni.corservice.client.MovieClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@Getter
public class MovieController {

    MovieClient movieClient;

    @GetMapping("/movie/{id}")
    public ResponseEntity getMovieById(@RequestParam(name = "id") String id) {
        var response = getMovieClient().getMovieById(id);
        return ResponseEntity.ok(response);
    }

}
